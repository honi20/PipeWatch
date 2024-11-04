export interface ModelType {
  id: number;
  area: string;
  floor: number;
  name: string;
  image_path: string;
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
