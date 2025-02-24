import {BreadCrumbModel} from "@/modules/breadcrumb/model/BreadCrumbModel";
import {NextPage} from "next";
import {useForm} from "react-hook-form";
import {Customer} from "@/modules/profile/models/Customer";
import {useEffect, useState} from "react";
import {getMyProfile, updateCustomer} from "@/modules/profile/services/ProfileService";
import {ProfileRequest} from "@/modules/profile/models/ProfileRequest";
import {toast} from "react-toastify";
import {UPDATE_SUCCESSFULLY} from "@/common/constants/Common";
import ProfileLayout from "@/common/components/ProfileLayout";
import {Input} from "@/common/items/Input";
import Link from "next/link";

const crumb: BreadCrumbModel[] = [
  {
    pageName: "Home",
    url: "/"
  },
  {
    pageName: "Profile",
    url: "#"
  }
]

const Profile: NextPage = () => {
  const {handleSubmit, register} = useForm<Customer>()
  const [customer, setCustomer] = useState<Customer>()

  useEffect(() => {
    getMyProfile()
      .then((data) => {
        setCustomer(data)
      })
      .catch((error) => {
        console.log(error)
      })
  }, []);

  const onSubmit = async (data: any, event: any) => {
    const request: ProfileRequest = {
      firstName: event.target.firstName.value,
      lastName: event.target.lastName.value,
      email: event.target.email.value
    }
    updateCustomer(request).then(async (_res) => {
      toast.success(UPDATE_SUCCESSFULLY)
    })
  }

  return (
    <ProfileLayout breadcrumb={crumb} menuActive="profile" title="Profile">
      <form onSubmit={handleSubmit(onSubmit)} className="w-75">
        <Input
          labelText="Username"
          field="username"
          defaultValue={customer?.username}
          register={register}
          disabled
        />
        <Input
          labelText="First name"
          field="firstName"
          defaultValue={customer?.firstName}
          register={register}
          disabled
        /><Input
        labelText="Last name"
        field="lastName"
        defaultValue={customer?.lastName}
        register={register}
        disabled
      />
        <Input labelText="Email" field="email" defaultValue={customer?.email} register={register} />
        <div className="text-center">
          <button className="btn btn-primary" type='submit'>
            Update
          </button>
          <Link href="/">
            <button className="btn btn-secondary">Cancel</button>
          </Link>
        </div>
      </form>
    </ProfileLayout>
  )
}

export default Profile;
