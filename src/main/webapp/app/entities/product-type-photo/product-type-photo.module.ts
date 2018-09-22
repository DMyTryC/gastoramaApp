import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GastoramaSharedModule } from 'app/shared';
import {
  ProductTypePhotoComponent,
  ProductTypePhotoDetailComponent,
  ProductTypePhotoUpdateComponent,
  ProductTypePhotoDeletePopupComponent,
  ProductTypePhotoDeleteDialogComponent,
  productTypePhotoRoute,
  productTypePhotoPopupRoute
} from './';

const ENTITY_STATES = [...productTypePhotoRoute, ...productTypePhotoPopupRoute];

@NgModule({
  imports: [GastoramaSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ProductTypePhotoComponent,
    ProductTypePhotoDetailComponent,
    ProductTypePhotoUpdateComponent,
    ProductTypePhotoDeleteDialogComponent,
    ProductTypePhotoDeletePopupComponent
  ],
  entryComponents: [
    ProductTypePhotoComponent,
    ProductTypePhotoUpdateComponent,
    ProductTypePhotoDeleteDialogComponent,
    ProductTypePhotoDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GastoramaProductTypePhotoModule {}
