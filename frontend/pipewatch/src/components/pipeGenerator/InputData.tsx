import { useState, useMemo } from "react";
import { IconButton } from "@components/common/IconButton";
import clsx from "clsx";

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
  const { t } = useTranslation();

  const [pipelineName, setPipelineName] = useState("");
  const [groundInfo, setGroundInfo] = useState("G");
  const [floorNum, setFloorNum] = useState<number>(0);
  const [query, setQuery] = useState("");

  const [selectedLocation, setSelectedLocation] = useState<Location | null>();

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
      { id: 4, name: "해피해피주연하우스" },
    ],
    []
  );

  const filteredLocation =
    query === ""
      ? locationList
      : locationList.filter((location) => {
          return location.name.toLowerCase().includes(query.toLowerCase());
        });

  const updateFloorInfo = (groundInfo: string) => {
    setGroundInfo(groundInfo);
  };

  const [isFloorNumInvalid, setIsFloorNumInvalid] = useState(false);

  const handleFloorNumChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;

    // 숫자 여부를 확인하고 상태를 설정
    if (/^\d*$/.test(value)) {
      console.log("value:", value);
      console.log("floorNum:", floorNum);
      setFloorNum(Number(value)); // 숫자로 변환하여 설정
      setIsFloorNumInvalid(false); // 숫자면 경고 색상 해제
      console.log("floorNumNext:", floorNum);
    } else {
      setIsFloorNumInvalid(true); // 숫자가 아니면 경고
    }
  };

  // 버튼 활성화
  const isFormValid: boolean =
    pipelineName !== "" &&
    selectedLocation?.name !== "선택" &&
    groundInfo !== "" &&
    !!floorNum &&
    !isFloorNumInvalid &&
    !(query === "" && !selectedLocation);

  return (
    <div className="p-[40px]">
      <div className="text-[24px] font-bold mb-[20px]">
        {t("pipeGenerator.inputData.title")}
      </div>
      <p className="text-[16px]">
        {t("pipeGenerator.inputData.instructions.dataEntryPrompt")}
      </p>

      <div className="flex justify-center w-full mt-[60px]">
        <div className="relative text-gray-800 py-[60px] px-[50px] w-[500px] h-[300px] flex flex-col gap-[20px] justify-center items-center bg-whiteBox shadow-md rounded-[12px] shadow-gray-500">
          <div className="flex items-center w-full h-full">
            <label className="flex-[2]">
              {t("pipeGenerator.inputData.formData.pipelineName")}
            </label>
            <div className="flex-[4] h-full">
              <Input
                type="text"
                value={pipelineName}
                onChange={(event) => setPipelineName(event.target.value)}
                className="focus:outline-success h-full w-full px-5 bg-white rounded-[5px] box-border"
                required
              />
            </div>
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
              <div className="relative flex-[4] h-full">
                <ComboboxInput
                  aria-label="location"
                  displayValue={(location: Location) => location?.name}
                  onChange={(event) => setQuery(event.target.value)}
                  placeholder={`${t(
                    "pipeGenerator.inputData.formData.select"
                  )}`}
                  className="w-full focus:outline-success bg-white rounded-[5px] h-full px-5"
                />
                <ComboboxButton className="group absolute inset-y-0 right-0 px-2.5 bg-transparent">
                  <ExpandMoreIcon sx={{ fontSize: "20px" }} />
                </ComboboxButton>
              </div>

              <ComboboxOptions
                anchor="bottom"
                className={clsx(
                  "w-[var(--input-width)] bg-gray-200 rounded-[5px] my-[8px] border border-white/5 p-1 [--anchor-gap:var(--spacing-1)] empty:invisible",
                  "transition duration-100 ease-in data-[leave]:data-[closed]:opacity-0"
                )}
              >
                {filteredLocation.map((location) => (
                  <ComboboxOption
                    key={location.id}
                    value={location}
                    className="group flex cursor-default items-center gap-2 rounded-lg py-3 px-3 select-none data-[focus]:bg-gray-500/20"
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

            <div className="h-full flex-[4] flex gap-[8px]">
              <div className="h-full flex flex-[3] gap-2">
                <button
                  onClick={() => updateFloorInfo("G")} // floorInfo 값은 데이터 전송에만 필요하기 때문에 G / UG로 구분함(translation X)
                  className={`bg-gray-200 w-full h-full rounded-[24px] px-[12px] py-[4px] text-white ${
                    groundInfo === "G" ? "bg-gray-500" : "bg-gray-200"
                  }`}
                >
                  {t("pipeGenerator.inputData.formData.ground")}
                </button>
                <button
                  onClick={() => updateFloorInfo("UG")}
                  className={`bg-gray-200 w-full h-full rounded-[24px] px-[12px] py-[4px] text-white ${
                    groundInfo === "UG" ? "bg-gray-500" : "bg-gray-200"
                  }`}
                >
                  {t("pipeGenerator.inputData.formData.underground")}
                </button>
              </div>
              <div className="relative w-full flex flex-[2]">
                <Input
                  type="text"
                  // onChange={(e) => setFloorNum(e.target.value)}
                  onChange={handleFloorNumChange}
                  className={`focus:outline-success h-full w-full px-5 border-black bg-white rounded-[5px] ${
                    isFloorNumInvalid && "border-solid border-2 border-warn"
                  }`}
                />
                <span className="absolute text-gray-500 transform -translate-y-1/2 right-5 top-1/2">
                  {t("pipeGenerator.inputData.formData.floor")}
                </span>
              </div>
            </div>
          </div>

          {isFloorNumInvalid && (
            <span className="absolute bottom-6 right-[100px] text-warn text-[12px]">
              {t("pipeGenerator.inputData.formData.error")}
            </span>
          )}
        </div>
      </div>

      {isFormValid && (
        <div className="flex justify-center w-full my-[20px]">
          <IconButton
            handleClick={() => console.log("button Clicked")}
            text={t("pipeGenerator.commonButtons.save")}
            color={"bg-primary-500"}
            hoverColor={"hover:bg-primary-500/80"}
          />
        </div>
      )}
    </div>
  );
};
