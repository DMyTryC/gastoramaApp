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
import { Confectioner } from 'app/shared/model/confectioner.model';
import { ConfectionerService } from './confectioner.service';
import { ConfectionerComponent } from './confectioner.component';
import { ConfectionerDetailComponent } from './confectioner-detail.component';
import { ConfectionerUpdateComponent } from './confectioner-update.component';
import { ConfectionerDeletePopupComponent } from './confectioner-delete-dialog.component';
import { IConfectioner } from 'app/shared/model/confectioner.model';

@Injectable({ providedIn: 'root' })
export class ConfectionerResolve implements Resolve<IConfectioner> {
  constructor(private service: ConfectionerService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service
        .find(id)
        .pipe(
          map((confectioner: HttpResponse<Confectioner>) => confectioner.body)
        );
    }
    return of(new Confectioner());
  }
}

export const confectionerRoute: Routes = [
  {
    path: 'confectioner',
    component: ConfectionerComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.confectioner.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'confectioner/:id/view',
    component: ConfectionerDetailComponent,
    resolve: {
      confectioner: ConfectionerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.confectioner.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'confectioner/new',
    component: ConfectionerUpdateComponent,
    resolve: {
      confectioner: ConfectionerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.confectioner.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'confectioner/:id/edit',
    component: ConfectionerUpdateComponent,
    resolve: {
      confectioner: ConfectionerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.confectioner.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const confectionerPopupRoute: Routes = [
  {
    path: 'confectioner/:id/delete',
    component: ConfectionerDeletePopupComponent,
    resolve: {
      confectioner: ConfectionerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.confectioner.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
