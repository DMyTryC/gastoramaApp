import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConfectioner } from 'app/shared/model/confectioner.model';

@Component({
  selector: 'jhi-confectioner-detail',
  templateUrl: './confectioner-detail.component.html'
})
export class ConfectionerDetailComponent implements OnInit {
  confectioner: IConfectioner;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ confectioner }) => {
      this.confectioner = confectioner;
    });
  }

  previousState() {
    window.history.back();
  }
}
