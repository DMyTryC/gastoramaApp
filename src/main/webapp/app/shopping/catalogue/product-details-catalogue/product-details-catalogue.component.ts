import {Component, Input, OnInit } from '@angular/core';
import { CartStateService } from 'app/shopping/cart/cartState.service';
import { CartProduct } from 'app/shared/model/product.model';
import {Router} from '@angular/router';
import {BehaviorSubject} from 'rxjs';

@Component({
    selector: 'jhi-product-details-catalogue',
    templateUrl: './product-details-catalogue.component.html',
    styleUrls: ['product-details-catalogue.scss', '../../../fonts-style.scss']
})
export class ProductDetailsCatalogueComponent implements OnInit {
    @Input() prod: CartProduct;

    behavior: BehaviorSubject<CartProduct>;
    isEditing = false;
    quantityEdited: number;
    router: Router;
    isTooLong = false;

    ngOnInit() {
        this.prod.quantity = 0;
        this.prod.isInCart = false;

        this.behavior = this.cartS.getBehavior(this.prod.id);
        if (this.behavior) {
            this.prod.quantity = this.behavior.getValue().quantity;
            this.prod.isInCart = this.behavior.getValue().isInCart;

            this.behavior.subscribe(newProd => {
                this.prod = newProd;
            });
        }

        if (this.prod.name.length > 20) {
            this.isTooLong = true;
        }

    }

    constructor(private cartS: CartStateService, router: Router) {
        this.router = router;
    }

    add() {
        if (this.prod.stock >= this.prod.quantity + 1) {
            this.prod.quantity++;

            if (this.behavior) {
                this.behavior.next(this.prod);
            }
        }
        console.log(this.prod.name, this.prod.quantity);
        if (this.prod.quantity === 1) {
            this.prod.isInCart = true;
            this.behavior = this.cartS.add(this.prod);

            this.behavior.subscribe(value => {
                this.prod = value;
            });
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
                this.cartS.remove(this.prod);
            }
        }
        this.isEditing = false;
    }

    edition() {
        this.quantityEdited = this.prod.quantity;
        this.isEditing = true;
    }

}
