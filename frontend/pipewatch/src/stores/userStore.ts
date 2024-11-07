import { create } from "zustand";

interface EmployeeInfo {
  empNo: number;
  department: string;
  empClass: string;
}

interface UserInfo {
  name: string;
  email: string;
  role: string;
  state: string;
  enterpriseName: string;
  employee: EmployeeInfo | null;
}

interface UserState {
  isLogin: boolean;
  setLogin: (status: boolean) => void;
  role: string | null;
  setRole: (role: string) => void;
  userInfo: UserInfo | null;
  setUserInfo: (userinfo: UserInfo) => void;
}

export const useUserStore = create<UserState>((set) => ({
  isLogin: false,
  setLogin: (status) => set({ isLogin: status }),
  role: null,
  setRole: (role) => set({ role }),
  userInfo: null,
  setUserInfo: (userInfo) =>
    set({ userInfo, isLogin: true, role: userInfo.role }),
}));
