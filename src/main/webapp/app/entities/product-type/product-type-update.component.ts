import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IProductType } from 'app/shared/model/product-type.model';
import { ProductTypeService } from './product-type.service';

@Component({
  selector: 'jhi-product-type-update',
  templateUrl: './product-type-update.component.html'
})
export class ProductTypeUpdateComponent implements OnInit {
  private _productType: IProductType;
  isSaving: boolean;

  constructor(
    private productTypeService: ProductTypeService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ productType }) => {
      this.productType = productType;
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    if (this.productType.id !== undefined) {
      this.subscribeToSaveResponse(
        this.productTypeService.update(this.productType)
      );
    } else {
      this.subscribeToSaveResponse(
        this.productTypeService.create(this.productType)
      );
    }
  }

  private subscribeToSaveResponse(
    result: Observable<HttpResponse<IProductType>>
  ) {
    result.subscribe(
      (res: HttpResponse<IProductType>) => this.onSaveSuccess(),
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
  get productType() {
    return this._productType;
  }

  set productType(productType: IProductType) {
    this._productType = productType;
  }
}
