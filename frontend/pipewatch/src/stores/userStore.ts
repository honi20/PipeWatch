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
  name: string | null;
  setName: (name: string) => void;
  role: string | null;
  setRole: (role: string) => void;
  userState: string | null;
  setUserState: (state: string) => void;
  userInfo: UserInfo | null;
  setUserInfo: (userinfo: UserInfo | null) => void;
}

export const useUserStore = create<UserState>((set) => ({
  isLogin: false,
  setLogin: (status) => set({ isLogin: status }),
  name: null,
  setName: (name) => set({ name }),
  role: null,
  setRole: (role) => set({ role }),
  userState: null,
  setUserState: (userState) => set({ userState }),
  userInfo: null,
  setUserInfo: (userInfo) =>
    set({ userInfo, isLogin: true, role: userInfo?.role }),
}));
