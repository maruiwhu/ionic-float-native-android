import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
declare let cordova: any;
// declare let  Baichuan:any;

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  constructor(public navCtrl: NavController) {

  }

  showFloat(){
	cordova.plugins.FloatWindow.showFloat(['this is a title'],function(){
		console.log('click')
			cordova.plugins.Baichuan.showPage({type:'page',url:'https://uland.taobao.com/coupon/edetail?e=KRtfD%2BHwGtIGQASttHIRqZmhRfgM3QnuFa4wdtTF6RDKmpsEBOsxNCs%2Bp7zlNnVz%2BdU3GywD85tPbOyIp01KG5Q5wfGz%2Fu%2BNKH0Sqb0wdcnMBAjZVSbr6yZ6Y%2FpkHtT5QS0Flu%2FfbSovkBQlP112cJ5ECHpSy25Ge6L%2Bf9DtnlWwiR4U6pJ%2FjnQfk9LmPRVb&traceId=0b8e351915389854890058433e&union_lens=lensId:0b092931_07b3_16652aef27c_2999&thispid=mm_50761234_33364180_118740063&src=fklm_hltk&from=tool&sight=fklm'}, 
				[{'pid':''}, {} ,{}], function(){
					alert('success ')
				}, function(){alert('error')});
	},error=>alert(error));
}

 hideFloat(){
	cordova.plugins.FloatWindow.hideFloat([],result=>alert(result),error=>alert(error));
}

 registerClipBoard(){
	cordova.plugins.FloatWindow.registerClipBoardListener([],result=>{console.log(result)
	alert(result)},error=>alert(error));
}

 unRegisterClipBoard(){
	cordova.plugins.FloatWindow.unRegisterClipBoardListener([],result=>alert(result),error=>alert(error));
}

startTaobaoApp(){cordova.plugins.FloatWindow.startApp(['com.maruiwhu.cordovapluginwindowalert'],function(){
	console.log('start app success')
	cordova.plugins.FloatWindow.registerClipBoardListener([],function(string){
		console.log(string)
	},error=>alert(error));
},error=>alert(error));
}

startOtherApp(){
	cordova.plugins.FloatWindow.startApp(['com.taobao.taobao2'],result=>alert(result),error=>alert(error));
}


checkPermission(){
	cordova.plugins.FloatWindow.checkOverlaysPermission([],result=>alert(result),error=>alert(error));
}

startPermission(){
	cordova.plugins.FloatWindow.requestOverlaysPermission([],result=>alert(result),error=>alert(error));
}

}
