import {NextPage} from "next";
import {useRouter} from "next/router";
import {useEffect, useState} from "react";
import {Product} from "@/modules/catalog/models/Product";
import {SubmitHandler, useForm} from "react-hook-form";
import {FormProduct} from "@/modules/catalog/models/FormProduct";
import {mapFormProductToProductPayload} from "@/modules/catalog/models/ProductPayload";
import {getProduct, updateProduct} from "@/modules/catalog/services/ProductService";
import {PRODUCT_URL, ResponseStatus} from "@/constants/Common";
import {handleUpdatingResponse} from "@/common/services/ResponseStatusHandlingService";
import {toastError} from "@/common/services/ToastService";
import {Tab, Tabs} from "react-bootstrap";
import {
  CrossSellProduct, ProductCategoryMapping,
  ProductGeneralInformation,
  ProductImage, ProductSEO,
  ProductVariation, RelatedProduct
} from "@/modules/catalog/components";
import ProductAttributes from "../[id]/productAttributes"
import Link from "next/link";

const EditProduct: NextPage  = () => {
  const router = useRouter();
  const { id } = router.query;

  const [product, setProduct] = useState<Product>();
  const [isLoading, setIsLoading] = useState(false);
  const [tabKey, setTabKey] = useState('general');

  const {
    register,
    setValue,
    handleSubmit,
    getValues,
    watch,
    formState: { errors },
  } = useForm<FormProduct>();

  useEffect(() => {
    setIsLoading(true);
    if (id) {
      getProduct(+id)
        .then((data) => {
          if (data.id) {
            setProduct(data);
            setIsLoading(false);
          } else {
            toastError(data.detail)
            router.push(PRODUCT_URL).catch((error) => console.log(error))
          }
        })
        .catch((error) => console.log(error))
    }
  }, [id]);

  const onSubmit: SubmitHandler<FormProduct> = async (data) => {
    if (id) {
      const payload = mapFormProductToProductPayload(data);
      const productResponse = await updateProduct(+id, payload);
      if (productResponse.status === ResponseStatus.SUCCESS) {
        await router.push(PRODUCT_URL).catch((error) => console.log(error));
      }
      handleUpdatingResponse(productResponse)
    }
  }

  useEffect(() => {
    if (Object.keys(errors).length) {
      setTabKey('general');
      setTimeout(() => {
        document.getElementById(Object.keys(errors)[0])?.scrollIntoView();
      }, 0)
    }
  }, [errors]);

  if (isLoading) return <p>Loading...</p>
  if (!product) {
    return <p>No product</p>
  } else {
    return (
      <div className="create-product">
        <h2>Update Product: {product.name}</h2>

        <form onSubmit={handleSubmit(onSubmit)}>
          <Tabs className="mb-3" activeKey={tabKey} onSelect={(event: any) => setTabKey(event)}>
            <Tab eventKey={'general'} title="General Information">
              <ProductGeneralInformation register={register} errors={errors} setValue={setValue} watch={watch} />
            </Tab>
            <Tab eventKey={'image'} title="Product Images">
              <ProductImage product={product} setValue={setValue} />
            </Tab>
            <Tab eventKey={'variation'} title="Product Variations">
              <ProductVariation getValue={getValues} setValue={setValue} />
            </Tab>
            <Tab eventKey={'attribute'} title="Product Attributes">
              <ProductAttributes />
            </Tab>
            <Tab eventKey={'category'} title="Category Mapping">
              <ProductCategoryMapping product={product} setValue={setValue} getValue={getValues} />
            </Tab>
            <Tab eventKey={'related'} title="Related Products">
              <RelatedProduct setValue={setValue} getValue={getValues} />
            </Tab>
            <Tab eventKey={'cross-sell'} title="Cross-sell Product">
              <CrossSellProduct setValue={setValue} getValue={getValues} />
            </Tab>
            <Tab eventKey={'seo'} title="SEO">
              <ProductSEO product={product} register={register} errors={errors} />
            </Tab>
          </Tabs>
          {tabKey == "attribute" ? (
            <div className="text-center"></div>
          ) : (
            <div className="text-center">
              <button className="btn btn-primary" type="submit">Save</button>
              <Link href="/catalog/products">
                <button className="btn btn-secondary m-3">Cancel</button>
              </Link>
            </div>
          )}
        </form>
      </div>
    )
  }
}

export default EditProduct;
