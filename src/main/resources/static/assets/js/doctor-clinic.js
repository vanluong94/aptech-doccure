const clinicMapCont = document.getElementById("clinic_location_map");
let clinicMap, clinicInfoWindow, clinicMarker, clinicMapCenter = { lat: -34.401133113281986, lng: 150.87797718048094 };
let clinicInputLat = document.getElementById('clinicInputLat'), 
	clinicInputLong = document.getElementById('clinicInputLong')

function initMap() {

    let clinicHasLocated = false;

    if (clinicInputLat.value && clinicInputLong.value) {
        clinicMapCenter = {
            lat: parseFloat(clinicInputLat.value),
            lng: parseFloat(clinicInputLong.value)
        };
        clinicHasLocated = true;
    }

	clinicMap = new google.maps.Map(clinicMapCont, {
		center: clinicMapCenter,
		zoom: clinicHasLocated ? 16 : 14,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	});
    
	clinicInfoWindow = new google.maps.InfoWindow();

    if (!clinicHasLocated) {
        locateCurrentPosition();
    }

	initMarker();
}

function initMarker() {
	clinicMarker = new google.maps.Marker({
		position: clinicMap.getCenter(),
		draggable: true
	});
	
	google.maps.event.addListener(clinicMarker, 'dragend', function (evt) {
		console.log('Marker dropped: Current Lat: ' + evt.latLng.lat().toFixed(3) + ' Current Lng: ' + evt.latLng.lng().toFixed(3));
	});
	
	google.maps.event.addListener(clinicMarker, 'dragstart', function (evt) {
		console.log('Currently dragging marker...')
	});

	google.maps.event.addListener(clinicMarker, 'position_changed', function(evt) {
		clinicInputLat.value = clinicMarker.getPosition().lat();
		clinicInputLong.value = clinicMarker.getPosition().lng();
	});
	
	// clinicMap.setCenter(clinicMarker.position);
	clinicMarker.setMap(clinicMap);
}

function locateCurrentPosition() {
	if (navigator.geolocation) {
		addLoadingOverlay(clinicMapCont);
		navigator.geolocation.getCurrentPosition(
			(position) => {
				const pos = {
					lat: position.coords.latitude,
					lng: position.coords.longitude,
				};
				
				// clinicInfoWindow.setPosition(pos);
				// clinicInfoWindow.setContent("Location found.");
				// clinicInfoWindow.open(clinicMap);
				clinicMap.setCenter(pos);
				clinicMap.setZoom(16);
				clinicMarker.setPosition(pos);
				removeLoadingOverlay(clinicMapCont);
			},
			() => {
				removeLoadingOverlay(clinicMapCont);
				handleLocationError(true, clinicInfoWindow, clinicMap.getCenter());
			}
		);
	} else {
		// Browser doesn't support Geolocation
		handleLocationError(false, clinicInfoWindow, clinicMap.getCenter());
	}
}

function handleLocationError(browserHasGeolocation) {
	alert(
		browserHasGeolocation
		? "Error: The Geolocation service failed."
		: "Error: Your browser doesn't support geolocation."
	)
	// infoWindow.setPosition(pos);
	// infoWindow.setContent(
	// 	browserHasGeolocation
	// 	? "Error: The Geolocation service failed."
	// 	: "Error: Your browser doesn't support geolocation."
	// );
	// infoWindow.open(map);
}

window.initMap = initMap;