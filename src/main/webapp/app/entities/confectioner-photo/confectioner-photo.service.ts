import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IConfectionerPhoto } from 'app/shared/model/confectioner-photo.model';

type EntityResponseType = HttpResponse<IConfectionerPhoto>;
type EntityArrayResponseType = HttpResponse<IConfectionerPhoto[]>;

@Injectable({ providedIn: 'root' })
export class ConfectionerPhotoService {
  private resourceUrl = SERVER_API_URL + 'api/confectioner-photos';
  private resourceSearchUrl = SERVER_API_URL +
    'api/_search/confectioner-photos';

  constructor(private http: HttpClient) {}

  create(
    confectionerPhoto: IConfectionerPhoto
  ): Observable<EntityResponseType> {
    return this.http.post<IConfectionerPhoto>(
      this.resourceUrl,
      confectionerPhoto,
      { observe: 'response' }
    );
  }

  update(
    confectionerPhoto: IConfectionerPhoto
  ): Observable<EntityResponseType> {
    return this.http.put<IConfectionerPhoto>(
      this.resourceUrl,
      confectionerPhoto,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConfectionerPhoto>(`${this.resourceUrl}/${id}`, {
      observe: 'response'
    });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConfectionerPhoto[]>(this.resourceUrl, {
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
    return this.http.get<IConfectionerPhoto[]>(this.resourceSearchUrl, {
      params: options,
      observe: 'response'
    });
  }
}
