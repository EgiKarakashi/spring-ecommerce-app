import {useEffect, useState} from "react";
import {ProductThumbnail} from "@/modules/catalog/models/ProductThumbnail";
import {getFeaturedProducts} from "@/modules/catalog/services/ProductService";
import {Col, Container, Row} from "react-bootstrap";
import ReactPaginate from "react-paginate";
import ProductCard from "@/common/components/ProductCard";

const FeaturedProduct = () => {
  const [products, setProducts] = useState<ProductThumbnail[]>([]);
  const [pageNo, setPageNo] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    getFeaturedProducts(pageNo)
      .then((res) => {
        setProducts(res.productList);
        setTotalPages(res.totalPage)
      })
  }, [pageNo]);

  const changePage = ({selected}: any) => {
    setPageNo(selected);
  }

  return (
    <Container className="featured-product-container">
      <div className="title">Featured products</div>
      <Row xs={5} xxl={6}>
        {products.length > 0 &&
          products.map((product) => (
            <Col key={product.id}>
              <ProductCard product={product} />
            </Col>
          ))}
      </Row>
      {totalPages > 1 && (
        <ReactPaginate
          forcePage={pageNo}
          previousLabel={'Previous'}
          nextLabel={'Next'}
          pageCount={totalPages}
          onPageChange={changePage}
          containerClassName={'pagination-container'}
          previousClassName={'previous-btn'}
          nextClassName={'next-btn'}
          disabledClassName={'pagination-disabled'}
          activeClassName={'pagination-active'}
        />
      )}
    </Container>
  );
}

export  default FeaturedProduct
