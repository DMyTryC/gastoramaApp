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
import { ProductTypePhoto } from 'app/shared/model/product-type-photo.model';
import { ProductTypePhotoService } from './product-type-photo.service';
import { ProductTypePhotoComponent } from './product-type-photo.component';
import { ProductTypePhotoDetailComponent } from './product-type-photo-detail.component';
import { ProductTypePhotoUpdateComponent } from './product-type-photo-update.component';
import { ProductTypePhotoDeletePopupComponent } from './product-type-photo-delete-dialog.component';
import { IProductTypePhoto } from 'app/shared/model/product-type-photo.model';

@Injectable({ providedIn: 'root' })
export class ProductTypePhotoResolve implements Resolve<IProductTypePhoto> {
  constructor(private service: ProductTypePhotoService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service
        .find(id)
        .pipe(
          map(
            (productTypePhoto: HttpResponse<ProductTypePhoto>) =>
              productTypePhoto.body
          )
        );
    }
    return of(new ProductTypePhoto());
  }
}

export const productTypePhotoRoute: Routes = [
  {
    path: 'product-type-photo',
    component: ProductTypePhotoComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.productTypePhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'product-type-photo/:id/view',
    component: ProductTypePhotoDetailComponent,
    resolve: {
      productTypePhoto: ProductTypePhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.productTypePhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'product-type-photo/new',
    component: ProductTypePhotoUpdateComponent,
    resolve: {
      productTypePhoto: ProductTypePhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.productTypePhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'product-type-photo/:id/edit',
    component: ProductTypePhotoUpdateComponent,
    resolve: {
      productTypePhoto: ProductTypePhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.productTypePhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const productTypePhotoPopupRoute: Routes = [
  {
    path: 'product-type-photo/:id/delete',
    component: ProductTypePhotoDeletePopupComponent,
    resolve: {
      productTypePhoto: ProductTypePhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.productTypePhoto.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
