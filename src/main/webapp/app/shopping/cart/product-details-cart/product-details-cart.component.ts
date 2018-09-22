import {Component, Input, OnInit} from '@angular/core';
import { CartProduct } from 'app/shared/model/product.model';
import {CartStateService} from 'app/shopping/cart/cartState.service';
import {BehaviorSubject} from 'rxjs';

@Component({
    selector: 'jhi-product-details-cart',
    templateUrl: './product-details-cart.component.html',
    styleUrls: ['product-details-cart.scss', '../../../fonts-style.scss']
})
export class ProductDetailsCartComponent implements OnInit {
    @Input() prod: CartProduct;
    behavior: BehaviorSubject<CartProduct>;
    isEditing = false;
    quantityEdited: number;
    trash = '\u{2A2F}';

  ngOnInit() {

        this.behavior = this.cartS.getBehavior(this.prod.id);

        this.behavior.subscribe(value => {
            console.log('cart notif');
            console.log(value);
            this.prod = value;
        });
    }

    constructor(private cartS: CartStateService) { }

    add() {
        if (this.prod.stock >= this.prod.quantity + 1) {
            this.prod.quantity++;
            this.behavior.next(this.prod);
        }
    }

    remove() {
        this.prod.quantity--;
        console.log(this.prod.name, this.prod.quantity);
        if (this.prod.quantity === 0) {
            // this.behavior.unsubscribe();
            this.behavior = null;
            this.cartS.remove(this.prod);
            this.prod.isInCart = false;
        }
    }

    cancelEdition() {
        this.isEditing = false;
    }

    confirmEdition() {
        if (this.quantityEdited === 0 || (Number(this.quantityEdited) && this.quantityEdited > 0)) {
            if (this.quantityEdited > this.prod.stock) {
                this.quantityEdited = this.prod.stock;
            }
            this.prod.quantity = this.quantityEdited;
            this.behavior.next(this.prod);
            if (this.prod.quantity === 0) {
                this.prod.isInCart = false;
            }
        }
        this.isEditing = false;
    }

    edition() {
        this.quantityEdited = this.prod.quantity;
        this.isEditing = true;
    }

    throwProduct() {
        // this.behavior.unsubscribe();
        this.behavior = null;
        this.cartS.remove(this.prod);
    }
}
