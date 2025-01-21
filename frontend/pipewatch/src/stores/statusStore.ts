import { create } from "zustand";

interface StatusState {
  isSuccess: boolean;
  isFailed: boolean;
  setIsSuccess: (status: boolean) => void;
  setIsFailed: (status: boolean) => void;
}

export const statusStore = create<StatusState>((set) => ({
  isSuccess: false,
  isFailed: false,
  setIsSuccess: (status: boolean) => {
    set({ isSuccess: status });

    setTimeout(() => {
      set({ isSuccess: false });
    }, 3000);
  },
  setIsFailed: (status: boolean) => {
    set({ isFailed: status });

    setTimeout(() => {
      set({ isFailed: false });
    }, 3000);
  },
}));
