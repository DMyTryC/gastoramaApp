/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GastoramaTestModule } from '../../../test.module';
import { ProductTypePhotoComponent } from 'app/entities/product-type-photo/product-type-photo.component';
import { ProductTypePhotoService } from 'app/entities/product-type-photo/product-type-photo.service';
import { ProductTypePhoto } from 'app/shared/model/product-type-photo.model';

describe('Component Tests', () => {
  describe('ProductTypePhoto Management Component', () => {
    let comp: ProductTypePhotoComponent;
    let fixture: ComponentFixture<ProductTypePhotoComponent>;
    let service: ProductTypePhotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [ProductTypePhotoComponent],
        providers: []
      })
        .overrideTemplate(ProductTypePhotoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductTypePhotoComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProductTypePhotoService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ProductTypePhoto(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.productTypePhotos[0]).toEqual(
        jasmine.objectContaining({ id: 123 })
      );
    });
  });
});
