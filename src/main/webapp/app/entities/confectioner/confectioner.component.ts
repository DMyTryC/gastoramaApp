import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IConfectioner } from 'app/shared/model/confectioner.model';
import { Principal } from 'app/core';
import { ConfectionerService } from './confectioner.service';

@Component({
  selector: 'jhi-confectioner',
  templateUrl: './confectioner.component.html'
})
export class ConfectionerComponent implements OnInit, OnDestroy {
  confectioners: IConfectioner[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    private confectionerService: ConfectionerService,
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
      this.confectionerService
        .search({
          query: this.currentSearch
        })
        .subscribe(
          (res: HttpResponse<IConfectioner[]>) =>
            (this.confectioners = res.body),
          (res: HttpErrorResponse) => this.onError(res.message)
        );
      return;
    }
    this.confectionerService.query().subscribe(
      (res: HttpResponse<IConfectioner[]>) => {
        this.confectioners = res.body;
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
    this.registerChangeInConfectioners();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IConfectioner) {
    return item.id;
  }

  registerChangeInConfectioners() {
    this.eventSubscriber = this.eventManager.subscribe(
      'confectionerListModification',
      response => this.loadAll()
    );
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
