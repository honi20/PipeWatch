import { useState, useRef } from "react";
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

  const allowedFormats = [".gltf", ".png", ".jpg", ".jpeg"];

  const [imgFile, setImgFile] = useState("");
  const fileRef = useRef<HTMLInputElement>(null);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      const file = e.target.files[0];
      const fileImg = fileRef?.current?.files?.[0];
      if (fileImg) {
        const reader = new FileReader();
        reader.readAsDataURL(fileImg);
        reader.onloadend = () => {
          setImgFile(reader.result as string);
        };
      }

      if (allowedFormats.some((format) => file.name.endsWith(format))) {
        setStatus("initial");
        setFile(file);
      } else {
        alert(
          t(
            "pipeGenerator.uploadModel.directUpload.instructions.allowedFileTypes"
          )
        );
      }
    }
  };

  const [uploadProgress, setUploadProgress] = useState(0);
  const apiClient = getApiClient();

  const handleUpload = async () => {
    if (!file) {
      alert(t("pipeGenerator.uploadModel.directUpload.instructions.noFile"));
      return;
    } else {
      const formData = new FormData();
      formData.append("file", file);

      setStatus("uploading");

      try {
        const isGltf = file.name.endsWith("gltf");
        const apiPath = isGltf
          ? "/api/models/upload-file"
          : "/api/models/upload-img";

        const response = await apiClient.post(apiPath, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
          onUploadProgress: (progressEvent) => {
            const percentage = Math.round(
              (progressEvent.loaded * 100) / (progressEvent?.total ?? 1)
            );
            setUploadProgress(percentage);
          },
        });
        setStatus("success");

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

      <div className="text-[16px] flex items-center justify-start gap-2 my-[4px]">
        {t("pipeGenerator.uploadModel.directUpload.instructions.Format")}
        {allowedFormats.map((format) => (
          <div
            key={format}
            className={`text-white px-[8px] py-[2px] rounded-[10px] text-[12px] ${
              format === ".gltf" ? "bg-primary-500" : "bg-primary-200"
            }`}
          >
            {format}
          </div>
        ))}
      </div>
      <p className="text-[14px]">
        {t(
          "pipeGenerator.uploadModel.directUpload.instructions.imageFileMessage"
        )}
      </p>

      <div className="flex justify-center w-full my-[20px]">
        <div className="w-[500px] h-[300px] flex flex-col justify-center items-center bg-whiteBox shadow-md rounded-[12px] shadow-gray-500">
          <label className="flex flex-col items-center justify-center preview">
            <input
              type="file"
              className="hidden"
              multiple={false}
              onChange={handleFileChange}
              accept=".gltf, .png, .jpg, .jpeg"
              ref={fileRef}
            />

            {status === "initial" || status === "uploading" ? (
              file ? (
                <img
                  className="max-w-[200px] max-h-[200px] my-[20px]"
                  src={imgFile}
                  alt="미리보기 이미지"
                />
              ) : (
                <DriveFolderUploadIcon
                  sx={{ fontSize: "60px", color: "#D9D9D9" }}
                />
              )
            ) : status === "success" ? (
              <CheckCircleIcon sx={{ fontSize: "60px", color: "#499B50" }} />
            ) : (
              status === "fail" && (
                <CancelIcon sx={{ fontSize: "60px", color: "#FF5353" }} />
              )
            )}

            {status === "initial" ? (
              <p className="text-gray-500 underline preview_msg">
                {file
                  ? t(
                      "pipeGenerator.uploadModel.directUpload.uploadBox.reselectFile"
                    )
                  : t(
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
          <div className="flex items-center justify-center gap-4">
            <FilePresentIcon sx={{ fontSize: "24px", color: "#499B50" }} />
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
