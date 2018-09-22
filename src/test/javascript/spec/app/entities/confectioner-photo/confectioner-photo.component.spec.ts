/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GastoramaTestModule } from '../../../test.module';
import { ConfectionerPhotoComponent } from 'app/entities/confectioner-photo/confectioner-photo.component';
import { ConfectionerPhotoService } from 'app/entities/confectioner-photo/confectioner-photo.service';
import { ConfectionerPhoto } from 'app/shared/model/confectioner-photo.model';

describe('Component Tests', () => {
  describe('ConfectionerPhoto Management Component', () => {
    let comp: ConfectionerPhotoComponent;
    let fixture: ComponentFixture<ConfectionerPhotoComponent>;
    let service: ConfectionerPhotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [ConfectionerPhotoComponent],
        providers: []
      })
        .overrideTemplate(ConfectionerPhotoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConfectionerPhotoComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConfectionerPhotoService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ConfectionerPhoto(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.confectionerPhotos[0]).toEqual(
        jasmine.objectContaining({ id: 123 })
      );
    });
  });
});
