import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import {Component, Input, OnInit} from '@angular/core';
import {CartProduct, IProduct} from 'app/shared/model/product.model';
import {CartStateService} from 'app/shopping/cart/cartState.service';
import {ProductService} from 'app/entities/product';
import {ActivatedRoute, Router} from '@angular/router';
import {CartProdImpl} from 'app/shared/model/product.model';
import 'rxjs-compat/add/operator/do';
import {BehaviorSubject, Observable} from 'rxjs';

@Component({
    selector: 'jhi-product-details',
    templateUrl: './product-details.component.html',
    styleUrls: ['product-details.scss']
})
export class ProductDetailsComponent implements OnInit {

    id: number;
    prod: CartProduct = new CartProdImpl();
    isEditing = false;
    quantityEdited: number;
    behavior: BehaviorSubject<CartProduct>;
    router: Router;

    constructor(private cartS: CartStateService, private prodS: ProductService, private activatedRoute: ActivatedRoute,
                private routerA: Router) {

        this.router = routerA;
    }

    ngOnInit() {

        this.activatedRoute.params
            .do(params => this.id = +params['id'])
            .flatMap(value => this.prodS.find(this.id))
            .do(res => this.prod = this.prodS.iProductToCartProduct(res.body))
            .subscribe(value => {
                this.behavior = this.cartS.getBehavior(this.id);
                if (this.behavior) {
                    this.prod.quantity = this.behavior.getValue().quantity;
                    this.prod.isInCart = this.behavior.getValue().isInCart;

                    this.behavior.subscribe(this.subscribeToCartProduct);
                }
                // console.log(this.behavior);
                // console.log(this.prod);
            });
    }

    add() {
        if (this.prod.stock >= this.prod.quantity + 1) {
            this.prod.quantity++;
            if (this.behavior) {
                this.behavior.next(this.prod);
            }
        }
        if (this.prod.quantity === 1) {
            this.prod.isInCart = true;
            this.behavior = this.cartS.add(this.prod);

            this.behavior.subscribe(this.subscribeToCartProduct);
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
            if (this.behavior) {
                this.behavior.next(this.prod);
            }
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

    private subscribeToCartProduct(newProd: CartProduct) {
      console.log('detail notif : ');
      console.log(newProd);
      this.prod = newProd;
    }
}
