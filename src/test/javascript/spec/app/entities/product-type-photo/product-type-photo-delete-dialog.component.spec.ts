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
import { ProductTypePhotoDeleteDialogComponent } from 'app/entities/product-type-photo/product-type-photo-delete-dialog.component';
import { ProductTypePhotoService } from 'app/entities/product-type-photo/product-type-photo.service';

describe('Component Tests', () => {
  describe('ProductTypePhoto Management Delete Component', () => {
    let comp: ProductTypePhotoDeleteDialogComponent;
    let fixture: ComponentFixture<ProductTypePhotoDeleteDialogComponent>;
    let service: ProductTypePhotoService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [ProductTypePhotoDeleteDialogComponent]
      })
        .overrideTemplate(ProductTypePhotoDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProductTypePhotoDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProductTypePhotoService);
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
