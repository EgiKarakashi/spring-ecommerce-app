import {BreadCrumbModel} from "@/modules/breadcrumb/model/BreadCrumbModel";
import {NextPage} from "next";
import {useForm} from "react-hook-form";
import {useRouter} from "next/router";
import {Address} from "@/modules/address/models/AddressModel";
import {createUserAddress} from "@/modules/customer/services/CustomerService";
import {CREATE_SUCCESSFULLY} from "@/common/constants/Common";
import {toast} from "react-toastify";
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
    pageName: 'Create',
    url: '#',
  },
];

const CreateAddress: NextPage = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<Address>();

  const router = useRouter();

  const onSubmit = async (data: any) => {
    const {
      contactName,
      phone,
      addressLine1,
      city,
      zipCode,
      districtId,
      stateOrProvinceId,
      countryId,
    } = data;
    const request: Address = {
      contactName,
      phone,
      addressLine1,
      city,
      zipCode,
      districtId: parseInt(districtId),
      stateOrProvinceId: parseInt(stateOrProvinceId),
      countryId: parseInt(countryId),
    };
    createUserAddress(request)
      .then(() => {
        toast.success(CREATE_SUCCESSFULLY);
        router.push('/address');
      })
      .catch((e) => {
        console.log(e);
      });
  };

  return (
    <ProfileLayout breadcrumb={crumb} title="Create Address" menuActive="address">
      <AddressForm
        handleSubmit={handleSubmit(onSubmit)}
        isDisplay={true}
        register={register}
        errors={errors}
        address={undefined}
        setValue={() => {}}
      />
    </ProfileLayout>
  );
};

export default CreateAddress;
