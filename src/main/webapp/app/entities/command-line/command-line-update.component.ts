import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ICommandLine } from 'app/shared/model/command-line.model';
import { CommandLineService } from './command-line.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';
import { ICommand } from 'app/shared/model/command.model';
import { CommandService } from 'app/entities/command';

@Component({
  selector: 'jhi-command-line-update',
  templateUrl: './command-line-update.component.html'
})
export class CommandLineUpdateComponent implements OnInit {
  private _commandLine: ICommandLine;
  isSaving: boolean;

  products: IProduct[];

  commands: ICommand[];

  constructor(
    private jhiAlertService: JhiAlertService,
    private commandLineService: CommandLineService,
    private productService: ProductService,
    private commandService: CommandService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ commandLine }) => {
      this.commandLine = commandLine;
    });
    this.productService.query().subscribe(
      (res: HttpResponse<IProduct[]>) => {
        this.products = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.commandService.query().subscribe(
      (res: HttpResponse<ICommand[]>) => {
        this.commands = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    if (this.commandLine.id !== undefined) {
      this.subscribeToSaveResponse(
        this.commandLineService.update(this.commandLine)
      );
    } else {
      this.subscribeToSaveResponse(
        this.commandLineService.create(this.commandLine)
      );
    }
  }

  private subscribeToSaveResponse(
    result: Observable<HttpResponse<ICommandLine>>
  ) {
    result.subscribe(
      (res: HttpResponse<ICommandLine>) => this.onSaveSuccess(),
      (res: HttpErrorResponse) => this.onSaveError()
    );
  }

  private onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError() {
    this.isSaving = false;
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackProductById(index: number, item: IProduct) {
    return item.id;
  }

  trackCommandById(index: number, item: ICommand) {
    return item.id;
  }
  get commandLine() {
    return this._commandLine;
  }

  set commandLine(commandLine: ICommandLine) {
    this._commandLine = commandLine;
  }
}
