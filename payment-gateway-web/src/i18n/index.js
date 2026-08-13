import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import en from "./en.json";

i18n.use(initReactI18next).init({
  debug: false,
  lng: "en",
  fallbackLng: "en",
  supportedLngs: ["en"],
  resources: {
    en: {
      translation: en,
    },
  },
  interpolation: {
    escapeValue: false,
  },
});

// 默认当前浏览器语言
i18n.resolvedLanguage;

export default i18n;
