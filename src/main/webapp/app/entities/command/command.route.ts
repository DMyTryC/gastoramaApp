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
import { Command } from 'app/shared/model/command.model';
import { CommandService } from './command.service';
import { CommandComponent } from './command.component';
import { CommandDetailComponent } from './command-detail.component';
import { CommandUpdateComponent } from './command-update.component';
import { CommandDeletePopupComponent } from './command-delete-dialog.component';
import { ICommand } from 'app/shared/model/command.model';

@Injectable({ providedIn: 'root' })
export class CommandResolve implements Resolve<ICommand> {
  constructor(private service: CommandService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service
        .find(id)
        .pipe(map((command: HttpResponse<Command>) => command.body));
    }
    return of(new Command());
  }
}

export const commandRoute: Routes = [
  {
    path: 'command',
    component: CommandComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.command.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'command/:id/view',
    component: CommandDetailComponent,
    resolve: {
      command: CommandResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.command.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'command/new',
    component: CommandUpdateComponent,
    resolve: {
      command: CommandResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.command.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'command/:id/edit',
    component: CommandUpdateComponent,
    resolve: {
      command: CommandResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.command.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const commandPopupRoute: Routes = [
  {
    path: 'command/:id/delete',
    component: CommandDeletePopupComponent,
    resolve: {
      command: CommandResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gastoramaApp.command.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
