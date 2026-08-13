import request from "@/utils/request"

// 查询企业客户公司列表
export function listBusinessCustomerCorporation(query) {
    return request({
        url: "/payment/businessCustomerCorporation/list",
        method: "get",
        params: query
    })
}

// 查询企业客户公司列表
export function getFeeModels() {
    return request({
        url: "/payment/merchantFeeModel/list",
        method: "get"
    })
}

// 查询企业客户公司详细
export function getBusinessCustomerCorporation(id) {
    return request({
        url: "/payment/businessCustomerCorporation/" + id,
        method: "get"
    })
}

// 新增企业客户公司
export function addBusinessCustomerCorporation(data) {
    return request({
        url: "/payment/businessCustomerCorporation",
        method: "post",
        data: data
    })
}

// 修改企业客户公司
export function updateBusinessCustomerCorporation(data) {
    return request({
        url: "/payment/businessCustomerCorporation",
        method: "put",
        data: data
    })
}

// 删除企业客户公司
export function delBusinessCustomerCorporation(id) {
    return request({
        url: "/payment/businessCustomerCorporation/" + id,
        method: "delete"
    })
}

// 导出企业客户公司
export function exportBusinessCustomerCorporation(query) {
    return request({
        url: "/payment/businessCustomerCorporation/export",
        method: "get",
        params: query
    })
}

// 审核企业客户公司
export function auditBusinessCustomerCorporation(data) {
    return request({
        url: "/payment/businessCustomerCorporation/audit",
        method: "post",
        data: data
    })
}

// 重置密码
export function resetPassword(userId) {
    return request({
        url: "/payment/businessCustomerCorporation/resetPassword/" + userId,
        method: "post"
    })
}


//获取关联商户IDs
export function getShopIds() {
    return request({
        url: "/payment/businessCustomerCorporation/listByBusinessProfileStatusThree",
        method: "get"
    })
}

