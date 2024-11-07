// import { useTranslation } from "react-i18next";
// import { getApiClient } from "@src/stores/apiClient";
import { ModelListView } from "@src/components/pipeViewer/ModelListView";
import NoAccessImage from "@assets/images/status/no_access.png";
import NoPipeModelImage from "@assets/images/status/no_pipe_model.png";
import { ModelType } from "@src/components/pipeViewer/PipeType";

export const PipeViewer = () => {
  // const { t } = useTranslation();

  // 파이프 모델 get - 모델 조회
  // 모델이 없을 때
  // const tempModelList = [];
  // 모델이 있을 때

  const tempModelList: ModelType[] = [
    {
      id: 1,
      name: "model1",
      area: "역삼 멀티캠퍼스",
      floor: 14,
      imagePath: "src/assets/images/sample/sample_pipe_model.png",
      pipelineName: "pipeline1",
      memolist: [],
      modifiedDate: new Date(),
    },
    {
      id: 2,
      name: "model1",
      area: "경덕이네 집",
      floor: 1,
      imagePath: "@assets/images/sample/mallang.png",
      pipelineName: "pipeline",
      memolist: [],
      modifiedDate: new Date(),
    },
    {
      id: 3,
      name: "model1",
      area: "역삼 멀티캠퍼스",
      floor: -1,
      imagePath: "src/assets/images/sample/sample_pipe_model.png",
      pipelineName: "pipeline",
      memolist: [],
      modifiedDate: new Date(),
    },
    {
      id: 4,
      name: "model1",
      area: "역삼 멀티캠퍼스",
      floor: 14,
      imagePath: "src/assets/images/sample/sample_pipe_model.png",
      pipelineName: "pipeline",
      memolist: [],
      modifiedDate: new Date(),
    },
    {
      id: 5,
      name: "model1",
      area: "역삼 멀티캠퍼스",
      floor: 14,
      imagePath: "src/assets/images/sample/sample_pipe_model.png",
      pipelineName: "pipeline",
      memolist: [],
      modifiedDate: new Date(),
    },
    {
      id: 6,
      name: "model1",
      area: "역삼 멀티캠퍼스",
      floor: 14,
      imagePath: "src/assets/images/sample/sample_pipe_model.png",
      pipelineName: "pipeline",
      memolist: [],
      modifiedDate: new Date(),
    },
    {
      id: 7,
      name: "model1",
      area: "경덕이네 집",
      floor: 1,
      imagePath: "src/assets/images/sample/mallang.png",
      pipelineName: "말랑파이프",
      memolist: [],
      modifiedDate: new Date(),
    },
    {
      id: 8,
      name: "model1",
      area: "경덕이네 집",
      floor: 2,
      imagePath: "src/assets/images/sample/posil.png",
      pipelineName: "포실파이프",
      memolist: [],
      modifiedDate: new Date(),
    },
    {
      id: 9,
      name: "model1",
      area: "경덕이네 집",
      floor: 1,
      imagePath: "src/assets/images/sample/mallang.png",
      pipelineName: "말랑파이프",
      memolist: [],
      modifiedDate: new Date(),
    },
    {
      id: 10,
      name: "model1",
      area: "경덕이네 집",
      floor: 2,
      imagePath: "src/assets/images/sample/posil.png",
      pipelineName: "포실파이프",
      memolist: [],
      modifiedDate: new Date(),
    },
  ];
  // 모델 없을 경우
  // tempModelList = [];

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
          tempModelList && tempModelList.length > 0 ? (
            <ModelListView modelList={tempModelList} />
          ) : (
            <div className="flex flex-col items-center gap-[40px]">
              {tempModelList && Array.isArray(tempModelList) ? (
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
