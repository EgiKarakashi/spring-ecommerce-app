import {NextPage} from "next";
import {useRouter} from "next/router";
import {FieldErrorsImpl, SubmitHandler, useForm} from "react-hook-form";
import {FormProduct} from "@/modules/catalog/models/FormProduct";
import {useEffect, useState} from "react";
import {mapFormProductToProductPayload} from "@/modules/catalog/models/ProductPayload";
import {createProduct} from "@/modules/catalog/services/ProductService";
import {ProductAttributeValuePost} from "@/modules/catalog/models/ProductAttributeValuePost";
import {PRODUCT_URL} from "@/constants/Common";
import {createProductAttributeValueOfProduct} from "@/modules/catalog/services/ProductAttributeValueService";
import {handleCreatingResponse} from "@/common/services/ResponseStatusHandlingService";
import {toast} from "react-toastify";
import {Tab, Tabs} from "react-bootstrap";
import Link from "next/link";

import {
  CrossSellProduct, ProductAttributes, ProductCategoryMapping,
  ProductGeneralInformation,
  ProductImage, ProductSEO,
  ProductVariation, RelatedProduct
} from "@/modules/catalog/components";

const ProductCreate: NextPage = () => {
  const router = useRouter();
  const {
    register,
    setValue,
    handleSubmit,
    getValues,
    watch,
    formState: { errors }
  } = useForm<FormProduct>({
    defaultValues: {
      isVisibleIndividually: true,
      isPublished: true,
      isAllowedToOrder: true
    }
  })
  const [tabKey, setTabKey] = useState<string>("general")



  const onSubmitForm: SubmitHandler<FormProduct> = async (data) => {
    try {
      const payload = mapFormProductToProductPayload(data);
      const productResponse = await createProduct(payload);

      if (productResponse.status === 201) {
        const responseBody = await productResponse.json();
        for (const attribute of data.productAttributes || []) {
          let productAttribute: ProductAttributeValuePost = {
            productId: responseBody.id,
            productAttributeId: attribute.id,
            value: attribute.value
          }
          await createProductAttributeValueOfProduct(productAttribute);
        }
        await router.push(PRODUCT_URL)
      }

      handleCreatingResponse(productResponse)
    } catch (e) {
      toast.error("Create product failed")
    }
  }

  useEffect(() => {
    if (Object.keys(errors).length) {
      setTabKey("general");
      setTimeout(() => {
        document.getElementById(Object.keys(errors)[0])?.scrollIntoView();
      }, 0)
    }
  }, [errors]);

  return (
    <div className="create-product">
      <h2>Create Product</h2>

      <form onSubmit={handleSubmit(onSubmitForm)}>
        <Tabs className="mb-3" activeKey={tabKey} onSelect={(event: any) => setTabKey(event)}>
          <Tab title="General Information" eventKey={'general'}>
            <ProductGeneralInformation
              register={register}
              errors={errors as FieldErrorsImpl<FormProduct>}
              setValue={setValue}
              watch={watch}
            />
          </Tab>
          <Tab eventKey={'image'} title="Product Images">
            <ProductImage setValue={setValue} />
          </Tab>
          <Tab eventKey={'variation'} title="Product Variations">
            <ProductVariation getValue={getValues} setValue={setValue} />
          </Tab>

          <Tab eventKey={'attribute'} title="Product Attributes">
            <ProductAttributes setValue={setValue} getValue={getValues} />
          </Tab>
          <Tab eventKey={'category'} title="Category Mapping">
            <ProductCategoryMapping setValue={setValue} getValue={getValues} />
          </Tab>
          <Tab eventKey={'related'} title="Related Products">
            <RelatedProduct setValue={setValue} getValue={getValues} />
          </Tab>
          <Tab eventKey={'cross-sell'} title="Cross-sell Product">
            <CrossSellProduct setValue={setValue} getValue={getValues} />
          </Tab>
          <Tab eventKey={'seo'} title="SEO">
            <ProductSEO register={register} errors={errors as FieldErrorsImpl<FormProduct>} />
          </Tab>
        </Tabs>
        <div className="text-center">
          <button className="btn btn-primary" type="submit">
            Create
          </button>
          <Link href="/catalog/products">
            <button className="btn btn-secondary m-3">Cancel</button>
          </Link>
        </div>
      </form>
    </div>
  )
}

export  default ProductCreate;
