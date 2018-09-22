import { IProduct } from 'app/shared/model//product.model';
import { IConfectionerPhoto } from 'app/shared/model//confectioner-photo.model';
import { IUser } from 'app/core/user/user.model';

export interface IConfectioner {
  id?: number;
  address?: string;
  description?: string;
  products?: IProduct[];
  photos?: IConfectionerPhoto[];
  userId?: IUser;
}

export class Confectioner implements IConfectioner {
  constructor(
    public id?: number,
    public address?: string,
    public description?: string,
    public products?: IProduct[],
    public photos?: IConfectionerPhoto[],
    public userId?: IUser
  ) {}
}
