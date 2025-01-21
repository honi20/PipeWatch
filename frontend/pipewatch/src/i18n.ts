import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import enTranslations from "@locales/en/translation.json";
import koTranslations from "@locales/ko/translation.json";

type LanguageType = "ko" | "en";

export const getLanguageFromStorage = (): LanguageType | null => {
  return localStorage.getItem("language") as LanguageType | null;
};

const initialLanguage: LanguageType = getLanguageFromStorage() || "ko";

i18n.use(initReactI18next).init({
  resources: {
    ko: { translation: koTranslations },
    en: { translation: enTranslations },
  },
  // lng: "ko",
  lng: initialLanguage,
  fallbackLng: "ko",
  interpolation: {
    escapeValue: false,
  },
});

export default i18n;
