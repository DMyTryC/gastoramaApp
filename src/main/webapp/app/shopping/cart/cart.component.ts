import {Component, OnDestroy, OnInit} from '@angular/core';
import { CartStateService } from './cartState.service';
import {CartProduct, IProduct} from 'app/shared/model/product.model';

@Component({
    selector: 'jhi-cart',
    templateUrl: './cart.component.html',
    styleUrls: ['cart.scss']
})
export class CartComponent implements OnInit, OnDestroy {
    products: CartProduct[] = [];
    display = 'none';
    total: number;

  constructor(private cartS: CartStateService) {}

    ngOnInit() {
        this.cartS.getBehaviorsObs().subscribe(value => {

            const newProducts: CartProduct[] = [];

            value.forEach(value1 => {
                newProducts.push(value1.getValue());
            });

            this.products = newProducts;
        });
        this.cartS.getTotalCart().subscribe(value => this.total = value);
    }

    emptyCart() {
        this.display = 'none';
        this.products.forEach(value => {
            this.cartS.remove(value);
        });
    }

    confirmCart() {
        // this.cartS.createCommand();
    }

    ngOnDestroy() {
    }

    openModal() {
      this.display = 'block';
      console.log(this.display);
    }

    onCloseHandled() {
      this.display = 'none';
      console.log(this.display);
    }
}
