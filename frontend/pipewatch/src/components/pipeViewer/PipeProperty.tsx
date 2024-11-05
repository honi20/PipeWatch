import React from "react";
import { ModelType } from "@components/pipeViewer/PipeType";

interface PipePropertyProps {
  pipe: ModelType;
}
export const PipeProperty: React.FC<PipePropertyProps> = ({ pipe }) => {
  return (
    <div className="w-[400px] h-[600px] flex flex-col bg-block rounded-[30px] p-[50px]  text-white justify-between items-center gap-5">
      <div className="flex flex-col w-full h-full gap-10">
        {/* header */}
        <div className="flex flex-col items-center w-full">
          <h2 className="text-[30px] font-bold">{pipe.pipelineName}</h2>
          <p className="text-[20px]">{pipe.area}</p>
        </div>
      </div>
    </div>
  );
};
