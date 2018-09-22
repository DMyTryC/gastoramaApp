/* tslint:disable max-line-length */
import {
  ComponentFixture,
  TestBed,
  inject,
  fakeAsync,
  tick
} from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { GastoramaTestModule } from '../../../test.module';
import { IngredientDeleteDialogComponent } from 'app/entities/ingredient/ingredient-delete-dialog.component';
import { IngredientService } from 'app/entities/ingredient/ingredient.service';

describe('Component Tests', () => {
  describe('Ingredient Management Delete Component', () => {
    let comp: IngredientDeleteDialogComponent;
    let fixture: ComponentFixture<IngredientDeleteDialogComponent>;
    let service: IngredientService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [IngredientDeleteDialogComponent]
      })
        .overrideTemplate(IngredientDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IngredientDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IngredientService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it(
        'Should call delete service on confirmDelete',
        inject(
          [],
          fakeAsync(() => {
            // GIVEN
            spyOn(service, 'delete').and.returnValue(of({}));

            // WHEN
            comp.confirmDelete(123);
            tick();

            // THEN
            expect(service.delete).toHaveBeenCalledWith(123);
            expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
            expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
          })
        )
      );
    });
  });
});
