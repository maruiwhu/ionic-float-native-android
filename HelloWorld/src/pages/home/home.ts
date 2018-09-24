import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
declare let cordova: any;

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  constructor(public navCtrl: NavController) {

  }

  showFloat(){
	cordova.plugins.FloatWindow.showFloat(['this is a title'],result=>alert(result),error=>alert(error));
}
 hideFloat(){
	cordova.plugins.FloatWindow.hideFloat([],result=>alert(result),error=>alert(error));
}

}
