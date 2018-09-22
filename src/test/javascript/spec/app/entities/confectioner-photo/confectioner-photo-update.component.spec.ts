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
import { ConfectionerPhotoUpdateComponent } from 'app/entities/confectioner-photo/confectioner-photo-update.component';
import { ConfectionerPhotoService } from 'app/entities/confectioner-photo/confectioner-photo.service';
import { ConfectionerPhoto } from 'app/shared/model/confectioner-photo.model';

describe('Component Tests', () => {
  describe('ConfectionerPhoto Management Update Component', () => {
    let comp: ConfectionerPhotoUpdateComponent;
    let fixture: ComponentFixture<ConfectionerPhotoUpdateComponent>;
    let service: ConfectionerPhotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [ConfectionerPhotoUpdateComponent]
      })
        .overrideTemplate(ConfectionerPhotoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConfectionerPhotoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConfectionerPhotoService);
    });

    describe('save', () => {
      it(
        'Should call update service on save for existing entity',
        fakeAsync(() => {
          // GIVEN
          const entity = new ConfectionerPhoto(123);
          spyOn(service, 'update').and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.confectionerPhoto = entity;
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
          const entity = new ConfectionerPhoto();
          spyOn(service, 'create').and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.confectionerPhoto = entity;
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
