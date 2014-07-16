$(document).ready(function(){
    	
    	var myCenter=new google.maps.LatLng(49.785210, 9.976927);
    	var marker=new google.maps.Marker({
    	position:myCenter
    	});
    	
    	var map=    new google.maps.Map(
                 document.getElementById('map-canvas'), 
                  { zoom: 18,
                    center: myCenter,
                    mapTypeId: google.maps.MapTypeId.HYBRID });
    	
    	marker.setMap(map);
    
    	var contentString = '<div id="contentProbandMap">'+
    	'<div id="siteNotice">'+
    	'</div>'+
    	'<h2 id="firstHeading" class="firstHeading">Gebäude 82</h2>'+
    	'<div id="bodyContent">'+
    	'<p><b>Raum: 00.120</b> befindet sich im ersten Stock'+
    	'</br>des Gebäudes 82. Sie durchqueren den Flur </br>im ersten Stock'+
    	' und gehen durch die Glastür.</br> Der Raum befindet sich '+
    	'auf der rechten Seite.</p>'

    	'</div>'+
    	'</div>';

    	var infowindow = new google.maps.InfoWindow({
    	    content: contentString
    	});
    
    $('#myMapModal').on('shown.bs.modal', function() {
    	resizeShit();
    	});
    	
    function resizeShit(){	
    var center = map.getCenter();
    google.maps.event.trigger(map, "resize");
    map.setCenter(center); 
	
	
	
	//var infowindow = new google.maps.InfoWindow({
	//  content: contentString
	//});

	google.maps.event.addListener(marker, 'click', function() {
	  infowindow.open(map,marker);
	  });
	
	
	infowindow.open(map,marker);
    };
    	
    	
    });
