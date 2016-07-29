/**
 * MortgagePage5b js file
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
 * Form validation page6a
 */
function validateForm() {
	
	var applWorkPhone = document.forms["myForm"]["applWorkPhone"].value;
	if (!applWorkPhone) {
		document.getElementById('input_4002').innerHTML = "<span style='color:red'>*This field is Required.</span>";
		document.getElementById('phonedatata2').focus();
		return false;
	} else {
		document.getElementById("input_4002").innerHTML = "";
	}
	
	var applBirthday = document.forms["myForm"]["applBirthday"].value;
	if (!applBirthday) {
		document.getElementById('input_4004').innerHTML = "<span style='color:red'>*This field is Required.</span>";
		document.getElementById('datepicker').focus();
		return false;
	} else {
		document.getElementById("input_4004").innerHTML = "";
	}
	
	var applInsurNum = document.forms["myForm"]["applInsurNum"].value;
	if (!applInsurNum) {
		document.getElementById('input_4005').innerHTML = "<span style='color:red'>*This field is Required.</span>";
		document.getElementById('phonedatata7').focus();
		return false;
	} else {
		document.getElementById("input_4005").innerHTML = "";
	}
	/* 
	var additionalApplicants = document.forms["myForm"]["additionalApplicants"].value;
	if (!additionalApplicants) {
		document.getElementById('input_4008').innerHTML = "<span style='color:red'>*This field is Required.</span>";

		return false;
	} else {
		document.getElementById("input_4008").innerHTML = "";
	}
	 */
	
	return true;
}



/**
 * angular code here
 */



var app = angular.module("demoApp", ['ui.bootstrap']);

app.controller('DemoController', function ($scope) {
    init();
    function init() {
    	
        $scope.newItemType = '';
        $scope.company="";
        $scope.change = function () {
            console.log($scope.newItemType)
        };
    }
    $scope.singleModel = 1;
	var areYouCanadian1 = document.forms["myForm"]["areYouCanadian"].value;
    if(areYouCanadian1 !="" && areYouCanadian1 != null)
	$scope.additionalApplicant = areYouCanadian1;
    
	var areMovedIn1 = document.forms["myForm"]["areMovedIn"].value;
    if(areMovedIn1 !="" && areMovedIn1 != null)
	$scope.movedCanada = areMovedIn1;
});

/**
 * for select function 
 */


window.onload = function() {
	SelectElement();
};
function SelectElement() {   
  
    var dependant1 = document.forms["myForm"]["dependantss"].value;  
   
    document.forms["myForm"]["applDependants"].value = dependant1;
}

/**
 * for Mask Number
 */


$(function() {
    $.mask.definitions['~'] = "[+-]";

    $("#phonedatata2").mask("999-999-9999");
});

$(function() {
    $.mask.definitions['~'] = "[+-]";

    $("#phonedatata7").mask("999-999-999");
});
/**
 * for data toggle
 */

$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});

/**
 * for datepicker
 */

$(function() {
    $( "#datepicker" ).datepicker({
    	yearRange: "-100:-18",
        maxDate: "-18Y", 
        minDate: "-100Y",
      changeMonth: true,
      changeYear: true,
      defaultDate: new Date().getDate() - 365*30+7	
      
    });
  });

/**
 * on change  background color of select drop down field
 */

function changeBackgroundOfSelectedField() {
	var appRelStatus = document.forms["myForm"]["applDependants"].value;
	if (!appRelStatus) {
		document.getElementById('applDependantsID').style.backgroundColor = "#ffffff";
	}else{
		document.getElementById('applDependantsID').style.backgroundColor = "#33cc33";
	}
}