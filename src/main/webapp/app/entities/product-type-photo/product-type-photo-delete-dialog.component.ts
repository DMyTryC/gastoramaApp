import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import {
  NgbActiveModal,
  NgbModal,
  NgbModalRef
} from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProductTypePhoto } from 'app/shared/model/product-type-photo.model';
import { ProductTypePhotoService } from './product-type-photo.service';

@Component({
  selector: 'jhi-product-type-photo-delete-dialog',
  templateUrl: './product-type-photo-delete-dialog.component.html'
})
export class ProductTypePhotoDeleteDialogComponent {
  productTypePhoto: IProductTypePhoto;

  constructor(
    private productTypePhotoService: ProductTypePhotoService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.productTypePhotoService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'productTypePhotoListModification',
        content: 'Deleted an productTypePhoto'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-product-type-photo-delete-popup',
  template: ''
})
export class ProductTypePhotoDeletePopupComponent implements OnInit, OnDestroy {
  private ngbModalRef: NgbModalRef;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal
  ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ productTypePhoto }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(
          ProductTypePhotoDeleteDialogComponent as Component,
          { size: 'lg', backdrop: 'static' }
        );
        this.ngbModalRef.componentInstance.productTypePhoto = productTypePhoto;
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
