/**
 * Created by huangchen on 2016/12/3.
 */
import {Injectable} from '@angular/core';
import {Headers, Http} from '@angular/http';
import {WITCH_TOPONE_URL, WOLF_TOPONE_URL, SEER_TOPONE_URL} from "../constant/uri.constant";

@Injectable()
export class TopOneService {
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

    getWolfTopOne(): Promise<any> {
        return this.http.get(WOLF_TOPONE_URL)
            .toPromise()
            .then(response => {
                return response.json();
            })
            .catch(this.handleError);
    }

    getWitchTopOne(): Promise<any> {
        return this.http.get(WITCH_TOPONE_URL)
            .toPromise()
            .then(response => {
                return response.json();
            })
            .catch(this.handleError);
    }

    getSeerTopOne(): Promise<any> {
        return this.http.get(SEER_TOPONE_URL)
            .toPromise()
            .then(response => {
                return response.json();
            })
            .catch(this.handleError);
    }

}