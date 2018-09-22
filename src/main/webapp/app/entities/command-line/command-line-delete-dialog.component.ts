import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import {
  NgbActiveModal,
  NgbModal,
  NgbModalRef
} from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICommandLine } from 'app/shared/model/command-line.model';
import { CommandLineService } from './command-line.service';

@Component({
  selector: 'jhi-command-line-delete-dialog',
  templateUrl: './command-line-delete-dialog.component.html'
})
export class CommandLineDeleteDialogComponent {
  commandLine: ICommandLine;

  constructor(
    private commandLineService: CommandLineService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.commandLineService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'commandLineListModification',
        content: 'Deleted an commandLine'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-command-line-delete-popup',
  template: ''
})
export class CommandLineDeletePopupComponent implements OnInit, OnDestroy {
  private ngbModalRef: NgbModalRef;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal
  ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ commandLine }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(
          CommandLineDeleteDialogComponent as Component,
          { size: 'lg', backdrop: 'static' }
        );
        this.ngbModalRef.componentInstance.commandLine = commandLine;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate([{ outlets: { popup: null } }], {
              replaceUrl: true,
              queryParamsHandling: 'merge'
            });
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate([{ outlets: { popup: null } }], {
              replaceUrl: true,
              queryParamsHandling: 'merge'
            });
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
