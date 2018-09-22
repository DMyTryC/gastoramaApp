import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GastoramaSharedModule } from 'app/shared';
import { GastoramaAdminModule } from 'app/admin/admin.module';
import {
  ConfectionerComponent,
  ConfectionerDetailComponent,
  ConfectionerUpdateComponent,
  ConfectionerDeletePopupComponent,
  ConfectionerDeleteDialogComponent,
  confectionerRoute,
  confectionerPopupRoute
} from './';

const ENTITY_STATES = [...confectionerRoute, ...confectionerPopupRoute];

@NgModule({
  imports: [
    GastoramaSharedModule,
    GastoramaAdminModule,
    RouterModule.forChild(ENTITY_STATES)
  ],
  declarations: [
    ConfectionerComponent,
    ConfectionerDetailComponent,
    ConfectionerUpdateComponent,
    ConfectionerDeleteDialogComponent,
    ConfectionerDeletePopupComponent
  ],
  entryComponents: [
    ConfectionerComponent,
    ConfectionerUpdateComponent,
    ConfectionerDeleteDialogComponent,
    ConfectionerDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GastoramaConfectionerModule {}
