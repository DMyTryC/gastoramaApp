import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IConfectionerPhoto } from 'app/shared/model/confectioner-photo.model';
import { ConfectionerPhotoService } from './confectioner-photo.service';
import { IConfectioner } from 'app/shared/model/confectioner.model';
import { ConfectionerService } from 'app/entities/confectioner';

@Component({
  selector: 'jhi-confectioner-photo-update',
  templateUrl: './confectioner-photo-update.component.html'
})
export class ConfectionerPhotoUpdateComponent implements OnInit {
  private _confectionerPhoto: IConfectionerPhoto;
  isSaving: boolean;

  confectioners: IConfectioner[];

  constructor(
    private jhiAlertService: JhiAlertService,
    private confectionerPhotoService: ConfectionerPhotoService,
    private confectionerService: ConfectionerService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ confectionerPhoto }) => {
      this.confectionerPhoto = confectionerPhoto;
    });
    this.confectionerService.query().subscribe(
      (res: HttpResponse<IConfectioner[]>) => {
        this.confectioners = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    if (this.confectionerPhoto.id !== undefined) {
      this.subscribeToSaveResponse(
        this.confectionerPhotoService.update(this.confectionerPhoto)
      );
    } else {
      this.subscribeToSaveResponse(
        this.confectionerPhotoService.create(this.confectionerPhoto)
      );
    }
  }

  private subscribeToSaveResponse(
    result: Observable<HttpResponse<IConfectionerPhoto>>
  ) {
    result.subscribe(
      (res: HttpResponse<IConfectionerPhoto>) => this.onSaveSuccess(),
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

  trackConfectionerById(index: number, item: IConfectioner) {
    return item.id;
  }
  get confectionerPhoto() {
    return this._confectionerPhoto;
  }

  set confectionerPhoto(confectionerPhoto: IConfectionerPhoto) {
    this._confectionerPhoto = confectionerPhoto;
  }
}
