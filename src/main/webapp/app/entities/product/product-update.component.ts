import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from './product.service';
import { IConfectioner } from 'app/shared/model/confectioner.model';
import { ConfectionerService } from 'app/entities/confectioner';
import { IProductType } from 'app/shared/model/product-type.model';
import { ProductTypeService } from 'app/entities/product-type';
import { IIngredient } from 'app/shared/model/ingredient.model';
import { IngredientService } from 'app/entities/ingredient';

@Component({
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html'
})
export class ProductUpdateComponent implements OnInit {
  private _product: IProduct;
  isSaving: boolean;

  confectioners: IConfectioner[];

  producttypes: IProductType[];

  ingredients: IIngredient[];
  passDate: string;

  constructor(
    private jhiAlertService: JhiAlertService,
    private productService: ProductService,
    private confectionerService: ConfectionerService,
    private productTypeService: ProductTypeService,
    private ingredientService: IngredientService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
    });
    this.confectionerService.query().subscribe(
      (res: HttpResponse<IConfectioner[]>) => {
        this.confectioners = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.productTypeService.query().subscribe(
      (res: HttpResponse<IProductType[]>) => {
        this.producttypes = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.ingredientService.query().subscribe(
      (res: HttpResponse<IIngredient[]>) => {
        this.ingredients = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    this.product.passDate = moment(this.passDate, DATE_TIME_FORMAT);
    if (this.product.id !== undefined) {
      this.subscribeToSaveResponse(this.productService.update(this.product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(this.product));
    }
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>) {
    result.subscribe(
      (res: HttpResponse<IProduct>) => this.onSaveSuccess(),
      (res: HttpErrorResponse) => this.onSaveError()
    );
  }

  private onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError() {
    this.isSaving = false;
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackConfectionerById(index: number, item: IConfectioner) {
    return item.id;
  }

  trackProductTypeById(index: number, item: IProductType) {
    return item.id;
  }

  trackIngredientById(index: number, item: IIngredient) {
    return item.id;
  }

  getSelected(selectedVals: Array<any>, option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
  get product() {
    return this._product;
  }

  set product(product: IProduct) {
    this._product = product;
    this.passDate = moment(product.passDate).format(DATE_TIME_FORMAT);
  }
}
