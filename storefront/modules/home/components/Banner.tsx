import {Carousel, Container} from "react-bootstrap";
import ImageWithFallback from "@/common/components/ImageWithFallback";
import mainBanner1 from "../../../asset/images/main-banner-1.jpg"
import mainBanner2 from "../../../asset/images/main-banner-2.jpg"
import mainBanner3 from "../../../asset/images/main-banner-3.jpg"
import subBanner from "../../../asset/images/sub-banner.jpg"
import Link from "next/link";

const listMainBanner = [mainBanner1.src, mainBanner2.src, mainBanner3.src]


const Banner = () => {
  return (
    <Container className="home-banner-container">
      <div className="home-banner-wrapper">
        <div className="main-banner">
          <Carousel>
            {listMainBanner.map((banner, index) => (
              <Carousel.Item key={index}>
                <Link href="/products">
                  <ImageWithFallback  className="d-block w-100" src={banner} alt={`Banner ${index + 1}`}/>
                </Link>
              </Carousel.Item>
            ))}
          </Carousel>
          <div>
          <Link href="/products">
            <ImageWithFallback className="sub-banner" src={subBanner.src} alt={`sub-banner`} />
          </Link>
          </div>
        </div>
      </div>
    </Container>
  )
}

export default Banner;
