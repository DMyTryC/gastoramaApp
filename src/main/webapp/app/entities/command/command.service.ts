import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICommand } from 'app/shared/model/command.model';

type EntityResponseType = HttpResponse<ICommand>;
type EntityArrayResponseType = HttpResponse<ICommand[]>;

@Injectable({ providedIn: 'root' })
export class CommandService {
  private resourceUrl = SERVER_API_URL + 'api/commands';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/commands';

  constructor(private http: HttpClient) {}

  create(command: ICommand): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(command);
    return this.http
      .post<ICommand>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(command: ICommand): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(command);
    return this.http
      .put<ICommand>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICommand>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICommand[]>(this.resourceUrl, {
        params: options,
        observe: 'response'
      })
      .pipe(
        map((res: EntityArrayResponseType) =>
          this.convertDateArrayFromServer(res)
        )
      );
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, {
      observe: 'response'
    });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICommand[]>(this.resourceSearchUrl, {
        params: options,
        observe: 'response'
      })
      .pipe(
        map((res: EntityArrayResponseType) =>
          this.convertDateArrayFromServer(res)
        )
      );
  }

  private convertDateFromClient(command: ICommand): ICommand {
    const copy: ICommand = Object.assign({}, command, {
      date:
        command.date != null && command.date.isValid()
          ? command.date.toJSON()
          : null
    });
    return copy;
  }

  private convertDateFromServer(res: EntityResponseType): EntityResponseType {
    res.body.date = res.body.date != null ? moment(res.body.date) : null;
    return res;
  }

  private convertDateArrayFromServer(
    res: EntityArrayResponseType
  ): EntityArrayResponseType {
    res.body.forEach((command: ICommand) => {
      command.date = command.date != null ? moment(command.date) : null;
    });
    return res;
  }
}
