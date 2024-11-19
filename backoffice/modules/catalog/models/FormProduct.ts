import {Media} from "@/modules/catalog/models/Media";
import {ProductAttributeValue} from "@/modules/catalog/models/ProductAttributeValue";
import {ProductVariation} from "@/modules/catalog/models/ProductVariation";

export type FormProduct = {
  name?: string;
  slug?: string;
  brandId?: number;
  categoryIds?: number[];
  description?: string;
  shortDescription?: string;
  specification?: string;
  sku?: string;
  gtin?: string;
  weight?: number;
  dimensionUnit?: string;
  length?: number;
  width?: number;
  height?: number;
  price?: number;
  isAllowedToOrder?: boolean;
  isPublished?: boolean;
  isFeatured?: boolean;
  isVisibleIndividually?: boolean;
  stockTrackingEnabled?: boolean;
  taxIncluded?: boolean;
  thumbnailMedia?: Media;
  productImageMedias?: Media[];
  metaTitle?: string;
  metaKeyword?: string;
  metaDescription?: string;
  relateProduct?: number[];
  crossSell?: number[];
  productAttributes?: ProductAttributeValue[];
  productVariations?: ProductVariation[];
  taxClassId?: number;
}
