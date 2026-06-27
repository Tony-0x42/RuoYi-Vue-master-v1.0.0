<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('oa.expense.userName')" prop="userName">
        <el-input
          v-model="queryParams.userName"
          :placeholder="$t('oa.expense.placeholder.userName')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('oa.expense.category')" prop="categoryId">
        <el-select v-model="queryParams.categoryId" :placeholder="$t('oa.expense.placeholder.category')" clearable>
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
          <el-option :label="$t('oa.expense.statusDraft')" :value="0" />
          <el-option :label="$t('oa.expense.statusApproving')" :value="1" />
          <el-option :label="$t('oa.expense.statusFinance')" :value="2" />
          <el-option :label="$t('oa.expense.statusPending')" :value="3" />
          <el-option :label="$t('oa.expense.statusFinished')" :value="4" />
          <el-option :label="$t('oa.expense.statusRejected')" :value="9" />
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
          v-hasPermi="['oa:expenseReport:add']"
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
          v-hasPermi="['oa:expenseReport:edit']"
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
          v-hasPermi="['oa:expenseReport:remove']"
        >{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="reportList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.expense.userName')" align="center" prop="userName" width="120" />
      <el-table-column :label="$t('oa.expense.category')" align="center" prop="categoryName" width="120" />
      <el-table-column :label="$t('oa.expense.totalAmount')" align="center" prop="totalAmount" width="120" />
      <el-table-column :label="$t('oa.expense.reason')" align="center" prop="reason" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('common.status')" align="center" prop="status" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="warning">{{ $t('oa.expense.statusDraft') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="primary">{{ $t('oa.expense.statusApproving') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="primary">{{ $t('oa.expense.statusFinance') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 3" type="success">{{ $t('oa.expense.statusPending') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 4" type="success">{{ $t('oa.expense.statusFinished') }}</el-tag>
          <el-tag v-else type="info">{{ $t('oa.expense.statusRejected') }}</el-tag>
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
            icon="el-icon-s-promotion"
            @click="handleSubmit(scope.row)"
            v-hasPermi="['oa:expenseReport:edit']"
            v-if="scope.row.status === 0"
          >{{ $t('oa.expense.submit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:expenseReport:edit']"
          >{{ $t('common.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:expenseReport:remove']"
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

    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('oa.expense.userName')" prop="userName">
              <el-input v-model="form.userName" :placeholder="$t('oa.expense.placeholder.userName')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.expense.category')" prop="categoryId">
              <el-select v-model="form.categoryId" :placeholder="$t('oa.expense.placeholder.category')" style="width:100%">
                <el-option
                  v-for="item in categoryList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.expense.reason')" prop="reason">
              <el-input v-model="form.reason" type="textarea" :placeholder="$t('oa.expense.placeholder.reason')" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.expense.loan')" prop="loanId">
              <el-select v-model="form.loanId" :placeholder="$t('oa.expense.placeholder.loan')" clearable style="width:50%">
                <el-option
                  v-for="item in availableLoanList"
                  :key="item.id"
                  :label="item.reason + ' (' + item.amount + ')'"
                  :value="item.id"
                />
              </el-select>
              <el-input-number v-model="form.offsetAmount" :min="0" :precision="2" style="margin-left:10px" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">{{ $t('oa.expense.items') }}</el-divider>
        <el-table :data="form.items" border>
          <el-table-column :label="$t('oa.expense.category')" align="center" width="180">
            <template slot-scope="scope">
              <el-select v-model="scope.row.categoryId" size="mini" style="width:100%">
                <el-option
                  v-for="item in categoryList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column :label="$t('oa.expense.amount')" align="center" width="140">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.amount" :min="0" :precision="2" size="mini" style="width:100%" />
            </template>
          </el-table-column>
          <el-table-column :label="$t('oa.expense.invoice')" align="center" width="140">
            <template slot-scope="scope">
              <el-select v-model="scope.row.invoiceId" size="mini" clearable style="width:100%">
                <el-option
                  v-for="item in invoiceList"
                  :key="item.id"
                  :label="item.invoiceNo"
                  :value="item.id"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column :label="$t('oa.expense.expenseDate')" align="center" width="150">
            <template slot-scope="scope">
              <el-date-picker v-model="scope.row.expenseDate" type="date" size="mini" value-format="yyyy-MM-dd" style="width:100%" />
            </template>
          </el-table-column>
          <el-table-column :label="$t('oa.expense.description')" align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.description" size="mini" />
            </template>
          </el-table-column>
          <el-table-column :label="$t('common.operation')" align="center" width="80">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-delete" @click="removeItem(scope.$index)">{{ $t('common.delete') }}</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-row style="margin-top:10px">
          <el-col :span="24" style="text-align:center">
            <el-button type="primary" size="mini" icon="el-icon-plus" @click="addItem">{{ $t('oa.expense.addItem') }}</el-button>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('common.submit') }}</el-button>
        <el-button @click="cancel">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReport, getReport, addReport, updateReport, delReport, submitReport } from "@/api/oa/expense"
import { listCategory, listInvoice, listAvailableLoan } from "@/api/oa/expense"

export default {
  name: "OaExpenseReport",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      reportList: [],
      categoryList: [],
      invoiceList: [],
      availableLoanList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        categoryId: undefined,
        status: undefined
      },
      form: {
        items: []
      },
      rules: {
        userName: [
          { required: true, message: this.$t('oa.expense.required.userName'), trigger: "blur" }
        ],
        categoryId: [
          { required: true, message: this.$t('oa.expense.required.category'), trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getCategoryList()
    this.getInvoiceList()
    this.getAvailableLoanList()
    this.getList()
  },
  methods: {
    getCategoryList() {
      listCategory({ pageNum: 1, pageSize: 1000, status: 1 }).then(response => {
        this.categoryList = response.rows
      })
    },
    getInvoiceList() {
      listInvoice({ pageNum: 1, pageSize: 1000, status: 0 }).then(response => {
        this.invoiceList = response.rows
      })
    },
    getAvailableLoanList() {
      listAvailableLoan().then(response => {
        this.availableLoanList = response.rows
      })
    },
    getList() {
      this.loading = true
      listReport(this.queryParams).then(response => {
        this.reportList = response.rows
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
        userName: undefined,
        userId: undefined,
        deptId: undefined,
        categoryId: undefined,
        reason: undefined,
        loanId: undefined,
        offsetAmount: 0,
        status: 0,
        items: []
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
      this.title = this.$t('oa.expense.addReport')
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getReport(id).then(response => {
        this.form = response.data
        if (!this.form.items) {
          this.form.items = []
        }
        this.open = true
        this.title = this.$t('oa.expense.editReport')
      })
    },
    addItem() {
      this.form.items.push({
        categoryId: undefined,
        amount: 0,
        invoiceId: undefined,
        description: undefined,
        expenseDate: undefined
      })
    },
    removeItem(index) {
      this.form.items.splice(index, 1)
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const data = { ...this.form }
          if (data.id != undefined) {
            updateReport(data).then(() => {
              this.$modal.msgSuccess(this.$t('common.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addReport(data).then(() => {
              this.$modal.msgSuccess(this.$t('common.addSuccess'))
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleSubmit(row) {
      this.$modal.confirm(this.$t('oa.expense.confirm.submit', { id: row.id })).then(function() {
        return submitReport(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('oa.expense.submitSuccess'))
      }).catch(() => {})
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.expense.confirm.deleteReport', { ids })).then(function() {
        return delReport(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    }
  }
}
</script>
