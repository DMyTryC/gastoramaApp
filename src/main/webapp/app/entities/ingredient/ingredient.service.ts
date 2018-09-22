import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIngredient } from 'app/shared/model/ingredient.model';

type EntityResponseType = HttpResponse<IIngredient>;
type EntityArrayResponseType = HttpResponse<IIngredient[]>;

@Injectable({ providedIn: 'root' })
export class IngredientService {
  private resourceUrl = SERVER_API_URL + 'api/ingredients';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/ingredients';

  constructor(private http: HttpClient) {}

  create(ingredient: IIngredient): Observable<EntityResponseType> {
    return this.http.post<IIngredient>(this.resourceUrl, ingredient, {
      observe: 'response'
    });
  }

  update(ingredient: IIngredient): Observable<EntityResponseType> {
    return this.http.put<IIngredient>(this.resourceUrl, ingredient, {
      observe: 'response'
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIngredient>(`${this.resourceUrl}/${id}`, {
      observe: 'response'
    });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIngredient[]>(this.resourceUrl, {
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
    return this.http.get<IIngredient[]>(this.resourceSearchUrl, {
      params: options,
      observe: 'response'
    });
  }
}
