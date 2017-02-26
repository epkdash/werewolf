import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import {AppModule} from './module/app.module';
import './style/css/main.css';
import './style/sass/main.scss';
import './style/css/font-awesome.min.css';
import './style/css/passport.css';



platformBrowserDynamic().bootstrapModule(AppModule);

