import { useTranslation } from "react-i18next";

export const InputData = () => {
  const { t } = useTranslation();

  return <div>{t("pipeGenerator.inputData.title")}</div>;
};