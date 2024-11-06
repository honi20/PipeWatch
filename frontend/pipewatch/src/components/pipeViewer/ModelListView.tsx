import { useState, useEffect } from "react";
import SelectPipeModelIcon from "@assets/icons/select_pipe_model.png";
import { AreaType, ModelType } from "@src/components/pipeViewer/PipeType";
import { AreaListbox } from "./listbox/AreaListbox";
import "./viewer.css";
import { FloorListbox } from "./listbox/FloorListbox";
import GLTFViewer from "@src/components/pipeViewer/GLTFViewer";
import { PipeMemo } from "@src/components/pipeViewer/PipeMemo";
import { PipeProperty } from "@src/components/pipeViewer/PipeProperty";

interface ModelListViewProps {
  modelList: ModelType[];
}

export const ModelListView: React.FC<ModelListViewProps> = ({ modelList }) => {
  const [selectModel, setSelectModel] = useState<ModelType | null>(null);
  const [selectedArea, setSelectedArea] = useState<AreaType | null>(null);
  const [selectedFloor, setSelectedFloor] = useState<number | null>(null);
  const [floorList, setFloorList] = useState<number[]>([]);
  const [selectView, setSelectView] = useState<"MEMO" | "PROPERTY">("PROPERTY");
  const [cardFlipClass, setCardFlipClass] = useState("");

  useEffect(() => {
    setCardFlipClass(
      selectView === "MEMO" ? "rotateY(180deg)" : "rotateY(0deg)"
    );
  }, [selectView]);

  // api 받기
  const floorDict: { [key: string]: number[] } = {
    "역삼 멀티캠퍼스": [-1, 14],
    "경덕이네 집": [1, 2],
  };
  // 임의로 pipe 만듦
  const pipe = {
    pipeName: "파이프 이름이다",
    pipeArea: "파이프 장소",
    pipeFloor: -10,
    pipeMaterial: "aluminum",
    outerDiameter: 10,
    innerDiameter: 10,
    fluidMaterial: "water",
    flowRate: 10,
  };
  // 장소 및 장소에 따른 floorList 변경
  const handleAreaChange = (selectedArea: AreaType) => {
    setSelectedArea(selectedArea);
    setFloorList(floorDict[selectedArea.area] || []);
    setSelectedFloor(null); // 층 초기화
  };

  const handleFloorChange = (floor: number) => {
    setSelectedFloor(floor);
  };

  // 선택된 Area에 따라 모델 필터링
  const filteredModelList = modelList.filter((model) => {
    const matchesArea = selectedArea ? model.area === selectedArea.area : true;
    const matchesFloor = selectedFloor ? model.floor === selectedFloor : true; // model.floor가 어떤 속성을 의미하는지 확인 필요
    return matchesArea && matchesFloor;
  });

  return (
    <div className="w-full h-full">
      {/* 장소 및 층 선택 */}
      <div className="flex gap-5 mx-6">
        <AreaListbox onAreaChange={handleAreaChange} />
        <FloorListbox
          onFloorChange={handleFloorChange}
          floorList={floorList}
          selectedFloor={selectedFloor} // 선택된 층을 prop으로 전달
        />
      </div>

      {/* pipe list view */}
      <div className="flex justify-center w-full py-[20px] bg-block overflow-x-auto scrollable">
        <div className="flex gap-4 flex-nowrap ">
          {filteredModelList.map((model) => (
            <div className="relative w-[100px] h-[100px]" key={model.id}>
              {(selectModel === null || selectModel.id !== model.id) && (
                <div
                  className="absolute inset-0 bg-black opacity-50 rounded-[20px]"
                  onClick={() => {
                    setSelectModel(model);
                  }}
                />
              )}
              <img
                src={model.imagePath}
                className="h-full bg-gray-400 rounded-[20px] object-cover"
                style={{ zIndex: -1 }}
              />
            </div>
          ))}
        </div>
      </div>

      {/* 모델 렌더링 뷰 */}
      <div className="flex items-center justify-center gap-[20px] w-full h-full bg-gray-400">
        {selectModel ? (
          // 모델id에 따른 gltf url 넣기
          <div className="relative w-full h-full">
            <div>{selectModel.id}</div>
            <GLTFViewer gltfUrl="/assets/models/PipeLine.gltf" />
            <div className="absolute card-container top-5 right-10">
              <div className="card" style={{ transform: cardFlipClass }}>
                <div className="card-front">
                  <PipeProperty
                    pipe={pipe}
                    onViewChange={() => setSelectView("MEMO")}
                  />
                </div>
                <div className="card-back">
                  <PipeMemo
                    pipeId={selectModel.id}
                    onViewChange={() => setSelectView("PROPERTY")}
                  />
                </div>
              </div>
            </div>
          </div>
        ) : (
          // 선택된 모델이 없는 경우
          <div className="flex items-center gap-2 h-svh">
            <img src={SelectPipeModelIcon} width={"60px"} />
            <p className="text-[30px] text-gray-800 font-bold">
              파이프 모델을 선택하세요.
            </p>
          </div>
        )}
      </div>
    </div>
  );
};
