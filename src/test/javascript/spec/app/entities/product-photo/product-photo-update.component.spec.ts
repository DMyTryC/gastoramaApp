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
import { ProductPhotoUpdateComponent } from 'app/entities/product-photo/product-photo-update.component';
import { ProductPhotoService } from 'app/entities/product-photo/product-photo.service';
import { ProductPhoto } from 'app/shared/model/product-photo.model';

describe('Component Tests', () => {
  describe('ProductPhoto Management Update Component', () => {
    let comp: ProductPhotoUpdateComponent;
    let fixture: ComponentFixture<ProductPhotoUpdateComponent>;
    let service: ProductPhotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [ProductPhotoUpdateComponent]
      })
        .overrideTemplate(ProductPhotoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductPhotoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProductPhotoService);
    });

    describe('save', () => {
      it(
        'Should call update service on save for existing entity',
        fakeAsync(() => {
          // GIVEN
          const entity = new ProductPhoto(123);
          spyOn(service, 'update').and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.productPhoto = entity;
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
          const entity = new ProductPhoto();
          spyOn(service, 'create').and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.productPhoto = entity;
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
