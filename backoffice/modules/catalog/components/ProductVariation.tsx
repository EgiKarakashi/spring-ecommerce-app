import {UseFormGetValues, UseFormSetValue} from "react-hook-form";
import {FormProduct} from "@/modules/catalog/models/FormProduct";
import {useRouter} from "next/router";
import React, {useEffect, useMemo, useState} from "react";
import {SingleValue} from "react-select";
import {ProductOption} from "@/modules/catalog/models/ProductOption";
import {ProductVariation} from "@/modules/catalog/models/ProductVariation";
import {toast} from "react-toastify";
import Select from "react-select";
import {getVariationsByProductId} from "@/modules/catalog/services/ProductService";
import {getProductOptions} from "@/modules/catalog/services/ProductOptionService";
import ProductVariant from "@/modules/catalog/components/ProductVariant";

type Props = {
  getValue: UseFormGetValues<FormProduct>;
  setValue: UseFormSetValue<FormProduct>;
};

const ProductVariations = ({ getValue, setValue }: Props) => {
  const router = useRouter();
  const { id } = router.query;

  const [currentOption, setCurrentOption] = useState<SingleValue<ProductOption>>(null);
  const [productOptions, setProductOptions] = useState<ProductOption[]>([]);
  const [selectedOptions, setSelectedOptions] = useState<string[]>([]);
  const [optionCombines, setOptionCombines] = useState<string[]>([]);
  const [optionValueArray, setOptionValueArray] = useState<any>({});

  useEffect(() => {
    if (id) {
      loadExistingVariant(+id);
    }
  }, [id]);

  const loadExistingVariant = (id: number) => {
    getVariationsByProductId(id).then((results) => {
      if (results) {
        const listOptionCombine: string[] = [];
        const productVariants: ProductVariation[] = [];
        results.forEach((item) => {
          listOptionCombine.push(item.name || '');

          productVariants.push({
            id: item.id,
            optionName: item.name || '',
            optionGTin: item.gtin || '',
            optionSku: item.sku || '',
            optionPrice: item.price || 0,
            optionThumbnail: item.thumbnail,
            optionImages: item.productImages,
            optionValuesByOptionId: item.options,
          });
        });

        setOptionCombines(listOptionCombine);
        setValue('productVariations', productVariants);
      }
    });
  };

  const options = useMemo(() => {
    return Array.isArray(productOptions)
      ? productOptions.map((option) => ({ value: option.name, label: option.name }))
      : [];
  }, [productOptions]);


  let listVariant = useMemo(() => {
    return getValue('productVariations') || [];
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [optionCombines]);

  useEffect(() => {
    getProductOptions().then((data) => {
      setProductOptions(data);
    });
  }, []);

  const onAddOption = (event: React.MouseEvent<HTMLElement>) => {
    event.preventDefault();
    if (!currentOption) {
      toast.info('Select options first');
    } else {
      const index = selectedOptions.indexOf(currentOption.name);
      if (index === -1) {
        setSelectedOptions([...selectedOptions, currentOption.name]);
        if (!optionValueArray[currentOption.name]) {
          setOptionValueArray({ ...optionValueArray, ...{ [currentOption.name]: 1 } });
        }
      } else {
        toast.info(`${currentOption.name} is selected. Select Other`);
      }
    }
  };

  const onDeleteOption = (event: React.MouseEvent<HTMLElement>, option: string) => {
    event.preventDefault();
    setOptionCombines([]);
    const result = selectedOptions.filter((_option) => _option !== option);
    setSelectedOptions([...result]);
  };

  const onGenerate = (event: React.MouseEvent<HTMLElement>) => {
    event.preventDefault();
    const formProductVariations = getValue('productVariations') || [];
    const optionValuesByOptionId = generateProductOptionCombinations();

    if (optionValuesByOptionId.size === 0) {
      return toast.warn('Please Input Values Option');
    }

    const productName = getValue('name');
    const variationName = [productName, ...Array.from(optionValuesByOptionId.values())]
      .join(' ')
      .trim();

    const checkVariationName = formProductVariations.some(
      (variation) => variation.optionName == variationName
    );

    if (checkVariationName) {
      return toast.warning('Combined Option Values are Duplicated');
    }

    let optionValuesByOptionIds = {};

    optionValuesByOptionId.forEach((value, key, fooMap) => {
      optionValuesByOptionIds = Object.assign(optionValuesByOptionIds, { [key]: value });
    });

    const newVariation: ProductVariation = {
      optionName: variationName,
      optionGTin: getValue('gtin') ?? '',
      optionSku: getValue('sku') ?? '',
      optionPrice: getValue('price') ?? 0,
      optionValuesByOptionId: optionValuesByOptionIds,
    };
    setOptionCombines([variationName]);
    setValue('productVariations', [...formProductVariations, newVariation]);
  };

  const generateProductOptionCombinations = (): Map<number, string[]> => {
    const optionValuesByOptionId = new Map<number, string[]>();
    let isEmptyOptions = false;
    const optionValues = [] as string[];
    selectedOptions.forEach((option) => {
      if (isEmptyOptions) return;
      document.getElementsByName(option).forEach((element) => {
        const value = (element as HTMLInputElement).value;
        if (value !== '') {
          optionValues.push(value);
        }
      });
      const optionValue = (document.getElementById(option) as HTMLInputElement).value;
      if (optionValue === '') {
        isEmptyOptions = true;
        return;
      }
      const productOption = productOptions.find((productOption) => productOption.name === option);
      const productOptionId = productOption?.id ?? -1;
      optionValuesByOptionId.set(productOptionId, optionValues);
    });
    return isEmptyOptions ? new Map<number, string[]>() : optionValuesByOptionId;
  };

  const onDeleteVariation = (variant: ProductVariation) => {
    const result = optionCombines.filter((optionName) => optionName !== variant.optionName);
    setOptionCombines(result);

    let productVar = getValue('productVariations') || [];
    productVar = productVar.filter((item) => item.optionName !== variant.optionName);
    setValue('productVariations', productVar);
  };

  const addOptionValue = (option: string) => {
    setOptionValueArray({ ...optionValueArray, ...{ [option]: optionValueArray[option] + 1 } });
  };

  const removeOptionValue = (option: string) => {
    setOptionValueArray({ ...optionValueArray, ...{ [option]: optionValueArray[option] - 1 } });
    if (optionValueArray[option] === 0) {
      removeOptionValue(option);
    }
  };

  return (
    <>
      {/* Selection */}
      <div className="mb-3 d-flex justify-content-evenly">
        <label className="form-label m-0" htmlFor="option">
          Available Options
        </label>
        <Select
          className="w-50"
          options={options}
          isClearable
          isOptionDisabled={(option) => selectedOptions.includes(option.value.toString())}
          onChange={(option) => {
            if (option?.label) {
              setCurrentOption({
                id: +option.value,
                name: option.label,
              });
            }
          }}
        />
        <button className="btn btn-success" onClick={(event) => onAddOption(event)}>
          Add Option
        </button>
      </div>

      {/* Value options */}
      {selectedOptions.length > 0 && (
        <div className="mb-3">
          <h5 className="mb-3">Value Options</h5>
          <div className="mb-3">
            {(selectedOptions || []).map((option) => (
              <div className="mb-3 d-flex gap-4 option-value-box" key={option}>
                <label className="form-label flex-grow-1" htmlFor={option}>
                  {option}
                </label>
                <div className="w-75">
                  {[...Array(optionValueArray[option])].map((e, i, arr) => {
                    return (
                      <div
                        key={optionValueArray[option] + i + e}
                        className="d-flex gap-2 w-100 mb-3"
                      >
                        <div className="w-75">
                          <input type="text" id={option} name={option} className="form-control" />
                        </div>
                        {i == arr.length - 1 && (
                          <i
                            className="fa fa-plus fa-lg"
                            style={{ paddingTop: '12px' }}
                            aria-hidden="true"
                            onClick={() => addOptionValue(option)}
                          ></i>
                        )}
                        <i
                          className="fa fa-minus fa-lg"
                          style={{ paddingTop: '12px' }}
                          aria-hidden="true"
                          onClick={() => removeOptionValue(option)}
                        ></i>
                      </div>
                    );
                  })}
                </div>
                <span
                  aria-hidden="true"
                  className="close"
                  onClick={(event) => onDeleteOption(event, option)}
                >
                  <i className="bi bi-x"></i>
                </span>
              </div>
            ))}
          </div>
          <div className="text-center">
            <button className="btn btn-info" onClick={onGenerate}>
              Generate Combine
            </button>
          </div>
        </div>
      )}

      {/* Product variations */}
      {listVariant.length > 0 && (
        <div className="mb-3">
          <h5 className="mb-3">Product Variations</h5>

          {listVariant.map((variant, index) => (
            <ProductVariant
              key={variant.optionName}
              index={index}
              variant={variant}
              onDelete={onDeleteVariation}
            />
          ))}
        </div>
      )}
    </>
  );
};

export default ProductVariations;
