import { useTranslation } from "react-i18next";
import SampleImage from "@assets/images/sample/sample.png";

export const AboutService = () => {
  const { t } = useTranslation();

  const features = ["feature1", "feature2", "feature3", "feature4"];

  return (
    <div className="flex flex-col items-center justify-center">
      <h1 className="font-bold text-[80px] font-Esamanru">We are PIPE WATCH</h1>
      <div className="flex flex-col items-center my-4">
        <p className="text-[32px]">{t("aboutService.description1")}</p>
        <p className="text-[32px]">{t("aboutService.description2")}</p>
      </div>

      {features.map((feature, index) => {
        return (
          <div
            key={index}
            className="flex flex-col items-center justify-center my-10"
          >
            <img src={SampleImage} width={"300px"} />
            <div className="flex flex-col items-center my-4">
              <h2 className="font-bold text-[60px]">
                {t(`aboutService.${feature}.title`)}
              </h2>
              <p className="flex flex-col items-center text-[24px]">
                {t(`aboutService.${feature}.description1`)}
              </p>
              <p className="flex flex-col items-center text-[24px]">
                {t(`aboutService.${feature}.description2`)}
              </p>
            </div>
          </div>
        );
      })}
    </div>
  );
};
