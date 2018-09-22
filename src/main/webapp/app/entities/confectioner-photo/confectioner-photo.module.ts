import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GastoramaSharedModule } from 'app/shared';
import {
  ConfectionerPhotoComponent,
  ConfectionerPhotoDetailComponent,
  ConfectionerPhotoUpdateComponent,
  ConfectionerPhotoDeletePopupComponent,
  ConfectionerPhotoDeleteDialogComponent,
  confectionerPhotoRoute,
  confectionerPhotoPopupRoute
} from './';

const ENTITY_STATES = [
  ...confectionerPhotoRoute,
  ...confectionerPhotoPopupRoute
];

@NgModule({
  imports: [GastoramaSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ConfectionerPhotoComponent,
    ConfectionerPhotoDetailComponent,
    ConfectionerPhotoUpdateComponent,
    ConfectionerPhotoDeleteDialogComponent,
    ConfectionerPhotoDeletePopupComponent
  ],
  entryComponents: [
    ConfectionerPhotoComponent,
    ConfectionerPhotoUpdateComponent,
    ConfectionerPhotoDeleteDialogComponent,
    ConfectionerPhotoDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GastoramaConfectionerPhotoModule {}
