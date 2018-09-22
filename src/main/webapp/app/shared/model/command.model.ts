import { Moment } from 'moment';
import { ICommandLine } from 'app/shared/model//command-line.model';
import { IUser } from 'app/core/user/user.model';

export const enum DeliveryMode {
  DOMICILE = 'DOMICILE',
  PATISSERIE = 'PATISSERIE'
}

export const enum State {
  VALIDATED = 'VALIDATED',
  READY_TO_GO = 'READY_TO_GO',
  SEND = 'SEND',
  DONE = 'DONE'
}

export interface ICommand {
  id?: number;
  sumprice?: number;
  deliveryAddress?: string;
  date?: Moment;
  delivery?: DeliveryMode;
  state?: State;
  commandLines?: ICommandLine[];
  userId?: IUser;
}

export class Command implements ICommand {
  constructor(
    public id?: number,
    public sumprice?: number,
    public deliveryAddress?: string,
    public date?: Moment,
    public delivery?: DeliveryMode,
    public state?: State,
    public commandLines?: ICommandLine[],
    public userId?: IUser
  ) {}
}
