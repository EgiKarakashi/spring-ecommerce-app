import {BreadCrumbModel} from "@/modules/breadcrumb/model/BreadCrumbModel";
import Head from "next/head";
import BreadCrumbComponent from "@/common/components/BreadCrumbComponent";

const crumb: BreadCrumbModel[] = [
  {
    pageName: 'Home',
    url: '/'
  },
  {
    pageName: 'About',
    url: '/about'
  }
]

const About = () => {
  return (
    <>
      <Head>
        <title>About</title>
      </Head>
      <BreadCrumbComponent props={crumb} />
    </>
  )
}
