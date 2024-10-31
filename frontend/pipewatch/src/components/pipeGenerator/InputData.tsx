import { useState, useMemo } from "react";

import {
  Input,
  Combobox,
  ComboboxButton,
  ComboboxInput,
  ComboboxOption,
  ComboboxOptions,
} from "@headlessui/react";

import ExpandMoreIcon from "@mui/icons-material/ExpandMore";

import { useTranslation } from "react-i18next";

export const InputData = () => {
  const [groundInfo, setGroundInfo] = useState("G");
  const [query, setQuery] = useState("");

  const updateFloorInfo = (groundInfo: string) => {
    setGroundInfo(groundInfo);
  };

  const { t } = useTranslation();

  type Location = {
    id: number;
    name: string;
  };

  // 임시 장소 리스트
  const locationList: Location[] = useMemo(
    () => [
      { id: 1, name: "역삼 멀티캠퍼스" },
      { id: 2, name: "메가박스 서울숲점" },
      { id: 3, name: "경덕이네 집" },
    ],
    []
  );

  const [selectedLocation, setSelectedLocation] = useState<Location | null>({
    id: 0,
    name: "선택",
  });

  return (
    <div className="p-[40px]">
      <div className="text-[24px] font-bold mb-[20px]">
        {t("pipeGenerator.inputData.title")}
      </div>
      <p className="text-[16px]">
        {t("pipeGenerator.inputData.instructions.dataEntryPrompt")}
      </p>

      <div className="flex justify-center w-full my-[80px]">
        <div className="text-gray-800 p-[60px] w-[500px] h-[300px] flex flex-col gap-[20px] justify-center items-center bg-whiteBox shadow-md rounded-[12px] shadow-gray-500">
          <div className="flex items-center w-full h-full">
            <label className="flex-[2]">
              {t("pipeGenerator.inputData.formData.pipelineName")}
            </label>
            <Input
              type="text"
              value={""}
              onChange={(e) => {}}
              placeholder={"입력"}
              className="focus:outline-success h-full w-full flex-[3] px-5 bg-white rounded-[5px]"
              required
            />
          </div>

          <div className="flex items-center w-full h-full">
            <label className="flex-[2] ">
              {t("pipeGenerator.inputData.formData.location")}
            </label>

            <Combobox
              value={selectedLocation}
              onChange={setSelectedLocation}
              onClose={() => setQuery("")}
            >
              <div className="relative flex-[3]">
                <ComboboxInput
                  aria-label="location"
                  displayValue={(location: Location) => location?.name}
                  onChange={(event) => setQuery(event.target.value)}
                  className="w-full bg-white rounded-[5px] h-[40px] px-5"
                />
                <ComboboxButton className="group absolute inset-y-0 right-0 px-2.5 bg-transparent">
                  <ExpandMoreIcon sx={{ fontSize: "20px" }} />
                </ComboboxButton>
              </div>

              <ComboboxOptions
                anchor="bottom"
                className="bg-gray-200 border rounded-[5px] empty:invisible"
              >
                {locationList.map((location) => (
                  <ComboboxOption
                    key={location.id}
                    value={location}
                    // className="data-[focus]:bg-blue-100"
                    className="group flex cursor-default items-center gap-2 rounded-lg py-1.5 px-3 select-none data-[focus]:bg-gray-500/20"
                  >
                    {location.name}
                  </ComboboxOption>
                ))}
              </ComboboxOptions>
            </Combobox>
          </div>

          <div className="flex items-center w-full h-full">
            <label className="flex-[2]">
              {t("pipeGenerator.inputData.formData.floorInfo")}
            </label>
            <div className="flex-[3] flex gap-[8px]">
              <button
                onClick={() => updateFloorInfo("G")}
                className={`bg-gray-200 rounded-[24px] w-full px-[16px] py-[4px] text-white ${
                  groundInfo === "G" ? "bg-gray-500" : "bg-gray-200"
                }`}
              >
                지상
              </button>
              <button
                onClick={() => updateFloorInfo("UG")}
                className={`bg-gray-200 rounded-[24px] w-full px-[16px] py-[4px] text-white ${
                  groundInfo === "UG" ? "bg-gray-500" : "bg-gray-200"
                }`}
              >
                지하
              </button>
              <Input className="focus:outline-success h-[56px] w-full px-5 bg-white rounded-[5px]" />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
