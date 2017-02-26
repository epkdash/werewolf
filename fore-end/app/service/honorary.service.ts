/**
 * Created by huangchen on 2016/12/3.
 */
import {Injectable} from '@angular/core';
import {Headers, Http} from '@angular/http';
import {} from "../constant/uri.constant";
import {HONORARY_TOPONE_URL} from "../constant/uri.constant";

@Injectable()
export class HonoraryService {
    constructor(private http: Http) {
    }

    private headers = new Headers({
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET, POST, PATCH, PUT, DELETE, OPTIONS',
        'Access-Control-Allow-Headers': 'Origin, Content-Type, X-Auth-Token'
    });

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }

    getHonoraryPlayer(): Promise<any> {
        return this.http.get(HONORARY_TOPONE_URL)
            .toPromise()
            .then(response => {
                return response.json();
            })
            .catch(this.handleError);
    }

}