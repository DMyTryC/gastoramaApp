import { Injectable } from '@angular/core';
import 'rxjs/add/observable/of';
import { BehaviorSubject, Observable } from 'rxjs';
import {CartProduct} from 'app/shared/model/product.model';

@Injectable({
  providedIn: 'root'
})
export class CartStateService {

    // private products: CartProduct[] = [];
    private behaviors: BehaviorSubject<CartProduct>[] = [];
    private behaviorsObs = new BehaviorSubject(this.behaviors);
    private total = 0;
    private totalObs = new BehaviorSubject(this.total);

    add(prod: CartProduct): BehaviorSubject<CartProduct> {
        this.total += prod.price;
        this.totalObs.next(this.total);

        const behavior: BehaviorSubject<CartProduct> = new BehaviorSubject(prod);
        this.behaviors.push(behavior);
        this.behaviorsObs.next(this.behaviors);

        return behavior;
    }

    remove(prod: CartProduct) {

        this.total -= prod.price;
        this.totalObs.next(this.total);

        this.behaviors.forEach((value, index) => {

            const newProd: CartProduct = value.getValue();
            if (newProd.id === prod.id) {

                newProd.quantity = 0;
                newProd.isInCart = false;

                value.next(newProd);

                /*var prodIndex = this.products.indexOf(newProd);
                this.product.splice(prodIndex, 1);*/
                this.behaviors.splice(index, 1);

                return;
            }
        });

        this.behaviorsObs.next(this.behaviors);
    }

    getBehavior(id: number): BehaviorSubject<CartProduct> {

        return this.behaviors.find(value => value.getValue().id === id);
    }

    getBehaviorsObs(): BehaviorSubject<BehaviorSubject<CartProduct>[]> {
        return this.behaviorsObs;
    }

    getTotalCart(): Observable<number> {
       return this.totalObs;
    }

    /*isInCart(id: number): Observable<boolean> {
        this.behaviors.forEach(item => {
            if (item.id === id) {
                return Observable.of(true);
            }
        });
        return Observable.of(false);
    }

    getProducts(): Observable<CartProduct[]> {
        return this.behaviorsObs;
    }

    getQuantity(id: any): Observable<number> {
        this.behaviors.forEach(item => {
            const n = (id === item.id);
            console.log('item.id:' + item.id + '//id:' + id + '//item.id === id:' + n);
            if (item.id === id) {
                console.log('returned true');
                return item.quantity;
            }
        });
        return Observable.of(0);
    }

    getIsInCart(id: number): Observable<boolean> {
        this.behaviors.forEach(item => {
            if (item.id === id) {
                return item.isInCart;
            }
        });
        return Observable.of(false);
    }

    getProduct(id: number): Observable<CartProduct> {
        this.behaviors.forEach(item => {
            if (item.id === id) {
                console.log('returned : ' + item + ' qty : ' + item.quantity);
                return item;
            }
        });
        return Observable.of(null);
    }

    addProduct(p: CartProduct) {
        this.behaviors.push(p);
        this.behaviorsObs.next(this.behaviors);
        console.log('products in cart : ' + this.behaviors);
    }

    removeProduct(id: number) {
        this.behaviors.forEach((item, index) => {
            if (item.id === id) {
                this.behaviors.splice(index, 1);
            }
        });
        this.behaviorsObs.next(this.behaviors);
    }

    removeAll() {
        this.behaviors.forEach((item, index) => {
            item.quantity = 0;
            item.isInCart = false;
        });
        this.behaviors = [];
        this.behaviorsObs.next(this.behaviors);
    }

    createCommand() {
        console.log('commande créee :', this.behaviors);
    }

    throwProduct(id: number) {
        this.behaviors.forEach((item, index) => {
            if (item.id === id) {
                item.quantity = 0;
                item.isInCart = false;
            }
        });
        this.removeProduct(id);
    }

    notify() {
        this.behaviorsObs.next(this.behaviors);
    }

    notifyOneChanged(prod: CartProduct) {
        this.behaviors.forEach(value => {
            if(prod.id == value.id) {
                value = prod;
            }
        });

        this.behaviorsObs.next(this.behaviors);
    }*/
}
