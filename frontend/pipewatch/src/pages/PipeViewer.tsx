// import { useTranslation } from "react-i18next";
import { getApiClient } from "@src/stores/apiClient";
import { ModelListView } from "@src/components/pipeViewer/ModelListView";
import NoAccessImage from "@assets/images/status/no_access.png";
import NoPipeModelImage from "@assets/images/status/no_pipe_model.png";
import { ModelsType } from "@src/components/pipeViewer/PipeType";
import { useState, useEffect } from "react";

export const PipeViewer = () => {
  // const { t } = useTranslation();
  // const apiClient = getApiClient();
  const [models, setModels] = useState<ModelsType[]>([]);

  // modelList 조회 함수
  const getModelList = async () => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "get",
        url: "/api/models",
      });
      console.log(res.data.header.httpStatusCode, res.data.header.message);
      console.log(res.data.body.models);
      setModels(res.data.body.models);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    if (!models || models.length === 0) {
      console.log("모델리스트 없음");
      getModelList();
    }
  }, []);

  // 임시 계정명
  const tempUserRole: string = "admin";
  // const tempUserRole: string = "employee";
  const isAdmin: boolean = tempUserRole === "admin";

  return (
    <div className="">
      {/* <h2 className="font-bold text-[40px]">{t("pipeViewer.title")}</h2> */}
      <h2 className="font-bold text-[40px] mx-6">파이프 모델 조회</h2>

      <div className="flex items-center justify-center my-4 h-fit">
        {isAdmin ? (
          models && models.length > 0 ? (
            <ModelListView models={models} />
          ) : (
            <div className="flex flex-col items-center gap-[40px]">
              {models && Array.isArray(models) ? (
                <>
                  <img src={NoPipeModelImage} width="350px" alt="No Model" />
                  <div className="flex flex-col justify-center">
                    <div className="text-center font-bold text-[40px]">
                      등록된 파이프 모델이 없습니다.
                    </div>
                    <a
                      href="/pipe-generator"
                      className="text-center text-[20px] text-gray-500 hover:text-gray-200 underline"
                    >
                      먼저 파이프 모델을 생성하세요.
                    </a>
                  </div>
                </>
              ) : (
                <>
                  <img src={NoAccessImage} width="350px" alt="No Access" />
                  <div className="flex flex-col justify-center">
                    <div className="text-center font-bold text-[40px]">
                      접근 권한이 없습니다.
                    </div>
                    <a
                      href="/"
                      className="text-center text-[20px] text-gray-500 hover:text-gray-200 underline"
                    >
                      기업 인증 상태를 확인하세요.
                    </a>
                  </div>
                </>
              )}
            </div>
          )
        ) : null}
      </div>
    </div>
  );
};
