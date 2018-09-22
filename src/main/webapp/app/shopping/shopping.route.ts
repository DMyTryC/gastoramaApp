import { Route } from '@angular/router';

import {ProductDetailsComponent} from './product-details/product-details.component';
import {CatalogueComponent} from 'app/shopping/catalogue/catalogue.component';

export const shoppingRoutes: Route[] = [
    {
        path: '',
        component: CatalogueComponent,
        data: {
            authorities: [],
            pageTitle: 'catalogue.title'
        }
    },
    {
        path: 'productDetails/:id',
        component: ProductDetailsComponent,
        data: {
            authorities: [],
            pageTitle: 'productDetails.title'
        }
    }
];
