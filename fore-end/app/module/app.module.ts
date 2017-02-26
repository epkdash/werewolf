import {NgModule}      from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule}   from '@angular/forms';
import {HttpModule}    from '@angular/http';
import {AppRoutingModule} from './app-routing.module';
import {CodemirrorModule} from "ng2-codemirror";
import {MaterialModule} from "@angular/material";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

import '../rxjs-extensions';
import {ViewportComponent} from "../component/viewport/viewport.component";
import {BroadService} from "../service/broad.service";
import {HonoraryService} from "../service/honorary.service";
import {GameTaleService} from "../service/game-tale.service";
import {TopOneService} from "../service/top-one.service";
import {PlayerInfoService} from "../service/player-info.service";
import {ResumeComponent} from "../component/resume/resume.component";
import {BulletinComponent} from "../component/bulletin/bulletin.component";
import {MessageBroadComponent} from "../component/message-broad/message-broad.component";

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        AppRoutingModule,
        NgbModule.forRoot(),
        MaterialModule.forRoot(),
        CodemirrorModule
    ],
    declarations: [
        ViewportComponent,
        ResumeComponent,
        BulletinComponent,
        MessageBroadComponent
    ],
    entryComponents: [
    ],
    providers: [
        PlayerInfoService,
        TopOneService,
        BroadService,
        GameTaleService,
        HonoraryService
    ],
    bootstrap: [ViewportComponent]
})
export class AppModule {
}


