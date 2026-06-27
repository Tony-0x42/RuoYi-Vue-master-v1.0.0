<template>
  <div class="app-container">
    <el-row :gutter="0">
      <!-- 左侧分类树 -->
      <el-col :span="5" class="category-sidebar">
        <el-card shadow="never" class="tree-card">
          <div slot="header" class="clearfix">
            <span>流程分类</span>
            <el-button
              v-hasPermi="['bpm:category:add']"
              style="float: right; padding: 3px 0"
              type="text"
              icon="el-icon-plus"
              @click="handleCategoryAdd(0)"
            >新增</el-button>
          </div>
          <el-tree
            ref="categoryTree"
            :data="categoryTree"
            :props="treeProps"
            node-key="id"
            highlight-current
            default-expand-all
            :expand-on-click-node="false"
            @node-click="handleCategoryNodeClick"
          >
            <span class="custom-tree-node" slot-scope="{ node, data }">
              <span>{{ node.label }}</span>
              <span v-if="data.id !== 0" class="tree-node-actions">
                <el-button
                  type="text"
                  size="mini"
                  icon="el-icon-s-data"
                  title="配置变量"
                  @click.stop="handleConfigCategoryVariable(data)"
                  v-hasPermi="['bpm:variable:list']"
                />
                <el-button
                  type="text"
                  size="mini"
                  icon="el-icon-plus"
                  @click.stop="handleCategoryAdd(data.id)"
                  v-hasPermi="['bpm:category:add']"
                />
                <el-button
                  type="text"
                  size="mini"
                  icon="el-icon-edit"
                  @click.stop="handleCategoryUpdate(data)"
                  v-hasPermi="['bpm:category:edit']"
                />
                <el-button
                  type="text"
                  size="mini"
                  icon="el-icon-delete"
                  @click.stop="handleCategoryDelete(data)"
                  v-hasPermi="['bpm:category:remove']"
                />
              </span>
            </span>
          </el-tree>
        </el-card>
      </el-col>

      <!-- 右侧流程定义列表 -->
      <el-col :span="19" class="definition-main">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
          <el-form-item label="流程名称" prop="definitionName">
            <el-input
              v-model="queryParams.definitionName"
              placeholder="请输入流程名称"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="流程编码" prop="definitionCode">
            <el-input
              v-model="queryParams.definitionCode"
              placeholder="请输入流程编码"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              :disabled="selectedCategoryId === undefined"
              @click="handleAdd"
              v-hasPermi="['bpm:definition:add']"
            >新增</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="definitionList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
          <el-table-column label="流程名称" align="center" prop="definitionName" :show-overflow-tooltip="true" />
          <el-table-column label="流程编码" align="center" prop="definitionCode" :show-overflow-tooltip="true" />
          <el-table-column label="流程分类" align="center" prop="categoryName" />
          <el-table-column label="版本" align="center" prop="version" />
          <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280" fixed="right">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['bpm:definition:edit']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit-outline"
                @click="handleDesign(scope.row)"
                v-hasPermi="['bpm:definition:design']"
              >流程设计</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-upload2"
                @click="handleDeploy(scope.row)"
                v-hasPermi="['bpm:definition:deploy']"
              >发布</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-s-data"
                @click="handleConfigVariable(scope.row)"
              >配置变量</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['bpm:definition:remove']"
              >删除</el-button>
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

    <!-- 添加或修改流程定义对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="流程分类" prop="categoryId">
          <treeselect
            v-model="form.categoryId"
            :options="categoryTreeOptions"
            :normalizer="normalizer"
            :disabled="form.definitionId === undefined"
            placeholder="请选择流程分类"
          />
        </el-form-item>
        <el-form-item label="流程名称" prop="definitionName">
          <el-input v-model="form.definitionName" placeholder="请输入流程名称" />
        </el-form-item>
        <el-form-item label="流程编码" prop="definitionCode">
          <el-input v-model="form.definitionCode" placeholder="请输入流程编码" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改流程分类对话框 -->
    <el-dialog :title="categoryTitle" :visible.sync="categoryOpen" width="500px" append-to-body>
      <el-form ref="categoryForm" :model="categoryForm" :rules="categoryRules" label-width="80px">
        <el-form-item label="上级分类" prop="parentId">
          <treeselect v-model="categoryForm.parentId" :options="categoryTreeOptions" :normalizer="normalizer" placeholder="请选择上级分类" />
        </el-form-item>
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="categoryForm.categoryName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类编码" prop="categoryCode">
          <el-input v-model="categoryForm.categoryCode" placeholder="请输入分类编码" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input-number v-model="categoryForm.orderNum" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="categoryForm.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="categoryForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitCategoryForm">确 定</el-button>
        <el-button @click="cancelCategory">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDefinition, getDefinition, delDefinition, addDefinition, updateDefinition, deployDefinition } from "@/api/bpm/definition"
import { treeCategory, getCategory, delCategory, addCategory, updateCategory } from "@/api/bpm/category"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"

export default {
  name: "BpmDefinition",
  dicts: ['sys_normal_disable'],
  components: { Treeselect },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      definitionList: [],
      categoryTree: [],
      categoryTreeOptions: [],
      selectedCategoryId: undefined,
      treeProps: {
        children: 'children',
        label: 'label'
      },
      title: "",
      open: false,
      categoryTitle: "",
      categoryOpen: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        definitionName: undefined,
        definitionCode: undefined,
        categoryId: undefined
      },
      form: {},
      categoryForm: {},
      rules: {
        categoryId: [
          { required: true, message: "流程分类不能为空", trigger: "change" }
        ],
        definitionName: [
          { required: true, message: "流程名称不能为空", trigger: "blur" }
        ],
        definitionCode: [
          { required: true, message: "流程编码不能为空", trigger: "blur" }
        ]
      },
      categoryRules: {
        categoryName: [
          { required: true, message: "分类名称不能为空", trigger: "blur" }
        ],
        categoryCode: [
          { required: true, message: "分类编码不能为空", trigger: "blur" }
        ],
        orderNum: [
          { required: true, message: "显示顺序不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getCategoryTree()
    this.getCategoryOptions()
    this.getList()
  },
  methods: {
    getCategoryTree() {
      treeCategory({}).then(response => {
        this.categoryTree = [{
          id: 0,
          label: '全部流程',
          children: response.data
        }]
      })
    },
    getCategoryOptions() {
      treeCategory({}).then(response => {
        this.categoryTreeOptions = [{
          id: 0,
          label: '全部流程',
          children: response.data
        }]
      })
    },
    normalizer(node) {
      return {
        id: node.id,
        label: node.label,
        children: node.children && node.children.length ? node.children : undefined,
        isDisabled: node.id === this.categoryForm.categoryId
      }
    },
    getList() {
      this.loading = true
      listDefinition(this.queryParams).then(response => {
        this.definitionList = response.rows
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
        definitionId: undefined,
        categoryId: undefined,
        definitionName: undefined,
        definitionCode: undefined,
        remark: undefined
      }
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams.categoryId = undefined
      this.resetForm("queryForm")
      this.handleQuery()
      this.$refs.categoryTree && this.$refs.categoryTree.setCurrentKey(0)
    },
    handleAdd() {
      this.reset()
      this.form.categoryId = this.selectedCategoryId
      this.open = true
      this.title = "添加流程定义"
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.definitionId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const definitionId = row.definitionId || this.ids
      getDefinition(definitionId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改流程定义"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.definitionId != undefined) {
            updateDefinition(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDefinition(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const definitionIds = row.definitionId || this.ids
      this.$modal.confirm('是否确认删除流程定义编号为"' + definitionIds + '"的数据项？').then(function() {
        return delDefinition(definitionIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleDesign(row) {
      this.$router.push("/bpm/designer/index?definitionId=" + row.definitionId)
    },
    handleDeploy(row) {
      this.$modal.confirm('是否确认发布流程定义"' + row.definitionName + '"？').then(function() {
        return deployDefinition(row.definitionId)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("发布成功")
      }).catch(() => {})
    },
    handleConfigVariable(row) {
      this.$router.push("/bpm/variable?definitionId=" + row.definitionId)
    },
    handleConfigCategoryVariable(data) {
      this.$router.push("/bpm/variable?categoryId=" + data.id)
    },
    handleCategoryNodeClick(data) {
      if (data.id === 0) {
        this.queryParams.categoryId = undefined
        this.selectedCategoryId = undefined
      } else {
        this.queryParams.categoryId = data.id
        this.selectedCategoryId = data.id
      }
      this.handleQuery()
    },
    cancelCategory() {
      this.categoryOpen = false
      this.resetCategory()
    },
    resetCategory() {
      this.categoryForm = {
        categoryId: undefined,
        parentId: 0,
        categoryName: undefined,
        categoryCode: undefined,
        orderNum: 0,
        status: "0",
        remark: undefined
      }
      this.resetForm("categoryForm")
    },
    handleCategoryAdd(parentId) {
      this.resetCategory()
      this.categoryForm.parentId = parentId || 0
      this.categoryOpen = true
      this.categoryTitle = "添加流程分类"
      this.getCategoryOptions()
    },
    handleCategoryUpdate(data) {
      this.resetCategory()
      getCategory(data.id).then(response => {
        this.categoryForm = response.data
        this.categoryOpen = true
        this.categoryTitle = "修改流程分类"
        this.getCategoryOptions()
      })
    },
    handleCategoryDelete(data) {
      this.$modal.confirm('是否确认删除名称为"' + data.label + '"的分类？').then(() => {
        return delCategory(data.id)
      }).then(() => {
        this.$modal.msgSuccess("删除成功")
        this.getCategoryTree()
        this.getCategoryOptions()
        if (this.queryParams.categoryId === data.id) {
          this.queryParams.categoryId = undefined
          this.getList()
          this.$refs.categoryTree.setCurrentKey(0)
        }
      }).catch(() => {})
    },
    submitCategoryForm() {
      this.$refs["categoryForm"].validate(valid => {
        if (valid) {
          if (this.categoryForm.categoryId != undefined) {
            updateCategory(this.categoryForm).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.categoryOpen = false
              this.getCategoryTree()
              this.getCategoryOptions()
              this.getList()
            })
          } else {
            addCategory(this.categoryForm).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.categoryOpen = false
              this.getCategoryTree()
              this.getCategoryOptions()
            })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.category-sidebar {
  padding-right: 10px;
}
.tree-card {
  height: calc(100vh - 120px);
  overflow-y: auto;
}
.definition-main {
  padding-left: 10px;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.tree-node-actions {
  display: none;
}
.custom-tree-node:hover .tree-node-actions {
  display: inline-block;
}
</style>
