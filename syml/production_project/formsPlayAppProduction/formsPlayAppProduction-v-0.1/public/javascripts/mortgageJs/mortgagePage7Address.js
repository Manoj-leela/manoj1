/**
 * mortgagePage7Address js file
 * 
 */

/**
 * for load the page to quit disply before
 */

window.onbeforeunload = function() {
	return "Are you sure?";
};

$(document).ready(function() {
	$('form').submit(function() {
		window.onbeforeunload = null;
	});
});
/**
 * for validate the form
 */

/**
 * for angular code here
 */
var app = angular.module("demoApp", ['ui.bootstrap']);


app.controller('DemoController', function ($scope) {
    init();
   
    $scope.movedIn11="1/1/1990";
   
   
   
    function init() {
    	
        $scope.newItemType = '';
        $scope.company="";
        $scope.date = '03/19/2013';
        var additionalApplicants = document.forms["myForm"]["additionalApplicants"].value;
        $scope.data=additionalApplicants;

        var totalMonth1322 = document.forms["myForm"]["totalMonth131"].value;
        if(totalMonth1322 !="")
        $scope.applicantTotal1=totalMonth1322;
       
        var totalMonth13222 = document.forms["myForm"]["totalMonth132"].value;
        if(totalMonth13222 !="")
        $scope.applicantTotal2=totalMonth13222;
       
        var totalMonth1333 = document.forms["myForm"]["totalMonth133"].value;
        if(totalMonth1333 !="")
        $scope.applicantTotal3=totalMonth1333;
      
        var coTotalMonth2311 = document.forms["myForm"]["coTotalMonth231"].value;
        if(coTotalMonth2311 !="")
        $scope.coApplicantTotal=coTotalMonth2311;
       
        var coTotalMonth2322 = document.forms["myForm"]["coTotalMonth232"].value;
        if(coTotalMonth2322 !="")
        $scope.coApplicantTotal2=coTotalMonth2322;
       
        var coTotalMonth2333 = document.forms["myForm"]["coTotalMonth233"].value;
        if(coTotalMonth2333 !="")
        $scope.coApplicantTotal3=coTotalMonth2333;
      
        $( "#datepicker" ).datepicker({
           
            changeMonth: true,
            changeYear: true,
            onSelect: function (date) {
                $scope.movedIn1 = date;
           
                 $scope.$apply(function($scope){
                     // Change binded variable
                     $scope.assign($scope, date);
                 });
                 
            }
           
          });
        
        $( "#datepicker1" ).datepicker({
            changeMonth: true,
            changeYear: true,
            onSelect: function (date) {
                $scope.movedIn2 = date;
           
                 $scope.$apply(function($scope){
                     // Change binded variable
                     $scope.assign($scope, date);
                 });
                 
            }
           
          });
        $( "#datepicker2" ).datepicker({
            changeMonth: true,
            changeYear: true,
            onSelect: function (date) {
                $scope.movedIn3 = date;
           
                 $scope.$apply(function($scope){
                     // Change binded variable
                     $scope.assign($scope, date);
                 });
                 
            }
           
          });
        
    
    }
         
    $scope.calculateDate = function ($scope) {
    	
    	        	
        $scope.employetotal=0;
        var one= $scope.movedIn1;
       
        var date =one.split("/");
        var oneDate=date[0]+"/1/"+date[2];
        var total=0;
       
        var today = new Date();
        var datevDate=new Date(oneDate);
       
        var one_day=1000*60*60*24;
        var date1ms=today.getTime();
        var date2ms=datevDate.getTime();
       
        var totmili=date1ms-date2ms;
        var totalDays=Math.round(totmili/one_day);
        var totalMonths=Math.abs((totmili/one_day)/30);
       
        total=Math.floor(totalMonths);
       
        $scope.applicantTotal1 = total;
                
       
        if(total <36){
            one= $scope.movedIn2;
            date =one.split("/");
            oneDate=date[0]+"/1/"+date[2];
            datevDate=new Date(oneDate);
              
                one_day=1000*60*60*24;
                date1ms=date2ms;
          
               date2ms=datevDate.getTime();
               totmili=date1ms-date2ms;
               if(isNaN(totmili) == false){
                    
                  totalDays=Math.round(totmili/one_day);
                  totalMonths=Math.abs((totmili/one_day)/30);
                total=total+Math.floor(totalMonths);
                $scope.applicantTotal2 = total;
           
            }
              
             one= $scope.movedIn3;
            date =one.split("/");
            oneDate=date[0]+"/1/"+date[2];
               datevDate=new Date(oneDate);
              
                one_day=1000*60*60*24;
                date1ms=date2ms;
          
               date2ms=datevDate.getTime();
               totmili=date1ms-date2ms;
               if(isNaN(totmili) == false){
                    
                  totalDays=Math.round(totmili/one_day);
                  totalMonths=Math.abs((totmili/one_day)/30);
                total=total+Math.floor(totalMonths);
                $scope.applicantTotal3 = total;
           
            }
              
        }
        /* document.getElementById("applicantTotal2").value=total; */
    }
   
    $scope.calculateDate2 = function ($scope) {
    	
    	$( "#datepicker3" ).datepicker({
            changeMonth: true,
            changeYear: true,
            onSelect: function (date) {
                $scope.CoMovedIn1 = date;
           
                 $scope.$apply(function($scope){
                     // Change binded variable
                     $scope.assign($scope, date);
                 });
                 
            }
           
          });
    	
    	$( "#datepicker4" ).datepicker({
            changeMonth: true,
            changeYear: true,
            onSelect: function (date) {
                $scope.CoMovedIn2 = date;
           
                 $scope.$apply(function($scope){
                     // Change binded variable
                     $scope.assign($scope, date);
                 });
                 
            }
           
          });
    	
    	$( "#datepicker5" ).datepicker({
            changeMonth: true,
            changeYear: true,
            onSelect: function (date) {
                $scope.CoMovedIn3 = date;
           
                 $scope.$apply(function($scope){
                     // Change binded variable
                     $scope.assign($scope, date);
                 });
                 
            }
           
          });
    	
        $scope.employetotal=0;
        var one= $scope.CoMovedIn1;

        var date =one.split("/");
        var oneDate=date[0]+"/1/"+date[2];
        var total=0;
        var today = new Date();
        var datevDate=new Date(oneDate);
       
        var one_day=1000*60*60*24;
        var date1ms=today.getTime();
        var date2ms=datevDate.getTime();
       
        var totmili=date1ms-date2ms;
        var totalDays=Math.round(totmili/one_day);
        var totalMonths=Math.abs((totmili/one_day)/30);
       
        total=Math.floor(totalMonths);
        $scope.coApplicantTotal = total;
       
        if(total <36){
            one= $scope.CoMovedIn2;
            date =one.split("/");
            oneDate=date[0]+"/1/"+date[2];
               datevDate=new Date(oneDate);
              
                one_day=1000*60*60*24;
                date1ms=date2ms;
          
               date2ms=datevDate.getTime();
               totmili=date1ms-date2ms;
               if(isNaN(totmili) == false){
                    
                  totalDays=Math.round(totmili/one_day);
                  totalMonths=Math.abs((totmili/one_day)/30);
                total=total+Math.floor(totalMonths);
                $scope.coApplicantTotal2 = total;
                }
               one= $scope.CoMovedIn3;
            date =one.split("/");
            oneDate=date[0]+"/1/"+date[2];
               datevDate=new Date(oneDate);
              
                one_day=1000*60*60*24;
                date1ms=date2ms;
          
               date2ms=datevDate.getTime();
               totmili=date1ms-date2ms;
               if(isNaN(totmili) == false){
                    
                  totalDays=Math.round(totmili/one_day);
                  totalMonths=Math.abs((totmili/one_day)/30);
                total=total+Math.floor(totalMonths);
                $scope.coApplicantTotal3 = total;
            }
              
        }
    }
   
    /* $scope.copyValue = function () {
        $scope.coCurrentAddress1 = $scope.currentAddress1;
        $scope.coAppAddress2 = $scope.applAddress2;
        $scope.coAppAddress3 = $scope.appAddress3;
        
    } */
           
});


/**
 * validate the form
 */



function validateForm(){
	   
    var currentAddress1 = document.forms["myForm"]["currentAddress1"].value;
        if(!currentAddress1){
                document.getElementById('input_2001').innerHTML="<span style='color:red'>*This field is Required.</span>";
                document.getElementById('geocomplete').focus();
                return false;
        }else{
        document.getElementById("input_2001").innerHTML="";
        }
       
        var movedIn1 = document.forms["myForm"]["movedIn1"].value;
        if(!movedIn1){
                document.getElementById('input_2002').innerHTML="<span style='color:red'>*This field is Required.</span>";
                document.getElementById('datepicker').focus();
                return false;
        }else{
        document.getElementById("input_2002").innerHTML="";
        }
        var totalcurrentmonths121 = document.forms["myForm"]["totalcurrentmonths"].value;
       
        if(totalcurrentmonths121<36){
            var currentAddress2 = document.forms["myForm"]["currentAddress2"].value;
            if(!currentAddress2){
                    document.getElementById('input_2003').innerHTML="<span style='color:red'>*This field is Required.</span>";
                    document.getElementById('geocomplete1').focus();
                    return false;
            }else{
            document.getElementById("input_2003").innerHTML="";
            }   
           
            var movedIn2 = document.forms["myForm"]["movedIn2"].value;
            if(!movedIn2){
                    document.getElementById('input_2004').innerHTML="<span style='color:red'>*This field is Required.</span>";
                    document.getElementById('datepicker1').focus();
                    return false;
            }else{
            document.getElementById("input_2004").innerHTML="";
            }
           
            var totalpriormonths112 = document.forms["myForm"]["totalpriormonths1"].value;
           
            if(totalpriormonths112<36){
                var currentAddress3 = document.forms["myForm"]["currentAddress3"].value;   
                if(!currentAddress3){
                        document.getElementById('input_2005').innerHTML="<span style='color:red'>*This field is Required.</span>";
                        document.getElementById('geocomplete2').focus();
                        return false;
                }else{
                document.getElementById("input_2005").innerHTML="";
                }
           
                var movedIn3 = document.forms["myForm"]["movedIn3"].value;
                if(!movedIn3){
                        document.getElementById('input_2006').innerHTML="<span style='color:red'>*This field is Required.</span>";
                        document.getElementById('datepicker2').focus();
                        return false;
                }else{
                document.getElementById("input_2006").innerHTML="";
                }   
            }
        }
         var additionalApplicants1121 = document.forms["myForm"]["additionalApplicants"].value;
       

         if(additionalApplicants1121 == 'yes'){
            var CoCurrentAddress1 = document.forms["myForm"]["CoCurrentAddress1"].value;
            if(!CoCurrentAddress1){
                    document.getElementById('input_2021').innerHTML="<span style='color:red'>*This field is Required.</span>";
                    document.getElementById('geocomplete3').focus();
                    return false;
            }else{
            document.getElementById("input_2021").innerHTML="";
            }
           
            var CoMovedIn1 = document.forms["myForm"]["CoMovedIn1"].value;
            if(!CoMovedIn1){
                    document.getElementById('input_2022').innerHTML="<span style='color:red'>*This field is Required.</span>";
                    document.getElementById('datepicker3').focus();
                    return false;
            }else{
            document.getElementById("input_2022").innerHTML="";
            }
           
            var coAppTotalcurrentMonths121 = document.forms["myForm"]["coAppTotalcurrentMonths"].value;
            if(coAppTotalcurrentMonths121<36){
                var CoCurrentAddress2 = document.forms["myForm"]["CoCurrentAddress2"].value;
                if(!CoCurrentAddress2){
                        document.getElementById('input_2023').innerHTML="<span style='color:red'>*This field is Required.</span>";
                        document.getElementById('geocomplete4').focus();
                        return false;
                }else{
                document.getElementById("input_2023").innerHTML="";
                }
               
                var CoMovedIn2 = document.forms["myForm"]["CoMovedIn2"].value;
                if(!CoMovedIn2){
                        document.getElementById('input_2024').innerHTML="<span style='color:red'>*This field is Required.</span>";
                        document.getElementById('datepicker4').focus();
                        return false;
                }else{
                document.getElementById("input_2024").innerHTML="";
                }
               
                var coAppTotalpriorcurrentmonths121 = document.forms["myForm"]["coAppTotalpriorcurrentmonths1"].value;
                if(coAppTotalpriorcurrentmonths121<36){
                    var CoCurrentAddress4 = document.forms["myForm"]["CoCurrentAddress4"].value;
                    if(!CoCurrentAddress4){
                            document.getElementById('input_2025').innerHTML="<span style='color:red'>*This field is Required.</span>";
                            document.getElementById('geocomplete5').focus();
                            return false;
                    }else{
                    document.getElementById("input_2025").innerHTML="";
                    }
                   
                    var CoMovedIn3 = document.forms["myForm"]["CoMovedIn3"].value;
                    if(!CoMovedIn3){
                            document.getElementById('input_2026').innerHTML="<span style='color:red'>*This field is Required.</span>";
                            document.getElementById('datepicker5').focus();
                            return false;
                    }else{
                    document.getElementById("input_2026").innerHTML="";
                    }
                }
            }
        }
       
        return true;   
    }

/**
 * for selet function
 */


window.onload = function() {
    SelectElement();
};
function SelectElement() {  
    var currentAddress  = document.forms["myForm"]["currentAddress1212"].value;
    document.forms["myForm"]["currentAddress1"].value = currentAddress;   
    var moveIn1212  = document.forms["myForm"]["moveIn1212"].value; 
    document.forms["myForm"]["movedIn1"].value = moveIn1212;
   
    var currentAddress21212  = document.forms["myForm"]["currentAddress2121"].value; 
    document.forms["myForm"]["currentAddress2"].value = currentAddress21212;
    var moveIn21212  = document.forms["myForm"]["moveIn2121"].value; 
    document.forms["myForm"]["movedIn2"].value = moveIn21212;
   
    var currentAddress313  = document.forms["myForm"]["currentAddress3131"].value; 
    document.forms["myForm"]["currentAddress3"].value = currentAddress313;
    var moveIn313132  = document.forms["myForm"]["moveIn3131"].value; 
    document.forms["myForm"]["movedIn3"].value = moveIn313132;
  
    var coCurrentAddress4311  = document.forms["myForm"]["coCurrentAddress431"].value; 
    document.forms["myForm"]["CoCurrentAddress1"].value = coCurrentAddress4311;
    var coMoveIn4322  = document.forms["myForm"]["coMoveIn432"].value; 
    document.forms["myForm"]["CoMovedIn1"].value = coMoveIn4322;
   
    var coCurrentAddress4333  = document.forms["myForm"]["coCurrentAddress433"].value; 
    document.forms["myForm"]["CoCurrentAddress2"].value = coCurrentAddress4333;
    var coMoveIn4344  = document.forms["myForm"]["coMoveIn434"].value; 
    document.forms["myForm"]["CoMovedIn2"].value = coMoveIn4344;
   
    var coCurrentAddress4355  = document.forms["myForm"]["coCurrentAddress435"].value; 
    document.forms["myForm"]["CoCurrentAddress4"].value = coCurrentAddress4355;
    var coMoveIn4366  = document.forms["myForm"]["coMoveIn436"].value; 
    document.forms["myForm"]["CoMovedIn3"].value = coMoveIn4366;
}

/**
 * geocomplete function code here for every field
 */


$(function(){
    $("#geocomplete").geocomplete({
          types:["geocode", "establishment"]
      })
      .bind("geocode:result", function (event, result) {
          //$.log("Result: " + result.formatted_address);
          //alert("Result: " + result.formatted_address);
          document.getElementById("geocomplete").value = result.formatted_address;
          //copying value of geocomplete id to geocomplete3
          //document.getElementById("geocomplete3").value = result.formatted_address;
      })
      .bind("geocode:error", function (event, status) {
         // alert("ERROR: " + status);
      })
      .bind("geocode:multiple", function (event, results) {
         //alert("Multiple: " + results.length + " results found");
      });
});



$(function(){
    $("#geocomplete1").geocomplete({
          types:["geocode", "establishment"]
      })
      .bind("geocode:result", function (event, result) {
          //$.log("Result: " + result.formatted_address);
          //alert("Result: " + result.formatted_address);
          document.getElementById("geocomplete1").value = result.formatted_address;
      })
      .bind("geocode:error", function (event, status) {
          alert("ERROR: " + status);
      })
      .bind("geocode:multiple", function (event, results) {
         alert("Multiple: " + results.length + " results found");
      });
     
  });




$(function(){
    $("#geocomplete2").geocomplete({
          types:["geocode", "establishment"]
      })
      .bind("geocode:result", function (event, result) {
          //$.log("Result: " + result.formatted_address);
          //alert("Result: " + result.formatted_address);
          document.getElementById("geocomplete2").value = result.formatted_address;
      })
      .bind("geocode:error", function (event, status) {
          //alert("ERROR: " + status);
      })
      .bind("geocode:multiple", function (event, results) {
         //alert("Multiple: " + results.length + " results found");
      }); 
});



$(function(){
    $("#geocomplete3").geocomplete({
          types:["geocode", "establishment"]
      })
      .bind("geocode:result", function (event, result) {
          //$.log("Result: " + result.formatted_address);
        //  alert("Result: " + result.formatted_address);
        document.getElementById("geocomplete3").value = result.formatted_address;
      })
      .bind("geocode:error", function (event, status) {
          //alert("ERROR: " + status);
      })
      .bind("geocode:multiple", function (event, results) {
         //alert("Multiple: " + results.length + " results found");
      });
  });


$(function(){
    $("#geocomplete4").geocomplete({
          types:["geocode", "establishment"]
      })
      .bind("geocode:result", function (event, result) {
          //$.log("Result: " + result.formatted_address);
          //alert("Result: " + result.formatted_address);
          document.getElementById("geocomplete4").value = result.formatted_address;
      })
      .bind("geocode:error", function (event, status) {
          //alert("ERROR: " + status);
      })
      .bind("geocode:multiple", function (event, results) {
         //alert("Multiple: " + results.length + " results found");
      });
   
  });

$(function(){
    $("#geocomplete5").geocomplete({
          types:["geocode", "establishment"]
      })
      .bind("geocode:result", function (event, result) {
          //$.log("Result: " + result.formatted_address);
          //alert("Result: " + result.formatted_address);
          document.getElementById("geocomplete5").value = result.formatted_address;
      })
      .bind("geocode:error", function (event, status) {
          alert("ERROR: " + status);
      })
      .bind("geocode:multiple", function (event, results) {
         alert("Multiple: " + results.length + " results found");
      }); 
  });

/**
 * for data-toggle 
 */

$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();  
});