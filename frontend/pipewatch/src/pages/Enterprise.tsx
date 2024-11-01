import { SideBar } from "@src/components/enterprise/SideBar";
import { Outlet } from "react-router-dom";

export const Enterprise = () => {
  return (
    <div className="flex">
      {/* Sidebar */}
      <div className="flex flex-col w-[250px] min-h-screen fixed  text-white">
        <SideBar />
      </div>

      {/* Main Content */}
      <div className="flex-1 p-5 ml-[250px]">
        <Outlet />
      </div>
    </div>
  );
};
