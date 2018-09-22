import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICommandLine } from 'app/shared/model/command-line.model';
import { Principal } from 'app/core';
import { CommandLineService } from './command-line.service';

@Component({
  selector: 'jhi-command-line',
  templateUrl: './command-line.component.html'
})
export class CommandLineComponent implements OnInit, OnDestroy {
  commandLines: ICommandLine[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    private commandLineService: CommandLineService,
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
      this.commandLineService
        .search({
          query: this.currentSearch
        })
        .subscribe(
          (res: HttpResponse<ICommandLine[]>) => (this.commandLines = res.body),
          (res: HttpErrorResponse) => this.onError(res.message)
        );
      return;
    }
    this.commandLineService.query().subscribe(
      (res: HttpResponse<ICommandLine[]>) => {
        this.commandLines = res.body;
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
    this.registerChangeInCommandLines();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICommandLine) {
    return item.id;
  }

  registerChangeInCommandLines() {
    this.eventSubscriber = this.eventManager.subscribe(
      'commandLineListModification',
      response => this.loadAll()
    );
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
