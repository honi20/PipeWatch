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

export interface PipeType {
  pipeId: number;
  pipeUuid: string;
}

export interface PipelineType {
  pipelineId: number;
  pipes: PipeType[];
}

export interface ModelDetailType {
  name: string;
  modelingUrl: string;
  building: string;
  floor: number;
  isCompleted: boolean;
  updatedAt: string;
  pipelines: PipelineType[];
  creater: {
    userName: string;
    userUuid: string;
  };
}
interface PipeMaterialType {
  materialId: number;
  koreanName: string;
  englishName: string;
}

interface FluidMaterialType {
  materialId: number;
  koreanName: string;
  englishName: string;
}

export interface PropertyType {
  pipeMaterial: PipeMaterialType;
  outerDiameter: number;
  innerDiameter: number;
  fluidMaterial: FluidMaterialType;
  velocity: number;
}

export interface UpdatePropertyType {
  pipeMaterialId: number;
  outerDiameter: number;
  innerDiameter: number;
  fluidMaterialId: number;
  velocity: number;
}
