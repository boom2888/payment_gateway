import React from "react";
import { Auth } from "./auth";
import i18n from "i18next";
import { message } from "antd";
import { getClientTimezone } from "../utils/timezoneUtils";

// 测试：
// 正式：
export let BASE_URL = import.meta.env.VITE_API_BASE_URL;

// All backend requests share the token captured from the payment URL.
export const authFetch = (url, options = {}) => {
  const token = typeof localStorage !== "undefined" ? localStorage.getItem("PAY_TOKEN") : null;
  const headers = new Headers(options.headers || {});
  headers.set("Accept", "application/json, text/plain, */*");
  if (token) headers.set("Authorization", `Bearer ${token}`);
  return fetch(url, { ...options, headers });
};

// 对象转键值对
const queryString = (params) =>
  "?" +
  Object.keys(params)
    .map((i) => `${i}=${encodeURIComponent(params[i])}`)
    .join("&");

// 需要加token的路径
const includeUrlList = [];
// 排除不需要加token的路径
const excludeUrlList = [];

const bodyMethod = ["POST", "PUT", "PATCH"];

// 默认超时时间
const timeout = 100000;

const request = async (
  partiaUrl,
  query,
  body,
  method = "GET",
  type = "application/json"
) => {
  // message.destroy();
  // message.loading({
  //   content: i18n.t("Login.加载中"),
  //   duration: 0,
  //   key: "loading",
  // });
  const needBody = bodyMethod.includes(method);
  console.log(body,'body')

  // 请求前拦截器
  const needAuth = !excludeUrlList.some((item) => partiaUrl.startsWith(item));

  const controller = new AbortController();
  const timer = setTimeout(() => controller.abort(), timeout);
  const token = localStorage.getItem("PAY_TOKEN");
  const headers = {
   // Authorization: needAuth && Auth.getToken ? "Bearer " + Auth.getToken : "",
    Authorization: token ? "Bearer " + token : "",
    Accept: "application/json, text/plain, */*",
  };
  const pormise = (
    await fetch(BASE_URL + partiaUrl + (query ? queryString(query) : ""), {
      headers: {
        ...headers,
        // ...(needAuth ? { token: Auth.token ? Auth.token : "" } : {}),
        ...(needBody && type !== null ? { "Content-Type": type } : {}),
        // ...(Auth.token ? "Authorization" : "Bearer " + Auth.token),
        lang: i18n.language,
        timezone: getClientTimezone(),
        // lang: 'en_US',
      },
      method,
      ...(needBody
        ? { body: type === "application/json" ? JSON.stringify(body) : body }
        : {}),
      signal: controller.signal,
    })
  ).json();

  clearTimeout(timer);
  // 响应后拦截器
  const data = await pormise;
  const { code, msg } = data;
  // message.destroy("loading");
    
  if (code === 200) {
   // return pormise;
    return data;
  } else {
    console.log(data,'data')
    throw data;
   // return Promise.reject(pormise);
  }
};

export class API {
  static get(partiaUrl, query) {
    return request(partiaUrl, query);
  }

  static delete(partiaUrl, query) {
    return request(partiaUrl, query, undefined, "DELETE");
  }

  static post(partiaUrl, body, query) {
    return request(partiaUrl, query, body, "POST");
  }

  static postFile(partiaUrl, formDataBody, query) {
    return request(partiaUrl, query, formDataBody, "POST", null);
  }
}
