import { useTranslation } from "react-i18next";

export const Rendering = () => {
  const { t } = useTranslation();
  return <div>{t("pipeGenerator.rendering.title")}</div>;
};
