import { SideBar } from "@src/components/enterprise/SideBar";
import { Outlet } from "react-router-dom";

import { AccessBlocked } from "@src/components/common/AccessBlocked";
import { Loading } from "@src/components/common/Loading";

import useRole from "@src/hooks/useRole";

export const Enterprise = () => {
  const { role, isLoading } = useRole();

  if (role === null && isLoading) {
    return <Loading />;
  }

  if (role !== "ENTERPRISE") {
    <AccessBlocked />;
  }

  return (
    <div className="flex">
      {/* SideBar */}
      <div className="flex flex-col w-[250px] min-h-screen fixed  text-white">
        <SideBar />
      </div>
      {/* Main content */}
      <div className="flex-1 p-5 ml-[250px]">
        <Outlet />
      </div>
    </div>
  );
};
