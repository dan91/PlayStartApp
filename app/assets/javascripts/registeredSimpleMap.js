<script>
$(document).ready(function(){
	
	var map;	
	var center;
	var marker;
	var infowindow;
	
    var building_name;
    var building_description;
    var room_name;
    var room_description;
    var lat;
    var lng;
    	
    $(".mapButton").click(function(e) {
 	   e.preventDefault();
 	   
	   var statusCode = $(this).data('status');	
	   var felder = statusCode.split(';', 6);
	   
	  	alert(building_name = felder[0]);
    	alert(building_description = felder[1]);
		alert(room_name = felder[2]);
	   alert(room_description = felder[3]);
	   alert(lat = felder[4]);
	   alert(lng = felder[5]);
	   
	   
	   
	   $("#modalHead").html(building_name+", "+room_name);
	   
	   infowindow = new google.maps.InfoWindow({
   	    content: '<div id="infodiv"><h2 id="firstHeading" class="firstHeading">'+building_name+'</h2>'
   	    +'<div id="bodyContent">'+room_description+'</div></div>'
   	    
   		});
	   
	   $('#placeModal').on('shown.bs.modal', function() {
		   center=new google.maps.LatLng(lat, lng);
	       
		   
	    	
	    	map=    new google.maps.Map(
	                 document.getElementById('map-canvas'), 
	                  { zoom: 17,
	                    center: center,
	                    mapTypeId: google.maps.MapTypeId.HYBRID });
	    	
	    	
	    
		   
	    	google.maps.event.trigger(map, "resize");
		    map.setCenter(center); 
		    marker=new google.maps.Marker({
		    	   position:center
		    	});
		    marker.setMap(map);
		    
			google.maps.event.addListener(marker, 'click', function() {
			  infowindow.open(map,marker);
			  });
			
				infowindow.open(map,marker);
	    	});
	    	
	   
    });
    	
    
    infowindow.open(map,marker);
    	
});


</script>