export interface CategoryResponseDTO {
  id: number;
  categoryName: string;
  parentId?: number | null;
  parentCategoryName?: string;
}

export interface AdminCategoryRequestDTO {
  categoryName: string;
  parentCategoryId?: number | null;
}
