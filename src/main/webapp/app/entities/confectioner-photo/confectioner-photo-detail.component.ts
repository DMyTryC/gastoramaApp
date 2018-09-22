import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConfectionerPhoto } from 'app/shared/model/confectioner-photo.model';

@Component({
  selector: 'jhi-confectioner-photo-detail',
  templateUrl: './confectioner-photo-detail.component.html'
})
export class ConfectionerPhotoDetailComponent implements OnInit {
  confectionerPhoto: IConfectionerPhoto;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ confectionerPhoto }) => {
      this.confectionerPhoto = confectionerPhoto;
    });
  }

  previousState() {
    window.history.back();
  }
}
