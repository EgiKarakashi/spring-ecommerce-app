import {useEffect} from "react";
import {Product} from "@/modules/catalog/models/Product";
import {FormProduct} from "@/modules/catalog/models/FormProduct";
import {UseFormSetValue} from "react-hook-form";
import ChooseThumbnail from "@/modules/catalog/components/ChooseThumbnail";
import ChooseImages from "@/modules/catalog/components/ChooseImages";

type Props = {
  product?: Product;
  setValue: UseFormSetValue<FormProduct>;
};

const ProductImage = ({ product, setValue }: Props) => {
  useEffect(() => {
    setValue('thumbnailMedia', product?.thumbnailMedia);
    setValue('productImageMedias', product?.productImageMedias);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <>
      <div className="mb-3">
        <h4 className="mb-3">Thumbnail</h4>
        <ChooseThumbnail
          id="main-thumbnail"
          image={
            product?.thumbnailMedia
              ? { id: +product.thumbnailMedia.id, url: product.thumbnailMedia.url }
              : null
          }
          setValue={setValue}
          name="thumbnailMedia"
        />
      </div>
      <div className="mb-3">
        <h4 className="mb-3">Product Image</h4>

        <ChooseImages
          id="main-product-images"
          images={
            product?.productImageMedias
              ? product.productImageMedias.map((image) => ({
                id: image.id,
                url: image.url,
              }))
              : []
          }
          setValue={setValue}
          name="productImageMedias"
        />
      </div>
    </>
  );
};

export default ProductImage;
