import request from "@/utils/request"

export function getTopMetricsDataApi(query) {
  return request({
    url: "/payment/index/orderStatistics",
    method: "get",
    params: query
  })
}

export function getMerchantRankingApi(query) {
  return request({
    url: "/payment/index/shopRankStatistics",
    method: "get",
    params: query
  })
}

export function getOrderTrendDataApi(query) {
  return request({
    url: "/payment/index/orderNumCurve",
    method: "get",
    params: query
  })
}

export function getOrderTurnoverDataApi(query) {
  return request({
    url: "/payment/index/orderAmountCurve",
    method: "get",
    params: query
  })
}

export function getOrderAverageDataApi(query) {
  return request({
    url: "/payment/index/orderAvgAmountCurve",
    method: "get",
    params: query
  })
}

export function getRefundRateDataApi(query) {
  return request({
    url: "/payment/index/orderRateCurve",
    method: "get",
    params: query
  })
}

export function getPaymentMethodDataApi(query) {
  return request({
    url: "/payment/index/paymentMethodStats",
    method: "get",
    params: query
  })
}

export function getHourlySuccessRateApi(query) {
  return request({
    url: "/payment/index/hourlySuccessRate",
    method: "get",
    params: query
  })
}

export function getFailReasonStatsApi(query) {
  return request({
    url: "/payment/index/failReasonStats",
    method: "get",
    params: query
  })
}
