// import { useTranslation } from "react-i18next";
import NoAccessImage from "@assets/images/status/no_access.png";
import NoPipeModelImage from "@assets/images/status/no_pipe_model.png";

export const PipeViewer = () => {
  // const { t } = useTranslation();

  // 파이프 모델 get - 모델 조회
  // 모델이 없을 때
  // const tempModelList = [];
  // 모델이 있을 때
  // 형식 확인 후 수정 예정
  const tempModelList = [
    {
      id: 1,
      name: "model1",
      image_path: "@assets/images/sample/sample_pipe_model.png",
    },
  ];

  // 접근 권한
  // 임시 계정명
  const tempUserRole: string = "admin";
  // const tempUserRole: string = "employee";
  const isAdmin: boolean = tempUserRole === "admin";

  return (
    <div className="mx-6">
      {/* <h2 className="font-bold text-[40px]">{t("pipeViewer.title")}</h2> */}
      <h2 className="font-bold text-[40px]">파이프 모델 조회</h2>

      <div className="flex items-center justify-center h-[640px] my-10 ">
        {isAdmin ? (
          tempModelList.length > 0 ? (
            <div>Model List Component</div>
          ) : (
            <div className="flex flex-col items-center gap-[40px]">
              <img src={NoPipeModelImage} width={"350px"} />

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
            </div>
          )
        ) : (
          <div className="flex flex-col items-center gap-[40px]">
            <img src={NoAccessImage} width={"350px"} />

            <div className="flex flex-col justify-center">
              <div className="text-center font-bold text-[40px]">
                접근 권한이 없습니다.
              </div>
              <a
                href="/pipe-generator"
                className="text-center text-[20px] text-gray-500 hover:text-gray-200 underline"
              >
                기업 인증 상태를 확인하세요.
              </a>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
