import {
  Description,
  Dialog,
  DialogPanel,
  DialogTitle,
} from "@headlessui/react";
import { IconButton } from "@components/common/IconButton";
import { useTranslation } from "react-i18next";
import { useState } from "react";

import { getApiClient } from "@src/stores/apiClient";

type ManageCookiesProps = {
  isOpen: boolean;
  onClose: () => void;
};

export const ReVerifyModal = ({ isOpen, onClose }: ManageCookiesProps) => {
  const { t } = useTranslation();

  const [isRequested, setIsRequested] = useState(false);

  const enterpriseName = localStorage.getItem("enterpriseName");

  const apiClient = getApiClient();
  const roleRequest = async () => {
    try {
      const res = await apiClient.patch(`/api/users/role-request`);
      console.log("result", res);
      setIsRequested(true);
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <Dialog open={isOpen} onClose={onClose} className="relative z-50">
      <div className="fixed inset-0 flex items-center justify-center w-screen p-4 bg-black/80">
        <DialogPanel className="max-w-lg p-8 space-y-4 bg-white border rounded-[8px]">
          {isRequested ? (
            <>
              <DialogTitle className="text-[20px] my-[10px]">
                {/* {t("footer.manageCookies.description")} */}
                재인증 신청이 완료되었습니다.
              </DialogTitle>
              <div className="flex justify-center">
                <IconButton
                  handleClick={onClose}
                  // text={t("footer.manageCookies.buttonDeny")}
                  text={"닫기"}
                  color={"bg-gray-500"}
                  hoverColor={"hover:bg-gray-500/80"}
                />
              </div>
            </>
          ) : (
            <>
              <DialogTitle className="font-bold text-[24px]">
                {/* {t("footer.manageCookies.title")} */}
                기업 재인증 신청
              </DialogTitle>
              <Description className="text-[18px]">
                {/* {t("footer.manageCookies.description")} */}
                <b>{enterpriseName}</b>의 기업 재인증 신청을 요청합니다.
              </Description>
              <div className="flex justify-center gap-4">
                <IconButton
                  handleClick={onClose}
                  // text={t("footer.manageCookies.buttonDeny")}
                  text={"취소"}
                  color={"bg-gray-500"}
                  hoverColor={"hover:bg-gray-500/80"}
                />
                <IconButton
                  handleClick={() => roleRequest()}
                  // text={t("footer.manageCookies.buttonAccept")}
                  text={"재인증 신청"}
                  color={"bg-primary-500"}
                  hoverColor={"hover:bg-primary-500/80"}
                />
              </div>
            </>
          )}
        </DialogPanel>
      </div>
    </Dialog>
  );
};
