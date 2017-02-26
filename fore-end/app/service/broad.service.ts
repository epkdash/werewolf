/**
 * Created by huangchen on 2016/12/3.
 */
import {Injectable} from '@angular/core';
import {Headers, Http} from '@angular/http';
import {
    MESSAGE_BOARD_URL, TOPONE_BOARD_URL, ADD_WOLF_TOPONE_BOARD_URL,
    ADD_WITCH_TOPONE_BOARD_URL, ADD_SEER_TOPONE_BOARD_URL
} from "../constant/uri.constant";

@Injectable()
export class BroadService {
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

    getMessageBoards(): Promise<any[]> {
        return this.http.get(MESSAGE_BOARD_URL)
            .toPromise()
            .then(response => {
                console.log(response.json());
                return response.json()._embedded.messageBoardResourceList;
            })
            .catch(this.handleError);
    }

    addMessageBoard(playerId: string, message: string): Promise<void> {
        return this.http.post(
            MESSAGE_BOARD_URL,
            JSON.stringify({'playerId': playerId, 'message': message}),
            {headers: this.headers}
        ).toPromise().then(() => {
        }).catch(this.handleError);
    }

    getTopOneBoards(): Promise<any[]> {
        return this.http.get(TOPONE_BOARD_URL)
            .toPromise()
            .then(response => {
                console.log(response.json());
                return response.json()._embedded.topOneBoardResourceList;
            })
            .catch(this.handleError);
    }

    addWolfTopOneBoard(playerId: any, message: any): Promise<void> {
        return this.http.post(
            ADD_WOLF_TOPONE_BOARD_URL,
            JSON.stringify({'playerId': playerId, 'message': message}),
            {headers: this.headers}
        ).toPromise().then(() => {
        }).catch(this.handleError);
    }

    addWitchTopOneBoard(playerId: any, message: any): Promise<void> {
        return this.http.post(
            ADD_WITCH_TOPONE_BOARD_URL,
            JSON.stringify({'playerId': playerId, 'message': message}),
            {headers: this.headers}
        ).toPromise().then(() => {
        }).catch(this.handleError);
    }

    addSeerTopOneBoard(playerId: any, message: any): Promise<void> {
        return this.http.post(
            ADD_SEER_TOPONE_BOARD_URL,
            JSON.stringify({'playerId': playerId, 'message': message}),
            {headers: this.headers}
        ).toPromise().then(() => {
        }).catch(this.handleError);
    }
}