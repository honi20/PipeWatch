import { create } from "zustand";
import { getApiClient } from "@src/stores/apiClient";
import { MemoType } from "@src/components/pipeViewer/PipeType";

interface MemoState {
  memo: string;
  memoList: MemoType[] | null;
  setMemo: (memo: string) => void;
  getMemoList: (modelId: number) => Promise<void>;
  setMemoList: (memoList: MemoType[] | null) => void;
  postMemo: (modelId: number, memo: string) => Promise<void>;
  getPipeMemo: (pipeId: number) => Promise<void>;
  postPipeMemo: (pipeId: number, memo: string) => Promise<void>;
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
        url: `/api/models/memos/${modelId}`,
      });
      console.log(res.data.header.httpStatusCode, res.data.header.message);
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
        url: `/api/models/memos/${modelId}`,
        data: {
          memo: memo,
        },
      });
      console.log(res.data.header.httpStatusCode, res.data.header.message);
      // 메모 저장
      await get().getMemoList(modelId);
    } catch (err) {
      console.log(err);
    }
  },

  getPipeMemo: async (pipeId) => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "get",
        url: `/api/pipelines/pipes/${pipeId}`,
      });
      console.log(res.data.header.httpStatusCode, res.data.header.message);
      set({ memoList: res.data.body.memoList });
    } catch (err) {
      console.log(err);
    }
  },

  postPipeMemo: async (pipeId, memo) => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "post",
        url: `/api/models/memos/${pipeId}`,
        data: {
          memo: memo,
        },
      });
      console.log(res.data.header.httpStatusCode, res.data.header.message);
      // 메모 저장
      // await get().getMemoList(pipeId);
      set({ memoList: res.data.body.memoList });
    } catch (err) {
      console.log(err);
    }
  },
}));
