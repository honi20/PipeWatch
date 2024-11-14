import { getApiClient } from "@src/stores/apiClient";
import { persist } from "zustand/middleware";
import { create } from "zustand";

interface DefectState {
  viewDefect: { [modelId: number]: boolean };
  setViewDefect: (modelId: number, viewDefect: boolean) => void;
  defectedPipeList: number[] | null;
  setDefectedPipeList: (defectedPipeList: number[] | null) => void;
  getDefectedPipeList: (modelId: number) => Promise<void>;
  checkPipeDefection: (pipeId: number) => Promise<void>;
}

export const useDefectStore = create<
  DefectState,
  [["zustand/persist", DefectState]]
>(
  persist(
    (set, get) => ({
      // 결함 조회 여부
      viewDefect: {},
      setViewDefect: (modelId, viewDefect) => {
        const currentViewDefect = get().viewDefect;
        set({
          viewDefect: {
            ...currentViewDefect,
            [modelId]: viewDefect,
          },
        });
      },
      defectedPipeList: null,
      setDefectedPipeList: (defectedPipeList) => set({ defectedPipeList }),
      getDefectedPipeList: async (modelId) => {
        const apiClient = getApiClient();
        try {
          const res = await apiClient({
            method: "get",
            url: `/api/models/${modelId}`,
          });
          console.log(res.data.header.httpStatusCode, res.data.header.message);
          console.log(res.data.body);
          set({ defectedPipeList: res.data.body.defectPipeList });
        } catch (err) {
          console.log(err);
        }
      },
      checkPipeDefection: async (pipeId) => {
        const apiClient = getApiClient();
        try {
          const res = await apiClient({
            method: "patch",
            url: `/api/pipelines/pipes/${pipeId}/defect`,
          });
          console.log(res.data.header.httpStatusCode, res.data.header.message);
          const currentList = get().defectedPipeList || [];

          // pipeId가 리스트에 있으면 제거, 없으면 추가
          const updatedList = currentList.includes(pipeId)
            ? currentList.filter((id) => id !== pipeId)
            : [...currentList, pipeId];

          set({ defectedPipeList: updatedList });
        } catch (err) {
          console.log(err);
        }
      },
    }),
    {
      name: "defectStore",
    }
  )
);
