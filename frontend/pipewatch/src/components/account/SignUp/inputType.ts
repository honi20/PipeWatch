export interface CompanyType {
  id: number;
  company: string;
}

export interface CompanyListboxProps {
  onCompanyChange: (selectedCompany: CompanyType) => void;
}
