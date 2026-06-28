<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('oa.knowledgebase.articleTitle')" prop="title">
        <el-input
          v-model="queryParams.title"
          :placeholder="$t('oa.knowledgebase.placeholder.title')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('oa.knowledgebase.category')" prop="categoryId">
        <el-select v-model="queryParams.categoryId" :placeholder="$t('oa.knowledgebase.placeholder.category')" clearable style="width:160px">
          <el-option
            v-for="item in categoryList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('oa.knowledgebase.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('oa.knowledgebase.status')" clearable style="width:120px">
          <el-option :label="$t('oa.knowledgebase.statusDraft')" :value="0" />
          <el-option :label="$t('oa.knowledgebase.statusPublished')" :value="1" />
          <el-option :label="$t('oa.knowledgebase.statusOffline')" :value="2" />
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
          v-hasPermi="['oa:knowledgebase:add']"
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
          v-hasPermi="['oa:knowledgebase:edit']"
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
          v-hasPermi="['oa:knowledgebase:remove']"
        >{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="articleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.knowledgebase.articleTitle')" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <a class="link-type" style="cursor:pointer" @click="handleView(scope.row)">
            <el-tag v-if="scope.row.top === 1" size="mini" type="danger">{{ $t('oa.knowledgebase.top') }}</el-tag>
            {{ scope.row.title }}
          </a>
        </template>
      </el-table-column>
      <el-table-column :label="$t('oa.knowledgebase.category')" align="center" prop="categoryName" width="120" />
      <el-table-column :label="$t('oa.knowledgebase.author')" align="center" prop="author" width="120" />
      <el-table-column :label="$t('oa.knowledgebase.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" type="success">{{ $t('oa.knowledgebase.statusPublished') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="info">{{ $t('oa.knowledgebase.statusOffline') }}</el-tag>
          <el-tag v-else type="warning">{{ $t('oa.knowledgebase.statusDraft') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('oa.knowledgebase.viewCount')" align="center" prop="viewCount" width="80" />
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
          >{{ $t('oa.knowledgebase.view') }}</el-button>
          <el-button
            size="mini"
            type="text"
            :icon="scope.row.status === 1 ? 'el-icon-bottom' : 'el-icon-top'"
            @click="handleStatus(scope.row)"
            v-hasPermi="['oa:knowledgebase:edit']"
          >{{ scope.row.status === 1 ? $t('oa.knowledgebase.offline') : $t('oa.knowledgebase.publish') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:knowledgebase:edit']"
          >{{ $t('common.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:knowledgebase:remove']"
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

  </div>
</template>

<script>
import { listArticle, delArticle, updateArticleStatus, listCategory } from "@/api/oa/knowledgebase"

export default {
  name: "OaKnowledgebase",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      articleList: [],
      categoryList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        categoryId: undefined,
        status: undefined
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
      listArticle(this.queryParams).then(response => {
        this.articleList = response.rows
        this.total = response.total
        this.loading = false
      })
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
      this.$router.push('/oa/knowledgebase/form?mode=add')
    },
    handleUpdate(row) {
      const id = row ? row.id : this.ids[0]
      this.$router.push('/oa/knowledgebase/form?mode=edit&id=' + id)
    },
    handleView(row) {
      this.$router.push('/oa/knowledgebase/detail/' + row.id)
    },
    handleStatus(row) {
      const targetStatus = row.status === 1 ? 2 : 1
      const msg = targetStatus === 1 ? this.$t('oa.knowledgebase.publish') : this.$t('oa.knowledgebase.offline')
      this.$modal.confirm(this.$t('oa.knowledgebase.confirm.offline', { title: row.title })).then(function() {
        return updateArticleStatus(row.id, targetStatus)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(msg + this.$t('common.success'))
      }).catch(() => {})
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.knowledgebase.confirm.deleteArticle', { ids })).then(function() {
        return delArticle(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    }
  }
}
</script>
