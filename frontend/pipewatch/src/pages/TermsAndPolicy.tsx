import { useTranslation } from "react-i18next";

type Article = {
  title: string;
  content: {
    circle: string[];
    number: string[];
  };
};

type Chapter = {
  title: string;
  [key: string]: Article | string;
};

type Appendix = {
  title: string;
  content: {
    circle: string[];
    number: string[];
  };
};

type Terms = {
  title: string;
  [key: string]: Chapter | Appendix | string;
};

export const TermsAndPolicy = () => {
  const { t } = useTranslation();
  const terms: Terms = t("terms", { returnObjects: true }) as Terms;

  return (
    <div className="px-[30px] text-[20px]">
      <div className="border-b-4 border-solid border-primary-200 dark:border-primary-500 my-[30px] py-[10px]">
        <h1 className="text-[40px]">{terms.title}</h1>
      </div>
      {Object.entries(terms).map(([key, value], index) => {
        if (key === "title") return null;

        if (key === "appendix") {
          const appendix = value as Appendix;
          return (
            <section key={index}>
              <h2 className="text-[30px]">{appendix.title}</h2>
              <ul>
                {appendix.content.circle.map((item, idx) => (
                  <li key={`circle-${idx}`} className="text-[18px]">
                    {item}
                  </li>
                ))}
                {appendix.content.number.map((item, idx) => (
                  <li key={`number-${idx}`} className="text-[18px]">
                    {item}
                  </li>
                ))}
              </ul>
            </section>
          );
        }

        const chapter = value as Chapter;
        return (
          <section className="mb-[50px]" key={index}>
            <h2 className="text-[30px]">{chapter.title}</h2>
            {Object.entries(chapter)
              .filter(([key]) => key.startsWith("article"))
              .map(([, article], articleIndex) => {
                const articleData = article as Article;
                return (
                  <article key={articleIndex} className="my-[20px]">
                    <h3 className="text-[24px]">{articleData.title}</h3>
                    <ul>
                      {articleData.content.circle.map((item, idx) => (
                        <li key={`circle-${idx}`} className="text-[18px]">
                          {item}
                        </li>
                      ))}

                      {articleData.content.number.map((item, idx) => (
                        <div className="mx-[20px]">
                          <li key={`number-${idx}`} className="text-[18px]">
                            {item}
                          </li>
                        </div>
                      ))}
                    </ul>
                  </article>
                );
              })}
          </section>
        );
      })}
    </div>
  );
};
