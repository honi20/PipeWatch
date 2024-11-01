import { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";

export const SideBar = () => {
  const [activeSection, setActiveSection] = useState<number | null>(null);
  const enterpriseName = "개굴전자"; // 기업명 넣을 예정
  const navigate = useNavigate();
  const location = useLocation();
  console.log(location.pathname);
  const handleClick = (index: number): void => {
    // index === 0 인증, index === 1 조회/변경
    switch (index) {
      case 0:
        navigate("/enterprise/verification");
        setActiveSection(0);
        break;
      case 1:
        navigate("/enterprise/view");
        setActiveSection(1);
        break;
    }
  };
  return (
    <div className="flex flex-col fixed w-[250px] h-[500px] bg-block rounded-r-[20px] py-[50px] px-[20px] gap-[30px] text-white">
      {/* header */}
      <div>
        <div className="font-bold px-[10px]">{enterpriseName}</div>
        <div className="px-[10px]">Enterprise</div>
      </div>
      {/* line */}
      <div className="border-b border-white " />
      {/* navbar */}
      <div className="flex flex-col px-[10px] gap-2">
        <div className="font-bold ">사원 관리</div>
        <div
          className="flex items-center gap-2 cursor-pointer"
          onClick={() => handleClick(0)}
        >
          <div
            className={`w-1 h-5 ${
              activeSection === 0 ? "bg-primary-200" : "bg-transparent"
            }`}
          />
          <div>인증</div>
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
          조회/변경
        </div>
      </div>
    </div>
  );
};
