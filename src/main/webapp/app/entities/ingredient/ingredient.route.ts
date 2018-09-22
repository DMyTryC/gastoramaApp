import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import {
  Resolve,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Routes
} from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Ingredient } from 'app/shared/model/ingredient.model';
import { IngredientService } from './ingredient.service';
import { IngredientComponent } from './ingredient.component';
import { IngredientDetailComponent } from './ingredient-detail.component';
import { IngredientUpdateComponent } from './ingredient-update.component';
import { IngredientDeletePopupComponent } from './ingredient-delete-dialog.component';
import { IIngredient } from 'app/shared/model/ingredient.model';

@Injectable({ providedIn: 'root' })
export class IngredientResolve implements Resolve<IIngredient> {
  constructor(private service: IngredientService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service
        .find(id)
        .pipe(map((ingredient: HttpResponse<Ingredient>) => ingredient.body));
    }
    return of(new Ingredient());
  }
}

export const ingredientRoute: Routes = [
  {
    path: 'ingredient',
    component: IngredientComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.ingredient.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ingredient/:id/view',
    component: IngredientDetailComponent,
    resolve: {
      ingredient: IngredientResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.ingredient.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ingredient/new',
    component: IngredientUpdateComponent,
    resolve: {
      ingredient: IngredientResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.ingredient.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ingredient/:id/edit',
    component: IngredientUpdateComponent,
    resolve: {
      ingredient: IngredientResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.ingredient.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const ingredientPopupRoute: Routes = [
  {
    path: 'ingredient/:id/delete',
    component: IngredientDeletePopupComponent,
    resolve: {
      ingredient: IngredientResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.ingredient.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
