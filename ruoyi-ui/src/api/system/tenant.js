import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询租户信息列表
export function listTenant(query) {
  return request({
    url: '/system/tenant/list',
    method: 'get',
    params: query
  })
}

// 查询租户信息详细
export function getTenant(tenantId) {
  return request({
    url: '/system/tenant/' + parseStrEmpty(tenantId),
    method: 'get'
  })
}

// 新增租户信息
export function addTenant(data) {
  return request({
    url: '/system/tenant',
    method: 'post',
    data: data
  })
}

// 修改租户信息
export function updateTenant(data) {
  return request({
    url: '/system/tenant',
    method: 'put',
    data: data
  })
}

// 删除租户信息
export function delTenant(tenantId) {
  return request({
    url: '/system/tenant/' + tenantId,
    method: 'delete'
  })
}

// 租户状态修改
export function changeTenantStatus(tenantId, status) {
  const data = {
    tenantId,
    status
  }
  return request({
    url: '/system/tenant/changeStatus',
    method: 'put',
    data: data
  })
}
