import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductTypePhoto } from 'app/shared/model/product-type-photo.model';

@Component({
  selector: 'jhi-product-type-photo-detail',
  templateUrl: './product-type-photo-detail.component.html'
})
export class ProductTypePhotoDetailComponent implements OnInit {
  productTypePhoto: IProductTypePhoto;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ productTypePhoto }) => {
      this.productTypePhoto = productTypePhoto;
    });
  }

  previousState() {
    window.history.back();
  }
}
