import { SideBar } from "@src/components/enterprise/SideBar";
import { Outlet } from "react-router-dom";

import { AccessBlocked } from "@src/components/common/AccessBlocked";
// import { useUserStore } from "@src/stores/userStore";
import { useState } from "react";
import { getApiClient } from "@src/stores/apiClient";

import { useEffect } from "react";

export const Enterprise = () => {
  const [role, setRole] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const saveRole = async () => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient.get("/api/users/profile");
      const userInfo = res.data.body;
      setRole(userInfo.role);
    } catch (err) {
      console.error("UserInfo 저장 실패", err);
    } finally {
      setIsLoading(true);
    }
  };

  useEffect(() => {
    saveRole();
  }, []);

  if (role === null && isLoading) {
    return <div>로딩중</div>;
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
