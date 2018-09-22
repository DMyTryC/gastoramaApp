/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GastoramaTestModule } from '../../../test.module';
import { ConfectionerDetailComponent } from 'app/entities/confectioner/confectioner-detail.component';
import { Confectioner } from 'app/shared/model/confectioner.model';

describe('Component Tests', () => {
  describe('Confectioner Management Detail Component', () => {
    let comp: ConfectionerDetailComponent;
    let fixture: ComponentFixture<ConfectionerDetailComponent>;
    const route = ({
      data: of({ confectioner: new Confectioner(123) })
    } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [ConfectionerDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ConfectionerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConfectionerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.confectioner).toEqual(
          jasmine.objectContaining({ id: 123 })
        );
      });
    });
  });
});
