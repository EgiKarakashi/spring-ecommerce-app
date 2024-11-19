import {NextPage} from "next";
import {useEffect, useState} from "react";
import {Product} from "@/modules/catalog/models/Product";
import {deleteProduct, getProducts} from "@/modules/catalog/services/ProductService";
import {handleDeletingResponse} from "@/common/services/ResponseStatusHandlingService";
import Link from "next/link";
import {Button, Form, InputGroup, Stack, Table} from "react-bootstrap";
import styles from '../../../styles/Filter.module.css';
import {FaSearch} from "react-icons/fa";
import moment from "moment";
import ReactPaginate from "react-paginate";
import {Brand} from "@/modules/catalog/models/Brand";
import {getBrands} from "@/modules/catalog/services/BrandService";
import {ExportProduct} from "@/modules/catalog/components";
import ModalDeleteCustom from "@/common/items/ModalDeleteCustom";

const ProductList: NextPage = () => {
  let typingTimeOutRef: null | ReturnType<typeof setTimeout> = null;
  const [products, setProducts] = useState<Product[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [pageNo, setPageNo] = useState<number>(0);
  const [totalPage, setTotalPage] = useState<number>(0);
  const [brands, setBrands] = useState<Brand[]>([])
  const [brandName, setBrandName] = useState<string>("");
  const [productName, setProductName] = useState<string>("");
  const [showModalDelete, setShowModalDelete] = useState<boolean>(false);
  const [productNameWantToDelete, setProductNameWantToDelete] = useState<string>("");
  const [productIdWantToDelete, setProductIdWantToDelete] = useState<number>(-1);

  const handleClose = () => setShowModalDelete(false);
  const handleDelete = () => {
    if (productIdWantToDelete == -1) {
      return
    }
    deleteProduct(productIdWantToDelete)
      .then((response) => {
        setShowModalDelete(false);
        handleDeletingResponse(response, productNameWantToDelete);
        getProducts(pageNo, productName, brandName).then((data) => {
          setTotalPage(data.totalPages);
          setProducts(data.productContent);
          setIsLoading(false)
        })
      })
      .catch((error) => {
        console.log(error)
      })
  }

  useEffect(() => {
    setIsLoading(true);

    getProducts(pageNo, productName, brandName).then((data) => {
      setTotalPage(data.totalPages);
      setProducts(data.productContent);
      setIsLoading(false)
    })
  }, [pageNo, brandName, productName]);

  useEffect(() => {
    setIsLoading(true);
    getBrands().then((data) => {
      setBrands(data);
      setIsLoading(false)
    })
  }, [pageNo, brandName, productName])

  const searchingHandler = () => {
    if (typingTimeOutRef) {
      clearTimeout(typingTimeOutRef)
    }
    typingTimeOutRef = setTimeout(() => {
      let inputValue = (document.getElementById("product-name") as HTMLInputElement).value;
      setProductName(inputValue);
      setPageNo(0)
    }, 500)
  }

  const changePage = ({selected}: any) => {
    setPageNo(selected)
  }

  if (isLoading) return <p>Loading...</p>
  if (!products) return <p>No product</p>

  return (
    <>
      <div className="row mt-5">
        <div className="col-md-8">
          <h2 className="text-danger font-weight-bold mb-3">Products</h2>
        </div>
        <div className="col-md-4 text-right">
          <Link href="/catalog/products/create">
            <Button>Create Product</Button>
          </Link>
        </div>
        <br />
      </div>

      <div className="row mb-5">
        <div className="col-md-6">
          <Form.Select
            id="brand-filter"
            onChange={(e) => {
              setPageNo(0);
              setBrandName(e.target.value)
            }}
            className={styles.filterButton}
            defaultValue={brandName || ""}
          >
            <option value={''}>All</option>
            {brands.map((brand) => (
              <option key={brand.id} value={brand.name}>
                {brand.name}
              </option>
            ))}
          </Form.Select>
        </div>

        <div className="col-md-4">
          <Form>
            <InputGroup>
              <Form.Control
                id="product-name"
                placeholder="Search name ..."
                defaultValue={productName}
                onChange={searchingHandler}
              />
              <Button id="seach-category" variant="danger" onClick={searchingHandler}>
                <FaSearch/>
              </Button>
            </InputGroup>
          </Form>
        </div>
        <div>
          <ExportProduct productName={productName} brandName={brandName} />
        </div>
      </div>

      <Table striped bordered hover>
        <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Is Featured</th>
          <th>Is Allowed To Order</th>
          <th>Is Published</th>
          <th>Created Date</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        {products.map((product) => (
          <tr key={product.id}>
            <td>{product.id}</td>
            <td>{product.name}</td>
            <td>{product.isFeatured ? 'Yes' : 'No'}</td>
            <td>{product.isAllowedToOrder ? 'Yes' : 'No'}</td>
            <td>{product.isPublished ? 'Yes' : 'No'}</td>
            <td>
              {product.createdOn != null ? moment(product.createdOn).format('DD/MM/YYYY') : ''}
            </td>
            <td>
              <Stack direction="horizontal" gap={3}>
                <Link href={`/catalog/products/${product.id}/edit`}>
                  <button className="btn btn-outline-primary btn-sm" type="button">
                    Edit
                  </button>
                </Link>
                &nbsp;
                <button
                  className="btn btn-outline-danger btn-sm"
                  type="button"
                  onClick={() => {
                    setShowModalDelete(true);
                    setProductIdWantToDelete(product.id);
                    setProductNameWantToDelete(product.name);
                  }}
                >
                  Delete
                </button>
              </Stack>
            </td>
          </tr>
        ))}
        </tbody>
      </Table>
      <ModalDeleteCustom
        showModalDelete={showModalDelete}
        handleClose={handleClose}
        nameWantToDelete={productNameWantToDelete}
        handleDelete={handleDelete}
        action="delete"
      />
      {totalPage > 1 && (
        <ReactPaginate
          forcePage={pageNo}
          previousLabel={'Previous'}
          nextLabel={'Next'}
          pageCount={totalPage}
          onPageChange={changePage}
          containerClassName={'pagination-container'}
          previousClassName={'previous-btn'}
          nextClassName={'next-btn'}
          disabledClassName={'pagination-disabled'}
          activeClassName={'pagination-active'}
        />
      )}
    </>
  )
}

export default ProductList