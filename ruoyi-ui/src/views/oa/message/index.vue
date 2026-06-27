<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('oa.message.title')" prop="title">
        <el-input
          v-model="queryParams.title"
          :placeholder="$t('oa.message.placeholder.title')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('oa.message.type')" prop="type">
        <el-select v-model="queryParams.type" :placeholder="$t('oa.message.placeholder.type')" clearable>
          <el-option :label="$t('oa.message.todo')" value="todo" />
          <el-option :label="$t('oa.message.result')" value="result" />
          <el-option :label="$t('oa.message.notice')" value="notice" />
          <el-option :label="$t('oa.message.system')" value="system" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('oa.message.status')" prop="recipientStatus">
        <el-select v-model="queryParams.recipientStatus" :placeholder="$t('oa.message.status')" clearable>
          <el-option :label="$t('oa.message.unread')" :value="0" />
          <el-option :label="$t('oa.message.read')" :value="1" />
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
          @click="handleSend"
          v-hasPermi="['oa:message:send']"
        >{{ $t('oa.message.sendMessage') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-check"
          size="mini"
          :disabled="multiple"
          @click="handleMarkRead"
          v-hasPermi="['oa:message:query']"
        >{{ $t('oa.message.markRead') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['oa:message:remove']"
        >{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="messageList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.message.title')" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.recipientStatus === 0" size="mini" type="danger">{{ $t('oa.message.unread') }}</el-tag>
          <a class="link-type" style="cursor:pointer" @click="handleView(scope.row)">{{ scope.row.title }}</a>
        </template>
      </el-table-column>
      <el-table-column :label="$t('oa.message.type')" align="center" prop="type" width="100">
        <template slot-scope="scope">
          <span>{{ $t('oa.message.' + (scope.row.type || 'system')) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('oa.message.sender')" align="center" prop="sender" width="120" />
      <el-table-column :label="$t('oa.message.priority')" align="center" prop="priority" width="100">
        <template slot-scope="scope">
          <span>{{ $t('oa.message.' + (scope.row.priority || 'normal')) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('oa.message.status')" align="center" prop="recipientStatus" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.recipientStatus === 1" type="success">{{ $t('oa.message.read') }}</el-tag>
          <el-tag v-else type="warning">{{ $t('oa.message.unread') }}</el-tag>
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
          >{{ $t('oa.message.view') }}</el-button>
          <el-button
            v-if="scope.row.recipientStatus === 0"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleMarkRead(scope.row)"
            v-hasPermi="['oa:message:query']"
          >{{ $t('oa.message.markRead') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:message:remove']"
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

    <!-- 发送消息 -->
    <el-dialog :title="$t('oa.message.sendMessage')" :visible.sync="sendOpen" width="700px" append-to-body>
      <el-form ref="sendForm" :model="sendForm" :rules="sendRules" label-width="100px">
        <el-form-item :label="$t('oa.message.type')" prop="type">
          <el-select v-model="sendForm.type" :placeholder="$t('oa.message.placeholder.type')" style="width:100%">
            <el-option :label="$t('oa.message.todo')" value="todo" />
            <el-option :label="$t('oa.message.result')" value="result" />
            <el-option :label="$t('oa.message.notice')" value="notice" />
            <el-option :label="$t('oa.message.system')" value="system" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.message.title')" prop="title">
          <el-input v-model="sendForm.title" :placeholder="$t('oa.message.placeholder.title')" />
        </el-form-item>
        <el-form-item :label="$t('oa.message.content')" prop="content">
          <el-input v-model="sendForm.content" type="textarea" :rows="4" :placeholder="$t('oa.message.placeholder.content')" />
        </el-form-item>
        <el-form-item :label="$t('oa.message.priority')" prop="priority">
          <el-select v-model="sendForm.priority" :placeholder="$t('oa.message.placeholder.priority')" style="width:100%">
            <el-option :label="$t('oa.message.low')" value="low" />
            <el-option :label="$t('oa.message.normal')" value="normal" />
            <el-option :label="$t('oa.message.high')" value="high" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.message.channel')" prop="channel">
          <el-select v-model="sendForm.channel" :placeholder="$t('oa.message.placeholder.channel')" style="width:100%">
            <el-option :label="$t('oa.message.site')" value="site" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.message.recipient')" prop="recipientUserIds">
          <UserMultiSelect v-model="sendForm.recipientUserIds" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSend">{{ $t('common.submit') }}</el-button>
        <el-button @click="cancelSend">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>

    <!-- 查看消息 -->
    <el-dialog :title="$t('oa.message.viewMessage')" :visible.sync="viewOpen" width="600px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item :label="$t('oa.message.title')">{{ viewForm.title }}</el-descriptions-item>
        <el-descriptions-item :label="$t('oa.message.type')">{{ $t('oa.message.' + (viewForm.type || 'system')) }}</el-descriptions-item>
        <el-descriptions-item :label="$t('oa.message.sender')">{{ viewForm.sender }}</el-descriptions-item>
        <el-descriptions-item :label="$t('common.createTime')">{{ parseTime(viewForm.createTime) }}</el-descriptions-item>
        <el-descriptions-item :label="$t('oa.message.content')">
          <div v-html="viewForm.content"></div>
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">{{ $t('common.close') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMessage, sendMessage, readMessage, delMessage } from "@/api/oa/message"
import UserMultiSelect from "@/components/UserMultiSelect"

export default {
  name: "OaMessage",
  components: { UserMultiSelect },
  data() {
    return {
      loading: true,
      ids: [],
      recipientIds: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      messageList: [],
      sendOpen: false,
      viewOpen: false,
      sendForm: {},
      viewForm: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        type: undefined,
        recipientStatus: undefined
      },
      sendRules: {
        title: [
          { required: true, message: this.$t('oa.message.required.title'), trigger: "blur" }
        ],
        content: [
          { required: true, message: this.$t('oa.message.required.content'), trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listMessage(this.queryParams).then(response => {
        this.messageList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancelSend() {
      this.sendOpen = false
      this.resetSend()
    },
    resetSend() {
      this.sendForm = {
        type: "system",
        title: undefined,
        content: undefined,
        priority: "normal",
        channel: "site",
        recipientUserIds: ""
      }
      this.resetForm("sendForm")
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
      this.recipientIds = selection.map(item => item.recipientId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleSend() {
      this.resetSend()
      this.sendOpen = true
    },
    submitSend() {
      this.$refs["sendForm"].validate(valid => {
        if (valid) {
          const data = { ...this.sendForm }
          data.recipientUserIds = String(data.recipientUserIds || "")
            .split(",")
            .map(s => Number(s.trim()))
            .filter(id => !isNaN(id) && id > 0)
          sendMessage(data).then(() => {
            this.$modal.msgSuccess(this.$t('oa.message.sendSuccess'))
            this.sendOpen = false
            this.getList()
          })
        }
      })
    },
    handleView(row) {
      this.viewForm = row
      this.viewOpen = true
      if (row.recipientStatus === 0) {
        readMessage([row.recipientId]).then(() => {
          row.recipientStatus = 1
        })
      }
    },
    handleMarkRead(row) {
      const ids = row ? [row.recipientId] : this.recipientIds
      if (!ids || ids.length === 0) {
        this.$modal.msgError(this.$t('oa.message.pleaseSelect'))
        return
      }
      this.$modal.confirm(this.$t('oa.message.confirm.markRead')).then(() => {
        return readMessage(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('oa.message.markReadSuccess'))
      }).catch(() => {})
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.message.confirm.deleteMessage', { ids })).then(function() {
        return delMessage(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    }
  }
}
</script>
