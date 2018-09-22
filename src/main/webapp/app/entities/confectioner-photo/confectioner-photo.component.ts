import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IConfectionerPhoto } from 'app/shared/model/confectioner-photo.model';
import { Principal } from 'app/core';
import { ConfectionerPhotoService } from './confectioner-photo.service';

@Component({
  selector: 'jhi-confectioner-photo',
  templateUrl: './confectioner-photo.component.html'
})
export class ConfectionerPhotoComponent implements OnInit, OnDestroy {
  confectionerPhotos: IConfectionerPhoto[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    private confectionerPhotoService: ConfectionerPhotoService,
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
      this.confectionerPhotoService
        .search({
          query: this.currentSearch
        })
        .subscribe(
          (res: HttpResponse<IConfectionerPhoto[]>) =>
            (this.confectionerPhotos = res.body),
          (res: HttpErrorResponse) => this.onError(res.message)
        );
      return;
    }
    this.confectionerPhotoService.query().subscribe(
      (res: HttpResponse<IConfectionerPhoto[]>) => {
        this.confectionerPhotos = res.body;
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
    this.registerChangeInConfectionerPhotos();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IConfectionerPhoto) {
    return item.id;
  }

  registerChangeInConfectionerPhotos() {
    this.eventSubscriber = this.eventManager.subscribe(
      'confectionerPhotoListModification',
      response => this.loadAll()
    );
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
