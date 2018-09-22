import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IProductType } from 'app/shared/model/product-type.model';
import { Principal } from 'app/core';
import { ProductTypeService } from './product-type.service';

@Component({
  selector: 'jhi-product-type',
  templateUrl: './product-type.component.html'
})
export class ProductTypeComponent implements OnInit, OnDestroy {
  productTypes: IProductType[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    private productTypeService: ProductTypeService,
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
      this.productTypeService
        .search({
          query: this.currentSearch
        })
        .subscribe(
          (res: HttpResponse<IProductType[]>) => (this.productTypes = res.body),
          (res: HttpErrorResponse) => this.onError(res.message)
        );
      return;
    }
    this.productTypeService.query().subscribe(
      (res: HttpResponse<IProductType[]>) => {
        this.productTypes = res.body;
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
    this.registerChangeInProductTypes();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IProductType) {
    return item.id;
  }

  registerChangeInProductTypes() {
    this.eventSubscriber = this.eventManager.subscribe(
      'productTypeListModification',
      response => this.loadAll()
    );
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
