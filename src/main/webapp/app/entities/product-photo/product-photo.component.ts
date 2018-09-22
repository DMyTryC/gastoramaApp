import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IProductPhoto } from 'app/shared/model/product-photo.model';
import { Principal } from 'app/core';
import { ProductPhotoService } from './product-photo.service';

@Component({
  selector: 'jhi-product-photo',
  templateUrl: './product-photo.component.html'
})
export class ProductPhotoComponent implements OnInit, OnDestroy {
  productPhotos: IProductPhoto[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    private productPhotoService: ProductPhotoService,
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
      this.productPhotoService
        .search({
          query: this.currentSearch
        })
        .subscribe(
          (res: HttpResponse<IProductPhoto[]>) =>
            (this.productPhotos = res.body),
          (res: HttpErrorResponse) => this.onError(res.message)
        );
      return;
    }
    this.productPhotoService.query().subscribe(
      (res: HttpResponse<IProductPhoto[]>) => {
        this.productPhotos = res.body;
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
    this.registerChangeInProductPhotos();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IProductPhoto) {
    return item.id;
  }

  registerChangeInProductPhotos() {
    this.eventSubscriber = this.eventManager.subscribe(
      'productPhotoListModification',
      response => this.loadAll()
    );
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
