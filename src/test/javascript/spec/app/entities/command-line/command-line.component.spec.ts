/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GastoramaTestModule } from '../../../test.module';
import { CommandLineComponent } from 'app/entities/command-line/command-line.component';
import { CommandLineService } from 'app/entities/command-line/command-line.service';
import { CommandLine } from 'app/shared/model/command-line.model';

describe('Component Tests', () => {
  describe('CommandLine Management Component', () => {
    let comp: CommandLineComponent;
    let fixture: ComponentFixture<CommandLineComponent>;
    let service: CommandLineService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [CommandLineComponent],
        providers: []
      })
        .overrideTemplate(CommandLineComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommandLineComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CommandLineService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CommandLine(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.commandLines[0]).toEqual(
        jasmine.objectContaining({ id: 123 })
      );
    });
  });
});
