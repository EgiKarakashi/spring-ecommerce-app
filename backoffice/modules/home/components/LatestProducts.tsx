import {useEffect, useState} from "react";
import {Product} from "@/modules/catalog/models/Product";
import {getLatestProducts} from "@/modules/catalog/services/ProductService";
import {Table} from "react-bootstrap";
import moment from "moment";
import Link from "next/link";

const LatestProducts = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    (async () => {
      try {
        const res = await getLatestProducts(10);
        setProducts(res);
      } catch (e) {
        console.log(e)
        setProducts([])
      } finally {
        setLoading(false)
      }
    })()
  }, [])

  let tableContent;

  if (loading) {
    tableContent = (
      <tr>
        <td colSpan={5}>Loading...</td>
      </tr>
    )
  } else if (!products || products.length === 0) {
    tableContent = (
      <tr>
        <td colSpan={5}>No product available</td>
      </tr>
    )
  } else {
    tableContent = products.map((product) => (
      <tr key={product.id}>
        <td className="id-column">{product.id}</td>
        <td className="identify-column">{product.name}</td>
        <td>{product.slug}</td>
        <td className="created-on-column">
          {product.createdOn && moment(product.createdOn).format('MMMM Do YYYY, h:mm:ss a')}
        </td>
        <td className="details-column">
          <Link href={`/catalog/products/${product.parentId ?? product.id}/edit`}>
            <button className="btn btn-outline-primary btn-sm" type="button">
              Details
            </button>
          </Link>
        </td>
      </tr>
    ))
  }

  return (
    <>
      <h2 className="text-danger font-weight-bold mb-3">List of the 10 latest Products</h2>
      <Table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Slug</th>
          <th>Created On</th>
          <th>Action</th>
        </tr>
        </thead>
        <tbody>{tableContent}</tbody>
      </Table>
    </>
  )
}

export default LatestProducts;
