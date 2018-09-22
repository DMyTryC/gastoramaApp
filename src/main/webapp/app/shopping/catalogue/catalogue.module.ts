import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductDetailsCatalogueComponent } from './product-details-catalogue/product-details-catalogue.component';
import { CatalogueComponent } from './catalogue.component';
import { MatGridListModule } from '@angular/material';
import { FormsModule } from '@angular/forms';

import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CATALOGUE_ROUTE, catalogueRoutes} from './catalogue.route';

@NgModule({
  imports: [
    CommonModule,
    MatGridListModule,
    FormsModule,
    // NgxPaginationModule,
    RouterModule.forChild(catalogueRoutes)
  ],
  declarations: [ProductDetailsCatalogueComponent, CatalogueComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  exports: [CatalogueComponent]
})
export class CatalogueModule {}
