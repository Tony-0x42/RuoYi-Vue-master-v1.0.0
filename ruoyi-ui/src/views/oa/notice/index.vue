<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('oa.notice.title')" prop="title">
        <el-input
          v-model="queryParams.title"
          :placeholder="$t('oa.notice.placeholder.title')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('oa.notice.category')" prop="categoryId">
        <el-select v-model="queryParams.categoryId" :placeholder="$t('oa.notice.placeholder.category')" clearable>
          <el-option
            v-for="item in categoryList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('common.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('common.status')" clearable>
          <el-option :label="$t('oa.notice.statusDraft')" :value="0" />
          <el-option :label="$t('oa.notice.statusPublished')" :value="1" />
          <el-option :label="$t('oa.notice.statusOffline')" :value="2" />
        </el-select>
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
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['oa:notice:add']"
        >{{ $t('common.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['oa:notice:edit']"
        >{{ $t('common.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['oa:notice:remove']"
        >{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="noticeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.notice.title')" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <a class="link-type" style="cursor:pointer" @click="handleView(scope.row)">
            <el-tag v-if="scope.row.top === 1" size="mini" type="danger">{{ $t('oa.notice.top') }}</el-tag>
            {{ scope.row.title }}
          </a>
        </template>
      </el-table-column>
      <el-table-column :label="$t('oa.notice.category')" align="center" prop="categoryName" width="120" />
      <el-table-column :label="$t('oa.notice.scopeType')" align="center" prop="scopeType" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.scopeType ? $t('oa.notice.scope' + scope.row.scopeType.charAt(0).toUpperCase() + scope.row.scopeType.slice(1)) : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" type="success">{{ $t('oa.notice.statusPublished') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="info">{{ $t('oa.notice.statusOffline') }}</el-tag>
          <el-tag v-else type="warning">{{ $t('oa.notice.statusDraft') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('oa.notice.needConfirm')" align="center" prop="needConfirm" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.needConfirm === 1 ? $t('common.yes') : $t('common.no') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.createTime')" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >{{ $t('oa.notice.view') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-bottom"
            @click="handleOffline(scope.row)"
            v-hasPermi="['oa:notice:edit']"
            v-if="scope.row.status === 1"
          >{{ $t('oa.notice.offline') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:notice:edit']"
          >{{ $t('common.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:notice:remove']"
          >{{ $t('common.delete') }}</el-button>
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

    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('oa.notice.title')" prop="title">
              <el-input v-model="form.title" :placeholder="$t('oa.notice.placeholder.title')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.notice.category')" prop="categoryId">
              <el-select v-model="form.categoryId" :placeholder="$t('oa.notice.placeholder.category')" style="width:100%">
                <el-option
                  v-for="item in categoryList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.notice.scopeType')" prop="scopeType">
              <el-select v-model="form.scopeType" :placeholder="$t('oa.notice.placeholder.scopeType')" style="width:100%">
                <el-option :label="$t('oa.notice.scopeAll')" value="all" />
                <el-option :label="$t('oa.notice.scopeDept')" value="dept" />
                <el-option :label="$t('oa.notice.scopeRole')" value="role" />
                <el-option :label="$t('oa.notice.scopeUser')" value="user" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.notice.expireTime')" prop="expireTime">
              <el-date-picker
                v-model="form.expireTime"
                type="datetime"
                :placeholder="$t('oa.notice.placeholder.expireTime')"
                style="width:100%"
                value-format="yyyy-MM-dd HH:mm:ss"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('common.status')" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="0">{{ $t('oa.notice.statusDraft') }}</el-radio>
                <el-radio :label="1">{{ $t('oa.notice.statusPublished') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item>
              <el-checkbox v-model="form.top">{{ $t('oa.notice.top') }}</el-checkbox>
              <el-checkbox v-model="form.needConfirm">{{ $t('oa.notice.needConfirm') }}</el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.notice.content')" prop="content">
              <editor v-model="form.content" :min-height="192" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('common.submit') }}</el-button>
        <el-button @click="cancel">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>

    <notice-detail ref="detailRef" :category-list="categoryList" />
  </div>
</template>

<script>
import { listNotice, getNotice, addNotice, updateNotice, delNotice, offlineNotice, listCategory } from "@/api/oa/notice"
import NoticeDetail from "./detail"

export default {
  name: "OaNotice",
  components: { NoticeDetail },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      noticeList: [],
      categoryList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        categoryId: undefined,
        status: undefined
      },
      form: {},
      rules: {
        title: [
          { required: true, message: this.$t('oa.notice.required.title'), trigger: "blur" }
        ],
        categoryId: [
          { required: true, message: this.$t('oa.notice.required.category'), trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getCategoryList()
    this.getList()
  },
  methods: {
    getCategoryList() {
      listCategory({ pageNum: 1, pageSize: 1000, status: 1 }).then(response => {
        this.categoryList = response.rows
      })
    },
    getList() {
      this.loading = true
      listNotice(this.queryParams).then(response => {
        this.noticeList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: undefined,
        title: undefined,
        categoryId: undefined,
        content: undefined,
        scopeType: "all",
        scopeIds: undefined,
        needConfirm: false,
        top: false,
        status: 0,
        expireTime: undefined
      }
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = this.$t('oa.notice.addNotice')
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getNotice(id).then(response => {
        this.form = response.data
        this.form.needConfirm = this.form.needConfirm === 1
        this.form.top = this.form.top === 1
        this.open = true
        this.title = this.$t('oa.notice.editNotice')
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const data = { ...this.form }
          data.needConfirm = data.needConfirm ? 1 : 0
          data.top = data.top ? 1 : 0
          if (data.id != undefined) {
            updateNotice(data).then(() => {
              this.$modal.msgSuccess(this.$t('common.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addNotice(data).then(() => {
              this.$modal.msgSuccess(this.$t('common.addSuccess'))
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleView(row) {
      this.$refs.detailRef.open(row.id)
    },
    handleOffline(row) {
      this.$modal.confirm(this.$t('oa.notice.confirm.offline', { title: row.title })).then(function() {
        return offlineNotice(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('oa.notice.offlineSuccess'))
      }).catch(() => {})
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.notice.confirm.deleteNotice', { ids })).then(function() {
        return delNotice(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    }
  }
}
</script>
