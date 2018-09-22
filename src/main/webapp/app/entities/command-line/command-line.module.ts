import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GastoramaSharedModule } from 'app/shared';
import {
  CommandLineComponent,
  CommandLineDetailComponent,
  CommandLineUpdateComponent,
  CommandLineDeletePopupComponent,
  CommandLineDeleteDialogComponent,
  commandLineRoute,
  commandLinePopupRoute
} from './';

const ENTITY_STATES = [...commandLineRoute, ...commandLinePopupRoute];

@NgModule({
  imports: [GastoramaSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CommandLineComponent,
    CommandLineDetailComponent,
    CommandLineUpdateComponent,
    CommandLineDeleteDialogComponent,
    CommandLineDeletePopupComponent
  ],
  entryComponents: [
    CommandLineComponent,
    CommandLineUpdateComponent,
    CommandLineDeleteDialogComponent,
    CommandLineDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GastoramaCommandLineModule {}
