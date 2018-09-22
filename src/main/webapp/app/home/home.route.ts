import { Route } from '@angular/router';

import { HomeComponent } from './';
import {ConnectionFormComponent} from 'app/connection-form/connection-form.component';
import {ShoppingComponent} from 'app/shopping/shopping.component';
import {ProductDetailsComponent} from 'app/shopping/product-details/product-details.component';
import {shoppingRoutes} from 'app/shopping/shopping.route';
import {RegisterComponent} from 'app/account';
import {ContactFormComponent} from 'app/contact-form/contact-form.component';
import {UserDetailsComponent} from 'app/user-details/user-details.component';

export const homeRoutes: Route[] = [
    {   path: '',
        component: HomeComponent,
        data: {
            authorities: [],
            pageTitle: 'home.title'
        }
    },
    {   path: 'connection',
        component: ConnectionFormComponent,
        data: {
            pageTitle: 'home.title'
        }
    },
    {
        path: 'shopping',
        component: ShoppingComponent,
        data: {
            pageTitle: 'home.title'
        },
        children: shoppingRoutes

    },
    {
        path: 'product-details',
        component: ProductDetailsComponent,
        data: {
            pageTitle: 'home.title'
        }
    },
    {
        path: 'subscription',
        component: RegisterComponent
    },
    {
        path: 'contact',
        component: ContactFormComponent
    },
    {
        path: 'userDetails',
        component: UserDetailsComponent
    }
];
