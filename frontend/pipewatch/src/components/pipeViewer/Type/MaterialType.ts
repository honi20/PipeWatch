export interface MaterialType {
  materialId: number;
  koreanName: string;
  englishName: string;
}
export interface MaterialListType {
  type: string;
  materials: MaterialType[];
}
