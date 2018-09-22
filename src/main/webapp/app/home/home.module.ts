import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GastoramaSharedModule } from 'app/shared';
import { homeRoutes, HomeComponent } from './';
import {ShoppingComponent} from 'app/shopping/shopping.component';
import {ProductDetailsComponent} from 'app/shopping/product-details/product-details.component';
import {CartComponent} from 'app/shopping/cart/cart.component';
import {ProductDetailsCartComponent} from 'app/shopping/cart/product-details-cart/product-details-cart.component';
import {CatalogueModule} from 'app/shopping/catalogue/catalogue.module';
import {DEBUG_INFO_ENABLED} from 'app/app.constants';

@NgModule({
  imports: [
    GastoramaSharedModule,
      CatalogueModule,
    RouterModule.forRoot(homeRoutes, { useHash: true, enableTracing: DEBUG_INFO_ENABLED })
  ],
  declarations: [HomeComponent, ShoppingComponent, ProductDetailsComponent, CartComponent, ProductDetailsCartComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GastoramaHomeModule {}
