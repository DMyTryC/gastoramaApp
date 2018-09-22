import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICommand } from 'app/shared/model/command.model';
import { Principal } from 'app/core';
import { CommandService } from './command.service';

@Component({
  selector: 'jhi-command',
  templateUrl: './command.component.html'
})
export class CommandComponent implements OnInit, OnDestroy {
  commands: ICommand[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    private commandService: CommandService,
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
      this.commandService
        .search({
          query: this.currentSearch
        })
        .subscribe(
          (res: HttpResponse<ICommand[]>) => (this.commands = res.body),
          (res: HttpErrorResponse) => this.onError(res.message)
        );
      return;
    }
    this.commandService.query().subscribe(
      (res: HttpResponse<ICommand[]>) => {
        this.commands = res.body;
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
    this.registerChangeInCommands();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICommand) {
    return item.id;
  }

  registerChangeInCommands() {
    this.eventSubscriber = this.eventManager.subscribe(
      'commandListModification',
      response => this.loadAll()
    );
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
