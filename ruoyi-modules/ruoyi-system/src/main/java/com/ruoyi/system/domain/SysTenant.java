package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 租户信息对象 sys_tenant
 * 
 * @author ruoyi
 * @date 2023-11-29
 */
public class SysTenant extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 租户ID */
    private Long tenantId;

    /** 父级租户ID */
    private Long parentId;

    /** 租户账号 */
    @Excel(name = "租户账号")
    private String tenantName;

    /** 租户昵称 */
    @Excel(name = "租户昵称")
    private String nickName;

    /** 租户类型（00系统租户） */
    private String tenantType;

    /** 租户权限 */
    @Excel(name = "租户权限")
    private Long roleId;

    /** 租户邮箱 */
    @Excel(name = "租户邮箱")
    private String email;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;

    /** LOGO地址 */
    @Excel(name = "LOGO地址")
    private String logo;

    /** 背景地址 */
    @Excel(name = "背景地址")
    private String background;

    /** 租户状态（0正常 1停用） */
    @Excel(name = "租户状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setTenantId(Long tenantId) 
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId() 
    {
        return tenantId;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }
    public void setTenantName(String tenantName) 
    {
        this.tenantName = tenantName;
    }

    public String getTenantName() 
    {
        return tenantName;
    }
    public void setNickName(String nickName) 
    {
        this.nickName = nickName;
    }

    public String getNickName() 
    {
        return nickName;
    }
    public void setTenantType(String tenantType) 
    {
        this.tenantType = tenantType;
    }

    public String getTenantType() 
    {
        return tenantType;
    }
    public void setRoleId(Long roleId) 
    {
        this.roleId = roleId;
    }

    public Long getRoleId() 
    {
        return roleId;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setPhonenumber(String phonenumber) 
    {
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber() 
    {
        return phonenumber;
    }
    public void setLogo(String logo) 
    {
        this.logo = logo;
    }

    public String getLogo() 
    {
        return logo;
    }
    public void setBackground(String background) 
    {
        this.background = background;
    }

    public String getBackground() 
    {
        return background;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.tenantId);
    }

    public static boolean isAdmin(Long tenantId)
    {
        return tenantId != null && 1L == tenantId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("tenantId", getTenantId())
            .append("parentId", getParentId())
            .append("tenantName", getTenantName())
            .append("nickName", getNickName())
            .append("tenantType", getTenantType())
            .append("roleId", getRoleId())
            .append("email", getEmail())
            .append("phonenumber", getPhonenumber())
            .append("logo", getLogo())
            .append("background", getBackground())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
