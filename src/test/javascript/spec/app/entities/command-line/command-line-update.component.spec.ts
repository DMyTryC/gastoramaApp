/* tslint:disable max-line-length */
import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  tick
} from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { GastoramaTestModule } from '../../../test.module';
import { CommandLineUpdateComponent } from 'app/entities/command-line/command-line-update.component';
import { CommandLineService } from 'app/entities/command-line/command-line.service';
import { CommandLine } from 'app/shared/model/command-line.model';

describe('Component Tests', () => {
  describe('CommandLine Management Update Component', () => {
    let comp: CommandLineUpdateComponent;
    let fixture: ComponentFixture<CommandLineUpdateComponent>;
    let service: CommandLineService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [CommandLineUpdateComponent]
      })
        .overrideTemplate(CommandLineUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommandLineUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CommandLineService);
    });

    describe('save', () => {
      it(
        'Should call update service on save for existing entity',
        fakeAsync(() => {
          // GIVEN
          const entity = new CommandLine(123);
          spyOn(service, 'update').and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.commandLine = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.update).toHaveBeenCalledWith(entity);
          expect(comp.isSaving).toEqual(false);
        })
      );

      it(
        'Should call create service on save for new entity',
        fakeAsync(() => {
          // GIVEN
          const entity = new CommandLine();
          spyOn(service, 'create').and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.commandLine = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.create).toHaveBeenCalledWith(entity);
          expect(comp.isSaving).toEqual(false);
        })
      );
    });
  });
});
