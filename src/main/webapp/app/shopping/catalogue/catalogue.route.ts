import { Route } from '@angular/router';

import { CatalogueComponent } from './catalogue.component';

export const CATALOGUE_ROUTE: Route = {
    path: '',
    component: CatalogueComponent,
    data: {
        authorities: [],
        pageTitle: 'home.title'
    }
};

export const catalogueRoutes: Route[] = [
    {
        path: '',
        component: CatalogueComponent,
        data: {
            pageTitle: 'home.title'
        }
    },
];
