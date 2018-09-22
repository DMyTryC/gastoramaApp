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
import { ProductPhoto } from 'app/shared/model/product-photo.model';
import { ProductPhotoService } from './product-photo.service';
import { ProductPhotoComponent } from './product-photo.component';
import { ProductPhotoDetailComponent } from './product-photo-detail.component';
import { ProductPhotoUpdateComponent } from './product-photo-update.component';
import { ProductPhotoDeletePopupComponent } from './product-photo-delete-dialog.component';
import { IProductPhoto } from 'app/shared/model/product-photo.model';

@Injectable({ providedIn: 'root' })
export class ProductPhotoResolve implements Resolve<IProductPhoto> {
  constructor(private service: ProductPhotoService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service
        .find(id)
        .pipe(
          map((productPhoto: HttpResponse<ProductPhoto>) => productPhoto.body)
        );
    }
    return of(new ProductPhoto());
  }
}

export const productPhotoRoute: Routes = [
  {
    path: 'product-photo',
    component: ProductPhotoComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.productPhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'product-photo/:id/view',
    component: ProductPhotoDetailComponent,
    resolve: {
      productPhoto: ProductPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.productPhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'product-photo/new',
    component: ProductPhotoUpdateComponent,
    resolve: {
      productPhoto: ProductPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.productPhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'product-photo/:id/edit',
    component: ProductPhotoUpdateComponent,
    resolve: {
      productPhoto: ProductPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.productPhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const productPhotoPopupRoute: Routes = [
  {
    path: 'product-photo/:id/delete',
    component: ProductPhotoDeletePopupComponent,
    resolve: {
      productPhoto: ProductPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.productPhoto.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
