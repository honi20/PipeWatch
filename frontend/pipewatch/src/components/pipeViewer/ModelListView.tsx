import { useState } from "react";
import SelectPipeModelIcon from "@assets/icons/select_pipe_model.png";
import {
  AreaType,
  ModelType,
  FloorType,
} from "@src/components/pipeViewer/PipeType";
import { AreaListbox } from "@src/components/pipeViewer/AreaListbox";
import "./viewer.css";
import { FloorListbox } from "@src/components/pipeViewer/FloorListbox";

interface ModelListViewProps {
  modelList: ModelType[];
}

export const ModelListView: React.FC<ModelListViewProps> = ({ modelList }) => {
  const [selectModelId, setSelectModelId] = useState<number | null>(null);
  const [selectedArea, setSelectedArea] = useState<AreaType | null>(null);
  const [selectedFloor, setSelectedFloor] = useState<number | null>(null);
  const [floorList, setFloorList] = useState<number[]>([]);

  // api 받기
  const floorDict: { [key: string]: number[] } = {
    "역삼 멀티캠퍼스": [-1, 14],
    "경덕이네 집": [1, 2],
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
      <div className="flex gap-5 mx-6">
        <AreaListbox onAreaChange={handleAreaChange} />
        <FloorListbox
          onFloorChange={handleFloorChange}
          floorList={floorList}
          selectedFloor={selectedFloor} // 선택된 층을 prop으로 전달
        />
      </div>

      <div className="flex justify-center w-full py-[20px] bg-block overflow-x-auto scrollable">
        <div className="flex gap-4 flex-nowrap ">
          {filteredModelList.map((model) => (
            <div className="relative w-[100px] h-[100px]" key={model.id}>
              {selectModelId !== model.id && (
                <div
                  className="absolute inset-0 bg-black opacity-50 rounded-[20px]"
                  onClick={() => {
                    setSelectModelId(model.id);
                  }}
                />
              )}
              <img
                src={model.image_path}
                className="h-full bg-gray-400 rounded-[20px] object-cover"
                style={{ zIndex: -1 }}
              />
            </div>
          ))}
        </div>
      </div>
      <div className="flex items-center justify-center gap-[20px] w-full h-full bg-gray-400">
        {selectModelId ? (
          <div>{selectModelId}</div>
        ) : (
          <div className="flex items-center gap-2">
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
