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
import { ConfectionerPhotoDeleteDialogComponent } from 'app/entities/confectioner-photo/confectioner-photo-delete-dialog.component';
import { ConfectionerPhotoService } from 'app/entities/confectioner-photo/confectioner-photo.service';

describe('Component Tests', () => {
  describe('ConfectionerPhoto Management Delete Component', () => {
    let comp: ConfectionerPhotoDeleteDialogComponent;
    let fixture: ComponentFixture<ConfectionerPhotoDeleteDialogComponent>;
    let service: ConfectionerPhotoService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [ConfectionerPhotoDeleteDialogComponent]
      })
        .overrideTemplate(ConfectionerPhotoDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConfectionerPhotoDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConfectionerPhotoService);
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
