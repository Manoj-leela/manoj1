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
 * Form Validation of page6
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
		document.getElementById('input_4005').innerHTML = "<span style='color:red;'>*Social Insurance number is required to verify credit history. If you do not have a Social Insurance Number, please call Visdom at 1-855-699-2400.</span>";

		document.getElementById('phonedatata7').focus();
		return false;
	} else {
		document.getElementById("input_4005").innerHTML = "";
	}

	var applDependants = document.forms["myForm"]["applDependants"].value;

	if (!applDependants) {
		document.getElementById('input_4007').innerHTML = "<span style='color:red'>*This field is Required.</span>";
		document.getElementById('dependentId').focus();
		return false;
	} else {
		document.getElementById("input_4007").innerHTML = "";
	}

	/*
	 * var areYouCanadianRess =
	 * document.forms["myForm"]["areYouCanadianRess"].value; if
	 * (!areYouCanadianRess) { document.getElementById('input_4008').innerHTML = "<span
	 * style='color:red'>*This field is Required.</span>";
	 * document.getElementById('input_4008').focus(); return false; } else {
	 * document.getElementById("input_4008").innerHTML = ""; }
	 * 
	 * var movedCanadas = document.forms["myForm"]["movedCanadas"].value; if
	 * (!movedCanadas) { document.getElementById('input_4009').innerHTML = "<span
	 * style='color:red'>*This field is Required.</span>";
	 * document.getElementById('input_4009').focus(); return false; } else {
	 * document.getElementById("input_4009").innerHTML = ""; }
	 */

	var additionalAppli12 = document.forms["myForm"]["additionalApplicants"].value;

	if (additionalAppli12 == 'yes') {

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
			document.getElementById('datepicker1').focus();
			return false;
		} else {
			document.getElementById("input_4103").innerHTML = "";
		}

		var coApplInsurNum = document.forms["myForm"]["coApplInsurNum"].value;
		if (!coApplInsurNum) {
			document.getElementById('input_4005').innerHTML = "<span style='color:red'>*Social Insurance number is required to verify credit history. If you do not have a Social Insurance Number, please call Visdom at 1-855-699-2400.</span>";
			document.getElementById('phonedatata8').focus();
			return false;
		} else {
			document.getElementById("input_4005").innerHTML = "";
		}

		var coAppDependants = document.forms["myForm"]["coAppDependants"].value;
		if (!coAppDependants) {
			document.getElementById('input_4106').innerHTML = "<span style='color:red'>*This field is Required.</span>";
			document.getElementById('coDependentId').focus();
			return false;
		} else {
			document.getElementById("input_4106").innerHTML = "";
		}

		/*
		 * var coApplicantss = document.forms["myForm"]["coApplicantss"].value;
		 * if (!coApplicantss) { document.getElementById('input_4107').innerHTML = "<span
		 * style='color:red'>*This field is Required.</span>";
		 * document.getElementById('input_4107').focus(); return false; } else {
		 * document.getElementById("input_4107").innerHTML = ""; }
		 * 
		 * var coAppMovedCanadae =
		 * document.forms["myForm"]["coAppMovedCanadae"].value; if
		 * (!coAppMovedCanadae) {
		 * document.getElementById('input_4108').innerHTML = "<span
		 * style='color:red'>*This field is Required.</span>";
		 * document.getElementById('input_4108').focus(); return false; } else {
		 * document.getElementById("input_4108").innerHTML = ""; }
		 */
	}
}

/**
 * for angular code
 */

var app = angular.module("demoApp", [ 'ui.bootstrap' ]);

app
		.controller(
				'DemoController',
				function($scope) {
					init();
					function init() {

						$scope.newItemType = '';
						$scope.company = "";
						var additionalApplicants = document.forms["myForm"]["additionalApplicants"].value;
						$scope.data = additionalApplicants;
						$scope.change = function() {
							console.log($scope.newItemType)
						};
					}

					$scope.singleModel = 1;
					var applicantNonResident333 = document.forms["myForm"]["applicantNonResident33"].value;
					if (applicantNonResident333 != null
							&& applicantNonResident333 != "")
						$scope.areYouCanadianRes = applicantNonResident333;

					var applicantMovedCanada333 = document.forms["myForm"]["applicantMovedCanada33"].value;
					if (applicantMovedCanada333 != null
							&& applicantMovedCanada333 != "")
						$scope.movedCanada = applicantMovedCanada333;

					var coApplicantNonResident333 = document.forms["myForm"]["coApplicantNonResident33"].value;
					if (coApplicantNonResident333 != null
							&& coApplicantNonResident333 != "")
						$scope.coApplicants = coApplicantNonResident333;

					var coApplicantMovedCanada333 = document.forms["myForm"]["coApplicantMovedCanada33"].value;
					if (coApplicantMovedCanada333 != null
							&& coApplicantMovedCanada333 != "")
						$scope.coAppMovedCanada = coApplicantMovedCanada333;

				});

/**
 * for load the select function
 */

window.onload = function() {
	SelectElement();
};
function SelectElement() {   
    
    var applicantDependant  = document.forms["myForm"]["applicantDependant11"].value;  
    document.forms["myForm"]["applDependants"].value = applicantDependant;
    
    var coApplicantDependant  = document.forms["myForm"]["coApplicantDependant11"].value;  
    document.forms["myForm"]["coAppDependants"].value = coApplicantDependant;
    
    /* var applicantNonResident  = document.forms["myForm"]["applicantNonResident11"].value;  
    document.forms["myForm"]["garageSize"].value = applicantNonResident;
    
    var applicantMovedCanada  = document.forms["myForm"]["applicantMovedCanada11"].value;  
    document.forms["myForm"]["garageSize"].value = applicantMovedCanada; */
}
/**
 * Mask Function for number
 */

$(function() {
    $.mask.definitions['~'] = "[+-]";

    $("#phonedatata2").mask("999-999-9999");
});

$(function() {
    $.mask.definitions['~'] = "[+-]";

    $("#phonedatata5").mask("999-999-9999");
});

$(function() {
    $.mask.definitions['~'] = "[+-]";

    $("#phonedatata7").mask("999-999-999");
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
 * for datepicker 1
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
 * for datepicker 2
 */

$(function() {
    $( "#datepicker1" ).datepicker({
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
		document.getElementById('dependentId').style.backgroundColor = "#ffffff";
	}else{
		document.getElementById('dependentId').style.backgroundColor = "#33cc33";
	}
	
	var appRelStatus = document.forms["myForm"]["coAppDependants"].value;
	if (!appRelStatus) {
		document.getElementById('coDependentId').style.backgroundColor = "#ffffff";
	}else{
		document.getElementById('coDependentId').style.backgroundColor = "#33cc33";
	}
}
