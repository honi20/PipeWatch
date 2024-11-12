import NoAccessImage from "@assets/images/status/no_access.png";
import { Button } from "@headlessui/react";
import { useNavigate } from "react-router-dom";

export const AccessBlocked = () => {
  const navigate = useNavigate();

  return (
    <div
      className="flex flex-col items-center justify-center w-full"
      style={{ height: "calc(100vh - 220px)" }}
    >
      <img src={NoAccessImage} width="350px" alt="No Access" />

      <div className="text-center font-bold text-[40px] my-[20px]">
        접근 권한이 없습니다.
      </div>

      <Button
        className={`h-[56px] px-[20px] border-[1px] border-solid dark:border-white  text-white rounded-lg
           dark:bg-button-background bg-primary-500
        `}
        disabled={false}
        onClick={() => navigate("/")}
      >
        홈으로
      </Button>
    </div>
  );
};
