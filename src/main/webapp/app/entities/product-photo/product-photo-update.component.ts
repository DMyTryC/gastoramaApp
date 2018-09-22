import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IProductPhoto } from 'app/shared/model/product-photo.model';
import { ProductPhotoService } from './product-photo.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';

@Component({
  selector: 'jhi-product-photo-update',
  templateUrl: './product-photo-update.component.html'
})
export class ProductPhotoUpdateComponent implements OnInit {
  private _productPhoto: IProductPhoto;
  isSaving: boolean;

  products: IProduct[];

  constructor(
    private jhiAlertService: JhiAlertService,
    private productPhotoService: ProductPhotoService,
    private productService: ProductService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ productPhoto }) => {
      this.productPhoto = productPhoto;
    });
    this.productService.query().subscribe(
      (res: HttpResponse<IProduct[]>) => {
        this.products = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    if (this.productPhoto.id !== undefined) {
      this.subscribeToSaveResponse(
        this.productPhotoService.update(this.productPhoto)
      );
    } else {
      this.subscribeToSaveResponse(
        this.productPhotoService.create(this.productPhoto)
      );
    }
  }

  private subscribeToSaveResponse(
    result: Observable<HttpResponse<IProductPhoto>>
  ) {
    result.subscribe(
      (res: HttpResponse<IProductPhoto>) => this.onSaveSuccess(),
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

  trackProductById(index: number, item: IProduct) {
    return item.id;
  }
  get productPhoto() {
    return this._productPhoto;
  }

  set productPhoto(productPhoto: IProductPhoto) {
    this._productPhoto = productPhoto;
  }
}
