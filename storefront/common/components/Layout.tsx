import React from "react";
import Head from "next/head";
import AuthenticationInfo from "@/common/components/AuthenticationInfo";
import Header from "@/common/components/common/Header";
import Footer from "@/common/components/common/Footer";

type Props = {
  children: React.ReactNode;
};

export default function Layout({ children }: Props) {
  return (
    <>
      <Head>
        <title>Ecommerce - Storefront</title>
        <meta name="description" content="Yet another shop storefront" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Header>
        <AuthenticationInfo />
      </Header>
      <div className="body">{children}</div>
      <Footer />
    </>
  );
}
