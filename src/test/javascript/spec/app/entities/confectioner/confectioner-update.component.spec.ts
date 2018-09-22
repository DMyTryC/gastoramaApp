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
import { ConfectionerUpdateComponent } from 'app/entities/confectioner/confectioner-update.component';
import { ConfectionerService } from 'app/entities/confectioner/confectioner.service';
import { Confectioner } from 'app/shared/model/confectioner.model';

describe('Component Tests', () => {
  describe('Confectioner Management Update Component', () => {
    let comp: ConfectionerUpdateComponent;
    let fixture: ComponentFixture<ConfectionerUpdateComponent>;
    let service: ConfectionerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [ConfectionerUpdateComponent]
      })
        .overrideTemplate(ConfectionerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConfectionerUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConfectionerService);
    });

    describe('save', () => {
      it(
        'Should call update service on save for existing entity',
        fakeAsync(() => {
          // GIVEN
          const entity = new Confectioner(123);
          spyOn(service, 'update').and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.confectioner = entity;
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
          const entity = new Confectioner();
          spyOn(service, 'create').and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.confectioner = entity;
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
