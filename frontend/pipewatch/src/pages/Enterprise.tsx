import { useState } from "react";
import { SideBar } from "@src/components/enterprise/SideBar";
import { Outlet } from "react-router-dom";

export const Enterprise = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  return (
    <div className="flex">
      {/* 추후 반응형으로 고치기 */}
      <button className="p-2 m-4 lg:hidden" onClick={toggleSidebar}>
        {isSidebarOpen ? "Close Menu" : "Open Menu"}
      </button>

      {/* Sidebar */}
      <div
        className={`${
          isSidebarOpen ? "block" : "hidden"
        } lg:flex flex-col w-[250px] min-h-screen  fixed lg:static`}
      >
        <SideBar />
      </div>

      {/* Table */}
      <div
        className={`flex-1 p-5 ${
          isSidebarOpen ? "ml-[250px]" : "ml-0"
        } lg:ml-0`}
      >
        <Outlet />
      </div>
    </div>
  );
};
