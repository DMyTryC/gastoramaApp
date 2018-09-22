import { Moment } from 'moment';
import { IProductPhoto } from 'app/shared/model//product-photo.model';
import { IConfectioner } from 'app/shared/model//confectioner.model';
import { IProductType } from 'app/shared/model//product-type.model';
import { ICommandLine } from 'app/shared/model//command-line.model';
import { IIngredient } from 'app/shared/model//ingredient.model';
import * as moment from 'moment';

export interface IProduct {
  id?: number;
  name?: string;
  price?: number;
  weight?: number;
  pieces?: number;
  passDate?: Moment;
  stock?: number;
  photos?: IProductPhoto[];
  confectioner?: IConfectioner;
  productType?: IProductType;
  commandLines?: ICommandLine[];
  ingredients?: IIngredient[];
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string,
    public price?: number,
    public weight?: number,
    public pieces?: number,
    public passDate?: Moment,
    public stock?: number,
    public photos?: IProductPhoto[],
    public confectioner?: IConfectioner,
    public productType?: IProductType,
    public commandLines?: ICommandLine[],
    public ingredients?: IIngredient[]
  ) {}
}

export interface CartProduct extends IProduct {
  quantity: number;
  isInCart: boolean;
}

export class CartProdImpl implements CartProduct {
    commandLines: ICommandLine[];
    confectioner: IConfectioner;
    id: number;
    ingredients: IIngredient[];
    isInCart: boolean;
    name: string;
    passDate: moment.Moment;
    photos: IProductPhoto[];
    pieces: number;
    price: number;
    productType: IProductType;
    quantity: number;
    stock: number;
    weight: number;

}
