import Banner from "@/modules/home/components/Banner";
import Category from "@/modules/home/components/Category";
import FeaturedProduct from "@/modules/home/components/FeaturedProduct";

export default function Home() {
  return (
    <div className="homepage-container">
      <Banner />

      <Category />

      <FeaturedProduct />
    </div>
  );
}
