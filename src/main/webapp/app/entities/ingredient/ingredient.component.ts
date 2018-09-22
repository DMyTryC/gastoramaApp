import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IIngredient } from 'app/shared/model/ingredient.model';
import { Principal } from 'app/core';
import { IngredientService } from './ingredient.service';

@Component({
  selector: 'jhi-ingredient',
  templateUrl: './ingredient.component.html'
})
export class IngredientComponent implements OnInit, OnDestroy {
  ingredients: IIngredient[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    private ingredientService: IngredientService,
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
      this.ingredientService
        .search({
          query: this.currentSearch
        })
        .subscribe(
          (res: HttpResponse<IIngredient[]>) => (this.ingredients = res.body),
          (res: HttpErrorResponse) => this.onError(res.message)
        );
      return;
    }
    this.ingredientService.query().subscribe(
      (res: HttpResponse<IIngredient[]>) => {
        this.ingredients = res.body;
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
    this.registerChangeInIngredients();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IIngredient) {
    return item.id;
  }

  registerChangeInIngredients() {
    this.eventSubscriber = this.eventManager.subscribe(
      'ingredientListModification',
      response => this.loadAll()
    );
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
