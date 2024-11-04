// import {
//   Listbox,
//   ListboxButton,
//   ListboxOption,
//   ListboxOptions,
// } from "@headlessui/react";

import SamplePipeModel from "@assets/images/sample/sample_pipe_model.png";
import SelectPipeModelIcon from "@assets/icons/select_pipe_model.png";

export const ModelListView = () => {
  return (
    <div className="w-full h-full bg-gray-800">
      <div className="mx-6">장소, 층 Listbox</div>
      <div className="flex justify-center w-full py-[20px] bg-block">
        <div className="w-[100px] h-[100px]">
          <img
            src={SamplePipeModel}
            className="h-full bg-gray-400 rounded-[20px] object-cover"
          />
        </div>
      </div>
      <div className="flex items-center justify-center gap-[20px] w-full h-full bg-gray-400">
        <img src={SelectPipeModelIcon} width={"60px"} />
        <p className="text-[30px] text-gray-800 font-bold">
          파이프 모델을 선택하세요.
        </p>
      </div>
    </div>
  );
};
