/**
 * Created by huangchen on 2016/12/3.
 */
import {NgModule}             from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {BulletinComponent}   from '../component/bulletin/bulletin.component';
import {ResumeComponent} from "../component/resume/resume.component";

const routes: Routes = [
    {path: '', redirectTo: '/bulletin', pathMatch: 'full'},
    {path: 'bulletin', component: BulletinComponent},
    {path: 'bulletin/:passport', component: BulletinComponent},
    {path: 'resume/:passport', component: ResumeComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})

export class AppRoutingModule {
}