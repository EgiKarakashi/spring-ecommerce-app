import {NextPage} from "next";
import {useRouter} from "next/router";
import {useForm} from "react-hook-form";
import {CustomerCreateVM} from "@/modules/customer/models/Customer";
import {createCustomer} from "@/modules/customer/services/CustomerService";
import {CUSTOMER_URL} from "@/constants/Common";
import {handleCreatingResponse} from "@/common/services/ResponseStatusHandlingService";
import Link from "next/link";
import CustomerInformation from "@/modules/customer/components/CustomerInformation";

const CustomerCreate: NextPage = () => {
  const router = useRouter();
  const {
    register,
    handleSubmit,
    formState: { errors },
    watch,
  } = useForm<CustomerCreateVM>();

  const handleSubmitCustomer = async (event: CustomerCreateVM) => {
    let customer: CustomerCreateVM = {
      username: event.username,
      email: event.email,
      firstName: event.firstName,
      lastName: event.lastName,
      password: event.password,
      role: event.role,
    };

    let response = await createCustomer(customer);

    if (response.status === 201) {
      router.replace(CUSTOMER_URL);
    }
    handleCreatingResponse(response);
  };

  return (
    <div className="row mt-5">
      <div className="col-md-8">
        <h2>Create customer</h2>
        <form onSubmit={handleSubmit(handleSubmitCustomer)}>
          <CustomerInformation register={register} errors={errors} watch={watch} />
          <button className="btn btn-primary" type="submit">
            Save
          </button>
          <Link href="/customers">
            <button className="btn btn-primary" style={{ background: 'red', marginLeft: '30px' }}>
              Cancel
            </button>
          </Link>
        </form>
      </div>
    </div>
  );
};

export default CustomerCreate;
