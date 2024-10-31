import { useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";
import React from "react";
export const CompletedCard = () => {
  const location = useLocation();
  const { t } = useTranslation();
  const pathname = location.pathname;

  let content;

  if (pathname === "/contact/completed") {
    const variable = "기업명"; // 추후 기업명 넣을 예정
    const messages = t("verification.enterprise", {
      variable,
      returnObjects: true,
    });
    const messageArray: string[] = Array.isArray(messages) ? messages : [];
    content = (
      <div className="flex flex-col items-center w-full">
        {messageArray.map((message, index) => {
          const parts = message.split(variable); // 메시지를 variable로 분리
          console.log(parts);
          return (
            <p className="w-full text-center" key={index}>
              {parts.map((part, partIndex) => (
                <React.Fragment key={partIndex}>
                  {part}
                  {partIndex < parts.length - 1 && <strong>{variable}</strong>}
                </React.Fragment>
              ))}
            </p>
          );
        })}
      </div>
    );
  } else if (pathname === "/account/auth/completed") {
    const variable = "paori"; // 추후 사용자ID 넣을 예정
    const messages = t("verification.personal", {
      variable,
      returnObjects: true,
    });
    const messageArray: string[] = Array.isArray(messages) ? messages : [];
    content = (
      <div>
        {messageArray.map((message, index) => {
          const parts = message.split(variable); // 메시지를 variable로 분리
          console.log(parts);
          return (
            <p key={index}>
              {parts.map((part, partIndex) => (
                <React.Fragment key={partIndex}>
                  {part}
                  {partIndex < parts.length - 1 && <strong>{variable}</strong>}
                </React.Fragment>
              ))}
            </p>
          );
        })}
      </div>
    );
  }

  return (
    <div className="flex flex-col h-full bg-block rounded-[30px] p-[50px] gap-[40px]">
      {/* header */}
      <div className="flex justify-center font-semibold text-[28px]">
        {t("verification.title")}
      </div>
      {content}
    </div>
  );
};
