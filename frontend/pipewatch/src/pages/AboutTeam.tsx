import { useTranslation } from "react-i18next";
import member1 from "@assets/images/members/member1.png";
import member2 from "@assets/images/members/member2.png";
import member3 from "@assets/images/members/member3.png";
import member4 from "@assets/images/members/member4.png";
import member5 from "@assets/images/members/member5.png";
import member6 from "@assets/images/members/member6.png";

import AutoAwesomeIcon from "@mui/icons-material/AutoAwesome";

export const AboutTeam = () => {
  const { t } = useTranslation();
  const members = [
    { id: "member1", image: member1 },
    { id: "member2", image: member2 },
    { id: "member3", image: member3 },
    { id: "member4", image: member4 },
    { id: "member5", image: member5 },
    { id: "member6", image: member6 },
  ];

  return (
    <div className="flex flex-col items-center justify-center">
      <h1 className="font-bold text-[80px] font-Esamanru">Team PAORI</h1>
      <div className="flex flex-col items-center my-4">
        <p className="text-[32px]">{t("aboutTeam.description")}</p>
      </div>

      <div className="grid grid-cols-3 gap-x-20">
        {members.map((member, index) => {
          return (
            <div
              key={index}
              className="flex flex-col items-center justify-start my-10"
            >
              <img src={member.image} width={"150px"} />
              <div className="flex flex-col items-center my-4 gap-y-4">
                <p className="flex gap-[4px] items-center font-bold text-[20px]">
                  {t(`aboutTeam.${member.id}.name`)}
                  {["팀장", "Lead"].some((role) =>
                    t(`aboutTeam.${member.id}.role`).includes(role)
                  ) && <AutoAwesomeIcon sx={{ color: "#FFEB3B" }} />}
                </p>
                <p
                  className={`flex flex-col items-center text-[16px] bg-primary-500 text-white px-[12px] py-[4px] rounded-[20px]
                    ${
                      t(`aboutTeam.${member.id}.role`).includes("FE") &&
                      "bg-success"
                    }
                      `}
                >
                  {t(`aboutTeam.${member.id}.role`)}
                </p>
                <p className="flex flex-col items-center text-center text-[16px] whitespace-pre-line">
                  {t(`aboutTeam.${member.id}.description`)}
                </p>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};
