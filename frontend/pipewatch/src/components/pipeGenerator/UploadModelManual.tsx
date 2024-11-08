import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";

import { IconButton } from "@components/common/IconButton";

import DriveFolderUploadIcon from "@mui/icons-material/DriveFolderUpload";
import ReplayIcon from "@mui/icons-material/Replay";
import FilePresentIcon from "@mui/icons-material/FilePresent";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import CancelIcon from "@mui/icons-material/Cancel";

import { getApiClient } from "@src/stores/apiClient";

export const UploadModelManual = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const [file, setFile] = useState<File | null>(null);

  const [status, setStatus] = useState<
    "initial" | "uploading" | "success" | "fail"
  >("initial");

  const [modelId, setModelId] = useState<string>("");

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      const file = e.target.files[0];
      if (file.name.endsWith(".gltf")) {
        setStatus("initial");
        setFile(file);
        console.log("file:", file);
      } else {
        alert("GLTF 파일로 업로드해주세요.");
      }
    }
  };

  const [uploadProgress, setUploadProgress] = useState(0);
  const apiClient = getApiClient();

  const handleUpload = async () => {
    if (!file) {
      alert("파일 없음");
      return;
    } else {
      setStatus("uploading");

      const formData = new FormData();
      formData.append("file", file);

      try {
        const response = await apiClient.post(
          "/api/models/upload-file",
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
            onUploadProgress: (progressEvent) => {
              const percentage = Math.round(
                (progressEvent.loaded * 100) / (progressEvent?.total ?? 1)
              );

              setUploadProgress(percentage);
            },
          }
        );

        setStatus("success");

        console.log("upload model: ", response.data.header.message);
        console.log("modelId in UploadModel: ", response.data.body.modelId);
        setModelId(response.data.body.modelId);
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
    // POST로 받아진 modelId 전달 & 모델 렌더링 페이지로 이동
    navigate("/pipe-generator/input-data", { state: { modelId: modelId } });
  };

  console.log(file);
  return (
    <>
      <p className="text-[16px]">
        {t("pipeGenerator.uploadModel.directUpload.instructions.uploadModel")}
      </p>
      <p className="text-[16px]">
        {t("pipeGenerator.uploadModel.directUpload.instructions.Format")}
        <span className="font-bold">.gltf</span>
      </p>

      <div className="flex justify-center w-full my-[20px]">
        <div className="w-[500px] h-[300px] flex flex-col justify-center items-center bg-whiteBox shadow-md rounded-[12px] shadow-gray-500">
          <label className="flex flex-col items-center justify-center preview">
            <input
              type="file"
              className="hidden"
              multiple={false}
              onChange={handleFileChange}
              accept=".gltf"
            />
            {status === "initial" || status === "uploading" ? (
              <DriveFolderUploadIcon
                sx={{ fontSize: "96px", color: "#D9D9D9" }}
              />
            ) : status === "success" ? (
              <CheckCircleIcon sx={{ fontSize: "96px", color: "#499B50" }} />
            ) : (
              status === "fail" && (
                <CancelIcon sx={{ fontSize: "96px", color: "#FF5353" }} />
              )
            )}
            {status === "initial" ? (
              <p className="text-gray-500 underline preview_msg">
                {t(
                  "pipeGenerator.uploadModel.directUpload.uploadBox.selectFile"
                )}
              </p>
            ) : (
              status === "uploading" && (
                <div className="flex flex-col items-center justify-center">
                  <div>{uploadProgress}%</div>
                  <progress
                    value={uploadProgress}
                    max="100"
                    className="rounded-[20px]"
                  />
                </div>
              )
            )}
          </label>

          <div
            className={`flex justify-center w-full my-[10px] ${
              status === "success"
                ? "text-success"
                : status === "fail"
                ? " text-warn"
                : "text-black"
            }`}
          >
            <Result status={status} />
          </div>
        </div>
      </div>

      {file && status === "initial" && (
        <div className="flex items-center justify-center gap-6 w-full text-[16px]">
          <div className="flex items-center justify-center gap-2">
            <FilePresentIcon sx={{ fontSize: "40px", color: "#499B50" }} />
            <p>
              {t(
                "pipeGenerator.uploadModel.directUpload.uploadBox.uploadedFile"
              )}
            </p>
            <div className="bg-gray-200 rounded-[8px] px-[20px] py-[6px]">
              {file.name}
            </div>
          </div>
          <IconButton
            handleClick={() => handleUpload()}
            text={t("pipeGenerator.uploadModel.directUpload.uploadBox.upload")}
            color={"bg-primary-500"}
            hoverColor={"hover:bg-primary-500/80"}
            icon={""}
          />
        </div>
      )}

      {status === "success" && (
        <div className="flex justify-center w-full">
          <IconButton
            handleClick={() => handleSave()}
            text={t("pipeGenerator.commonButtons.save")}
            color={"bg-primary-500"}
            hoverColor={"hover:bg-primary-500/80"}
          />
        </div>
      )}
      {status === "fail" && (
        <div className="flex justify-center w-full">
          <IconButton
            handleClick={() => window.location.reload()}
            text={t("pipeGenerator.commonButtons.retry")}
            color={"bg-gray-800"}
            hoverColor={"hover:bg-gray-800/80"}
            icon={<ReplayIcon sx={{ fontSize: "20px" }} />}
          />
        </div>
      )}
    </>
  );
};
