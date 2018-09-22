/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GastoramaTestModule } from '../../../test.module';
import { ProductPhotoComponent } from 'app/entities/product-photo/product-photo.component';
import { ProductPhotoService } from 'app/entities/product-photo/product-photo.service';
import { ProductPhoto } from 'app/shared/model/product-photo.model';

describe('Component Tests', () => {
  describe('ProductPhoto Management Component', () => {
    let comp: ProductPhotoComponent;
    let fixture: ComponentFixture<ProductPhotoComponent>;
    let service: ProductPhotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [ProductPhotoComponent],
        providers: []
      })
        .overrideTemplate(ProductPhotoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductPhotoComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProductPhotoService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ProductPhoto(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.productPhotos[0]).toEqual(
        jasmine.objectContaining({ id: 123 })
      );
    });
  });
});
