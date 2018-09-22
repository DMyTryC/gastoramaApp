import { IProduct } from 'app/shared/model//product.model';

export interface IIngredient {
  id?: number;
  name?: string;
  allergen?: boolean;
  products?: IProduct[];
}

export class Ingredient implements IIngredient {
  constructor(
    public id?: number,
    public name?: string,
    public allergen?: boolean,
    public products?: IProduct[]
  ) {
    this.allergen = this.allergen || false;
  }
}
