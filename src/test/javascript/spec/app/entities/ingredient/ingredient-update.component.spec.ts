/* tslint:disable max-line-length */
import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  tick
} from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { GastoramaTestModule } from '../../../test.module';
import { IngredientUpdateComponent } from 'app/entities/ingredient/ingredient-update.component';
import { IngredientService } from 'app/entities/ingredient/ingredient.service';
import { Ingredient } from 'app/shared/model/ingredient.model';

describe('Component Tests', () => {
  describe('Ingredient Management Update Component', () => {
    let comp: IngredientUpdateComponent;
    let fixture: ComponentFixture<IngredientUpdateComponent>;
    let service: IngredientService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GastoramaTestModule],
        declarations: [IngredientUpdateComponent]
      })
        .overrideTemplate(IngredientUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IngredientUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IngredientService);
    });

    describe('save', () => {
      it(
        'Should call update service on save for existing entity',
        fakeAsync(() => {
          // GIVEN
          const entity = new Ingredient(123);
          spyOn(service, 'update').and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.ingredient = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.update).toHaveBeenCalledWith(entity);
          expect(comp.isSaving).toEqual(false);
        })
      );

      it(
        'Should call create service on save for new entity',
        fakeAsync(() => {
          // GIVEN
          const entity = new Ingredient();
          spyOn(service, 'create').and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.ingredient = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.create).toHaveBeenCalledWith(entity);
          expect(comp.isSaving).toEqual(false);
        })
      );
    });
  });
});
