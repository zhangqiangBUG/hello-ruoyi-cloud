package com.ruoyi.system.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.domain.SysRole;
import com.ruoyi.system.api.model.LoginUser;
import com.ruoyi.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.system.domain.SysTenant;
import com.ruoyi.system.service.ISysTenantService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 租户信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-29
 */
@RestController
@RequestMapping("/tenant")
public class SysTenantController extends BaseController
{
    @Autowired
    private ISysTenantService sysTenantService;
    @Autowired
    private ISysRoleService roleService;

    /**
     * 查询租户信息列表
     */
    @RequiresPermissions("system:tenant:list")
    @GetMapping("/list")
    public TableDataInfo list(SysTenant sysTenant)
    {
        if(StringUtils.isNull(sysTenant.getTenantId())){
            sysTenant.setParentId(SecurityUtils.getLoginUser().getTenantid());
        }
        startPage();
        List<SysTenant> list = sysTenantService.selectSysTenantList(sysTenant);
        return getDataTable(list);
    }

    /**
     * 导出租户信息列表
     */
    @RequiresPermissions("system:tenant:export")
    @Log(title = "租户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysTenant sysTenant)
    {
        if(StringUtils.isNull(sysTenant.getTenantId())){
            sysTenant.setParentId(SecurityUtils.getLoginUser().getTenantid());
        }
        List<SysTenant> list = sysTenantService.selectSysTenantList(sysTenant);
        ExcelUtil<SysTenant> util = new ExcelUtil<SysTenant>(SysTenant.class);
        util.exportExcel(response, list, "租户信息数据");
    }

    /**
     * 获取租户信息详细信息
     */
    @RequiresPermissions("system:tenant:query")
    @GetMapping(value = { "/", "/{tenantId}" })
    public AjaxResult getInfo(@PathVariable(value = "tenantId", required = false) Long tenantId)
    {
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        if (StringUtils.isNotNull(tenantId)){
            SysTenant tenant = sysTenantService.selectSysTenantByTenantId(tenantId);
            ajax.put(AjaxResult.DATA_TAG, tenant);
        }
        return ajax;
    }

    /**
     * 新增租户信息
     */
    @RequiresPermissions("system:tenant:add")
    @Log(title = "租户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysTenant sysTenant)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        sysTenant.setParentId(loginUser.getTenantid());
        sysTenant.setCreateBy(loginUser.getUsername());
        return toAjax(sysTenantService.insertSysTenant(sysTenant));
    }

    /**
     * 修改租户信息
     */
    @RequiresPermissions("system:tenant:edit")
    @Log(title = "租户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysTenant sysTenant)
    {
        return toAjax(sysTenantService.updateSysTenant(sysTenant));
    }

    /**
     * 删除租户信息
     */
    @RequiresPermissions("system:tenant:remove")
    @Log(title = "租户信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{tenantIds}")
    public AjaxResult remove(@PathVariable Long[] tenantIds)
    {
        return toAjax(sysTenantService.deleteSysTenantByTenantIds(tenantIds));
    }

    /**
     * 状态修改
     */
    @RequiresPermissions("system:tenant:edit")
    @Log(title = "租户状态管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysTenant sysTenant)
    {
        sysTenant.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(sysTenantService.changeTenantStatus(sysTenant));
    }
}
