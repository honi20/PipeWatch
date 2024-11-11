export interface CompanyType {
  enterpriseId: number;
  name: string;
  industry: string;
}

export interface CompanyListboxProps {
  onCompanyChange: (selectedCompany: CompanyType) => void;
}

// {
//   "email": "pipewatch@paori.com",
//   "password": "ssafy1234",
//   "name": "파오리",
//   "enterpriseId": 1,
//   "empNo": 1123456,
//   "department": "IT 개발부",
//   "empClass": "대리",
//   "verifyCode": "603942"
// }
