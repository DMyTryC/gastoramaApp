import { IProduct } from 'app/shared/model//product.model';
import { ICommand } from 'app/shared/model//command.model';

export interface ICommandLine {
  id?: number;
  quantity?: number;
  product?: IProduct;
  command?: ICommand;
}

export class CommandLine implements ICommandLine {
  constructor(
    public id?: number,
    public quantity?: number,
    public product?: IProduct,
    public command?: ICommand
  ) {}
}
