import { IProduct } from 'app/shared/model//product.model';

export interface IProductPhoto {
  id?: number;
  url?: string;
  product?: IProduct;
}

export class ProductPhoto implements IProductPhoto {
  constructor(
    public id?: number,
    public url?: string,
    public product?: IProduct
  ) {}
}
