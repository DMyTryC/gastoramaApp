/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GastoramaTestModule } from '../../../test.module';
import { ProductTypePhotoDetailComponent } from 'app/entities/product-type-photo/product-type-photo-detail.component';
import { ProductTypePhoto } from 'app/shared/model/product-type-photo.model';

describe('Component Tests', () => {
  describe('ProductTypePhoto Management Detail Component', () => {
    let comp: ProductTypePhotoDetailComponent;
    let fixture: ComponentFixture<ProductTypePhotoDetailComponent>;
    const route = ({
      data: of({ productTypePhoto: new ProductTypePhoto(123) })
    } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [ProductTypePhotoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ProductTypePhotoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProductTypePhotoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.productTypePhoto).toEqual(
          jasmine.objectContaining({ id: 123 })
        );
      });
    });
  });
});
