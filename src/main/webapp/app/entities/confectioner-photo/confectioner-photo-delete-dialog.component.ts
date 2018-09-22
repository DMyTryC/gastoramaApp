import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import {
  NgbActiveModal,
  NgbModal,
  NgbModalRef
} from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IConfectionerPhoto } from 'app/shared/model/confectioner-photo.model';
import { ConfectionerPhotoService } from './confectioner-photo.service';

@Component({
  selector: 'jhi-confectioner-photo-delete-dialog',
  templateUrl: './confectioner-photo-delete-dialog.component.html'
})
export class ConfectionerPhotoDeleteDialogComponent {
  confectionerPhoto: IConfectionerPhoto;

  constructor(
    private confectionerPhotoService: ConfectionerPhotoService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.confectionerPhotoService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'confectionerPhotoListModification',
        content: 'Deleted an confectionerPhoto'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-confectioner-photo-delete-popup',
  template: ''
})
export class ConfectionerPhotoDeletePopupComponent
  implements OnInit, OnDestroy {
  private ngbModalRef: NgbModalRef;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal
  ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ confectionerPhoto }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(
          ConfectionerPhotoDeleteDialogComponent as Component,
          { size: 'lg', backdrop: 'static' }
        );
        this.ngbModalRef.componentInstance.confectionerPhoto = confectionerPhoto;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate([{ outlets: { popup: null } }], {
              replaceUrl: true,
              queryParamsHandling: 'merge'
            });
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate([{ outlets: { popup: null } }], {
              replaceUrl: true,
              queryParamsHandling: 'merge'
            });
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
