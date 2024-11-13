import React, { useEffect, useState, ChangeEvent } from "react";
import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";
import { PipeMaterialListbox } from "./listbox/PipeMaterialListbox";
import { Input, Button } from "@headlessui/react";
import clsx from "clsx";
import { PipelineType } from "./Type/PipeType";
import { getApiClient } from "@src/stores/apiClient";
import { PropertyType, UpdatePropertyType } from "./Type/PipeType";
import { FluidMaterialListbox } from "@src/components/pipeViewer/listbox/FluidMaterialListbox";
import {
  MaterialType,
  MaterialListType,
} from "@src/components/pipeViewer/Type/MaterialType";

interface ModelPropertyProps {
  modelId: number;
  pipelines: PipelineType[];
  modelName: string;
  onViewChange: () => void;
  building: string;
  floor: number;
}

export const ModelProperty: React.FC<ModelPropertyProps> = (props) => {
  const { modelId, modelName, pipelines, onViewChange, building, floor } =
    props;
  const [pipelineProperty, setPipelineProperty] = useState<PropertyType>();
  const [pipeMaterialId, setPipeMaterialId] = useState<number>(1);
  const [pipeOuterDiameter, setPipeOuterDiameter] = useState<number>(0);
  const [pipeInnerDiameter, setPipeInnerDiameter] = useState<number>(0);
  const [fluidMaterialId, setFluidMaterialId] = useState<number>(4);
  const [fluidFlowRate, setFluidFlowRate] = useState<number>(0);
  const [pipeMaterialList, setPipeMaterialList] = useState<MaterialType[]>();
  const [fluidMaterialList, setFluidMaterialList] = useState<MaterialType[]>();

  // fetchMaterial 리스트조회
  const fetchMaterial = async () => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "get",
        url: "/api/pipelines/materials",
      });
      console.log(res.data.header.httpStatusCode, res.data.header.message);
      console.log(res.data.body);

      const materialList = res.data.body;

      // pipeMaterialList 필터링
      const pipeMaterialList = materialList
        .filter((item: MaterialListType) => item.type === "PIPE")
        .flatMap((item: MaterialListType) => item.materials);

      // fluidMaterialList 필터링
      const fluidMaterialList = materialList
        .filter((item: MaterialListType) => item.type === "FLUID")
        .flatMap((item: MaterialListType) => item.materials);
      setPipeMaterialList(pipeMaterialList);
      setFluidMaterialList(fluidMaterialList);
      console.log(pipeMaterialList, fluidMaterialList);
    } catch (err) {
      console.log(pipeMaterialList);
      console.log(err);
    }
  };

  // pipelineId 상세조회
  const getPipelineDetail = async () => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "get",
        url: `/api/pipelines/${pipelines[0].pipelineId}`,
      });
      console.log(res.data.header.httpStatusCode, res.data.header.message);
      console.log(res.data.body);

      if (res.data.body.property) {
        const property = res.data.body.property;
        setPipelineProperty(property);
        setPipeMaterialId(property.pipeMaterial.materialId);
        setPipeOuterDiameter(property.outerDiameter);
        setPipeInnerDiameter(property.innerDiameter);
        setFluidMaterialId(property.fluidMaterial.materialId);
        setFluidFlowRate(property.velocity);
      } else {
        setPipeMaterialId(1); // 기본값 설정
        setPipeOuterDiameter(0); // 기본값 설정
        setPipeInnerDiameter(0); // 기본값 설정
        setFluidMaterialId(4); // 기본값 설정
        setFluidFlowRate(0);
      }
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    if (!pipelineProperty) {
      setPipeMaterialId(1); // 기본값 설정
      setPipeOuterDiameter(0); // 기본값 설정
      setPipeInnerDiameter(0); // 기본값 설정
      setFluidMaterialId(4); // 기본값 설정
      setFluidFlowRate(0); // 기본값 설정
      // pipelineProperty가 null인 경우 isChanged를 true로 변경
      setIsChanged(true);
    } else {
      // pipelineProperty가 로드된 후에 isChanged를 false로 초기화
      setIsChanged(false);
    }
  }, [pipelineProperty]);

  useEffect(() => {
    // pipeMaterialList와 fluidMaterialList가 없는 경우에만 fetch
    if (!pipeMaterialList || !fluidMaterialList) {
      fetchMaterial();
    }
  }, []);

  useEffect(() => {
    getPipelineDetail();
  }, [pipelines, modelId]);

  // input이 바뀌었을때 true
  const [isChanged, setIsChanged] = useState(false);

  useEffect(() => {
    if (pipelineProperty) {
      const hasChanged =
        pipeMaterialId !== pipelineProperty.pipeMaterial.materialId ||
        pipeOuterDiameter !== pipelineProperty.outerDiameter ||
        pipeInnerDiameter !== pipelineProperty.innerDiameter ||
        fluidMaterialId !== pipelineProperty.fluidMaterial.materialId ||
        fluidFlowRate !== pipelineProperty.velocity;

      setIsChanged(hasChanged);
    }
  }, [
    pipeMaterialId,
    pipeOuterDiameter,
    pipeInnerDiameter,
    fluidMaterialId,
    fluidFlowRate,
    pipelineProperty,
  ]);

  const updatePipelineProperty = async (data: UpdatePropertyType) => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "put",
        url: `/api/pipelines/${pipelines[0].pipelineId}/property`,
        data: data,
      });
      console.log(res.data.header.httpStatusCode, res.data.header.message);
      setIsChanged(false);
    } catch (err) {
      console.log(err);
    }
  };
  const handleChangeButton = () => {
    // data 만들기
    const data: UpdatePropertyType = {
      pipeMaterialId: pipeMaterialId,
      outerDiameter: pipeOuterDiameter,
      innerDiameter: pipeInnerDiameter,
      fluidMaterialId: fluidMaterialId,
      velocity: fluidFlowRate,
    };
    // 단일 파이프라인 속성 정보 수정 API
    updatePipelineProperty(data);
  };
  return (
    <div className="w-[400px] h-[680px] flex flex-col bg-block rounded-[30px] px-[50px] py-[30px] text-white justify-between items-center gap-5">
      <div className="flex flex-col w-full h-full">
        {/* navigate */}
        <div className="flex justify-start cursor-pointer hover:text-primary-200">
          <div className="flex" onClick={onViewChange}>
            <ChevronLeftIcon />
            <p>메모</p>
          </div>
        </div>
        <div className="flex flex-col w-full h-full gap-7">
          {/* header */}
          <div className="flex flex-col items-center w-full">
            <h2 className="text-[30px] font-bold">{modelName}</h2>
            <p className="text-[20px]">
              {building} {floor > 0 ? floor : `지하 ${-floor}`}층
            </p>
          </div>
          <div className="flex flex-col items-center justify-between w-full h-full gap-5">
            <div className="flex flex-col items-center gap-10">
              {/* 파이프 속성 */}
              <div className="flex flex-col w-full gap-4">
                <h3 className="text-[20px] font-bold self-start px-1">
                  파이프 속성
                </h3>
                <div className="flex items-center justify-between w-full gap-2 px-1">
                  <div className="w-[100px] px-1">재질</div>
                  <PipeMaterialListbox
                    pipeMaterialList={pipeMaterialList}
                    value={pipeMaterialId ? pipeMaterialId : 1}
                    onChange={setPipeMaterialId}
                  />
                </div>
                <div className="flex items-center justify-between w-full gap-2 px-1">
                  <div className="w-[100px] px-1">Outer Diameter</div>
                  <div className="relative w-full">
                    <Input
                      type="number"
                      value={pipeOuterDiameter}
                      onChange={(e: ChangeEvent<HTMLInputElement>) =>
                        setPipeOuterDiameter(Number(e.target.value))
                      }
                      className={clsx(
                        "block w-full pl-5 pr-10 rounded-md border-none bg-black/40 py-2 px-3 text-sm/6 text-white",
                        "focus:outline-none data-[focus]:outline-2 data-[focus]:-outline-offset-2 data-[focus]:outline-white/25"
                      )}
                      style={{ paddingRight: "3rem" }} // 단위 공간 확보
                    />
                    <span className="absolute text-sm text-gray-800 transform -translate-y-1/2 pointer-events-none right-3 top-1/2">
                      mm
                    </span>
                  </div>
                </div>
                <div className="flex items-center justify-between w-full gap-2 px-1">
                  <div className="w-[100px] px-1">Inner Diameter</div>
                  <div className="relative w-full">
                    <Input
                      type="number"
                      value={pipeInnerDiameter}
                      onChange={(e: ChangeEvent<HTMLInputElement>) =>
                        setPipeInnerDiameter(Number(e.target.value))
                      }
                      className={clsx(
                        "block w-full pl-5 pr-10 rounded-md border-none bg-black/40 py-2 px-3 text-sm/6 text-white",
                        "focus:outline-none data-[focus]:outline-2 data-[focus]:-outline-offset-2 data-[focus]:outline-white/25"
                      )}
                      style={{ paddingRight: "3rem" }}
                    />
                    <span className="absolute text-sm text-gray-800 transform -translate-y-1/2 pointer-events-none right-3 top-1/2">
                      mm
                    </span>
                  </div>
                </div>
              </div>

              {/* 유체 속성 */}
              <div className="flex flex-col w-full gap-4">
                <h3 className="text-[20px] font-bold self-start px-1">
                  유체 속성
                </h3>
                <div className="flex items-center justify-between w-full gap-2 px-1">
                  <div className="w-[100px] px-1">재질</div>
                  <FluidMaterialListbox
                    fluidMaterialList={fluidMaterialList}
                    value={fluidMaterialId}
                    onChange={setFluidMaterialId}
                  />
                </div>
                <div className="flex items-center justify-between w-full gap-2 px-1">
                  <div className="w-[100px] px-1">Flow Rate</div>
                  <div className="relative w-full">
                    <Input
                      type="number"
                      value={fluidFlowRate}
                      onChange={(e: ChangeEvent<HTMLInputElement>) =>
                        setFluidFlowRate(Number(e.target.value))
                      }
                      className={clsx(
                        "block w-full pl-5 pr-10 rounded-md border-none bg-black/40 py-2 px-3 text-sm/6 text-white",
                        "focus:outline-none data-[focus]:outline-2 data-[focus]:-outline-offset-2 data-[focus]:outline-white/25"
                      )}
                      style={{ paddingRight: "3rem" }}
                    />
                    <span className="absolute text-sm text-gray-800 transform -translate-y-1/2 pointer-events-none right-3 top-1/2">
                      m<sup>3</sup>/s
                    </span>
                  </div>
                </div>
              </div>
            </div>
            <Button
              className={`h-fit w-fit px-5 py-2 text-white rounded-3xl ${
                isChanged
                  ? "bg-primary-500"
                  : "bg-button-background cursor-not-allowed"
              }`}
              disabled={!isChanged}
              onClick={handleChangeButton}
            >
              속성 변경
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};
