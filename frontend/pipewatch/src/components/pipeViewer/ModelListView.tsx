import { useState, useEffect } from "react";
import SelectPipeModelIcon from "@assets/icons/select_pipe_model.png";
import { ModelsType, BuildingType } from "@components/pipeViewer/PipeType";
import { BuildingListbox } from "@components/pipeViewer/listbox/BuildingListbox";
import "./viewer.css";
import { FloorListbox } from "./listbox/FloorListbox";
import { getApiClient } from "@src/stores/apiClient";
import { ModelDetailView } from "@src/components/pipeViewer/ModelDetailView";

interface ModelListViewProps {
  models: ModelsType[];
}

export const ModelListView: React.FC<ModelListViewProps> = ({ models }) => {
  const [selectModel, setSelectModel] = useState<ModelsType | null>(null);
  const [selectedBuilding, setSelectedBuilding] = useState<string | null>(null);
  const [selectedFloor, setSelectedFloor] = useState<number | null>(null);
  const [buildingList, setBuildingList] = useState<BuildingType[]>([]);
  const [floorList, setFloorList] = useState<number[]>([]);

  // 건물 및 층수 조회 함수
  const getBuildingFloors = async () => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "get",
        url: "/api/enterprises/floors",
      });
      console.log(res.data.header.httpStatusCode, res.data.header.message);
      console.log(res.data.body);
      setBuildingList(res.data.body.buildings);
    } catch (err) {
      console.log(err);
    }
  };

  // 건물 리스트 업데이트 함수
  useEffect(() => {
    if (!buildingList || buildingList.length === 0) {
      getBuildingFloors();
    }
  }, [buildingList]);

  // 선택한 건물에 따른 층 리스트 필터링 함수
  useEffect(() => {
    if (selectedBuilding && selectedBuilding !== null) {
      const filteredBuildingList: BuildingType[] = buildingList.filter(
        (elem) => elem.building === selectedBuilding
      );
      setFloorList(filteredBuildingList[0].floors);
    }
  }, [selectedBuilding, buildingList]);

  // 건물 선택 및 층 초기화 함수
  const handleBuildingChange = (selectedBuilding: string | null) => {
    setSelectedBuilding(selectedBuilding);
    setSelectedFloor(null);
  };

  // 층 선택 함수
  const handleFloorChange = (floor: number) => {
    setSelectedFloor(floor);
  };

  // 선택된 건물에 따라 모델 필터링
  const filteredModelList = models.filter((model) => {
    const matchesBuilding = selectedBuilding
      ? model.building === selectedBuilding
      : true;
    const matchesFloor = selectedFloor ? model.floor === selectedFloor : true;
    return matchesBuilding && matchesFloor;
  });

  return (
    <div className="w-full h-full">
      {/* 장소 및 층 선택 */}
      <div className="flex gap-5 mx-6">
        <BuildingListbox
          onBuildingChange={handleBuildingChange}
          buildingList={buildingList}
        />
        <FloorListbox
          onFloorChange={handleFloorChange}
          floorList={floorList}
          selectedFloor={selectedFloor} // 선택된 층을 prop으로 전달
        />
      </div>

      {/* pipe list view */}
      <div className="flex justify-center w-full h-[140px] py-[20px] bg-block overflow-x-auto scrollable">
        <div className="flex gap-4 flex-nowrap ">
          {filteredModelList.map((item) => (
            <div className="relative w-[100px] h-[100px]" key={item.modelId}>
              {(selectModel === null ||
                selectModel.modelId !== item.modelId) && (
                <div
                  className="absolute inset-0 bg-black opacity-50 rounded-[20px]"
                  onClick={() => {
                    setSelectModel(item);
                  }}
                />
              )}
              <img
                src={item.previewUrl}
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
          <ModelDetailView modelId={selectModel.modelId} />
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
