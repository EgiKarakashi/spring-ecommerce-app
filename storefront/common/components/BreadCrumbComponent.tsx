import {BreadCrumbModel} from "@/modules/breadcrumb/model/BreadCrumbModel";
import {Breadcrumb} from "react-bootstrap";

type Props = {
  props: BreadCrumbModel[]
}

export default function BreadCrumbComponent({props}: Props) {
  return (
    <Breadcrumb className="pt-3">
      {props.map((page: BreadCrumbModel, index: number)=> (
        <Breadcrumb.Item href={page.url} key={index} active={index === props.length - 1}>
          {page.pageName}
        </Breadcrumb.Item>
      ))}
    </Breadcrumb>
  )
}
