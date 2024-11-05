export interface MemoType {
  memo: string;
  writer: {
    userUuid: string;
    userName: string;
  };
  createdAt: string;
}

export interface ModelType {
  id: number;
  area: string;
  floor: number;
  name: string;
  imagePath: string;
  pipelineName: string;
  memolist: MemoType[];
  modifiedDate: Date;
}

export interface AreaType {
  id: number;
  area: string;
}

export interface AreaListboxProps {
  onAreaChange: (selectedArea: AreaType) => void;
}

export interface FloorListboxProps {
  onFloorChange: (selectedFloor: number) => void;
  floorList: number[];
  selectedFloor: number | null;
}
