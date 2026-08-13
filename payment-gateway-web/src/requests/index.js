import { API } from "./api";

// 模版 get
// export const a = async (query) => {
//   return await API.get("aa/bb", query);
// };
// 模版 post
// export const a = async (body) => {
//   return await API.post("aa/bb", body);
// };


//获取订单详情
export const getOrderInfo = async (body) => {
 return await API.get("/web/orderInfo", body);
};
//支付
export const payApi = async (body) => {
  return await API.post("/web/pay", body);
};
export const createStripePayment = async (body) => {
    return await API.get("/web/createStripePayment", body);
};

