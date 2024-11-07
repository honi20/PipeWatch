export interface MemoType {
  memo: string;
  writer: {
    userUuid: string;
    userName: string;
  };
  createdAt: string;
}

export interface ModelsType {
  modelId: number;
  building: string;
  floor: number;
  name: string;
  previewUrl: string;
  updatedAt: string;
}

export interface BuildingType {
  building: string | null;
  floors: number[];
}

export interface BuildingListboxProps {
  onBuildingChange: (selectedBuilding: string | null) => void;
  buildingList: BuildingType[];
}

export interface FloorListboxProps {
  onFloorChange: (selectedFloor: number) => void;
  floorList: number[];
  selectedFloor: number | null;
}
