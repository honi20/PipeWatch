import React, { createContext, useContext, useState, ReactNode } from "react";

type SelectViewType = "MODEL_MEMO" | "PROPERTY" | "PIPE_MEMO";

type SelectViewContextType = {
  selectView: SelectViewType;
  setSelectView: React.Dispatch<React.SetStateAction<SelectViewType>>;
};

const SelectViewContext = createContext<SelectViewContextType | undefined>(
  undefined
);

export const SelectViewProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [selectView, setSelectView] = useState<SelectViewType>("MODEL_MEMO");
  return (
    <SelectViewContext.Provider value={{ selectView, setSelectView }}>
      {children}
    </SelectViewContext.Provider>
  );
};

export const useSelectView = () => {
  const context = useContext(SelectViewContext);
  if (!context) {
    throw new Error("useSelectView must be used within a SelectViewProvider");
  }
  return context;
};
