import { useTranslation } from "react-i18next";
import { useState, useEffect } from "react";
import SelectPipeModelIcon from "@assets/icons/select_pipe_model.png";
import { ModelsType, BuildingType } from "./Type/PipeType";
import { BuildingListbox } from "@components/pipeViewer/listbox/BuildingListbox";
import "./viewer.css";
import { FloorListbox } from "./listbox/FloorListbox";
import { getApiClient } from "@src/stores/apiClient";
import { ModelDetailView } from "@src/components/pipeViewer/ModelDetailView";
import { PipeProvider } from "@src/components/context/PipeContext";
import { SelectViewProvider } from "@src/components/context/SelectViewContext";
import clsx from "clsx";
import HighlightOffIcon from "@mui/icons-material/HighlightOff";

interface ModelListViewProps {
  models: ModelsType[];
}

export const ModelListView: React.FC<ModelListViewProps> = ({ models }) => {
  const { t } = useTranslation();
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
        url: "/api/enterprises/buildings/floors",
      });
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

  // 모델 삭제 기능 구현 및 편집 버튼
  const [editMode, setEditMode] = useState(false);
  const [deleteModelList, setDeleteModelList] = useState<number[]>([]);

  // 삭제된 모델 제외
  const visibleModelList = filteredModelList.filter(
    (model) => !deleteModelList.includes(model.modelId)
  );

  const handleDeleteBtn = (modelId: number) => {
    console.log("button click: ", modelId);
    setDeleteModelList((prev) => [...prev, modelId]);
    console.log(deleteModelList);
  };
  const deleteModel = async (modelId: number) => {
    try {
      const apiClient = getApiClient();
      const res = await apiClient({
        method: "delete",
        url: `/api/models/${modelId}`,
      });
      console.log("삭제 성공: ", res.data);
    } catch (err) {
      console.log(err);
    }
  };
  const handleEditClick = async () => {
    if (editMode) {
      try {
        await Promise.all(deleteModelList.map((item) => deleteModel(item)));
        console.log("모든 모델 삭제 완료");
      } catch (error) {
        console.error("모델 삭제 중 오류 발생: ", error);
      }
    }
    setEditMode((prev) => !prev);
  };
  return (
    <div className="w-full h-full">
      {/* 장소 및 층 선택 */}
      <div className="flex items-center justify-between pb-5 mx-6">
        <div className="flex gap-3">
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
        <button
          className="border dark:border-none rounded-lg bg-white dark:bg-white/5 py-1.5 text-sm/6 text-black dark:text-white w-[120px] font-center"
          onClick={handleEditClick}
        >
          {editMode
            ? t("PipeViewer.ModelListView.done")
            : t("PipeViewer.ModelListView.edit")}
        </button>
      </div>

      {/* pipe list view */}
      <div className="flex justify-center w-full h-[140px] py-[20px] bg-newBlock overflow-x-auto scrollable">
        {/* <div className="flex justify-center w-full h-[140px] py-[20px] bg-block overflow-x-auto scrollable"> */}
        <div className="flex gap-4 flex-nowrap ">
          {visibleModelList.map((item) => (
            <div className="relative w-[100px] h-[100px]" key={item.modelId}>
              {(selectModel === null ||
                selectModel.modelId !== item.modelId) && (
                <div
                  className={clsx(
                    "absolute inset-0 bg-black opacity-50 rounded-[20px] z-1"
                  )}
                  onClick={() => {
                    setSelectModel(item);
                  }}
                />
              )}
              {editMode && (
                <HighlightOffIcon
                  className="absolute z-10 text-gray-800 rounded-full h-[20px] w-[20px] top-1 left-1 animate-shake"
                  onClick={() => handleDeleteBtn(item.modelId)}
                />
              )}
              <img
                src={item.previewUrl}
                className={clsx(
                  "h-full rounded-[20px] object-cover bg-white",
                  editMode ? "animate-shake" : ""
                )}
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
          <SelectViewProvider>
            <PipeProvider>
              <ModelDetailView modelId={selectModel.modelId} />
            </PipeProvider>
          </SelectViewProvider>
        ) : (
          // 선택된 모델이 없는 경우
          <div className="flex items-center gap-2 h-svh">
            <img src={SelectPipeModelIcon} width={"60px"} />
            <p className="text-[30px] text-gray-800 font-bold">
              {t("PipeViewer.ModelListView.selectModel")}
            </p>
          </div>
        )}
      </div>
    </div>
  );
};
