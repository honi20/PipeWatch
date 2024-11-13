import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";

export const SideBar = () => {
  const [activeSection, setActiveSection] = useState<number | null>(null);

  const enterpriseName = localStorage.getItem("enterpriseName");
  const navigate = useNavigate();
  const location = useLocation();
  const { t } = useTranslation();

  useEffect(() => {
    switch (location.pathname) {
      case "/enterprise/verification":
        setActiveSection(0);
        break;
      case "/enterprise/view":
        setActiveSection(1);
        break;
      default:
        setActiveSection(null);
        break;
    }
  }, [location.pathname]);

  const handleClick = (index: number): void => {
    // index === 0 인증, index === 1 조회/변경
    switch (index) {
      case 0:
        navigate("/enterprise/verification");
        break;
      case 1:
        navigate("/enterprise/view");
        break;
    }
  };
  return (
    <div className="flex flex-col fixed w-[250px] h-[500px] bg-block rounded-r-[20px] py-[50px] px-[20px] gap-[30px] text-white">
      {/* header */}
      <div>
        <div className="font-bold px-[10px]">{enterpriseName}</div>
        <div className="px-[10px]">{t("enterprise.enterprise")}</div>
      </div>
      {/* line */}
      <div className="border-b border-white " />
      {/* navbar */}
      <div className="flex flex-col px-[10px] gap-2">
        <div className="font-bold ">{t("enterprise.manage")}</div>
        <div
          className="flex items-center gap-2 cursor-pointer"
          onClick={() => handleClick(0)}
        >
          <div
            className={`w-1 h-5 ${
              activeSection === 0 ? "bg-primary-200" : "bg-transparent"
            }`}
          />
          <div>{t("enterprise.verification.sidebarTitle")}</div>
        </div>
        <div
          className="flex items-center gap-2 cursor-pointer"
          onClick={() => handleClick(1)}
        >
          <div
            className={`w-1 h-5 ${
              activeSection === 1 ? "bg-primary-200" : "bg-transparent"
            }`}
          />
          {t("enterprise.view.sidebarTitle")}
        </div>
      </div>
    </div>
  );
};
