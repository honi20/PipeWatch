import { useTranslation } from "react-i18next";

export const Completed = () => {
  const { t } = useTranslation();

  return <div>{t("pipeGenerator.completed.title")}</div>;
};