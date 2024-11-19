import {Category} from "@/modules/catalog/models/Category";
import {Media} from "@/modules/catalog/models/Media";

export type Product = {
  id: number;
  name: string;
  shortDescription: string;
  description: string;
  specification: string;
  sku: string;
  gtin: string;
  slug: string;
  weight: number;
  dimensionUnit: string;
  length: number;
  width: number;
  height: number;
  price: number;
  metaTitle: string;
  metaKeyword: string;
  metaDescription: string;
  isAllowedToOrder: boolean;
  isPublished: boolean;
  isFeatured: boolean;
  isVisible?: boolean;
  stockTrackingEnabled: boolean;
  taxIncluded: boolean;
  brandId: number;
  categories: Category[];
  thumbnailMedia: Media;
  productImageMedias: Media[];
  createdOn: Date;
  taxClassId: number;
  parentId: number
}