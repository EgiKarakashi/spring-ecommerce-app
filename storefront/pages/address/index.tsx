import {NextPage} from "next";
import { Address as AddressModel } from "@/modules/address/models/AddressModel";
import {BreadCrumbModel} from "@/modules/breadcrumb/model/BreadCrumbModel";
import {useRouter} from "next/router";
import {useEffect, useState} from "react";
import {chooseDefaultAddress, deleteUserAddress, getUserAddress} from "@/modules/customer/services/CustomerService";
import {deleteAddress} from "@/modules/address/services/AddressService";
import {toast} from "react-toastify";
import {DELETE_SUCCESSFULLY, UPDATE_SUCCESSFULLY} from "@/common/constants/Common";
import ProfileLayout from "@/common/components/ProfileLayout";
import {BiPlusMedical} from "react-icons/bi";

import styles from '../../styles/address.module.css';
import clsx from "clsx";
import { TiContacts } from "react-icons/ti";
import {HiCheckCircle} from "react-icons/hi";
import Link from "next/link";
import {FiEdit} from "react-icons/fi";
import {FaTrash} from "react-icons/fa";
import ModalDeleteCustom from "@/common/items/ModalDeleteCustom";
import ModalChooseDefaultAddress from "@/common/items/ModalChooseDefaultAddress";

const crumb: BreadCrumbModel[] = [
  {
    pageName: 'Home',
    url: '/'
  },
  {
    pageName: 'Address',
    url: '/#'
  }
]

const Address: NextPage = () => {
  const router = useRouter()
  const [addresses, setAddresses] = useState<AddressModel[]>([])
  const [showModalDelete, setShowModalDelete] = useState<boolean>(false)
  const [addressIdWantToDelete, setAddressIdWantToDelete] = useState<number>(0)
  const [showModalChooseDefaultAddress, setShowModalChooseDefaultAddress] = useState<boolean>(false)
  const [defaultAddress, setDefaultAddress] = useState<number>(0)
  const [currentDefaultAddress, setCurrentDefaultAddress] = useState<number>(0)

  const handleClose = () => {
    setShowModalDelete(false)
  }

  const handleDelete = () => {
    if (addressIdWantToDelete == 0) {
      return
    }
    deleteUserAddress(addressIdWantToDelete || 0)
      .then(() => {
        deleteAddress(addressIdWantToDelete || 0)
        getUserAddress().then((res) => {
          setAddresses(res)
        })
        setShowModalDelete(false)
        toast.success(DELETE_SUCCESSFULLY)
      })
      .catch((err) => {
       console.log(err)
      })
  }

  const handleCloseModalChooseDefaultAddress = () => {
    setShowModalChooseDefaultAddress(false)
  }

  const handleChoose = () => {
    chooseDefaultAddress(defaultAddress)
      .then(() => {
        {
          setShowModalChooseDefaultAddress(false)
          toast.success(UPDATE_SUCCESSFULLY)
          setCurrentDefaultAddress(defaultAddress)
        }
      })
      .catch((err) => {
        console.log(err)
      })
  }

  useEffect(() => {
    getUserAddress().then((res) => {
      setAddresses(res)
      setCurrentDefaultAddress(res.find((address: any) => address.isActive == true)?.id)
    })
  }, []);

  return (
    <ProfileLayout breadcrumb={crumb} title="Address" menuActive="address">
      <div className="d-grid gap-2">
        <button className="btn-create-address" onClick={() => router.push("/address/create")}>
          <BiPlusMedical />
          <span className="ms-2">Create address</span>
        </button>
      </div>

      <div className="container p-0">
        <div className="row">
          {addresses?.length == 0 ? (
            <>No address found</>
          ) : (
            addresses?.map((address) => {
              return (
                <div className="col-lg-4 md-6 col-sm-12" key={address?.id}>
                  <div className={styles['card-wrapper']}>
                    <div className={clsx(styles['card-layout'], 'd-flex')}>
                      <div
                        className="d-flex justify-content-center align-items-center"
                        style={{
                          width: '100px',
                          background: '#ea1161',
                          borderRadius: '5px 0 0 5px',
                          filter: 'brightness(90%)',
                        }}
                      >
                        <div style={{fontSize: '50px'}}>
                          <TiContacts  style={{color: '#ffffff'}}/>
                        </div>
                      </div>
                      <div
                        className="p-2"
                        style={{
                          background: '#ea1161',
                          borderRadius: '0 5px 5px 0',
                          width: '100%',
                        }}
                      >
                        <div>
                          {address?.id == currentDefaultAddress && (
                            <div className="m-2" style={{ float: 'right' }}>
                              <div
                                style={{
                                  width: '15px',
                                  height: '15px',
                                  borderRadius: '50%',
                                  background: '#0eea5d',
                                }}
                              ></div>
                            </div>
                          )}
                          <p style={{fontSize: '14px'}}>Contact name: {address.contactName}</p>
                          <p
                            style={{
                            fontSize: '14px',
                            wordBreak: 'break-word',
                          }}>
                            Address: {address.addressLine1}
                          </p>
                          <p style={{ fontSize: '14px' }}>Phone number: {address.phone}</p>
                        </div>
                        <div
                          className="d-flex justify-content-end"
                          style={{ position: 'relative', bottom: '0' }}
                        >
                          <div
                            className="m-1"
                            data-toggle="tooltip"
                            title="Active"
                            style={{ cursor: 'pointer' }}
                            onClick={() => {
                              setShowModalChooseDefaultAddress(true);
                              setDefaultAddress(address.id || 0);
                              if (defaultAddress != 0) {
                              }
                            }}
                          >
                            <HiCheckCircle />
                          </div>
                          <div className="m-1" data-toggle="tooltip" title="Edit">
                            <Link href={{ pathname: `/address/${address.id}/edit` }}>
                              <FiEdit />
                            </Link>
                          </div>
                          <div
                            className="m-1"
                            data-toggle="tooltip"
                            title="Delete"
                            onClick={() => {
                              setShowModalDelete(true);
                              setAddressIdWantToDelete(address.id || 0);
                            }}
                          >
                            <FaTrash className={styles['remove-address']} />
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              )
            })
          )}
        </div>
      </div>
      <ModalDeleteCustom
      showModalDelete={showModalDelete}
      handleClose={handleClose}
      handleDelete={handleDelete}
      action="delete"
      />
       <ModalChooseDefaultAddress
         showModalChooseDefaultAddress={showModalChooseDefaultAddress}
         handleClose={handleCloseModalChooseDefaultAddress}
         handleChoose={handleChoose}
         />
    </ProfileLayout>
  )
}

export default Address;
