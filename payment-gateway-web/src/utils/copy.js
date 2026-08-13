import { message } from "antd";
import i18next from "i18next";

export const copyText = (text) => {
  console.log(text,'text')
  const el = document.createElement("textarea");
  el.value = text;
  document.body.appendChild(el);
  el.select();
  document.execCommand("copy");
  document.body.removeChild(el);
  message.success(i18next.t("ReplicatingSuccess"));
};
