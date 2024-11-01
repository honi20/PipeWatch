import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";

import { IconButton } from "@components/common/IconButton";

import DriveFolderUploadIcon from "@mui/icons-material/DriveFolderUpload";
import ReplayIcon from "@mui/icons-material/Replay";

export const UploadModelManual = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const [file, setFile] = useState<File | null>(null);
  const [status, setStatus] = useState<
    "initial" | "uploading" | "success" | "fail"
  >("initial");

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      setStatus("initial");
      setFile(e.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (file) {
      setStatus("uploading");

      const formData = new FormData();
      formData.append("file", file);

      try {
        const result = await fetch("", {
          method: "POST",
          body: formData,
        });

        const data = await result.json();

        console.log(data);
        setStatus("success");
      } catch (error) {
        console.error(error);
        setStatus("fail");
      }
    }
  };

  const Result = ({ status }: { status: string }) => {
    if (status === "success") {
      return t(
        "pipeGenerator.uploadModel.directUpload.uploadBox.statusMessages.success"
      );
    } else if (status === "fail") {
      return t(
        "pipeGenerator.uploadModel.directUpload.uploadBox.statusMessages.failed"
      );
    } else if (status === "uploading") {
      return t(
        "pipeGenerator.uploadModel.directUpload.uploadBox.statusMessages.uploading"
      );
    } else {
      return null;
    }
  };

  // 저장 버튼 Click Action
  const handleSave = () => {
    // POST 함수 추가 예정

    // 모델 렌더링 페이지로 이동
    navigate("/pipe-generator/input-data");
  };

  return (
    <>
      <p className="text-[16px]">
        {t("pipeGenerator.uploadModel.directUpload.instructions.uploadModel")}
      </p>
      <p className="text-[16px]">
        {t(
          "pipeGenerator.uploadModel.directUpload.instructions.recommendedFormat"
        )}
        : <span className="font-bold">.gltf</span>
      </p>

      <div className="flex justify-center w-full my-[20px]">
        <div className="w-[500px] h-[300px] flex flex-col justify-center items-center bg-whiteBox shadow-md rounded-[12px] shadow-gray-500">
          <label className="flex flex-col items-center justify-center preview">
            <input
              type="file"
              className="hidden"
              multiple
              onChange={handleFileChange}
            />
            <DriveFolderUploadIcon
              sx={{ fontSize: "96px", color: "#D9D9D9" }}
            />
            <p className="text-gray-500 underline preview_msg">
              {t("pipeGenerator.uploadModel.directUpload.uploadBox.selectFile")}
            </p>
          </label>

          {file && (
            <section>
              File details:
              <ul>
                <li>Name: {file.name}</li>
                <li>Type: {file.type}</li>
                <li>Size: {file.size} bytes</li>
              </ul>
            </section>
          )}

          {file && (
            <button onClick={handleUpload} className="submit">
              Upload a file
            </button>
          )}

          <p className="preview_uploading">
            {t(
              "pipeGenerator.uploadModel.directUpload.uploadBox.statusMessages.uploading"
            )}
          </p>
          <p>
            {t(
              "pipeGenerator.uploadModel.directUpload.uploadBox.statusMessages.success"
            )}
          </p>
          <p>
            {t(
              "pipeGenerator.uploadModel.directUpload.uploadBox.statusMessages.failed"
            )}
          </p>
        </div>
      </div>

      <Result status={status} />

      <div className="flex justify-center w-full">
        <IconButton
          handleClick={() => handleSave()}
          text={t("pipeGenerator.commonButtons.save")}
          color={"bg-primary-500"}
          hoverColor={"hover:bg-primary-500/80"}
        />
      </div>
      <div className="flex justify-center w-full">
        <IconButton
          handleClick={() => window.location.reload()}
          text={t("pipeGenerator.commonButtons.retry")}
          color={"bg-gray-800"}
          hoverColor={"hover:bg-gray-800/80"}
          icon={<ReplayIcon sx={{ fontSize: "20px" }} />}
        />
      </div>
    </>
  );
};
