import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from "rxjs";
import { map, catchError, tap } from 'rxjs/operators';
import { Order } from "../models/order";

const ErpEndpoint = 'http://localhost:7002/api/';
const MesEndpoint = 'http://localhost:7001/api/';
const ScadaEndpoint = 'http://localhost:7000/api/';
const httpOptions = {
    headers: new HttpHeaders({
        'Content-Type': 'application/json'
    })
};

@Injectable()
export class DataService {

    public loading: boolean;

    constructor(private http: HttpClient) { }

    private extractData(res: Response) {
        let body = res;
        return body || {};
    }

    /**
     * Create new order and retrieve order number.
     */
    public createOrder(): Observable<any> {
        return this.http.post(ErpEndpoint + 'create-order', null, httpOptions);
    }

    /**
     * /add-order-item/:order-id/:beer-name/:quantity
     */
    public addOrderItem(orderNumber: number, beerName: string, quantity: number): Observable<any>{
        return this.http.post(ErpEndpoint + 'add-order-item/' + orderNumber + '/' + beerName + '/' + quantity, null, httpOptions);
    }

    public getOrders(status: string): Observable<any> {
        return this.http.get(ErpEndpoint + 'view-orders/' + status);
    }


}