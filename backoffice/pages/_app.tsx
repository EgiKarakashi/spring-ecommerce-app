import "@/styles/globals.css";
import type { AppProps } from "next/app";
import Head from "next/head";
import Script from "next/script";
import Layout from "@/common/components/Layout";

import 'bootstrap-icons/font/bootstrap-icons.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-quill-new/dist/quill.snow.css';
import 'react-toastify/dist/ReactToastify.css';

import '../styles/common/style.css';
import '../styles/globals.css';
import '../styles/TextEditor.css';

const App = ({Component, pageProps}: AppProps) => {
  return (
   <>
     <Head>
       <meta name="viewport" content="width=device-width, initial-scale=1" />
     </Head>
     <Script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" />
     <Layout>
       <Component {...pageProps} />
     </Layout>
   </>
  )
}

export default App;
