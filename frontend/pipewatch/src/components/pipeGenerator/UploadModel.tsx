import { useTranslation } from "react-i18next";

export const UploadModel = () => {
  const { t } = useTranslation();

  return <div>{t("pipeGenerator.uploadModel.title")}</div>;
};