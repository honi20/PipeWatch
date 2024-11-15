import { SideBar } from "@src/components/enterprise/SideBar";
import { Outlet } from "react-router-dom";

import { AccessBlocked } from "@src/components/common/AccessBlocked";
import { useUserStore } from "@src/stores/userStore";

export const Enterprise = () => {
  const { role } = useUserStore();

  return (
    <div className="flex">
      {role !== "ENTERPRISE" ? (
        <AccessBlocked />
      ) : (
        <>
          {/* SideBar */}
          <div className="flex flex-col w-[250px] min-h-screen fixed  text-white">
            <SideBar />
          </div>
          {/* Main content */}
          <div className="flex-1 p-5 ml-[250px]">
            <Outlet />
          </div>
        </>
      )}
    </div>
  );
};
