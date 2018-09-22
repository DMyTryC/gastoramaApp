import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GastoramaSharedModule } from 'app/shared';
import {
  ProductPhotoComponent,
  ProductPhotoDetailComponent,
  ProductPhotoUpdateComponent,
  ProductPhotoDeletePopupComponent,
  ProductPhotoDeleteDialogComponent,
  productPhotoRoute,
  productPhotoPopupRoute
} from './';

const ENTITY_STATES = [...productPhotoRoute, ...productPhotoPopupRoute];

@NgModule({
  imports: [GastoramaSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ProductPhotoComponent,
    ProductPhotoDetailComponent,
    ProductPhotoUpdateComponent,
    ProductPhotoDeleteDialogComponent,
    ProductPhotoDeletePopupComponent
  ],
  entryComponents: [
    ProductPhotoComponent,
    ProductPhotoUpdateComponent,
    ProductPhotoDeleteDialogComponent,
    ProductPhotoDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GastoramaProductPhotoModule {}
