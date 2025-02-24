import React from "react";
import {BreadCrumbModel} from "@/modules/breadcrumb/model/BreadCrumbModel";
import {Container} from "react-bootstrap";
import Head from "next/head";
import BreadCrumbComponent from "@/common/components/BreadCrumbComponent";
import UserProfileLeftSideBar from "@/common/components/UserProfileLeftSideBar";

type Props = {
  children: React.ReactNode;
  breadcrumb: BreadCrumbModel[];
  title?: string;
  menuActive: string;
}

export default function ProfileLayout({children, breadcrumb, title, menuActive}: Props) {
  return (
    <Container>
      <Head>
        <title>{title ?? "Profile"}</title>
      </Head>
      <div
        className="d-flex justify-content-between pt-5 col-md-12 mb-2"
        style={{height: '100px'}}
      >
      <BreadCrumbComponent props={breadcrumb} />
      </div>
      <div className="container mb-5">
        <div className="row">
          <div className="col-md-3 p-0">
            <UserProfileLeftSideBar type={menuActive} />
          </div>
          <div className="col-md-9">{children}</div>
        </div>
      </div>
    </Container>
  )
}
