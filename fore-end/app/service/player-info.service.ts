/**
 * Created by huangchen on 2016/12/3.
 */
import {Injectable} from '@angular/core';
import {Headers, Http} from '@angular/http';
import {LOGIN_URL, PLAYER_STATS_URL} from "../constant/uri.constant";

@Injectable()
export class PlayerInfoService {
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

    getPlayerInfo(passport: string): Promise<any> {
        let loginUrl: string = LOGIN_URL;
        loginUrl = loginUrl.replace(/\{passport\}/, passport);
        return this.http.get(loginUrl)
            .toPromise()
            .then(response => {
                console.log(response.json());
                return response.json();
            })
            .catch(this.handleError);
    }

    getPlayerStats(playerId: string): Promise<any> {
        let playerStatsUrl: string = PLAYER_STATS_URL;
        playerStatsUrl = playerStatsUrl.replace(/\{playerId\}/, playerId);
        return this.http.get(playerStatsUrl)
            .toPromise()
            .then(response => {
                console.log(response.json());
                return response.json();
            })
            .catch(this.handleError);
    }

}