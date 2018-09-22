import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import {
  NgbActiveModal,
  NgbModal,
  NgbModalRef
} from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIngredient } from 'app/shared/model/ingredient.model';
import { IngredientService } from './ingredient.service';

@Component({
  selector: 'jhi-ingredient-delete-dialog',
  templateUrl: './ingredient-delete-dialog.component.html'
})
export class IngredientDeleteDialogComponent {
  ingredient: IIngredient;

  constructor(
    private ingredientService: IngredientService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.ingredientService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'ingredientListModification',
        content: 'Deleted an ingredient'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-ingredient-delete-popup',
  template: ''
})
export class IngredientDeletePopupComponent implements OnInit, OnDestroy {
  private ngbModalRef: NgbModalRef;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal
  ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ ingredient }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(
          IngredientDeleteDialogComponent as Component,
          { size: 'lg', backdrop: 'static' }
        );
        this.ngbModalRef.componentInstance.ingredient = ingredient;
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
