export interface CompanyType {
  enterpriseId: number;
  name: string;
  industry: string;
}

export interface CompanyListboxProps {
  onCompanyChange: (selectedCompany: CompanyType) => void;
}
