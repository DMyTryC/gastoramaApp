/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GastoramaTestModule } from '../../../test.module';
import { CommandLineDetailComponent } from 'app/entities/command-line/command-line-detail.component';
import { CommandLine } from 'app/shared/model/command-line.model';

describe('Component Tests', () => {
  describe('CommandLine Management Detail Component', () => {
    let comp: CommandLineDetailComponent;
    let fixture: ComponentFixture<CommandLineDetailComponent>;
    const route = ({
      data: of({ commandLine: new CommandLine(123) })
    } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [CommandLineDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CommandLineDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CommandLineDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.commandLine).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
