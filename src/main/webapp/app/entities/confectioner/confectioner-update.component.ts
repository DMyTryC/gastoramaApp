import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IConfectioner } from 'app/shared/model/confectioner.model';
import { ConfectionerService } from './confectioner.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-confectioner-update',
  templateUrl: './confectioner-update.component.html'
})
export class ConfectionerUpdateComponent implements OnInit {
  private _confectioner: IConfectioner;
  isSaving: boolean;

  users: IUser[];

  constructor(
    private jhiAlertService: JhiAlertService,
    private confectionerService: ConfectionerService,
    private userService: UserService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ confectioner }) => {
      this.confectioner = confectioner;
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
    if (this.confectioner.id !== undefined) {
      this.subscribeToSaveResponse(
        this.confectionerService.update(this.confectioner)
      );
    } else {
      this.subscribeToSaveResponse(
        this.confectionerService.create(this.confectioner)
      );
    }
  }

  private subscribeToSaveResponse(
    result: Observable<HttpResponse<IConfectioner>>
  ) {
    result.subscribe(
      (res: HttpResponse<IConfectioner>) => this.onSaveSuccess(),
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
  get confectioner() {
    return this._confectioner;
  }

  set confectioner(confectioner: IConfectioner) {
    this._confectioner = confectioner;
  }
}
