import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import enTranslations from "../locales/en/translation.json";
import koTranslations from "../locales/ko/translation.json";

i18n.use(initReactI18next).init({
  resources: {
    ko: { translation: koTranslations },
    en: { translation: enTranslations },
  },
  lng: "ko",
  fallbackLng: "ko",
  interpolation: {
    escapeValue: false,
  },
});

export default i18n;
