import { IProduct } from 'app/shared/model//product.model';
import { IProductTypePhoto } from 'app/shared/model//product-type-photo.model';

export interface IProductType {
  id?: number;
  name?: string;
  products?: IProduct[];
  photos?: IProductTypePhoto[];
}

export class ProductType implements IProductType {
  constructor(
    public id?: number,
    public name?: string,
    public products?: IProduct[],
    public photos?: IProductTypePhoto[]
  ) {}
}
