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
import { ChangeEvent, useState, FocusEvent, useMemo, useCallback } from "react";
import clsx from "clsx";

const ContactUsCard = () => {
  const { t } = useTranslation();
  const [companyName, setCompanyName] = useState("");
  const [contactEmail, setContactEmail] = useState("");
  const [contactPhoneNumber, setContactPhoneNumber] = useState("");
  const [showCompanyNameError, setShowCompanyNameError] = useState(false);
  const [showContactEmailError, setShowContactEmailError] = useState(false);
  const [showContactPhoneNumberError, setShowContactPhoneNumberError] =
    useState(false);

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

  // industry 관련
  type Industry = {
    id: number;
    name: string;
  };

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
  const [query, setQuery] = useState("");

  const filteredIndustry =
    query === ""
      ? industryList
      : industryList.filter((industry) =>
          industry.name.toLowerCase().includes(query.toLowerCase())
        );

  const isFormValid: boolean =
    !showCompanyNameError &&
    !showContactEmailError &&
    !showContactPhoneNumberError &&
    companyName &&
    contactEmail &&
    contactPhoneNumber;

  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px]">
      <div className="flex flex-col gap-[10px]">
        <div className="flex justify-center font-semibold text-[28px]">
          {t("contact.card.title")}
        </div>
        <div className="text-center whitespace-normal break-keep">
          <Trans i18nKey="contact.card.subtitle" components={{ br: <br /> }} />
        </div>
      </div>

      <div className="flex flex-col gap-[20px]">
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
            <span className="text-warn">{t("contact.card.error")}</span>
          )}
        </div>

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
              className={clsx(
                "w-[var(--input-width)] bg-black rounded-xl border border-white/5 p-1 transition duration-100 ease-in"
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
            <span className="text-warn">{t("contact.card.error")}</span>
          )}
        </div>

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
            required
          />
          {showContactPhoneNumberError && (
            <span className="text-warn">{t("contact.card.error")}</span>
          )}
        </div>
      </div>

      <Button
        className={`h-[56px] w-full text-white rounded-lg ${
          isFormValid
            ? "bg-primary-500"
            : "bg-button-background cursor-not-allowed"
        }`}
        disabled={!isFormValid}
      >
        {t("contact.card.button")}
      </Button>
    </div>
  );
};

export default ContactUsCard;
