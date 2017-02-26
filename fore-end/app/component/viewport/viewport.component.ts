import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {PlayerInfoService} from "../../service/player-info.service";
import {Location}                 from '@angular/common';

@Component({
    selector: 'viewport',
    templateUrl: './viewport.component.html'
})
export class ViewportComponent implements OnInit {

    private playerInfo: any;
    private isLogin: boolean = false;
    private isResume: boolean = false;
    private passport: string;
    private errorMsg: string;

    constructor(private router: Router, private playerInfoService: PlayerInfoService, private location: Location) {

    }

    ngOnInit(): void {
        let $body = $('body');
        let $menu = $('#menu');
        $menu.appendTo($body)
            .on('click', function (event) {
                event.stopPropagation();
                $body.removeClass('is-menu-visible');
            })
            .find('.inner')
            .on('click', '.close', function (event) {
                event.preventDefault();
                event.stopPropagation();
                event.stopImmediatePropagation();
                $body.removeClass('is-menu-visible');
            })
            .on('click', function (event) {
                event.stopPropagation();
            });
    }

    onLogIn(passport: string): void {
        this.passport = passport;
        this.playerInfoService.getPlayerInfo(passport).then(
            playerInfo => {
                if(playerInfo.playerId == ''){
                    this.errorMsg = '你不是咱村上的人吧';
                    this.clearPassportInput();
                }else{
                    this.playerInfo = playerInfo;
                    console.log('asfasdfasdfasdf');
                    console.log(playerInfo);
                    this.isLogin = true;
                    let $body = $('body');
                    $body.removeClass('is-menu-visible');
                    this.router.navigate(['/bulletin', passport]);
                }
            }
        );
    }

    clearPassportInput(): void {
        var $input = $(".ipt-fake-box input");
        var $realInput = $(".ipt-real-nick");
        $input.each(function () {
           $(this).val('');
        });
        $realInput.val('');
    }

    passportInput(el: any): void {
        let value = el.value;
        console.log(value);
        var $input = $(".ipt-fake-box input");
        if (/^[0-9]*$/g.test(value)) {
            let pwd: string = value.trim();
            let len = pwd.length;
            for (let i = 0; i < len; i++) {
                $input.eq(i).val(pwd[i]);
            }
            $input.each(function () {
                var index = $(this).index();
                if (index >= len) {
                    $(this).val('');
                }
            });
            if (len == 6) {
                el.blur();
                this.onLogIn(pwd);
            }
        } else {
            this.clearPassportInput();
        }
    }

    gotoResume(): void {
        this.isResume = true;
        this.router.navigate(['/resume', this.playerInfo.passport]);
    }

    goBakeHome(): void{
        this.isResume = false;
        this.location.back();
    }

    openLoginDialog(): void {
        let $body = $('body');
        $body.addClass('is-menu-visible');
    }


}
