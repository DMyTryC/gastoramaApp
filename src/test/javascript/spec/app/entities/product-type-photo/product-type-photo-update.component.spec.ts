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
import { ProductTypePhotoUpdateComponent } from 'app/entities/product-type-photo/product-type-photo-update.component';
import { ProductTypePhotoService } from 'app/entities/product-type-photo/product-type-photo.service';
import { ProductTypePhoto } from 'app/shared/model/product-type-photo.model';

describe('Component Tests', () => {
  describe('ProductTypePhoto Management Update Component', () => {
    let comp: ProductTypePhotoUpdateComponent;
    let fixture: ComponentFixture<ProductTypePhotoUpdateComponent>;
    let service: ProductTypePhotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [ProductTypePhotoUpdateComponent]
      })
        .overrideTemplate(ProductTypePhotoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductTypePhotoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProductTypePhotoService);
    });

    describe('save', () => {
      it(
        'Should call update service on save for existing entity',
        fakeAsync(() => {
          // GIVEN
          const entity = new ProductTypePhoto(123);
          spyOn(service, 'update').and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.productTypePhoto = entity;
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
          const entity = new ProductTypePhoto();
          spyOn(service, 'create').and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.productTypePhoto = entity;
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
