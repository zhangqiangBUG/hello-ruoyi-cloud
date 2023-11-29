package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysTenantMapper;
import com.ruoyi.system.domain.SysTenant;
import com.ruoyi.system.service.ISysTenantService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-29
 */
@Service
public class SysTenantServiceImpl implements ISysTenantService 
{
    @Autowired
    private SysTenantMapper sysTenantMapper;
    @Autowired
    private ISysUserService userService;

    /**
     * 查询租户信息
     * 
     * @param tenantId 租户信息主键
     * @return 租户信息
     */
    @Override
    public SysTenant selectSysTenantByTenantId(Long tenantId)
    {
        return sysTenantMapper.selectSysTenantByTenantId(tenantId);
    }

    /**
     * 查询租户信息列表
     * 
     * @param sysTenant 租户信息
     * @return 租户信息
     */
    @Override
    public List<SysTenant> selectSysTenantList(SysTenant sysTenant)
    {
        return sysTenantMapper.selectSysTenantList(sysTenant);
    }

    /**
     * 新增租户信息
     * 
     * @param sysTenant 租户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertSysTenant(SysTenant sysTenant)
    {
        sysTenant.setCreateTime(DateUtils.getNowDate());

        SysTenant tenant = sysTenantMapper.checkTenantNameUnique(sysTenant.getTenantName());
        if(tenant!=null){
            throw new ServiceException(sysTenant.getTenantName()+":租户账号已存在");
        }
        sysTenantMapper.insertSysTenant(sysTenant);
        tenant = sysTenantMapper.checkTenantNameUnique(sysTenant.getTenantName());

        SysUser user = new SysUser();
        user.setUserName(sysTenant.getTenantName());
        user.setNickName(sysTenant.getNickName());
        user.setPhonenumber(sysTenant.getPhonenumber());
        user.setEmail(sysTenant.getEmail());
        user.setTenantId(tenant.getTenantId());
        user.setRoleIds(new Long[]{sysTenant.getRoleId()}); // 权限角色
        user.setPassword("111111");          // 默认密码
        user.setUserType("01");              // 默认用户类型（00-系统用户、01-普通用户）
        user.setCreateBy(sysTenant.getCreateBy());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));

        return userService.insertUser(user);
    }

    /**
     * 修改租户信息
     * 
     * @param sysTenant 租户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateSysTenant(SysTenant sysTenant)
    {
        if(sysTenant.isAdmin()){
            throw new ServiceException("不允许操作超级租户");
        }
        sysTenant.setUpdateTime(DateUtils.getNowDate());
        return sysTenantMapper.updateSysTenant(sysTenant);
    }

    /**
     * 批量删除租户信息
     * 
     * @param tenantIds 需要删除的租户信息主键
     * @return 结果
     */
    @Override
    public int deleteSysTenantByTenantIds(Long[] tenantIds)
    {
        return sysTenantMapper.deleteSysTenantByTenantIds(tenantIds);
    }

    /**
     * 删除租户信息信息
     * 
     * @param tenantId 租户信息主键
     * @return 结果
     */
    @Override
    public int deleteSysTenantByTenantId(Long tenantId)
    {
        return sysTenantMapper.deleteSysTenantByTenantId(tenantId);
    }

    /**
     * 修改租户状态
     *
     * @param sysTenant 租户信息
     * @return 结果
     */
    public int changeTenantStatus(SysTenant sysTenant){
        return this.updateSysTenant(sysTenant);
    }
}
