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
import { CommandLineDeleteDialogComponent } from 'app/entities/command-line/command-line-delete-dialog.component';
import { CommandLineService } from 'app/entities/command-line/command-line.service';

describe('Component Tests', () => {
  describe('CommandLine Management Delete Component', () => {
    let comp: CommandLineDeleteDialogComponent;
    let fixture: ComponentFixture<CommandLineDeleteDialogComponent>;
    let service: CommandLineService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [CommandLineDeleteDialogComponent]
      })
        .overrideTemplate(CommandLineDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CommandLineDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CommandLineService);
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
