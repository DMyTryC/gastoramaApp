import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IProductTypePhoto } from 'app/shared/model/product-type-photo.model';
import { ProductTypePhotoService } from './product-type-photo.service';
import { IProductType } from 'app/shared/model/product-type.model';
import { ProductTypeService } from 'app/entities/product-type';

@Component({
  selector: 'jhi-product-type-photo-update',
  templateUrl: './product-type-photo-update.component.html'
})
export class ProductTypePhotoUpdateComponent implements OnInit {
  private _productTypePhoto: IProductTypePhoto;
  isSaving: boolean;

  producttypes: IProductType[];

  constructor(
    private jhiAlertService: JhiAlertService,
    private productTypePhotoService: ProductTypePhotoService,
    private productTypeService: ProductTypeService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ productTypePhoto }) => {
      this.productTypePhoto = productTypePhoto;
    });
    this.productTypeService.query().subscribe(
      (res: HttpResponse<IProductType[]>) => {
        this.producttypes = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    if (this.productTypePhoto.id !== undefined) {
      this.subscribeToSaveResponse(
        this.productTypePhotoService.update(this.productTypePhoto)
      );
    } else {
      this.subscribeToSaveResponse(
        this.productTypePhotoService.create(this.productTypePhoto)
      );
    }
  }

  private subscribeToSaveResponse(
    result: Observable<HttpResponse<IProductTypePhoto>>
  ) {
    result.subscribe(
      (res: HttpResponse<IProductTypePhoto>) => this.onSaveSuccess(),
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

  trackProductTypeById(index: number, item: IProductType) {
    return item.id;
  }
  get productTypePhoto() {
    return this._productTypePhoto;
  }

  set productTypePhoto(productTypePhoto: IProductTypePhoto) {
    this._productTypePhoto = productTypePhoto;
  }
}
