$("#showSessions").click(function () {
	if($(this).text() == 'Anzeigen')
		$(this).text('Ausblenden')
	else
		$(this).text('Anzeigen')
	$("#events").fadeToggle();
})
				$('#duration').on('change', function() {
					h = parseInt($(this).val())/60;
					h = (Math.round(h * 2) / 2).toFixed(1);
					if(h == 0)
						h = 0.5;
					$("#vps").val(h);
				});
				
$('#searchStudies').on("click", function() {
	$("#search").show()
})


$('.expRem').on("click", function() {
	$(this).parent().parent().fadeOut(500, function() {
		$(this).remove();
	})
})
				events = new Array();
				
//$('#calendar').fullCalendar('render');
function listEvents() {
	events = $('#calendar').fullCalendar( 'clientEvents');
	$("#amount").text(events.length)
	$("#events").children().remove();
	$.each(events, function (key, value) {
		e = $("#event").clone();
		date = value.start.format("dddd, Do MMMM YYYY");
		time = value.start.format("H:mm")+"-"+value.end.format("H:mm");
		e.find("#date").text(date);
		e.find("#time").text(time);
		e.prop("id",value._id);
		$("#events").append(e);
		e.show();
	});

	$(".eventdel").on("click", function() {
		id = $(this).parent().parent().prop('id');
		$(this).parent().parent().fadeOut();
		$("#amount").text(parseInt($("#amount").text())-1)
		$('#calendar').fullCalendar('removeEvents',id);
		$('#calendar').fullCalendar('rerenderEvents');
	})
};			
$('#sessionCalendar').on('shown.bs.modal', function () {
	$('#calendar').fullCalendar('render');
	$(".fc-view-agendaWeek").css('overflow','visible');
	$("div").each(function () {
		if($(this).css('overflow-x') == 'hidden') {
			$(this).css('overflow-x','visible')
			$(this).css('overflow-y','visible')
		}

	})
});
$('#modalAddPersons').on('shown.bs.modal', function () {
	$('#addExp').click(function () {
		$(this).attr('disabled',true);
		$('.removeExp').show()
	})
	$('#expSearch').on('keypress', function () {
	if($('#expSearch').val().length == 1) {
		$('#exp').show();
		
		}
	})
});


$('#sessionCalendar').on('hidden.bs.modal', function() {
	listEvents();
});



	$(document).ready(function(){

	$('.form-horizontal').validate(
	{
		rules: {
			name: {
				minlength: 6,
				required: true
			},
			description: {
				minlength: 6,
				required: true,
			},
			duration: {
				required: true,
				digits:true,
				range: [15,600]
			}
		},
		highlight: function(element) {
			$(element).closest('.form-group').addClass('has-error');
		},
		unhighlight: function(element) {
			$(element).closest('.form-group').removeClass('has-error');

		}
	});
});
	$("#modalAddPersons").on("shown.bs.modal", function() {
		$("#expSearch").on('keyup',function() {
    		if ($(this).val().length >= 3) {
    			myJsRoutes.controllers.Experimenter.userSearch($(this).val(), "exp").ajax({
    				success : function(data) {
    					$("#resultsExp").html(data);
    					$(".addExp").on('click', function() {
    						click = $(this);
    						show = true;
    						console.log("addExp geklickt. gehe alle expRow durch...")
   							$(".expRow").find("button").each(function() {
   								console.log('vergleiche '+$(this).attr('id'));
			    				console.log('mit :'+click.attr('id'));
   								if($(this).attr('id') == click.attr('id')) {
   									show = false;
   									console.log('IDs sind gleich. Nicht anzeigen.');
   								}
   							});
							if(show) {
	  	    					row = $(".mt1em").last().clone();
	  	    					row.attr('id','new')
	    					    row.find("#name").text(click.data('user-name'));
	    					    row.find("input[type=hidden]").val(click.attr('id'))
	    					    row.find("input[type=radio]").attr('name', 'assignment_rights_'+click.data('user-id'));
	    					    row.find("#expRem").attr("id",click.attr("id"));
	 						    $("#rowsExp").append(row);
	 						    row.show();
	 						   $('.expRem').on("click", function() {
	 								$(this).parent().parent().fadeOut(function() {
	 									$(this).remove();
	 								})
	 							})
	       						console.log(row);
	 						    return false;
 							}
	    					
    						
    					})
    				},
    			})
    		}
    	});
	});
	
	$(document).on('ready', function () {
	
	/**
	Funktion zeigt nur die Räume zum aktuell ausgewählten Gebäude
	**/
	function selectRooms() {
		console.log('selectRooms');
		$(".buildingSelects").hide();
		id = $("#buildings option:selected").val();
		console.log('zeige id-'+id);
		$("#id-"+id).show();
	};
	
	/**
	Falls Gebäude geändert wird, rufe selectRooms() auf
	**/
	$("#buildings").on('change', function() {
		selectRooms();
	});
	
	/**
	Rufe beim Laden selectRooms() auf, damit für vorausgewähltes Gebäude Räume ausgwählt werden
	**/
	selectRooms();
	
	/**
	Blendet die Ortsinformationen bei Internetstudien aus
	**/
	$('.expType #1').click(function() {
  		 if($('.expType #0').is(':checked')) { 
   
   			$(".locationRadio").hide(); 
   			
   			}
		});

	
	$('.expType #0').click(function(){
		
		if($(".expType #1").is(":checked")){
 			 
 			 $(".locationRadio").show();
 			 
 		 }
		
	});
		})
		
		// Session-Dauer aus Eingabefeld
		dur = $("#duration option:selected").text();

function saveSessions() {
	$("#progress_sessions").html('...');
	events = $('#calendar').fullCalendar( 'clientEvents');
	eventsJson = []
	$.each(events, function (key, value) {
		datetime = value.start.format("YYYY-MM-DD HH:mm");
		e = {
				'datetime': datetime,
				'participations' : value.participations
		}
		eventsJson.push(e)
	});
	fin = {
			'room' : $(".buildingSelects:visible option:selected").val(),
			'events': eventsJson
	}
	myJsRoutes.controllers.Experimenter.saveSessions(experiment_id).ajax({
		method: 'post',
		data: JSON.stringify(fin),
		dataType: 'json',
        contentType: "application/json; charset=utf-8",
		success: function(data) {
			window.location = "/myStudies";
		}
	});
}

$("#save").click(function() {
	$(this).prop('disabled',true)
	/**
	Allgemeine Informationen speichern
	**/
	myJsRoutes.controllers.Experimenter.saveGeneralData(experiment_id).ajax({
		method: 'post',
		data: $(".form-horizontal").serialize(),
		success : function(data) {
			$("#progress_general").html('Erledigt.');
			saveSessions();
		}
		
	});
});	