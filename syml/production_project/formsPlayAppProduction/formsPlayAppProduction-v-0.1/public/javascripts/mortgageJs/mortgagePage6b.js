/**
 * MortgagePage6b js file
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
function validateForm() {
	
	var coApplWorkPhone = document.forms["myForm"]["coApplWorkPhone"].value;
	if (!coApplWorkPhone) {
		document.getElementById('input_4101').innerHTML = "<span style='color:red'>*This field is Required.</span>";
		document.getElementById('phonedatata5').focus();
		return false;
	} else {
		document.getElementById("input_4101").innerHTML = "";
	}
				
	var coApplBirthday = document.forms["myForm"]["coApplBirthday"].value;
	if (!coApplBirthday) {
		document.getElementById('input_4103').innerHTML = "<span style='color:red'>*This field is Required.</span>";
		document.getElementById('datepicker').focus();
		return false;
	} else {
		document.getElementById("input_4103").innerHTML = "";
	}
	
	var coApplInsurNum = document.forms["myForm"]["coApplInsurNum"].value;
	if (!coApplInsurNum) {
		document.getElementById('input_4104').innerHTML = "<span style='color:red'>*This field is Required.</span>";
		document.getElementById('phonedatata8').focus();
		return false;
	} else {
		document.getElementById("input_4104").innerHTML = "";
	}
				
	/* var coApplicantss = document.forms["myForm"]["coApplicantss"].value;
	if (!coApplicantss) {
		document.getElementById('input_4107').innerHTML = "<span style='color:red'>*This field is Required.</span>";

		return false;
	} else {
		document.getElementById("input_4107").innerHTML = "";
	} */
	
	
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
	var coAreUCanadianRes1 = document.forms["myForm"]["coAreUCanadianRes"].value;
    if(coAreUCanadianRes1 !="" && coAreUCanadianRes1 != null)
	$scope.coApplicants = coAreUCanadianRes1;
    
	var coMovedCanada1 = document.forms["myForm"]["coMovedCanada"].value;
    if(coMovedCanada1 !="" && coMovedCanada1 != null)
	$scope.coAppMovedCanada = coMovedCanada1;
    
});

/**
 * for select the Element from SelectElement Function
 */
window.onload = function() {
	SelectElement();
};
function SelectElement() {   
   
    var coDependents1 = document.forms["myForm"]["coDependents"].value;  
   
    document.forms["myForm"]["coAppDependants"].value = coDependents1;
}
/**
 * for Mask number 
 */

$(function() {
    $.mask.definitions['~'] = "[+-]";

    $("#phonedatata5").mask("999-999-9999");
});

$(function() {
    $.mask.definitions['~'] = "[+-]";

    $("#phonedatata8").mask("999-999-999");
});

/**
 * for data-toggle
 */

$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});



/**
 * date picker code here
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
 * on change  bacjground color
 */

function changeBackgroundOfSelectedField() {
	var appRelStatus = document.forms["myForm"]["coAppDependants"].value;
	if (!appRelStatus) {
		document.getElementById('coAppDependantsID').style.backgroundColor = "#ffffff";
	}else{
		document.getElementById('coAppDependantsID').style.backgroundColor = "#33cc33";
	}
}
