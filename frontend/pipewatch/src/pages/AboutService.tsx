import { useTranslation } from "react-i18next";

import Feature1 from "@assets/images/aboutService/feature1.png";
import Feature2 from "@assets/images/aboutService/feature2.png";
import Feature3 from "@assets/images/aboutService/feature3.png";

export const AboutService = () => {
  const { t } = useTranslation();

  const features = [
    { item: "feature1", img: Feature1 },
    { item: "feature2", img: Feature1 },
    { item: "feature3", img: Feature2 },
    { item: "feature4", img: Feature3 },
  ];

  return (
    <div className="flex flex-col items-center justify-center text-center">
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
            <div
              className={`min-w-[600px] max-w-[1200px] my-[50px] ${
                index === 2 && "w-[700px]"
              }`}
            >
              {index !== 0 ? (
                <img src={feature.img} className="object-fill" />
              ) : null}
            </div>

            <div className="flex flex-col items-center my-4 text-center">
              <h2
                className={`font-bold text-[60px] ${
                  index === 0 && "text-primary-200"
                }`}
              >
                {t(`aboutService.${feature.item}.title`)}
              </h2>
              <p
                className={`flex flex-col items-center text-[24px] ${
                  index === 0 && "text-[60px] font-bold mt-[40px]"
                } `}
              >
                {t(`aboutService.${feature.item}.description1`)}
              </p>
              <p
                className={`flex flex-col items-center text-[24px] ${
                  index === 0 &&
                  "text-[60px] font-bold mt-[40px] text-primary-200"
                }`}
              >
                {t(`aboutService.${feature.item}.description2`)}
              </p>
            </div>
          </div>
        );
      })}
    </div>
  );
};
