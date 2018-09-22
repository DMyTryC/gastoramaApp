import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import {
  Resolve,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Routes
} from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CommandLine } from 'app/shared/model/command-line.model';
import { CommandLineService } from './command-line.service';
import { CommandLineComponent } from './command-line.component';
import { CommandLineDetailComponent } from './command-line-detail.component';
import { CommandLineUpdateComponent } from './command-line-update.component';
import { CommandLineDeletePopupComponent } from './command-line-delete-dialog.component';
import { ICommandLine } from 'app/shared/model/command-line.model';

@Injectable({ providedIn: 'root' })
export class CommandLineResolve implements Resolve<ICommandLine> {
  constructor(private service: CommandLineService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service
        .find(id)
        .pipe(
          map((commandLine: HttpResponse<CommandLine>) => commandLine.body)
        );
    }
    return of(new CommandLine());
  }
}

export const commandLineRoute: Routes = [
  {
    path: 'command-line',
    component: CommandLineComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.commandLine.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'command-line/:id/view',
    component: CommandLineDetailComponent,
    resolve: {
      commandLine: CommandLineResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.commandLine.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'command-line/new',
    component: CommandLineUpdateComponent,
    resolve: {
      commandLine: CommandLineResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.commandLine.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'command-line/:id/edit',
    component: CommandLineUpdateComponent,
    resolve: {
      commandLine: CommandLineResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.commandLine.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const commandLinePopupRoute: Routes = [
  {
    path: 'command-line/:id/delete',
    component: CommandLineDeletePopupComponent,
    resolve: {
      commandLine: CommandLineResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.commandLine.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
