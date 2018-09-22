import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IConfectioner } from 'app/shared/model/confectioner.model';

type EntityResponseType = HttpResponse<IConfectioner>;
type EntityArrayResponseType = HttpResponse<IConfectioner[]>;

@Injectable({ providedIn: 'root' })
export class ConfectionerService {
  private resourceUrl = SERVER_API_URL + 'api/confectioners';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/confectioners';

  constructor(private http: HttpClient) {}

  create(confectioner: IConfectioner): Observable<EntityResponseType> {
    return this.http.post<IConfectioner>(this.resourceUrl, confectioner, {
      observe: 'response'
    });
  }

  update(confectioner: IConfectioner): Observable<EntityResponseType> {
    return this.http.put<IConfectioner>(this.resourceUrl, confectioner, {
      observe: 'response'
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConfectioner>(`${this.resourceUrl}/${id}`, {
      observe: 'response'
    });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConfectioner[]>(this.resourceUrl, {
      params: options,
      observe: 'response'
    });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, {
      observe: 'response'
    });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConfectioner[]>(this.resourceSearchUrl, {
      params: options,
      observe: 'response'
    });
  }
}
