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

$
$('.expRem').on("click", function() {
	$(this).parent().parent().fadeOut();
})
				events = new Array();
				
$('#calendar').fullCalendar('render');
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