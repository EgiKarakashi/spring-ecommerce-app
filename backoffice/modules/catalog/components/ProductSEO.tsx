import {Product} from "@/modules/catalog/models/Product";
import {FieldErrors, UseFormRegister} from "react-hook-form";
import {FormProduct} from "@/modules/catalog/models/FormProduct";
import {Input, TextArea} from "@/common/items/Input";

type Props = {
  product?: Product;
  register: UseFormRegister<FormProduct>;
  errors: FieldErrors<FormProduct>;
};

const ProductSEO = ({ product, register, errors }: Props) => {
  return (
    <>
      {product ? (
        <>
          <div>
            <Input
              labelText="Meta Title"
              field="metaTitle"
              defaultValue={product?.metaTitle}
              register={register}
              error={errors.metaTitle?.message}
            />
            <Input
              labelText="Meta Keyword"
              field="metaKeyword"
              defaultValue={product?.metaKeyword}
              register={register}
              error={errors.metaKeyword?.message}
            />
            <Input
              labelText="Meta Description"
              field="metaDescription"
              defaultValue={product?.metaDescription}
              register={register}
              error={errors.metaDescription?.message}
            />
          </div>
        </>
      ) : (
        <>
          <div>
            <Input labelText="Meta Title" field="metaTitle" register={register} />
            <TextArea labelText="Meta Keywords" field="metaKeyword" register={register} />
            <TextArea labelText="Meta Description" field="metaDescription" register={register} />
          </div>
        </>
      )}
    </>
  );
};

export default ProductSEO;
