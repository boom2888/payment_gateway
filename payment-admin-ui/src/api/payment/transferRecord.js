import request from '@/utils/request'

// 查询转账记录列表
export function listTransferRecord(query) {
    return request({
        url: '/payment/transferRecord/list',
        method: 'get',
        params: query
    })
}

// 查询转账记录详细
export function getTransferRecord(id) {
    return request({
        url: '/payment/transferRecord/' + id,
        method: 'get'
    })
}
