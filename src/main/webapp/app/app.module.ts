import './vendor.ts';

import { NgModule, Injector } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';

import {
  Ng2Webstorage,
  LocalStorageService,
  SessionStorageService
} from 'ngx-webstorage';
import { HttpClientModule } from '@angular/common/http';
import { JhiEventManager } from 'ng-jhipster';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { GastoramaSharedModule } from 'app/shared';
import { GastoramaCoreModule } from 'app/core';
import { GastoramaAppRoutingModule } from './app-routing.module';
import { GastoramaHomeModule } from './home/home.module';
import { GastoramaAccountModule } from './account/account.module';
import { GastoramaEntityModule } from './entities/entity.module';

import * as moment from 'moment';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import {
  JhiMainComponent,
  NavbarComponent,
  FooterComponent,
  PageRibbonComponent,
  ErrorComponent
} from './layouts';

import { ConnectionFormComponent } from './connection-form/connection-form.component';
import { ContactFormComponent } from './contact-form/contact-form.component';
import { UserDetailsComponent } from './user-details/user-details.component';

@NgModule({
  imports: [
    // RouterModule.forRoot( appRoutes, {enableTracing: true}),
      BrowserModule,
      GastoramaAppRoutingModule,
      Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
      GastoramaSharedModule,
      GastoramaCoreModule,
      GastoramaHomeModule,
      GastoramaAccountModule,
      GastoramaEntityModule,
      HttpClientModule,
      FormsModule,
      ReactiveFormsModule,
// jhipster-needle-angular-add-module JHipster will add new module here

  ],
  declarations: [
    JhiMainComponent,
    NavbarComponent,
    ErrorComponent,
    PageRibbonComponent,
    FooterComponent,
    ConnectionFormComponent,
    ContactFormComponent,
    UserDetailsComponent
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
      deps: [LocalStorageService, SessionStorageService]
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthExpiredInterceptor,
      multi: true,
      deps: [Injector]
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHandlerInterceptor,
      multi: true,
      deps: [JhiEventManager]
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: NotificationInterceptor,
      multi: true,
      deps: [Injector]
    }
  ],
  bootstrap: [JhiMainComponent]
})
export class GastoramaAppModule {
  constructor(private dpConfig: NgbDatepickerConfig) {
    this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
  }
}
