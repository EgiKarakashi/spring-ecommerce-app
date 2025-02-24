import {BreadCrumbModel} from "@/modules/breadcrumb/model/BreadCrumbModel";
import {NextPage} from "next";
import {useForm} from "react-hook-form";
import {Address} from "@/modules/address/models/AddressModel";
import {useRouter} from "next/router";
import {useEffect, useState} from "react";
import {getAddress, updateAddress} from "@/modules/address/services/AddressService";
import {UPDATE_SUCCESSFULLY} from "@/common/constants/Common";
import {toast} from "react-toastify";
import Head from "next/head";
import ProfileLayout from "@/common/components/ProfileLayout";
import AddressForm from "@/modules/address/components/AddressForm";

const crumb: BreadCrumbModel[] = [
  {
    pageName: 'Home',
    url: '/',
  },
  {
    pageName: 'Address',
    url: '/address',
  },
  {
    pageName: 'Edit',
    url: '#',
  },
];

const EditAddress: NextPage = () => {
  const {register, handleSubmit, formState: {errors}} = useForm<Address>()
  const router = useRouter()
  const {id} = router.query;
  router.isReady = true;
  const [address, setAddress] = useState<Address>()

  useEffect(() => {
    if (id) {
      getAddress(id as string)
        .then((res) => {
          if (res.title && res.title == 'Not Found') return;
          setAddress(res)
        })
    }
  }, [id]);

  const onSubmit = async (data: any ) => {
    const {
      contactName,
      phone,
      addressLine1,
      city,
      zipCode,
      districtId,
      stateOrProvinceId,
      countryId
    } = data;
    const request: Address = {
      contactName,
      phone,
      addressLine1,
      city,
      zipCode,
      districtId: parseInt(districtId),
      stateOrProvinceId: parseInt(stateOrProvinceId),
      countryId: parseInt(countryId)
    }
    updateAddress(id  as string, request)
      .then(() => {
        toast.success(UPDATE_SUCCESSFULLY)
        router.push("/address")
      })
      .catch((e) => console.log(e))
  }

  if (!id) return <></>
  if (id && !address) {
    return (
      <>
        <Head>
          <title>Edit Address</title>
        </Head>
        <div className="container pt-5" style={{ minHeight: '550px' }}>
          <h2 className="mb-3">Edit Address</h2>
          <p>Address not found</p>
        </div>
      </>
    )
  }

  return (
    <ProfileLayout breadcrumb={crumb} menuActive='address' title="Edit Address">
      <AddressForm
        handleSubmit={handleSubmit(onSubmit)}
        register={register}
        errors={errors}
        address={address}
        isDisplay={true}
        setValue={() => {}}
      />
    </ProfileLayout>
  )
}

export  default EditAddress;
