import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProductTypePhoto } from 'app/shared/model/product-type-photo.model';

type EntityResponseType = HttpResponse<IProductTypePhoto>;
type EntityArrayResponseType = HttpResponse<IProductTypePhoto[]>;

@Injectable({ providedIn: 'root' })
export class ProductTypePhotoService {
  private resourceUrl = SERVER_API_URL + 'api/product-type-photos';
  private resourceSearchUrl = SERVER_API_URL +
    'api/_search/product-type-photos';

  constructor(private http: HttpClient) {}

  create(productTypePhoto: IProductTypePhoto): Observable<EntityResponseType> {
    return this.http.post<IProductTypePhoto>(
      this.resourceUrl,
      productTypePhoto,
      { observe: 'response' }
    );
  }

  update(productTypePhoto: IProductTypePhoto): Observable<EntityResponseType> {
    return this.http.put<IProductTypePhoto>(
      this.resourceUrl,
      productTypePhoto,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductTypePhoto>(`${this.resourceUrl}/${id}`, {
      observe: 'response'
    });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductTypePhoto[]>(this.resourceUrl, {
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
    return this.http.get<IProductTypePhoto[]>(this.resourceSearchUrl, {
      params: options,
      observe: 'response'
    });
  }
}
