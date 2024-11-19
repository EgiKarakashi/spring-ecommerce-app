import {ProductVariationPost} from "@/modules/catalog/models/ProductVariationPost";

export type ProductVariationPut = ProductVariationPost & { id?: number };
