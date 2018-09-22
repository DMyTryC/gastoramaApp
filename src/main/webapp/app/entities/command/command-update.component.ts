import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { ICommand } from 'app/shared/model/command.model';
import { CommandService } from './command.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-command-update',
  templateUrl: './command-update.component.html'
})
export class CommandUpdateComponent implements OnInit {
  private _command: ICommand;
  isSaving: boolean;

  users: IUser[];
  date: string;

  constructor(
    private jhiAlertService: JhiAlertService,
    private commandService: CommandService,
    private userService: UserService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ command }) => {
      this.command = command;
    });
    this.userService.query().subscribe(
      (res: HttpResponse<IUser[]>) => {
        this.users = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    this.command.date = moment(this.date, DATE_TIME_FORMAT);
    if (this.command.id !== undefined) {
      this.subscribeToSaveResponse(this.commandService.update(this.command));
    } else {
      this.subscribeToSaveResponse(this.commandService.create(this.command));
    }
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<ICommand>>) {
    result.subscribe(
      (res: HttpResponse<ICommand>) => this.onSaveSuccess(),
      (res: HttpErrorResponse) => this.onSaveError()
    );
  }

  private onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError() {
    this.isSaving = false;
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
  get command() {
    return this._command;
  }

  set command(command: ICommand) {
    this._command = command;
    this.date = moment(command.date).format(DATE_TIME_FORMAT);
  }
}
