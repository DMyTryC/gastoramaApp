/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GastoramaTestModule } from '../../../test.module';
import { ProductPhotoDetailComponent } from 'app/entities/product-photo/product-photo-detail.component';
import { ProductPhoto } from 'app/shared/model/product-photo.model';

describe('Component Tests', () => {
  describe('ProductPhoto Management Detail Component', () => {
    let comp: ProductPhotoDetailComponent;
    let fixture: ComponentFixture<ProductPhotoDetailComponent>;
    const route = ({
      data: of({ productPhoto: new ProductPhoto(123) })
    } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [ProductPhotoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ProductPhotoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProductPhotoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.productPhoto).toEqual(
          jasmine.objectContaining({ id: 123 })
        );
      });
    });
  });
});
