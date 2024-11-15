import { create } from "zustand";
import { getApiClient } from "@src/stores/apiClient";

export interface MemoType {
  memo: string;
  memoId: number;
  writer: {
    userUuid: string;
    userName: string;
  };
  createdAt: string;
}

interface MemoState {
  memo: string;
  memoList: MemoType[] | null;
  setMemo: (memo: string) => void;
  getMemoList: (modelId: number) => Promise<void>;
  setMemoList: (memoList: MemoType[] | null) => void;
  postMemo: (modelId: number, memo: string) => Promise<void>;
  deleteMemo: (memoId: number) => Promise<void>;
}

export const useMemoStore = create<MemoState>((set, get) => ({
  memo: "",
  memoList: null,

  setMemo: (memo) => set({ memo }),
  setMemoList: (memoList) => set({ memoList }),

  getMemoList: async (modelId) => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "get",
        url: `/api/models/${modelId}/memos`,
      });
      console.log(res.data.header.httpStatusCode, res.data.header.message);
      console.log(res.data.body);
      set({ memoList: res.data.body.memoList });
    } catch (err) {
      console.log(err);
    }
  },

  postMemo: async (modelId, memo) => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "post",
        url: `/api/models/${modelId}/memos`,
        data: {
          memo: memo,
        },
      });
      console.log(res.data.header.httpStatusCode, res.data.header.message);
      // 메모 저장
      console.log(res.data)
      await get().getMemoList(modelId);
    } catch (err) {
      console.log(err);
    }
  },

  deleteMemo: async (memoId: number) => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "delete",
        url: `/api/models/memos/${memoId}`,
      });
      console.log(res);
      const currentList = get().memoList || [];
      const updatedMemoList = currentList
        ? currentList.filter((item) => item.memoId !== memoId)
        : [];
      set({ memoList: updatedMemoList });
    } catch (err) {
      console.log(err);
    }
  },
}));
