import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProductPhoto } from 'app/shared/model/product-photo.model';

type EntityResponseType = HttpResponse<IProductPhoto>;
type EntityArrayResponseType = HttpResponse<IProductPhoto[]>;

@Injectable({ providedIn: 'root' })
export class ProductPhotoService {
  private resourceUrl = SERVER_API_URL + 'api/product-photos';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/product-photos';

  constructor(private http: HttpClient) {}

  create(productPhoto: IProductPhoto): Observable<EntityResponseType> {
    return this.http.post<IProductPhoto>(this.resourceUrl, productPhoto, {
      observe: 'response'
    });
  }

  update(productPhoto: IProductPhoto): Observable<EntityResponseType> {
    return this.http.put<IProductPhoto>(this.resourceUrl, productPhoto, {
      observe: 'response'
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductPhoto>(`${this.resourceUrl}/${id}`, {
      observe: 'response'
    });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductPhoto[]>(this.resourceUrl, {
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
    return this.http.get<IProductPhoto[]>(this.resourceSearchUrl, {
      params: options,
      observe: 'response'
    });
  }
}
