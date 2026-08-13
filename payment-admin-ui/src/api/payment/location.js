import request from '@/utils/request'

// 获取省州列表
export function getStates(countryCode) {
    return request({
        url: '/payment/location/states',
        method: 'get',
        params: { countryCode }
    })
}

// 获取城市列表
export function getCities(countryCode, stateName) {
    return request({
        url: '/payment/location/cities',
        method: 'get',
        params: { countryCode, stateName }
    })
}
