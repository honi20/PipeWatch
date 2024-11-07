import React, { useState, useEffect, ChangeEvent } from "react";
import { Textarea } from "@headlessui/react";
import clsx from "clsx";
import { MemoType } from "@src/components/pipeViewer/PipeType";
import DeleteForeverIcon from "@mui/icons-material/DeleteForever";
import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";


interface PipeDetail {
  name: string;
  building: string;
  floor: number;
}
interface PipeMemoProps {
  pipeId: number;
  onViewChange: () => void;
}

export const PipeMemo: React.FC<PipeMemoProps> = ({ pipeId, onViewChange }) => {
  // pipe 이름, pipe 장소, 층, memolist 필요함
  const [pipe, setPipe] = useState<PipeDetail>();
  const [memo, setMemo] = useState<string>("");
  const [memoList, setMemoList] = useState<MemoType[]>([]);
  const modifiedAt = "2024-11-05 14:52:55";

  // model의 memolist 불러오는 함수

  useEffect(() => {
    if (pipeId && memoList && memoList.length === 0) {
      // pipe api 호출
      setPipe({
        name: "Pipeline Model",
        building: "역삼 멀티캠퍼스",
        floor: 20,
      });
      // api 호출
      setMemoList([
        {
          memo: "hihihi",
          writer: {
            userUuid: "8e7dfbe3-aeca-4392-8d90-c1d3ae4fd35f",
            userName: "파오리",
          },
          createdAt: "2024-11-03 14:52:57",
        },
        {
          memo: "hi",
          writer: {
            userUuid: "8e7dfbe3-aeca-4392-8d90-c1d3ae4fd35f",
            userName: "파오리",
          },
          createdAt: "2024-11-05 14:52:55",
        },
      ]);
      // setMemoList(pipe.memolist);
    }
  }, [pipeId]);

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      sendRequest(memo);
      setMemo("");
    }
  };

  const sendRequest = (newMemo: string) => {
    // api 요청보내기
    const newMemoObject: MemoType = {
      memo: newMemo,
      writer: {
        userUuid: "test",
        userName: "주연핑",
      },
      createdAt: new Date().toISOString(),
    };

    setMemoList((prev) => [newMemoObject, ...prev]);
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
        <div
          className="flex justify-start cursor-pointer hover:text-primary-200"
          onClick={onViewChange}
        >
          <ChevronLeftIcon />
          <p>속성</p>
        </div>
        <div className="flex flex-col w-full h-full gap-7">
          {/* header */}
          <div className="flex flex-col items-center w-full">
            <h2 className="text-[30px] font-bold">{pipe && pipe.name}</h2>
            <p className="text-[20px]">
              {pipe &&
                `${pipe.building} ${
                  pipe.floor > 0 ? `${pipe.floor}층` : `지하 ${-pipe.floor}층`
                }`}
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
            <ul className="max-h-[270px] mt-4 space-y-4 overflow-auto">
              {memoList.map((item, idx) => (
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
            {formatModifiedDate(new Date(modifiedAt))}
          </div>
        </div>
      </div>
    </div>
  );
};
