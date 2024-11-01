import { useTranslation, Trans } from "react-i18next";
import {
  Input,
  Button,
  Combobox,
  ComboboxButton,
  ComboboxInput,
  ComboboxOption,
  ComboboxOptions,
} from "@headlessui/react";
import { CheckIcon, ChevronDownIcon } from "@heroicons/react/20/solid";
import {
  ChangeEvent,
  useState,
  FocusEvent,
  useMemo,
  useCallback,
  useEffect,
} from "react";
import clsx from "clsx";
import { useNavigate } from "react-router-dom";

const ContactUsCard = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [companyName, setCompanyName] = useState("");
  const [contactEmail, setContactEmail] = useState("");
  const [contactPhoneNumber, setContactPhoneNumber] = useState("");
  const [showCompanyNameError, setShowCompanyNameError] = useState(false);
  const [showContactEmailError, setShowContactEmailError] = useState(false);
  const [showContactPhoneNumberError, setShowContactPhoneNumberError] =
    useState(false);
  const [query, setQuery] = useState("");

  // 유효성 검사 함수
  const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  const phonePattern = /^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$/;

  const validateInput = (value: string, pattern: RegExp) =>
    pattern.test(value.trim());

  // 유효성 검사 핸들러
  const handleChange = useCallback(
    (
      e: ChangeEvent<HTMLInputElement>,
      setValue: (value: string) => void,
      setShowError: (value: boolean) => void,
      pattern?: RegExp
    ) => {
      const value = e.target.value;
      setValue(value);
      setShowError(
        pattern ? !validateInput(value, pattern) : value.trim() === ""
      );
    },
    []
  );

  const handleBlur = useCallback(
    (
      e: FocusEvent<HTMLInputElement>,
      setShowError: (value: boolean) => void,
      pattern?: RegExp
    ) => {
      const value = e.target.value;
      setShowError(
        pattern ? !validateInput(value, pattern) : value.trim() === ""
      );
    },
    []
  );

  // industry
  type Industry = {
    id: number;
    name: string;
  };

  // 산업군 리스트
  const industryList: Industry[] = useMemo(
    () => [
      { id: 1, name: t("contact.industryList.manufacturing") },
      { id: 2, name: t("contact.industryList.electricity") },
      { id: 3, name: t("contact.industryList.construction") },
      { id: 4, name: t("contact.industryList.science") },
      { id: 5, name: t("contact.industryList.other") },
    ],
    [t]
  );

  const [selectedIndustry, setSelectedIndustry] = useState<Industry | null>(
    industryList[0]
  );

  useEffect(() => {
    if (selectedIndustry) {
      const updatedIndustry = industryList.find(
        (industry) => industry.id === selectedIndustry.id
      );
      // 영한 번역이 되었을 경우에만 industy를 update.
      if (updatedIndustry && updatedIndustry.name !== selectedIndustry.name) {
        setSelectedIndustry(updatedIndustry);
      }
    }
  }, [industryList, selectedIndustry]);

  const filteredIndustry =
    query === ""
      ? industryList
      : industryList.filter((industry) => {
          return industry.name.toLowerCase().includes(query.toLowerCase());
        });

  // 버튼 활성화
  const isFormValid: boolean =
    !showCompanyNameError &&
    !showContactEmailError &&
    !showContactPhoneNumberError &&
    companyName !== "" &&
    contactEmail !== "" &&
    contactPhoneNumber !== "";

  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
      {/* header */}
      <div className="flex flex-col gap-[10px]">
        {/* title */}
        <div className="flex justify-center font-semibold text-[28px]">
          {t("contact.card.title")}
        </div>
        {/* subtitle */}
        <div className="text-center whitespace-normal break-keep">
          <Trans i18nKey="contact.card.subtitle" components={{ br: <br /> }} />
        </div>
      </div>

      {/* content */}
      <div className="flex flex-col gap-[20px]">
        {/* companyName */}
        <div className="flex flex-col gap-[2px]">
          <Input
            type="text"
            value={companyName}
            onChange={(e) =>
              handleChange(e, setCompanyName, setShowCompanyNameError)
            }
            onBlur={(e) => handleBlur(e, setShowCompanyNameError)}
            className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
            placeholder={t("contact.card.companyName")}
            required
          />
          {showCompanyNameError && (
            <span className="w-full px-2 whitespace-normal text-warn break-keep">
              {t("contact.card.error")}
            </span>
          )}
        </div>

        {/* industry */}
        <div className="flex flex-col gap-[2px]">
          <Combobox
            value={selectedIndustry}
            onChange={setSelectedIndustry}
            onClose={() => setQuery("")}
          >
            <div className="relative">
              <ComboboxInput
                className="h-[56px] w-full rounded-[5px] px-5 pr-8 text-gray-500 truncate overflow-hidden whitespace-nowrap"
                displayValue={(industry: Industry) => industry?.name}
                onChange={(e) => setQuery(e.target.value)}
              />
              <ComboboxButton className="group absolute inset-y-0 right-0 px-2.5 bg-transparent">
                <ChevronDownIcon className="size-4 fill-black" />
              </ComboboxButton>
            </div>

            <ComboboxOptions
              // anchor="bottom"
              // transition
              className={clsx(
                "w-[var(--input-width)] bg-black rounded-xl border border-white/5 p-1 [--anchor-gap:var(--spacing-1)] empty:invisible",
                "transition duration-100 ease-in data-[leave]:data-[closed]:opacity-0"
              )}
            >
              {filteredIndustry.map((industry) => (
                <ComboboxOption
                  key={industry.id}
                  value={industry}
                  className="group flex cursor-default items-center gap-2 rounded-lg py-1.5 px-3 select-none data-[focus]:bg-primary-500/20"
                >
                  <CheckIcon className="invisible size-4 group-data-[selected]:visible fill-white" />
                  <div className="text-white text-sm/6">{industry.name}</div>
                </ComboboxOption>
              ))}
            </ComboboxOptions>
          </Combobox>
        </div>

        {/* 담당자이메일 */}
        <div className="flex flex-col gap-[2px]">
          <Input
            type="text"
            value={contactEmail}
            onChange={(e) =>
              handleChange(
                e,
                setContactEmail,
                setShowContactEmailError,
                emailPattern
              )
            }
            onBlur={(e) =>
              handleBlur(e, setShowContactEmailError, emailPattern)
            }
            className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
            placeholder={t("contact.card.contactEmail")}
            required
          />
          {showContactEmailError && (
            <span className="w-full px-2 whitespace-normal text-warn break-keep">
              {t("contact.card.error")}
            </span>
          )}
        </div>

        {/* 담당자전화번호 */}
        <div className="flex flex-col gap-[2px]">
          <Input
            type="tel"
            value={contactPhoneNumber}
            onChange={(e) =>
              handleChange(
                e,
                setContactPhoneNumber,
                setShowContactPhoneNumberError,
                phonePattern
              )
            }
            onBlur={(e) =>
              handleBlur(e, setShowContactPhoneNumberError, phonePattern)
            }
            className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
            placeholder={t("contact.card.contactPhoneNumber")}
            pattern="[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}"
            required
          />
          {showContactPhoneNumberError && (
            <span className="w-full px-2 whitespace-normal text-warn break-keep">
              {t("contact.card.error")}
            </span>
          )}
        </div>
      </div>

      {/* button */}
      <Button
        className={`h-[56px] w-full text-white rounded-lg ${
          isFormValid
            ? "bg-primary-500"
            : "bg-button-background cursor-not-allowed"
        }`}
        disabled={!isFormValid}
        onClick={() => navigate("./completed")}
      >
        {t("contact.card.button")}
      </Button>
    </div>
  );
};

export default ContactUsCard;
