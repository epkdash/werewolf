import {Component, OnInit, Renderer, ElementRef, AfterViewInit} from '@angular/core';
import {PlayerInfoService} from "../../service/player-info.service";
import {ActivatedRoute, Params} from "@angular/router";
import {ROLE_TRANS} from "../../constant/common.constant";

// let ndgrdata = {
//     tooltip: {
//         trigger: 'item',
//         formatter: "{a} <br/>{b} : {c} ({d}%)"
//     },
//     toolbox: {
//         show: true,
//         feature: {
//             mark: {show: true},
//             magicType: {
//                 show: true,
//                 type: ['pie', 'funnel']
//             },
//             saveAsImage: {show: true}
//         }
//     },
//     calculable: true,
//     series: [
//         {
//             name: '参与游戏次数',
//             type: 'pie',
//             radius: [30, 110],
//             center: ['75%', '50%'],
//             roseType: 'area',
//             data: [
//                 {value: 10, name: '狼人'},
//                 {value: 5, name: '女巫'},
//                 {value: 15, name: '预言家'},
//                 {value: 25, name: '普通村民'},
//                 {value: 20, name: '猎人'},
//                 {value: 35, name: '情侣'},
//                 {value: 30, name: '丘比特'},
//                 {value: 0, name: '野孩子'},
//                 {value: 12, name: '白狼'},
//                 {value: 42, name: '神圣的骑士'},
//                 {value: 61, name: '女仆'},
//                 {value: 100, name: '白痴'}
//             ]
//         }
//     ]
// };

@Component({
    selector: 'resume',
    templateUrl: './resume.component.html'
})
export class ResumeComponent implements OnInit, AfterViewInit {
    private dom: any;
    private renderer: any;
    private ndgrChart: any;
    private playerInfo: any;
    private playerStats: any;

    constructor(elem: ElementRef, renderer: Renderer, private route: ActivatedRoute
        , private playerInfoService: PlayerInfoService) {
        this.dom = elem.nativeElement;
        this.renderer = renderer;
    }

    ngAfterViewInit(): void {
        // let ndgr = this.dom.querySelector('#ndgr');
        //
        // let ndgrEcharts = require('echarts');
        // this.renderer.setElementStyle(ndgr, "width", "800px");
        // this.renderer.setElementStyle(ndgr, "height", "420px");
        // this.renderer.setElementStyle(ndgr, "left", "-400px");
        // this.ndgrChart = ndgrEcharts.init(ndgr);
        // this.ndgrChart.setOption(ndgrdata);
    }

    ngOnInit(): void {
        this.route.params.switchMap(
            (params: Params) => this.playerInfoService.getPlayerInfo(params['passport'])
        ).subscribe(playerInfo => {
            this.playerInfo = playerInfo;
            this.playerInfoService.getPlayerStats(playerInfo.playerId).then(playerStats=> {
                console.log("playerStats");
                console.log(playerStats);
                for(let role of playerStats.rolePlayGroupCount){
                    for(let trans of ROLE_TRANS){
                        if(trans.name == role.roleName){
                            role.roleTransName = trans.transName;
                            break;
                        }
                    }
                    let haveWin = false;
                    for(let roleWin of playerStats.roleWinPlayGroupCount){
                        if(roleWin.roleName == role.roleName){
                            role.roleWinCount = roleWin.roleCount;
                            haveWin = true;
                            break;
                        }
                    }
                    if(!haveWin){
                        role.roleWinCount = 0;
                        haveWin = false;
                    }
                }
                this.playerStats = playerStats;
            });
        });
    }

    computeWinRate(playerStats: any): string{
        if(playerStats.playCount != 0){
            return ((playerStats.playWinCount / playerStats.playCount)*100).toFixed(2)+'%';
        }else{
            return '0%'
        }
    }

}