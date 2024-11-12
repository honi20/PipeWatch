import React, { useEffect, ChangeEvent } from "react";
import { Textarea } from "@headlessui/react";
import clsx from "clsx";
import DeleteForeverIcon from "@mui/icons-material/DeleteForever";
import { useMemoStore } from "@src/stores/memoStore";

import ChevronRightIcon from "@mui/icons-material/ChevronRight";
interface PipeMemoProps {
  modelId: number;
  modelName: string;
  building: string;
  floor: number;
  updatedAt: string;
  onViewChange: () => void;
}

export const ModelMemo: React.FC<PipeMemoProps> = (props) => {
  const { memo, setMemo, memoList, getMemoList, postMemo } = useMemoStore();
  const { modelId, modelName, building, floor, updatedAt, onViewChange } =
    props;

  // memoList renderer
  useEffect(() => {
    getMemoList(modelId);
  }, [modelId]);

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
            <p>속성</p>
            <ChevronRightIcon />
          </div>
        </div>
        <div className="flex flex-col w-full h-full gap-7">
          {/* header */}
          <div className="flex flex-col items-center w-full">
            <h2 className="text-[30px] font-bold">{modelName}</h2>
            <p className="text-[20px]">
              {building} {floor > 0 ? `${floor}층` : `지하 ${-floor}층`}
            </p>
          </div>

          {/* 메모 input */}
          <div className="flex flex-col w-full">
            <h3 className="text-[20px] font-bold self-start px-1">메모</h3>
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
              {memoList &&
                memoList
                  .slice()
                  .reverse()
                  .map((item, idx) => (
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
                        onClick={() => console.log("삭제할거지롱 포실")}
                        className="self-end h-full text-gray-500 hover:text-primary-200"
                      />
                    </li>
                  ))}
            </ul>
          </div>
        </div>

        {/* modified date */}
        <div className="flex items-center justify-between w-full">
          <div className="text-[20px]">수정일</div>
          <div className="px-16 py-1 rounded-2xl bg-black/60">
            {formatModifiedDate(new Date(updatedAt))}
          </div>
        </div>
      </div>
    </div>
  );
};
