/**
 * Created by Administrator on 2016/7/23 0023.
 */
var app = angular.module("index",[]);
app.controller("indexCtrl",["$scope","$interval",function($scope, $interval){
    $interval(function(){
        var EndTime= new Date('2016/08/07 00:00:00');
        var NowTime = new Date();
        var t =EndTime.getTime() - NowTime.getTime();
        var d=0;
        var h=0;
        var m=0;
        var s=0;
        if(t>=0){
            d=Math.floor(t/1000/60/60/24);
            h=Math.floor(t/1000/60/60%24);
            m=Math.floor(t/1000/60%60);
            s=Math.floor(t/1000%60);
        }
        $scope.currentTime = d + "天" + h + "时" + m + "分" + s + "秒";
    },1000);

}]);