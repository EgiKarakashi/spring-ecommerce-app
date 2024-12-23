import {Product} from "@/modules/catalog/models/Product";
import {useRouter} from "next/router";
import {useEffect, useState} from "react";
import {getProducts} from "@/modules/catalog/services/ProductService";
import Modal from "react-bootstrap/Modal";
import {Button, Table} from "react-bootstrap";
import ReactPaginate from "react-paginate";

type Props = {
  show: boolean;
  onHide: () => void;
  label: string;
  onSelected: (product: Product) => void;
  selectedProduct: Product[];
};

const ShowProductModel = (props: Props) => {
  const router = useRouter();
  const { id } = router.query;


  const [selectedProduct, setSelectedProduct] = useState<Product[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [pageNo, setPageNo] = useState<number>(0);
  const [totalPage, setTotalPage] = useState<number>(0);

  useEffect(() => {
    getProducts(pageNo, '', '').then((data) => {
      if (id) {
        let filterProduct = data.productContent.filter((product) => product.id !== +id);
        setProducts(filterProduct);
      } else {
        setProducts(data.productContent);
      }
      setTotalPage(data.totalPages);
    });
  }, [pageNo, id]);

  useEffect(() => {
    setSelectedProduct(props.selectedProduct);
  }, [props.selectedProduct]);

  const changePage = ({ selected }: any) => {
    setPageNo(selected);
  };

  return (
    <Modal show={props.show} size="lg" aria-labelledby="contained-modal-title-vcenter" centered>
      <Modal.Header>
        <Modal.Title id="contained-modal-title-vcenter">{props.label}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Table striped bordered hover>
          <thead>
          <tr>
            <th style={{ width: '100px' }}>Select</th>
            <th>Product Name</th>
          </tr>
          </thead>
          <tbody>
          {(products || []).map((product) => (
            <tr key={product.id}>
              <td>
                <input
                  type="checkbox"
                  id={product.slug}
                  className="form-check-input"
                  style={{ cursor: 'pointer' }}
                  onClick={() => props.onSelected(product)}
                  checked={selectedProduct.some((_product) => _product.id === product.id)}
                />
              </td>
              <td>
                <label
                  className="form-check-label"
                  htmlFor={product.slug}
                  style={{ cursor: 'pointer' }}
                >
                  {product.name}
                </label>
              </td>
            </tr>
          ))}
          </tbody>
        </Table>
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
      </Modal.Body>
      <Modal.Footer>
        <Button onClick={props.onHide}>Close</Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ShowProductModel;
