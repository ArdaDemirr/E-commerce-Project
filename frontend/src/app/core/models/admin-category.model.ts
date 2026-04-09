export interface CategoryResponseDTO {
  id: number;
  name: string;
  parent?: CategoryResponseDTO;
}

export interface AdminCategoryRequestDTO {
  categoryName: string;
  parentCategoryId?: number | null;
}
