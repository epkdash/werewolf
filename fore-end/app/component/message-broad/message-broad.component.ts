import {Component, OnInit, Renderer, ElementRef, AfterViewInit} from '@angular/core';
import {BroadService} from "../../service/broad.service";

@Component({
    selector: 'message-broad',
    templateUrl: './message-broad.component.html'
})
export class MessageBroadComponent implements OnInit, AfterViewInit {
    private dom: any;
    private renderer: any;
    private messageBoards: any[];
    private playerInfo: any;

    constructor(elem: ElementRef, renderer: Renderer, private broadService: BroadService) {
        this.dom = elem.nativeElement;
        this.renderer = renderer;
    }

    ngAfterViewInit(): void {
        this.queryMessageBoards();
    }

    ngOnInit(): void {
    }

    setPlayerInfo(playerInfo: any): void {
        this.playerInfo = playerInfo;
    }

    queryMessageBoards(): void {
        this.broadService.getMessageBoards().then(arr=> {
                this.messageBoards = arr;
            }
        )
    }

    addMessageBoard(oMessage: any): void {
        console.log(oMessage.value)
        this.broadService.addMessageBoard(this.playerInfo.playerId, oMessage.value).then(()=> {
            oMessage.value = '';
            this.queryMessageBoards();
        });
    }

}