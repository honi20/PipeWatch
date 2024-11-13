import { useTranslation } from "react-i18next";
import React, { useEffect, ChangeEvent, useState } from "react";
import { Textarea, Checkbox } from "@headlessui/react";
import clsx from "clsx";
import DeleteForeverIcon from "@mui/icons-material/DeleteForever";
import { useMemoStore } from "@src/stores/memoStore";
import { usePipe } from "@src/components/context/PipeContext";
import { getApiClient } from "@src/stores/apiClient";
import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";
import CheckIcon from "@mui/icons-material/Check";

interface PipeMemoProps {
  modelName: string;
  building: string;
  floor: number;
  updatedAt: string;
  onViewChange: () => void;
  setIsTotalView: React.Dispatch<React.SetStateAction<boolean>>;
}

export const PipeMemo: React.FC<PipeMemoProps> = (props) => {
  const { t } = useTranslation();
  const { memo, setMemo, memoList, setMemoList } = useMemoStore();
  const {
    modelName,
    building,
    floor,
    updatedAt,
    onViewChange,
    setIsTotalView,
  } = props;
  const { selectedPipeId } = usePipe();
  const [pipeName, setPipeName] = useState<string>("");
  const [checked, setChecked] = useState(false);

  // MODEL_MEMO 및 TOTEL_VIEW로 전환
  const handleTotalView = () => {
    setIsTotalView(true);
    onViewChange();
  };
  // 파이프 이름 및 메모 리스트 조회
  const getPipeInfo = async (pipeId: number) => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "get",
        url: `/api/pipelines/pipes/${pipeId}`,
      });
      console.log(res.data.header.httpStatusCode, res.data.header.message);
      console.log(res.data.body);
      const { pipeName, memoList } = res.data.body;
      setPipeName(pipeName);
      setMemoList(memoList);
    } catch (err) {
      console.log(err);
    }
  };
  // 파이프 결함 체크
  const checkPipeDefection = async (pipeId: number) => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "patch",
        url: `/api/pipelines/pipes/${pipeId}/defect`,
      });
      console.log(res.data.header.httpStatusCode, res.data.header.message);
    } catch (err) {
      console.log(err);
    }
  };
  // 파이프 메모 생성
  const createPipeMemo = async (pipeId: number, memo: string) => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "post",
        url: `/api/pipelines/pipes/${pipeId}`,
        data: {
          memo: memo,
        },
      });
      // 결함 여부 받아서 렌더링 해야햄
      console.log(res.data.header.httpStatusCode, res.data.header.message);
      console.log(res.data.body);
      setMemoList(res.data.body.memoList);
    } catch (err) {
      console.log(err);
    }
  };

  // 파이프 메모 삭제
  const deletePipeMemo = async (memoId: number) => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "delete",
        url: `/api/pipelines/pipes/${memoId}`,
      });
      console.log(res);
      const updateList = memoList
        ? memoList.filter((item) => item.memoId !== memoId)
        : [];
      setMemoList(updateList);
    } catch (err) {
      console.log(err);
    }
  };

  // memoList renderer
  useEffect(() => {
    getPipeInfo(selectedPipeId);
  }, [selectedPipeId]);

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      createPipeMemo(selectedPipeId, memo);
      // postMemo(pipeId, memo);
      setMemo("");
    }
  };

  // format Memo Date
  const formatMemoDate = (date: Date) => {
    const year = String(date.getFullYear()).slice(-2); // 연도 마지막 두 자리
    const month = String(date.getMonth() + 1).padStart(2, "0"); // 월
    const day = String(date.getDate()).padStart(2, "0"); // 일
    const hours = String(date.getHours()).padStart(2, "0"); // 시간
    const minutes = String(date.getMinutes()).padStart(2, "0"); // 분

    return `${year}/${month}/${day} ${hours}:${minutes}`; // 포맷된 문자열 반환
  };

  // format Modified Date
  const formatModifiedDate = (date: Date) => {
    const year = String(date.getFullYear());
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}. ${month}. ${day}`;
  };

  return (
    <div className="w-[400px] h-[680px] flex flex-col bg-block rounded-[30px] px-[50px] py-[30px] text-white justify-between items-center gap-5">
      <div className="flex flex-col w-full h-full">
        {/* navigate */}
        <div className="flex justify-start cursor-pointer hover:text-primary-200">
          <div className="flex" onClick={handleTotalView}>
            <ChevronLeftIcon />
            <p>{modelName}</p>
          </div>
        </div>
        <div className="flex flex-col w-full h-full gap-7">
          {/* header */}
          <div className="flex flex-col items-center w-full">
            <h2 className="text-[30px] font-bold">
              {pipeName ? pipeName : ""}
            </h2>
            <p className="text-[20px]">
              {building}{" "}
              {floor > 0
                ? `${floor}${t("PipeViewer.ModelMemo.floorLabel")}`
                : `${t("PipeViewer.ModelMemo.basementLabel")} ${-floor}${t(
                    "PipeViewer.ModelMemo.floorLabel"
                  )}`}
            </p>
          </div>
          <div className="flex items-center w-full gap-2">
            <h3 className="text-[20px] font-bold self-start px-1">
              {t("PipeViewer.PipeMemo.checkDefect")}
            </h3>
            <Checkbox
              checked={checked}
              onChange={(isChecked: boolean) => {
                setChecked(isChecked);
                checkPipeDefection(selectedPipeId);
              }}
              className="p-1 rounded-md group size-8 bg-black/60 ring-1 ring-white/15 ring-inset "
            >
              {checked && (
                <CheckIcon className="hidden size-4 fill-black group-data-[checked]:block" />
              )}
            </Checkbox>
          </div>
          {/* 메모 input */}
          <div className="flex flex-col w-full">
            <h3 className="text-[20px] font-bold self-start px-1">
              {t("PipeViewer.PipeMemo.memoLabel")}
            </h3>
            <Textarea
              value={memo}
              onChange={(e: ChangeEvent<HTMLTextAreaElement>) =>
                setMemo(e.target.value)
              }
              onKeyDown={handleKeyDown}
              className={clsx(
                "mt-3 block w-full rounded-lg border-none bg-black/60 py-1.5 px-3 text-sm/6 text-white",
                "focus:outline-none data-[focus]:outline-2 data-[focus]:-outline-offset-2 data-[focus]:outline-white/25"
              )}
            />

            {/* 메모 조회창 */}
            <ul className="max-h-[330px] mt-4 space-y-4 overflow-auto">
              {Array.isArray(memoList) && memoList.length > 0 ? (
                memoList.map((item, idx) => (
                  <li
                    key={idx}
                    className="flex justify-between w-full gap-1 px-1 bg-gray-700"
                  >
                    <div className="flex flex-col gap-1">
                      <div className="text-[17px]">{item.memo}</div>
                      <div className="flex gap-2 text-[15px]">
                        <p>{item.writer.userName}</p>
                        <p className="text-gray-500">
                          {formatMemoDate(new Date(item.createdAt))}
                        </p>
                      </div>
                    </div>
                    <DeleteForeverIcon
                      onClick={() => deletePipeMemo(item.memoId)}
                      className="self-end h-full text-gray-500 hover:text-primary-200"
                    />
                  </li>
                ))
              ) : (
                <></>
              )}
            </ul>
          </div>
        </div>

        {/* modified date */}
        <div className="flex items-center justify-between w-full gap-3">
          <div className="text-[20px]">
            {t("PipeViewer.ModelMemo.modifiedDate")}
          </div>
          <div className="items-center justify-center flex-1 py-1 text-center px-auto rounded-2xl bg-black/60">
            {formatModifiedDate(new Date(updatedAt))}
          </div>
        </div>
      </div>
    </div>
  );
};
