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
import { ConfectionerPhoto } from 'app/shared/model/confectioner-photo.model';
import { ConfectionerPhotoService } from './confectioner-photo.service';
import { ConfectionerPhotoComponent } from './confectioner-photo.component';
import { ConfectionerPhotoDetailComponent } from './confectioner-photo-detail.component';
import { ConfectionerPhotoUpdateComponent } from './confectioner-photo-update.component';
import { ConfectionerPhotoDeletePopupComponent } from './confectioner-photo-delete-dialog.component';
import { IConfectionerPhoto } from 'app/shared/model/confectioner-photo.model';

@Injectable({ providedIn: 'root' })
export class ConfectionerPhotoResolve implements Resolve<IConfectionerPhoto> {
  constructor(private service: ConfectionerPhotoService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service
        .find(id)
        .pipe(
          map(
            (confectionerPhoto: HttpResponse<ConfectionerPhoto>) =>
              confectionerPhoto.body
          )
        );
    }
    return of(new ConfectionerPhoto());
  }
}

export const confectionerPhotoRoute: Routes = [
  {
    path: 'confectioner-photo',
    component: ConfectionerPhotoComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.confectionerPhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'confectioner-photo/:id/view',
    component: ConfectionerPhotoDetailComponent,
    resolve: {
      confectionerPhoto: ConfectionerPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.confectionerPhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'confectioner-photo/new',
    component: ConfectionerPhotoUpdateComponent,
    resolve: {
      confectionerPhoto: ConfectionerPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.confectionerPhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'confectioner-photo/:id/edit',
    component: ConfectionerPhotoUpdateComponent,
    resolve: {
      confectionerPhoto: ConfectionerPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.confectionerPhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const confectionerPhotoPopupRoute: Routes = [
  {
    path: 'confectioner-photo/:id/delete',
    component: ConfectionerPhotoDeletePopupComponent,
    resolve: {
      confectionerPhoto: ConfectionerPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.confectionerPhoto.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
