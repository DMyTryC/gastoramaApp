///<reference path='confectioner-photo/confectioner-photo.module.ts'/>
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { GastoramaProductModule } from './product/product.module';
import { GastoramaProductTypeModule } from './product-type/product-type.module';
import { GastoramaIngredientModule } from './ingredient/ingredient.module';
import { GastoramaConfectionerModule } from './confectioner/confectioner.module';
import { GastoramaProductPhotoModule } from './product-photo/product-photo.module';
import { GastoramaProductTypePhotoModule } from './product-type-photo/product-type-photo.module';
import { GastoramaConfectionerPhotoModule } from './confectioner-photo/confectioner-photo.module';
import { GastoramaCommandLineModule } from './command-line/command-line.module';
import { GastoramaCommandModule } from './command/command.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
  // prettier-ignore
  imports: [
        GastoramaProductModule,
        GastoramaProductTypeModule,
        GastoramaIngredientModule,
        GastoramaConfectionerModule,
        GastoramaCommandModule,
        GastoramaCommandLineModule,
        GastoramaConfectionerPhotoModule,
        GastoramaProductPhotoModule,
        GastoramaProductTypePhotoModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GastoramaEntityModule {}
