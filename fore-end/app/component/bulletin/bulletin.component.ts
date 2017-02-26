import {Component, OnInit, Renderer, ElementRef, AfterViewInit, ViewChild} from '@angular/core';
import {TopOneService} from "../../service/top-one.service";
import {BroadService} from "../../service/broad.service";
import {PlayerInfoService} from "../../service/player-info.service";
import {ActivatedRoute, Params} from "@angular/router";
import {MessageBroadComponent} from "../message-broad/message-broad.component";
import {GameTaleService} from "../../service/game-tale.service";
import {HonoraryService} from "../../service/honorary.service";

@Component({
    selector: 'bulletin',
    templateUrl: './bulletin.component.html'
})
export class BulletinComponent implements OnInit, AfterViewInit {
    private seerTopOnePlayer: any;
    private witchTopOnePlayer: any;
    private wolfTopOnePlayer: any;

    private topWolfBroad: string;
    private topWitchBroad: string;
    private topSeerBroad: string;

    private topBroad: any;
    private mainTale: any;
    private honorary: any;

    private playerInfo: any;
    private honoraryPlayer: any;

    @ViewChild(MessageBroadComponent)
    private messageBroadComponent: MessageBroadComponent;

    constructor(private topOneService: TopOneService, private broadService: BroadService,
                private playerInfoService: PlayerInfoService, private gameTaleService: GameTaleService,
                private honoraryService: HonoraryService,
                private actRoute: ActivatedRoute) {
    }

    ngAfterViewInit(): void {
        this.actRoute.params.switchMap((params: Params) => this.playerInfoService.getPlayerInfo(params['passport']))
            .subscribe(playerInfo => {
                console.log("this is router bulletin");
                console.log(playerInfo);
                if (playerInfo.playerId == '') {
                    this.playerInfo = {'playerId': 'niming', 'name': '匿名'};
                } else {
                    this.playerInfo = playerInfo
                }
                this.messageBroadComponent.setPlayerInfo(this.playerInfo);
                this.gameTaleService.getAllGameTale().then(gameTale=> {
                    console.log("gameTale");
                    console.log(gameTale)
                    this.topBroad = gameTale.topBroad;
                    this.mainTale = gameTale.mainTale;
                    this.honorary = gameTale.honorary;

                    this.honoraryService.getHonoraryPlayer().then(val=> {
                        this.honorary.oneExplain = this.honorary.oneExplain.replace(/\\\{maxScore\\\}/, val.villagerKilledTopOne.maxScore);
                        this.honorary.twoExplain = this.honorary.twoExplain.replace(/\\\{maxScore\\\}/, val.witchKillTopOne.maxScore);
                        this.honorary.threeExplain = this.honorary.threeExplain.replace(/\\\{maxScore\\\}/, val.seerFisrNightKilledTopOne.maxScore);
                        this.honorary.fourExplain = this.honorary.fourExplain.replace(/\\\{maxScore\\\}/, val.huntedTopOne.maxScore);
                        this.honorary.sixExplain = this.honorary.sixExplain.replace(/\\\{maxScore\\\}/, val.hurtPlayerTopOne.maxScore);
                        this.honorary.sixExplain = this.honorary.sixExplain.replace(/\\\{hurtedPlayerName\\\}/, val.hurtPlayerTopOne.hurtedPlayerName);
                        this.honorary.sixTale = this.honorary.sixTale.replace(/\\\{hurtedPlayerName\\\}/, val.hurtPlayerTopOne.hurtedPlayerName);

                        this.honoraryPlayer = val;
                    });

                    this.topOneService.getWolfTopOne().then(value => {
                        this.wolfTopOnePlayer = value;
                        this.topOneService.getWitchTopOne().then(value => {
                            this.witchTopOnePlayer = value;
                            this.topOneService.getSeerTopOne().then(value => {
                                this.seerTopOnePlayer = value;
                                this.getTopOneBoards();
                            });
                        });
                    });
                });

            });

    }

    ngOnInit(): void {

    }

    getTopOneBoards(): void {
        this.broadService.getTopOneBoards().then(topOneBoards => {
            for (let topOneBoard of topOneBoards) {
                switch (topOneBoard.type) {
                    case 'wolf':
                        if (this.wolfTopOnePlayer.playerId == topOneBoard.playerId) {
                            this.topWolfBroad = topOneBoard.message;
                        }
                        break;
                    case 'witch':
                        if (this.witchTopOnePlayer.playerId == topOneBoard.playerId) {
                            this.topWitchBroad = topOneBoard.message;
                        }
                        break;
                    case 'seer':
                        if (this.seerTopOnePlayer.playerId == topOneBoard.playerId) {
                            this.topSeerBroad = topOneBoard.message;
                        }
                        break;
                }
            }
        });
    }

    addWolfTopOneBoard(wolfTopOne: any): void {
        this.broadService.addWolfTopOneBoard('zhuangbin', wolfTopOne.value).then(()=> {
            this.getTopOneBoards();
        });
    }

    addWitchTopOneBoard(witchTopOne: any): void {
        this.broadService.addWitchTopOneBoard('jianghe', witchTopOne.value).then(()=> {
            this.getTopOneBoards();
        });
    }

    addSeerTopOneBoard(seerTopOne: any): void {
        this.broadService.addSeerTopOneBoard('xiangyang', seerTopOne.value).then(()=> {
            this.getTopOneBoards();
        });
    }
}