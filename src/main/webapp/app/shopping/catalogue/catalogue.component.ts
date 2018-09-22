import { Component, OnInit } from '@angular/core';
import { ProductService } from 'app/entities/product/product.service';
import { HttpResponse } from '@angular/common/http';
import { IProduct } from 'app/shared/model/product.model';
import { Router } from '@angular/router';
import {CartStateService} from 'app/shopping/cart/cartState.service';

@Component({
    selector: 'jhi-catalogue',
    templateUrl: './catalogue.component.html',
    styleUrls: ['catalogue.scss']
})
export class CatalogueComponent implements OnInit {
    products: IProduct[] = [];
    productWidth: number;

    constructor(private prodS: ProductService, private router: Router, private cartSrv: CartStateService) {}

    ngOnInit() {
        this.prodS
            .query()
            .subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body));
    }

    onResize(width: number) {
        this.productWidth = (width / Math.trunc(width / 210)) - 12;
    }
}
