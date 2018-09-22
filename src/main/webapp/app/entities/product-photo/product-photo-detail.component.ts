import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductPhoto } from 'app/shared/model/product-photo.model';

@Component({
  selector: 'jhi-product-photo-detail',
  templateUrl: './product-photo-detail.component.html'
})
export class ProductPhotoDetailComponent implements OnInit {
  productPhoto: IProductPhoto;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ productPhoto }) => {
      this.productPhoto = productPhoto;
    });
  }

  previousState() {
    window.history.back();
  }
}
