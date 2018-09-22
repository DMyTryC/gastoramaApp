import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import {
  NgbActiveModal,
  NgbModal,
  NgbModalRef
} from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProductPhoto } from 'app/shared/model/product-photo.model';
import { ProductPhotoService } from './product-photo.service';

@Component({
  selector: 'jhi-product-photo-delete-dialog',
  templateUrl: './product-photo-delete-dialog.component.html'
})
export class ProductPhotoDeleteDialogComponent {
  productPhoto: IProductPhoto;

  constructor(
    private productPhotoService: ProductPhotoService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.productPhotoService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'productPhotoListModification',
        content: 'Deleted an productPhoto'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-product-photo-delete-popup',
  template: ''
})
export class ProductPhotoDeletePopupComponent implements OnInit, OnDestroy {
  private ngbModalRef: NgbModalRef;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal
  ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ productPhoto }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(
          ProductPhotoDeleteDialogComponent as Component,
          { size: 'lg', backdrop: 'static' }
        );
        this.ngbModalRef.componentInstance.productPhoto = productPhoto;
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
