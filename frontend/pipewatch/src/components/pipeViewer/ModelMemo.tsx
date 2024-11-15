import { useTranslation } from "react-i18next";
import React, { useEffect, ChangeEvent } from "react";
import { Textarea, Checkbox } from "@headlessui/react";
import clsx from "clsx";
import DeleteForeverIcon from "@mui/icons-material/DeleteForever";
import { useMemoStore } from "@src/stores/memoStore";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import { ModelNameInput } from "@src/components/pipeViewer/input/ModelNameInput";
import CheckIcon from "@mui/icons-material/Check";
import { useDefectStore } from "@src/stores/defectStore";

interface PipeMemoProps {
  modelId: number;
  modelName: string;
  building: string;
  floor: number;
  updatedAt: string;
  onViewChange: () => void;
  hasPipeId: boolean;
}

export const ModelMemo: React.FC<PipeMemoProps> = (props) => {
  const { t } = useTranslation();
  const { viewDefect, setViewDefect } = useDefectStore();
  const { memo, setMemo, memoList, getMemoList, postMemo, deleteMemo } =
    useMemoStore();
  const {
    modelId,
    modelName,
    building,
    floor,
    updatedAt,
    onViewChange,
    hasPipeId,
  } = props;

  // memoList renderer
  useEffect(() => {
    getMemoList(modelId);
  }, [modelId]);

  const handleCheckboxChange = () => {
    setViewDefect(modelId, !viewDefect[modelId]);
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      postMemo(modelId, memo);
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
        <div className="flex justify-end cursor-pointer hover:text-primary-200">
          <div className="flex" onClick={onViewChange}>
            <p>{t("PipeViewer.ModelMemo.property")}</p>
            <ChevronRightIcon />
          </div>
        </div>
        <div className="flex flex-col w-full h-full gap-7">
          {/* header */}
          <div className="flex flex-col items-center w-full">
            <ModelNameInput modelId={modelId} currentName={modelName} />
            <p className="text-[20px]">
              {building}{" "}
              {floor > 0
                ? `${floor}${t("PipeViewer.ModelMemo.floorLabel")}`
                : `${t("PipeViewer.ModelMemo.basementLabel")} ${-floor}${t(
                    "PipeViewer.ModelMemo.floorLabel"
                  )}`}
            </p>
          </div>
          {/* 결함 탐지 */}
          {hasPipeId && (
            <div className="flex items-center w-full gap-2">
              <h3 className="text-[20px] font-bold self-start px-1">
                {t("PipeViewer.ModelMemo.viewDefectivePipe")}
              </h3>
              <Checkbox
                checked={!!viewDefect[modelId]}
                onChange={handleCheckboxChange}
                className="p-1 rounded-md group size-8 bg-black/60 ring-1 ring-white/15 ring-inset "
              >
                {viewDefect[modelId] && (
                  <CheckIcon className="hidden size-4 fill-black group-data-[checked]:block" />
                )}
              </Checkbox>
            </div>
          )}

          {/* 메모 input */}
          <div className="flex flex-col w-full">
            <h3 className="text-[20px] font-bold self-start px-1">
              {t("PipeViewer.ModelMemo.memoLabel")}
            </h3>
            <Textarea
              value={memo}
              onChange={(e: ChangeEvent<HTMLTextAreaElement>) =>
                setMemo(e.target.value)
              }
              onKeyDown={handleKeyDown}
              className={clsx(
                "mt-3 h-[50px] resize-none block w-full rounded-lg border-none bg-black/60 py-1.5 px-3 text-sm/6 text-white",
                "focus:outline-none data-[focus]:outline-2 data-[focus]:-outline-offset-2 data-[focus]:outline-white/25"
              )}
              placeholder={t("PipeViewer.enterMemo")}
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
                      onClick={() => deleteMemo(item.memoId)}
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
