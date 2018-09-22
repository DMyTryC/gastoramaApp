import { IProductType } from 'app/shared/model//product-type.model';

export interface IProductTypePhoto {
  id?: number;
  url?: string;
  productType?: IProductType;
}

export class ProductTypePhoto implements IProductTypePhoto {
  constructor(
    public id?: number,
    public url?: string,
    public productType?: IProductType
  ) {}
}
