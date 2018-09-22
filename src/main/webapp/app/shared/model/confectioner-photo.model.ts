import { IConfectioner } from 'app/shared/model//confectioner.model';

export interface IConfectionerPhoto {
  id?: number;
  url?: string;
  confectioner?: IConfectioner;
}

export class ConfectionerPhoto implements IConfectionerPhoto {
  constructor(
    public id?: number,
    public url?: string,
    public confectioner?: IConfectioner
  ) {}
}
