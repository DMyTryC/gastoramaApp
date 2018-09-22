import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIngredient } from 'app/shared/model/ingredient.model';

@Component({
  selector: 'jhi-ingredient-detail',
  templateUrl: './ingredient-detail.component.html'
})
export class IngredientDetailComponent implements OnInit {
  ingredient: IIngredient;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ ingredient }) => {
      this.ingredient = ingredient;
    });
  }

  previousState() {
    window.history.back();
  }
}
