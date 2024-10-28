import { useTranslation } from "react-i18next";

export const Home = () => {
  const { t } = useTranslation();

  return (
    <div>
      <h1 className="font-bold underline text-primary-500 dark:text-primary-200">
        {t("home.greeting1")}
      </h1>
      <h1 className="font-bold underline text-primary-500 dark:text-primary-200">
        {t("home.greeting2")}
      </h1>
    </div>
  );
};
