/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GastoramaTestModule } from '../../../test.module';
import { ConfectionerPhotoDetailComponent } from 'app/entities/confectioner-photo/confectioner-photo-detail.component';
import { ConfectionerPhoto } from 'app/shared/model/confectioner-photo.model';

describe('Component Tests', () => {
  describe('ConfectionerPhoto Management Detail Component', () => {
    let comp: ConfectionerPhotoDetailComponent;
    let fixture: ComponentFixture<ConfectionerPhotoDetailComponent>;
    const route = ({
      data: of({ confectionerPhoto: new ConfectionerPhoto(123) })
    } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [ConfectionerPhotoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ConfectionerPhotoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConfectionerPhotoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.confectionerPhoto).toEqual(
          jasmine.objectContaining({ id: 123 })
        );
      });
    });
  });
});
