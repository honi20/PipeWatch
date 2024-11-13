import React, { useEffect, useState } from "react";
import { getApiClient } from "@src/stores/apiClient";

interface NameInputProps {
  modelId: number;
  currentName: string;
}

export const ModelNameInput: React.FC<NameInputProps> = ({
  modelId,
  currentName,
}) => {
  const [modelName, setModelName] = useState(currentName); // 초기 모델명
  const [isEditing, setIsEditing] = useState(false); // 텍스트를 수정할 때 상태
  useEffect(() => {
    setModelName(currentName);
  }, [currentName]);

  // 파이프라인 모델 이름 수정 API
  interface updateNameType {
    name: string;
  }
  const updateModelName = async (data: updateNameType) => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "patch",
        url: `/api/models/${modelId}`,
        data: data,
      });
      console.log(res.data.header.httpStatusCode, res.data.header.message);
    } catch (err) {
      console.log(err);
    }
  };

  // 더블클릭 시 input으로 변경
  const handleDoubleClick = () => {
    setIsEditing(true);
  };

  // 입력값 변경
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setModelName(e.target.value);
  };

  // 엔터키로 입력 완료
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      setIsEditing(false); // 엔터 키를 눌렀을 때 input을 종료
      updateModelName({ name: modelName });
    }
  };

  // input 외부 클릭 시 종료
  const handleBlur = () => {
    setIsEditing(false); // input 외부 클릭 시 input을 종료
    updateModelName({ name: modelName });
  };

  return (
    <div className="flex flex-col items-center mx-3">
      {isEditing ? (
        <input
          type="text"
          value={modelName}
          onChange={handleChange}
          onBlur={handleBlur} // 바깥 클릭 시 종료
          onKeyDown={handleKeyDown} // 엔터 클릭 시 종료
          autoFocus
          placeholder="모델명을 입력하세요."
          className="text-[30px] font-bold border-none bg-transparent text-white max-w-[300px]"
        />
      ) : (
        <h2
          className="text-[30px] font-bold cursor-pointer text-center whitespace-normal break-words"
          onDoubleClick={handleDoubleClick}
          title="더블클릭해서 모델명을 변경하세요."
        >
          {modelName}
        </h2>
      )}
    </div>
  );
};
