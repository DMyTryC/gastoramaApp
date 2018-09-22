/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GastoramaTestModule } from '../../../test.module';
import { ConfectionerComponent } from 'app/entities/confectioner/confectioner.component';
import { ConfectionerService } from 'app/entities/confectioner/confectioner.service';
import { Confectioner } from 'app/shared/model/confectioner.model';

describe('Component Tests', () => {
  describe('Confectioner Management Component', () => {
    let comp: ConfectionerComponent;
    let fixture: ComponentFixture<ConfectionerComponent>;
    let service: ConfectionerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [ConfectionerComponent],
        providers: []
      })
        .overrideTemplate(ConfectionerComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConfectionerComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConfectionerService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Confectioner(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.confectioners[0]).toEqual(
        jasmine.objectContaining({ id: 123 })
      );
    });
  });
});
