import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GastoramaSharedModule } from 'app/shared';
import { GastoramaAdminModule } from 'app/admin/admin.module';
import {
  CommandComponent,
  CommandDetailComponent,
  CommandUpdateComponent,
  CommandDeletePopupComponent,
  CommandDeleteDialogComponent,
  commandRoute,
  commandPopupRoute
} from './';

const ENTITY_STATES = [...commandRoute, ...commandPopupRoute];

@NgModule({
  imports: [
    GastoramaSharedModule,
    GastoramaAdminModule,
    RouterModule.forChild(ENTITY_STATES)
  ],
  declarations: [
    CommandComponent,
    CommandDetailComponent,
    CommandUpdateComponent,
    CommandDeleteDialogComponent,
    CommandDeletePopupComponent
  ],
  entryComponents: [
    CommandComponent,
    CommandUpdateComponent,
    CommandDeleteDialogComponent,
    CommandDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GastoramaCommandModule {}
