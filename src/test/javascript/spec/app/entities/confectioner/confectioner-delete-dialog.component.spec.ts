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
import { ConfectionerDeleteDialogComponent } from 'app/entities/confectioner/confectioner-delete-dialog.component';
import { ConfectionerService } from 'app/entities/confectioner/confectioner.service';

describe('Component Tests', () => {
  describe('Confectioner Management Delete Component', () => {
    let comp: ConfectionerDeleteDialogComponent;
    let fixture: ComponentFixture<ConfectionerDeleteDialogComponent>;
    let service: ConfectionerService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [ConfectionerDeleteDialogComponent]
      })
        .overrideTemplate(ConfectionerDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConfectionerDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConfectionerService);
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
