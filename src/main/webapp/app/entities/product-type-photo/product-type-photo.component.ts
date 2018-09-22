import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IProductTypePhoto } from 'app/shared/model/product-type-photo.model';
import { Principal } from 'app/core';
import { ProductTypePhotoService } from './product-type-photo.service';

@Component({
  selector: 'jhi-product-type-photo',
  templateUrl: './product-type-photo.component.html'
})
export class ProductTypePhotoComponent implements OnInit, OnDestroy {
  productTypePhotos: IProductTypePhoto[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    private productTypePhotoService: ProductTypePhotoService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private activatedRoute: ActivatedRoute,
    private principal: Principal
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot &&
      this.activatedRoute.snapshot.params['search']
        ? this.activatedRoute.snapshot.params['search']
        : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.productTypePhotoService
        .search({
          query: this.currentSearch
        })
        .subscribe(
          (res: HttpResponse<IProductTypePhoto[]>) =>
            (this.productTypePhotos = res.body),
          (res: HttpErrorResponse) => this.onError(res.message)
        );
      return;
    }
    this.productTypePhotoService.query().subscribe(
      (res: HttpResponse<IProductTypePhoto[]>) => {
        this.productTypePhotos = res.body;
        this.currentSearch = '';
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.loadAll();
  }

  clear() {
    this.currentSearch = '';
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.principal.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInProductTypePhotos();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IProductTypePhoto) {
    return item.id;
  }

  registerChangeInProductTypePhotos() {
    this.eventSubscriber = this.eventManager.subscribe(
      'productTypePhotoListModification',
      response => this.loadAll()
    );
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
