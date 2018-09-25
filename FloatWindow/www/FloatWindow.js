var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'FloatWindow', 'coolMethod', [arg0]);
};

var floatFunc = function(){};  

floatFunc.prototype.showFloat = function(arg0, success, error) {
    exec(success, error, "FloatWindow", "showFloat", arg0);
};

floatFunc.prototype.hideFloat = function(arg0, success, error) {
    exec(success, error, "FloatWindow", "hideFloat", arg0);
};

floatFunc.prototype.registerClipBoardListener = function(arg0, success, error) {
    exec(success, error, "FloatWindow", "registerClipBoardListener", arg0);
};

floatFunc.prototype.unRegisterClipBoardListener = function(arg0, success, error) {
    exec(success, error, "FloatWindow", "unRegisterClipBoardListener", arg0);
};


var MYMATHFUNC = new floatFunc();
module.exports = MYMATHFUNC; 


