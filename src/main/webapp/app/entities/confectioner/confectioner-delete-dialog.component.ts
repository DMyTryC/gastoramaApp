import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import {
  NgbActiveModal,
  NgbModal,
  NgbModalRef
} from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IConfectioner } from 'app/shared/model/confectioner.model';
import { ConfectionerService } from './confectioner.service';

@Component({
  selector: 'jhi-confectioner-delete-dialog',
  templateUrl: './confectioner-delete-dialog.component.html'
})
export class ConfectionerDeleteDialogComponent {
  confectioner: IConfectioner;

  constructor(
    private confectionerService: ConfectionerService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.confectionerService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'confectionerListModification',
        content: 'Deleted an confectioner'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-confectioner-delete-popup',
  template: ''
})
export class ConfectionerDeletePopupComponent implements OnInit, OnDestroy {
  private ngbModalRef: NgbModalRef;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal
  ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ confectioner }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(
          ConfectionerDeleteDialogComponent as Component,
          { size: 'lg', backdrop: 'static' }
        );
        this.ngbModalRef.componentInstance.confectioner = confectioner;
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
