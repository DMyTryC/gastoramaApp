import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommandLine } from 'app/shared/model/command-line.model';

@Component({
  selector: 'jhi-command-line-detail',
  templateUrl: './command-line-detail.component.html'
})
export class CommandLineDetailComponent implements OnInit {
  commandLine: ICommandLine;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ commandLine }) => {
      this.commandLine = commandLine;
    });
  }

  previousState() {
    window.history.back();
  }
}
