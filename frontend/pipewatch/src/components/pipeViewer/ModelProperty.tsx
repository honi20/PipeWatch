import React, { useEffect, useState, ChangeEvent } from "react";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import { PipeMaterialListbox } from "./listbox/PipeMaterialListbox";
import { Input, Button } from "@headlessui/react";
import clsx from "clsx";
import { PipelineType } from "@src/components/pipeViewer/PipeType";
import { getApiClient } from "@src/stores/apiClient";
import { PropertyType } from "@src/components/pipeViewer/PipeType";
import { FluidMaterialListbox } from "@src/components/pipeViewer/listbox/FluidMaterialListbox";

interface ModelPropertyProps {
  pipelines: PipelineType[];
  onViewChange: () => void;
  building: string;
  floor: number;
}

export const ModelProperty: React.FC<ModelPropertyProps> = (props) => {
  const { pipelines, onViewChange, building, floor } = props;
  const [pipelineProperty, setPipelineProperty] = useState<PropertyType>();
  const [pipelineName, setPipelineName] = useState<string>();
  const [pipelineUpdatedAt, setPipelineUpdatedAt] = useState<string>();
  const [pipeMaterial, setPipeMaterial] = useState<string>(
    pipelineProperty ? pipelineProperty.pipeMaterial : "-"
  );
  const [pipeOuterDiameter, setPipeOuterDiameter] = useState<number>(
    pipelineProperty ? pipelineProperty.outerDiameter : 0
  );
  const [pipeInnerDiameter, setPipeInnerDiameter] = useState<number>(
    pipelineProperty ? pipelineProperty.innerDiameter : 0
  );
  const [fluidMaterial, setFluidMaterial] = useState<string>(
    pipelineProperty ? pipelineProperty.fluidMaterial : "-"
  );
  const [fluidFlowRate, setFluidFlowRate] = useState<number>(
    pipelineProperty ? pipelineProperty.velocity : 0
  );

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
      setPipelineName(res.data.body.name);
      setPipelineProperty(res.data.body.property);
      setPipelineUpdatedAt(res.data.body.updatedAt);
    } catch (err) {
      console.log(err);
    }
  };
  useEffect(() => {
    getPipelineDetail();
  }, [pipelines]);

  useEffect(() => {
    if (pipelineProperty) {
      setPipeMaterial(pipelineProperty!.pipeMaterial);
      setPipeOuterDiameter(pipelineProperty!.outerDiameter);
    }
  }, [pipelineProperty]);
  // input이 바뀌었을때 true
  const [isChanged, setIsChanged] = useState(false);

  // 값이 변경될 때마다 isChanged 상태 업데이트
  useEffect(() => {
    if (pipelineProperty) {
      setIsChanged(
        pipeMaterial !== pipelineProperty.pipeMaterial ||
          pipeOuterDiameter !== pipelineProperty.outerDiameter ||
          pipeInnerDiameter !== pipelineProperty.innerDiameter ||
          fluidFlowRate !== pipelineProperty.velocity
      );
    }
  }, [
    pipeMaterial,
    pipeOuterDiameter,
    pipeInnerDiameter,
    fluidFlowRate,
    pipelineProperty,
  ]);

  return (
    <div className="w-[400px] h-[680px] flex flex-col bg-block rounded-[30px] px-[50px] py-[30px] text-white justify-between items-center gap-5">
      <div className="flex flex-col w-full h-full">
        {/* navigate */}
        <div
          className="flex justify-end cursor-pointer hover:text-primary-200"
          onClick={onViewChange}
        >
          <p>메모</p>
          <ChevronRightIcon />
        </div>
        {pipelines && pipelineProperty && (
          <div className="flex flex-col w-full h-full gap-7">
            {/* header */}
            <div className="flex flex-col items-center w-full">
              <h2 className="text-[30px] font-bold">{pipelineName}</h2>
              <p className="text-[20px]">
                {building} {floor > 0 ? floor : `지하 ${-floor}`}층
              </p>
            </div>
            <div className="flex flex-col items-center w-full h-full gap-5">
              {/* 결함 탐지 */}
              <div className="flex flex-col w-full">
                <h3 className="text-[20px] font-bold self-start px-1">
                  결함 확인
                </h3>
              </div>

              {/* 파이프 속성 */}
              <div className="flex flex-col w-full gap-4">
                <h3 className="text-[20px] font-bold self-start px-1">
                  파이프 속성
                </h3>
                <div className="flex items-center justify-between w-full px-1">
                  <div className="w-[100px] px-1">재질</div>
                  <PipeMaterialListbox
                    value={pipeMaterial}
                    onChange={setPipeMaterial}
                  />
                </div>
                <div className="flex items-center justify-between w-full px-1">
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
                <div className="flex items-center justify-between w-full px-1">
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
                      style={{ paddingRight: "3rem" }} // 단위 공간 확보
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
                <div className="flex items-center justify-between w-full px-1">
                  <div className="w-[100px] px-1">재질</div>
                  <FluidMaterialListbox
                    value={fluidMaterial}
                    onChange={setFluidMaterial}
                  />
                </div>
                <div className="flex items-center justify-between w-full px-1">
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
                      style={{ paddingRight: "3rem" }} // 단위 공간 확보
                    />
                    <span className="absolute text-sm text-gray-800 transform -translate-y-1/2 pointer-events-none right-3 top-1/2">
                      m<sup>3</sup>/s
                    </span>
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
              >
                속성 변경
              </Button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
