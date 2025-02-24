import React, {useEffect, useState} from "react";
import {Image} from "react-bootstrap";
import clsx from "clsx";

type Props = {
  src: string;
  alt: string;
  width?: number | string;
  height?: number | string;
  className?: string;
  fallbackSrc?: string;
  style?: React.CSSProperties;
}

const ImageWithFallback = ({
  width = 500,
  height = 500,
  src,
  alt,
  className,
  style,
  fallbackSrc: customFallback = "/static/images/default-fallback-image.png",
  ...props
}: Props) => {
  const [fallback, setFallback] = useState<string | null>(null);
  const [srcUrl, setSrcUrl] = useState<string>(src);

  useEffect(() => {
    setSrcUrl(src)
    setFallback(null)
  }, [src]);

  return (
    <Image width={width} height={height}  style={style} className={clsx(className)}
      src={fallback || srcUrl} alt={alt} {...props}
           onError={(event) => {
             event.currentTarget.onerror = null;
             setFallback(customFallback)
           }}/>
  )
}

export default ImageWithFallback;
