<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 文件夹树 -->
      <el-col :span="5">
        <el-card>
          <div slot="header" class="clearfix">
            <span>{{ $t('oa.doc.folderTree') }}</span>
            <el-button
              v-hasPermi="['oa:doc:add']"
              style="float: right; padding: 3px 0"
              type="text"
              icon="el-icon-plus"
              @click="handleAddFolder"
            />
          </div>
          <el-tree
            ref="folderTree"
            :data="folderTree"
            :props="{ label: 'name', children: 'children' }"
            node-key="id"
            highlight-current
            default-expand-all
            @node-click="handleFolderClick"
          >
            <span slot-scope="{ node, data }" class="custom-tree-node">
              <span>{{ node.label }}</span>
              <span>
                <el-button
                  v-hasPermi="['oa:doc:edit']"
                  type="text"
                  size="mini"
                  icon="el-icon-edit"
                  @click.stop="handleUpdateFolder(data)"
                />
                <el-button
                  v-hasPermi="['oa:doc:remove']"
                  type="text"
                  size="mini"
                  icon="el-icon-delete"
                  @click.stop="handleDeleteFolder(data)"
                />
              </span>
            </span>
          </el-tree>
        </el-card>
      </el-col>

      <!-- 文件列表 -->
      <el-col :span="19">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item :label="$t('oa.doc.name')" prop="name">
            <el-input
              v-model="queryParams.name"
              :placeholder="$t('oa.doc.placeholder.keyword')"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search') }}</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-upload"
              size="mini"
              @click="handleUpload"
              v-hasPermi="['oa:doc:add']"
            >{{ $t('oa.doc.upload') }}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdateFile"
              v-hasPermi="['oa:doc:edit']"
            >{{ $t('oa.doc.rename') }}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDeleteFile"
              v-hasPermi="['oa:doc:remove']"
            >{{ $t('common.delete') }}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-refresh-left"
              size="mini"
              @click="enterRecycle"
            >{{ $t('oa.doc.enterRecycle') }}</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="fileList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
          <el-table-column :label="$t('oa.doc.name')" align="center" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <el-link type="primary" :underline="false" @click="handlePreview(scope.row)">
                <i :class="fileIcon(scope.row.fileType)" /> {{ scope.row.name }}
              </el-link>
            </template>
          </el-table-column>
          <el-table-column :label="$t('oa.doc.type')" align="center" prop="fileType" width="80" />
          <el-table-column :label="$t('oa.doc.size')" align="center" prop="size" width="100">
            <template slot-scope="scope">
              <span>{{ formatSize(scope.row.size) }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('oa.doc.currentVersion')" align="center" prop="currentVersion" width="90" />
          <el-table-column :label="$t('oa.doc.folder')" align="center" prop="folderName" width="120" />
          <el-table-column :label="$t('common.createTime')" align="center" prop="createTime" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="280">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-view" @click="handlePreview(scope.row)">{{ $t('oa.doc.preview') }}</el-button>
              <el-button size="mini" type="text" icon="el-icon-download" @click="handleDownload(scope.row)">{{ $t('oa.doc.download') }}</el-button>
              <el-button size="mini" type="text" icon="el-icon-copy-document" @click="handleVersions(scope.row)">{{ $t('oa.doc.versions') }}</el-button>
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdateFile(scope.row)" v-hasPermi="['oa:doc:edit']">{{ $t('common.edit') }}</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeleteFile(scope.row)" v-hasPermi="['oa:doc:remove']">{{ $t('common.delete') }}</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <!-- 文件夹对话框 -->
    <el-dialog :title="folderTitle" :visible.sync="folderOpen" width="500px" append-to-body>
      <el-form ref="folderForm" :model="folderForm" :rules="folderRules" label-width="100px">
        <el-form-item :label="$t('oa.doc.name')" prop="name">
          <el-input v-model="folderForm.name" :placeholder="$t('oa.doc.placeholder.folderName')" />
        </el-form-item>
        <el-form-item :label="$t('oa.doc.folder')">
          <el-select v-model="folderForm.parentId" clearable style="width:100%">
            <el-option :label="$t('oa.doc.allFiles')" :value="0" />
            <el-option
              v-for="item in flattenFolders(folderTree)"
              :key="item.id"
              :label="item.label"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('common.sort')">
          <el-input-number v-model="folderForm.sort" :min="0" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFolderForm">{{ $t('common.submit') }}</el-button>
        <el-button @click="cancelFolder">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>

    <!-- 文件重命名对话框 -->
    <el-dialog :title="$t('oa.doc.editFile')" :visible.sync="fileOpen" width="500px" append-to-body>
      <el-form ref="fileForm" :model="fileForm" :rules="fileRules" label-width="100px">
        <el-form-item :label="$t('oa.doc.name')" prop="name">
          <el-input v-model="fileForm.name" :placeholder="$t('oa.doc.placeholder.fileName')" />
        </el-form-item>
        <el-form-item :label="$t('oa.doc.folder')">
          <el-select v-model="fileForm.folderId" clearable style="width:100%">
            <el-option
              v-for="item in flattenFolders(folderTree)"
              :key="item.id"
              :label="item.label"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">{{ $t('common.submit') }}</el-button>
        <el-button @click="cancelFile">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>

    <!-- 上传对话框 -->
    <el-dialog :title="$t('oa.doc.upload')" :visible.sync="uploadOpen" width="500px" append-to-body>
      <el-upload
        ref="upload"
        drag
        :action="uploadAction"
        :headers="uploadHeaders"
        :data="uploadData"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :auto-upload="false"
      >
        <i class="el-icon-upload" />
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitUpload">{{ $t('common.submit') }}</el-button>
        <el-button @click="uploadOpen = false">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>

    <!-- 版本对话框 -->
    <el-dialog :title="$t('oa.doc.versionList')" :visible.sync="versionOpen" width="700px" append-to-body>
      <el-table :data="versionList">
        <el-table-column :label="$t('oa.doc.versionNo')" align="center" prop="versionNo" width="80" />
        <el-table-column :label="$t('oa.doc.size')" align="center" prop="size" width="100">
          <template slot-scope="scope">
            <span>{{ formatSize(scope.row.size) }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('oa.doc.remark')" align="center" prop="remark" :show-overflow-tooltip="true" />
        <el-table-column :label="$t('common.createTime')" align="center" prop="createTime" width="160">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="120">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-refresh-left" @click="handleRollback(scope.row)" v-hasPermi="['oa:doc:edit']">{{ $t('oa.doc.rollback') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 回收站对话框 -->
    <el-dialog :title="$t('oa.doc.recycle')" :visible.sync="recycleOpen" width="800px" append-to-body>
      <el-table :data="recycleList" v-loading="recycleLoading">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column :label="$t('oa.doc.name')" align="center" prop="name" :show-overflow-tooltip="true" />
        <el-table-column :label="$t('oa.doc.objectType')" align="center" prop="objectType" width="80" />
        <el-table-column :label="$t('oa.doc.deleteBy')" align="center" prop="deleteBy" width="100" />
        <el-table-column :label="$t('oa.doc.deleteTime')" align="center" prop="deleteTime" width="160">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.deleteTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="160">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-refresh-left" @click="handleRestore(scope.row)" v-hasPermi="['oa:doc:edit']">{{ $t('oa.doc.restore') }}</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete-solid" @click="handlePurge(scope.row)" v-hasPermi="['oa:doc:remove']">{{ $t('oa.doc.purge') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="recycleTotal>0"
        :total="recycleTotal"
        :page.sync="recycleQuery.pageNum"
        :limit.sync="recycleQuery.pageSize"
        @pagination="getRecycleList"
      />
    </el-dialog>
  </div>
</template>

<script>
import {
  listFolder, treeFolder, addFolder, updateFolder, delFolder,
  listFile, updateFile, delFile, listVersions, rollbackVersion,
  listRecycle, restoreRecycle, purgeRecycle
} from "@/api/oa/doc"
import request from '@/utils/request'
import { getToken } from '@/utils/auth'

export default {
  name: "OaDoc",
  data() {
    return {
      loading: true,
      recycleLoading: false,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      recycleTotal: 0,
      folderTree: [],
      fileList: [],
      versionList: [],
      recycleList: [],
      folderOpen: false,
      fileOpen: false,
      uploadOpen: false,
      versionOpen: false,
      recycleOpen: false,
      folderTitle: "",
      uploadAction: process.env.VUE_APP_BASE_API + "/api/v1/oa/docs/files/upload",
      uploadHeaders: { Authorization: "Bearer " + getToken() },
      uploadData: { folderId: undefined },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        folderId: undefined,
        name: undefined
      },
      recycleQuery: {
        pageNum: 1,
        pageSize: 10
      },
      folderForm: {},
      fileForm: {},
      folderRules: {
        name: [
          { required: true, message: this.$t('oa.doc.required.folderName'), trigger: "blur" }
        ]
      },
      fileRules: {
        name: [
          { required: true, message: this.$t('oa.doc.required.fileName'), trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getFolderTree()
    this.getList()
  },
  methods: {
    getFolderTree() {
      treeFolder().then(response => {
        this.folderTree = this.buildTree(response.data || [])
      })
    },
    buildTree(list) {
      const map = {}
      const roots = []
      list.forEach(item => {
        item.children = []
        map[item.id] = item
      })
      list.forEach(item => {
        if (item.parentId && item.parentId !== 0 && map[item.parentId]) {
          map[item.parentId].children.push(item)
        } else {
          roots.push(item)
        }
      })
      return roots
    },
    getList() {
      this.loading = true
      listFile(this.queryParams).then(response => {
        this.fileList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getRecycleList() {
      this.recycleLoading = true
      listRecycle(this.recycleQuery).then(response => {
        this.recycleList = response.rows
        this.recycleTotal = response.total
        this.recycleLoading = false
      })
    },
    handleFolderClick(data) {
      this.queryParams.folderId = data.id
      this.queryParams.pageNum = 1
      this.uploadData.folderId = data.id
      this.getList()
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        folderId: undefined,
        name: undefined
      }
      this.getList()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleAddFolder() {
      this.folderForm = {
        id: undefined,
        parentId: this.queryParams.folderId || 0,
        name: undefined,
        sort: 0
      }
      this.folderTitle = this.$t('oa.doc.addFolder')
      this.folderOpen = true
    },
    handleUpdateFolder(row) {
      this.folderForm = {
        id: row.id,
        parentId: row.parentId,
        name: row.name,
        sort: row.sort
      }
      this.folderTitle = this.$t('oa.doc.editFolder')
      this.folderOpen = true
    },
    submitFolderForm() {
      this.$refs["folderForm"].validate(valid => {
        if (valid) {
          if (this.folderForm.id != undefined) {
            updateFolder(this.folderForm).then(() => {
              this.$modal.msgSuccess(this.$t('common.editSuccess'))
              this.folderOpen = false
              this.getFolderTree()
            })
          } else {
            addFolder(this.folderForm).then(() => {
              this.$modal.msgSuccess(this.$t('common.addSuccess'))
              this.folderOpen = false
              this.getFolderTree()
            })
          }
        }
      })
    },
    cancelFolder() {
      this.folderOpen = false
    },
    handleDeleteFolder(row) {
      const ids = row.id
      this.$modal.confirm(this.$t('oa.doc.confirm.deleteFolder', { ids })).then(function() {
        return delFolder(ids)
      }).then(() => {
        this.getFolderTree()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    },
    handleUpload() {
      this.uploadData.folderId = this.queryParams.folderId
      this.uploadOpen = true
    },
    submitUpload() {
      this.$refs.upload.submit()
    },
    handleUploadSuccess() {
      this.$modal.msgSuccess(this.$t('common.addSuccess'))
      this.uploadOpen = false
      this.$refs.upload.clearFiles()
      this.getList()
    },
    handleUploadError() {
      this.$modal.msgError(this.$t('common.operateFailed'))
    },
    handleUpdateFile(row) {
      const id = row.id || this.ids[0]
      const file = this.fileList.find(item => item.id === id) || row
      this.fileForm = {
        id: file.id,
        name: file.name,
        folderId: file.folderId
      }
      this.fileOpen = true
    },
    submitFileForm() {
      this.$refs["fileForm"].validate(valid => {
        if (valid) {
          updateFile(this.fileForm).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
            this.fileOpen = false
            this.getList()
          })
        }
      })
    },
    cancelFile() {
      this.fileOpen = false
    },
    handleDeleteFile(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.doc.confirm.deleteFile', { ids })).then(function() {
        return delFile(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    },
    handleDownload(row) {
      request({
        url: '/api/v1/oa/docs/files/' + row.id + '/download',
        method: 'get',
        responseType: 'blob'
      }).then(res => {
        const blob = new Blob([res])
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.setAttribute('download', row.name)
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        URL.revokeObjectURL(link.href)
      })
    },
    handlePreview(row) {
      request({
        url: '/api/v1/oa/docs/files/' + row.id + '/preview',
        method: 'get',
        responseType: 'blob'
      }).then(res => {
        const blob = new Blob([res])
        const url = URL.createObjectURL(blob)
        window.open(url, '_blank')
      })
    },
    handleVersions(row) {
      this.versionOpen = true
      listVersions(row.id).then(response => {
        this.versionList = response.rows
      })
    },
    handleRollback(row) {
      this.$modal.confirm(this.$t('oa.doc.confirm.rollback')).then(() => {
        return rollbackVersion(row.fileId, row.id)
      }).then(() => {
        this.$modal.msgSuccess(this.$t('common.editSuccess'))
        this.getList()
      }).catch(() => {})
    },
    enterRecycle() {
      this.recycleOpen = true
      this.getRecycleList()
    },
    handleRestore(row) {
      restoreRecycle(row.id).then(() => {
        this.getRecycleList()
        this.getList()
        this.$modal.msgSuccess(this.$t('common.editSuccess'))
      })
    },
    handlePurge(row) {
      const ids = row.id
      this.$modal.confirm(this.$t('oa.doc.confirm.purge', { ids })).then(function() {
        return purgeRecycle(ids)
      }).then(() => {
        this.getRecycleList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    },
    flattenFolders(tree, prefix = '') {
      const result = []
      tree.forEach(node => {
        result.push({ id: node.id, label: prefix + node.name })
        if (node.children && node.children.length > 0) {
          result.push(...this.flattenFolders(node.children, prefix + node.name + ' / '))
        }
      })
      return result
    },
    fileIcon(type) {
      const imageTypes = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp']
      if (imageTypes.includes(type)) return 'el-icon-picture-outline'
      if (type === 'pdf') return 'el-icon-document'
      if (['doc', 'docx'].includes(type)) return 'el-icon-document'
      if (['xls', 'xlsx'].includes(type)) return 'el-icon-s-data'
      if (['ppt', 'pptx'].includes(type)) return 'el-icon-data-line'
      if (['zip', 'rar', '7z'].includes(type)) return 'el-icon-box'
      return 'el-icon-document'
    },
    formatSize(size) {
      if (!size) return '0 B'
      const units = ['B', 'KB', 'MB', 'GB']
      let index = 0
      let value = size
      while (value >= 1024 && index < units.length - 1) {
        value /= 1024
        index++
      }
      return value.toFixed(2) + ' ' + units[index]
    }
  }
}
</script>

<style scoped>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
