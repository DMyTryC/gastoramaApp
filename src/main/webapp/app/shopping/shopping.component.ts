import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'jhi-shopping',
  templateUrl: './shopping.component.html',
  styles: []
})
export class ShoppingComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
  }

}
