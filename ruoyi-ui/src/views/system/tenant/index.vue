<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="父级租户ID" prop="parentId">-->
<!--        <el-input-->
<!--          v-model="queryParams.parentId"-->
<!--          placeholder="请输入父级租户ID"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="租户账号" prop="tenantName">
        <el-input
          v-model="queryParams.tenantName"
          placeholder="请输入租户账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="租户昵称" prop="nickName">
        <el-input
          v-model="queryParams.nickName"
          placeholder="请输入租户昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="租户权限" prop="roleId">-->
<!--        <el-input-->
<!--          v-model="queryParams.roleId"-->
<!--          placeholder="请输入租户权限"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="租户邮箱" prop="email">-->
<!--        <el-input-->
<!--          v-model="queryParams.email"-->
<!--          placeholder="请输入租户邮箱"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="手机号码" prop="phonenumber">-->
<!--        <el-input-->
<!--          v-model="queryParams.phonenumber"-->
<!--          placeholder="请输入手机号码"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="LOGO地址" prop="logo">-->
<!--        <el-input-->
<!--          v-model="queryParams.logo"-->
<!--          placeholder="请输入LOGO地址"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="背景地址" prop="background">-->
<!--        <el-input-->
<!--          v-model="queryParams.background"-->
<!--          placeholder="请输入背景地址"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
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
          @click="handleAdd"
          v-hasPermi="['system:tenant:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:tenant:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:tenant:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:tenant:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tenantList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="租户ID" align="center" prop="tenantId" />-->
      <el-table-column label="租户账号" align="center" prop="tenantName" />
      <el-table-column label="租户昵称" align="center" prop="nickName" />
<!--      <el-table-column label="租户权限" align="center" prop="roleId" />-->
      <el-table-column label="租户邮箱" align="center" prop="email" />
      <el-table-column label="手机号码" align="center" prop="phonenumber" />
<!--      <el-table-column label="LOGO地址" align="center" prop="logo" />-->
<!--      <el-table-column label="背景地址" align="center" prop="background" />-->
      <el-table-column label="租户状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-switch
            disabled="scope.row.id !== '1' ? true : false"
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope" v-if="scope.row.tenantId !== 1">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:tenant:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:tenant:remove']"
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

    <!-- 添加或修改租户信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
<!--        <el-form-item label="父级租户ID" prop="parentId">-->
<!--          <el-input v-model="form.parentId" placeholder="请输入父级租户ID" />-->
<!--        </el-form-item>-->
        <el-form-item label="租户账号" prop="tenantName">
          <el-input v-model="form.tenantName" placeholder="请输入租户账号" />
        </el-form-item>
        <el-form-item label="租户昵称" prop="nickName">
          <el-input v-model="form.nickName" placeholder="请输入租户昵称" />
        </el-form-item>
        <el-form-item label="租户权限" prop="roleId">
<!--          <el-input v-model="form.roleId" placeholder="请输入租户权限" />-->
          <el-select v-model="form.roleId" placeholder="请选择角色">
            <el-option
              v-for="item in roleOptions"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId"
              :disabled="item.status == 1"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="租户邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入租户邮箱" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phonenumber">
          <el-input v-model="form.phonenumber" placeholder="请输入手机号码" />
        </el-form-item>
<!--        <el-form-item label="LOGO地址" prop="logo">-->
<!--          <el-input v-model="form.logo" placeholder="请输入LOGO地址" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="背景地址" prop="background">-->
<!--          <el-input v-model="form.background" placeholder="请输入背景地址" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="删除标志" prop="delFlag">-->
<!--          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />-->
<!--        </el-form-item>-->
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTenant, getTenant, delTenant, addTenant, updateTenant, changeTenantStatus } from "@/api/system/tenant";

export default {
  name: "Tenant",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 租户信息表格数据
      tenantList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        parentId: null,
        tenantName: null,
        nickName: null,
        tenantType: null,
        roleId: null,
        email: null,
        phonenumber: null,
        logo: null,
        background: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        tenantName: [
          { required: true, message: "租户账号不能为空", trigger: "blur" }
        ],
        nickName: [
          { required: true, message: "租户昵称不能为空", trigger: "blur" }
        ],
        roleId: [
          { required: true, message: "租户权限不能为空", trigger: "blur" }
        ],
      },
      // 角色选项
      roleOptions: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询租户信息列表 */
    getList() {
      this.loading = true;
      listTenant(this.queryParams).then(response => {
        this.tenantList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        tenantId: null,
        parentId: null,
        tenantName: null,
        nickName: null,
        tenantType: null,
        roleId: null,
        email: null,
        phonenumber: null,
        logo: null,
        background: null,
        status: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.tenantId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    // 租户状态修改
    handleStatusChange(row) {
        let text = row.status === "0" ? "启用" : "停用";
        this.$modal.confirm('确认要"' + text + '""' + row.tenantName + '"租户吗？').then(function() {
            return changeTenantStatus(row.tenantId, row.status);
        }).then(() => {
            this.$modal.msgSuccess(text + "成功");
        }).catch(function() {
            row.status = row.status === "0" ? "1" : "0";
        });
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      getTenant().then(response => {
        this.roleOptions = response.roles;
        this.open = true;
        this.title = "添加租户信息";
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const tenantId = row.tenantId || this.ids
      getTenant(tenantId).then(response => {
        this.form = response.data;
        this.roleOptions = response.roles;
        this.open = true;
        this.title = "修改租户信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.tenantId != null) {
            updateTenant(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTenant(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const tenantIds = row.tenantId || this.ids;
      this.$modal.confirm('是否确认删除租户信息编号为"' + tenantIds + '"的数据项？').then(function() {
        return delTenant(tenantIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/tenant/export', {
        ...this.queryParams
      }, `tenant_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
