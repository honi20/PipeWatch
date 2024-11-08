import { Suspense, useRef } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";

import { Leva } from "leva";
import { Loader } from "@react-three/drei";
import TestRendering from "@components/pipeGenerator/TestRendering";

import { IconButton } from "@components/common/IconButton";

// import SamplePipeAnimation from "@assets/images/sample/sample_pipe_animation.gif";
// import SamplePipeVideo from "@assets/images/sample/sample_pipe_video.mp4";

// import PlayCircleIcon from "@mui/icons-material/PlayCircle";

export const Rendering = () => {
  // const videoRef = useRef<HTMLVideoElement>(null);
  // const [isPlaying, setIsPlaying] = useState(false);

  const { t } = useTranslation();
  const navigate = useNavigate();

  // const handlePlayPause = () => {
  //   if (videoRef.current) {
  //     if (videoRef.current.paused) {
  //       videoRef.current.play();
  //       setIsPlaying(true);
  //     } else {
  //       videoRef.current.pause();
  //       setIsPlaying(false);
  //     }
  //   }
  // };

  const testRenderingRef = useRef<{ takeScreenshot: () => void }>(null);

  // 저장 버튼 Click Action
  const handleSave = () => {
    // POST 함수 추가 예정
    if (testRenderingRef.current) {
      testRenderingRef.current.takeScreenshot();
    }
    // 모델 렌더링 페이지로 이동
    navigate("/pipe-generator/completed");
  };

  return (
    <div className="p-[40px]">
      <div className="text-[24px] font-bold mb-[20px]">
        {t("pipeGenerator.rendering.title")}
      </div>
      <p className="text-[16px]">
        {t("pipeGenerator.rendering.instructions.completed.message")}
      </p>
      <p className="text-[16px]">
        {t("pipeGenerator.rendering.instructions.completed.previewAndSave")}
      </p>

      <div className="w-full h-[400px]">
        <Suspense fallback={<Loader />}>
          <TestRendering ref={testRenderingRef} />
          <Leva collapsed />
        </Suspense>
      </div>

      {/* <div className="flex justify-center w-full my-[20px]">
        <div className="w-[500px] h-[300px] flex flex-col justify-center items-center bg-whiteBox shadow-md rounded-[12px] shadow-gray-500">
          <img
            src={SamplePipeAnimation}
            className="w-full h-full object-cover rounded-[12px]"
          />
        </div>
      </div>
      <div className="flex justify-center w-full my-[20px]">
        <div
          onClick={handlePlayPause}
          className="relative cursor-pointer w-[500px] h-[300px] flex flex-col justify-center items-center bg-whiteBox shadow-md rounded-[12px] shadow-gray-500"
        >
          {!isPlaying && (
            <div className="absolute z-50 bg-transparent">
              <PlayCircleIcon sx={{ fontSize: "96px", color: "#FFFFFF" }} />
            </div>
          )}
          <video
            ref={videoRef}
            src={SamplePipeVideo}
            className="z-10 w-full h-full object-cover rounded-[12px]"
            loop
            muted
          />
        </div>
      </div> */}
      <div className="flex justify-center w-full">
        <IconButton
          // handleClick={() => handleSave()}
          handleClick={handleSave}
          text={t("pipeGenerator.commonButtons.save")}
          color={"bg-primary-500"}
          hoverColor={"hover:bg-primary-500/80"}
        />
      </div>
    </div>
  );
};
