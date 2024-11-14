import { useTranslation } from "react-i18next";

type Article = {
  title: string;
  content: string[];
};

type Chapter = {
  title: string;
  [key: string]: Article | string;
};

type Appendix = {
  title: string;
  content: string[];
};

type Terms = {
  title: string;
  [key: string]: Chapter | Appendix | string;
};

export const TermsAndPolicy = () => {
  const { t } = useTranslation();
  const terms: Terms = t("terms", { returnObjects: true }) as Terms;

  return (
    <div className="p-[30px] text-[20px]">
      <h1 className="text-[40px]">{terms.title}</h1>
      {Object.entries(terms).map(([chapterKey, chapter], chapterIndex) => {
        if (chapterKey === "title") return null;

        if (chapterKey === "appendix") {
          const appendix = chapter as Appendix;
          return (
            <section key={chapterIndex}>
              <h2 className="text-[30px]">{appendix.title}</h2>
              <ul>
                {appendix.content.map((item, itemIndex) => (
                  <li key={itemIndex}>{item}</li>
                ))}
              </ul>
            </section>
          );
        }

        const chapterData = chapter as Chapter;

        return (
          <section className="my-[40px]" key={chapterIndex}>
            <h2 className="text-[30px]">{chapterData.title}</h2>
            {Object.entries(chapterData)
              .filter(([key]) => key.startsWith("article"))
              .map(([, article], articleIndex) => {
                const articleData = article as Article;
                return (
                  <article className="my-[20px]" key={articleIndex}>
                    <h3 className="text-[24px]">{articleData.title}</h3>
                    <ul>
                      {articleData.content.map((item, itemIndex) => (
                        <li key={itemIndex}>{item}</li>
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
