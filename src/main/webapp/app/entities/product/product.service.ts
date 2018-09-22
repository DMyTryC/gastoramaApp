import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import {CartProdImpl, CartProduct, IProduct} from 'app/shared/model/product.model';

type EntityResponseType = HttpResponse<IProduct>;
type EntityArrayResponseType = HttpResponse<IProduct[]>;

@Injectable({ providedIn: 'root' })
export class ProductService {
  private resourceUrl = SERVER_API_URL + 'api/products';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/products';

  constructor(private http: HttpClient) {}

  create(product: IProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(product);
    return this.http
      .post<IProduct>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(product: IProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(product);
    return this.http
      .put<IProduct>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProduct[]>(this.resourceUrl, {
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
      .get<IProduct[]>(this.resourceSearchUrl, {
        params: options,
        observe: 'response'
      })
      .pipe(
        map((res: EntityArrayResponseType) =>
          this.convertDateArrayFromServer(res)
        )
      );
  }

  private convertDateFromClient(product: IProduct): IProduct {
    const copy: IProduct = Object.assign({}, product, {
      passDate:
        product.passDate != null && product.passDate.isValid()
          ? product.passDate.toJSON()
          : null
    });
    return copy;
  }

  private convertDateFromServer(res: EntityResponseType): EntityResponseType {
    res.body.passDate =
      res.body.passDate != null ? moment(res.body.passDate) : null;
    return res;
  }

  convertDateArrayFromServer(
    res: EntityArrayResponseType
  ): EntityArrayResponseType {
    res.body.forEach((product: IProduct) => {
      product.passDate =
        product.passDate != null ? moment(product.passDate) : null;
    });
    return res;
  }

  iProductToCartProduct(iProduct: IProduct): CartProduct {

    const cartProd: CartProduct = new CartProdImpl();

    cartProd.name = iProduct.name;
    cartProd.id = iProduct.id;
    cartProd.commandLines = iProduct.commandLines;
    cartProd.confectioner = iProduct.confectioner;
    cartProd.ingredients = iProduct.ingredients;
    cartProd.passDate = iProduct.passDate;
    cartProd.photos = iProduct.photos;
    cartProd.pieces = iProduct.pieces;
    cartProd.price = iProduct.price;
    cartProd.productType = iProduct.productType;
    cartProd.stock = iProduct.stock;
    cartProd.weight = iProduct.weight;

    cartProd.quantity = 0;
    cartProd.isInCart = false;

    return cartProd;
  }
}
