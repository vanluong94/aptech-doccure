/*
Author       : Dreamguys
Template Name: Doccure - Bootstrap Template
Version      : 1.3
*/

google.maps.visualRefresh = true;
const icons = {
	'default': 'assets/img/marker.png'
};
const resultsGrid = document.getElementById('resultsGrid');
const resultsCount = document.getElementById('resultsCount');
const resultsPanel = document.getElementById('resultsPanel');
let locatedMarkerIds = [];
let resultMarkers = [];
let infowindow = null;
let searchMap;

function initialize() {
	var center = new google.maps.LatLng(53.470692, -2.220328);
	var mapOptions = {
		zoom: 14,
		center: center,
		scrollwheel: false,
		mapTypeId: google.maps.MapTypeId.ROADMAP,
	};

	addLoadingOverlay(resultsPanel);

	searchMap = new google.maps.Map(document.getElementById('map'), mapOptions);
	// searchMap.slide = true;
	locateCurrentPosition();
	
	infowindow = new google.maps.InfoWindow();
	google.maps.event.addListener(infowindow, 'closeclick', function () {
		infowindow.close();
	});
	google.maps.event.addListener(searchMap, 'idle', function () {
		// console.log('idle');
	});

	// var oldcenter = new google.maps.LatLng(0,0);
	google.maps.event.addListener(searchMap, 'idle', function() { 
		var newcenter = searchMap.getCenter();
		if (searchMap.getZoom() >= 12 && google.maps.geometry.spherical.computeDistanceBetween(newcenter, center)>100) {
			var bounds =  searchMap.getBounds();
			var ne = bounds.getNorthEast();
			var sw = bounds.getSouthWest();
			let url = `/ajax/doctors/searchByMap?fromLat=${sw.lat()}&toLat=${ne.lat()}&fromLng=${sw.lng()}&toLng=${ne.lng()}`;
			fetch(url)
				.then((resp) => resp.json())
				.then((resp) => {
					let results = resp.data.results.map(({doctor, ...item}) => {
						return {
							icons: "default",
							...doctor,
							...item,
						};
					})

					let newMarkers = results.filter((item) => {
						if (locatedMarkerIds.indexOf(item.id) == -1) {
							locatedMarkerIds.push(item.id);
							return true;
						}
						return false;
					})

					loadMarkers(searchMap, newMarkers);
					loadResultsGrid(results);
				})
			center = searchMap.getCenter();     
		}
	});

	
}

function showMarkerInfo(marker) {
	var content =
		'<div class="profile-widget" style="width: 100%; display: inline-block;">' +
			'<div class="doc-img">' +
				'<a href="' + marker.profile_link + '" tabindex="0" target="_blank">' +
					'<img class="img-fluid" alt="' + marker.title + '" src="' + marker.avatar + '">' +
				'</a>' +
			'</div>' +
			'<div class="pro-content">' +
				'<h3 class="title">' +
					'<a href="' + marker.profile_link + '" tabindex="0">' + marker.title + '</a>' +
					'<i class="fas fa-check-circle verified"></i>' +
				'</h3>' +
				'<p class="speciality">' + marker.specialtiesText + '</p>' +
				'<div class="rating">' +
					'<i class="fas fa-star filled"></i>' +
					'<i class="fas fa-star filled"></i>' +
					'<i class="fas fa-star filled"></i>' +
					'<i class="fas fa-star filled"></i>' +
					'<i class="fas fa-star"></i>' +
					'<span class="d-inline-block average-rating"> (' + marker.totalReviews + ')</span>' +
				'</div>' +
				'<ul class="available-info">' +
					'<li><i class="fas fa-map-marker-alt"></i>' + marker.city + ' </li>' +
					(marker.upcomingAvailableDate ? ('<li><i class="far fa-clock"></i>Available on ' + marker.upcomingAvailableDate + '</li>') : '' ) +
				'</ul>' +
			'</div>' +
		'</div>';
	infowindow.setContent(content);
}

function loadMarkers(searchMap, markers) {
	for (var i = 0; i < markers.length; i++) {
		var item = markers[i];
		var latlng = new google.maps.LatLng(item.lat, item.lng);
		var marker = new google.maps.Marker({
			position: latlng,
			map: searchMap,
			profile_link: item.url,
			animation: google.maps.Animation.DROP,
			icon: {
				url: icons[item.icons],
				scaledSize: new google.maps.Size(32,32)
			},
			...item
		});
		markers[i] = marker;
		google.maps.event.addListener(marker, "click", function () {
			showMarkerInfo(this);
			infowindow.open(searchMap, this);
		});
	}
	google.maps.event.addListener(searchMap, 'zoom_changed', function () {
		if (searchMap.zoom > 16) searchMap.slide = false;
	});
}

function loadResultsGrid(items) {
	resultsGrid.innerHTML = '';
	resultsCount.innerText = items.length;
	let output = '';
	for (let item of items) {

		let rating = '';
		for(let i=1;i<6;i++) {
			if (i - item.avgRating < 0.5) {
				rating += '<i class="fas fa-star filled"></i>';
			} else if (i - item.avgRating == 0.5) {
				rating += '<i class="fas fa-star-half-alt filled"></i>';
			} else {
				rating += '<i class="far fa-star"></i>';
			}
		} 
		output += `
		<div class="col-sm-6 col-md-4 col-xl-6">
			<div class="profile-widget">
				<div class="doc-img">
					<a href="${item.url}">
						<img class="img-fluid" alt="User Image" src="${item.avatar}">
					</a>
					
					<a href="javascript:void(0)" class="fav-btn ${item.isFavorite ? 'fav-active' : ''}" data-doctor="${item.id}">
						<i class="far fa-bookmark"></i>
					</a>
				</div>
				<div class="pro-content">
					<h3 class="title">
						<a href="${item.url}">${item.title}</a>
						<i class="fas fa-check-circle verified"></i>
					</h3>
					<p class="speciality">${item.specialtiesText}</p>
					<div class="rating">
						${rating}
						<span class="d-inline-block average-rating">(${item.totalReviews})</span>
					</div>
					<ul class="available-info">
						<li>
							<i class="fas fa-map-marker-alt"></i> ${item.city}
						</li>
						<li>
							<i class="far fa-clock"></i> Available on ${item.upcomingAvailableDate}
						</li>
					</ul>
					<div class="row row-sm">
						<div class="col-6">
							<a href="${item.url}" class="btn view-btn">View Profile</a>
						</div>
						<div class="col-6">
							<a href="${item.bookingUrl}" class="btn book-btn">Book Now</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		`;
	}
	resultsGrid.innerHTML = output;

	resultsGrid.querySelectorAll('.fav-btn').forEach((btn) => {
		btn.onclick = (e) => {
			e.preventDefault();
			let $btn = jQuery(btn);
			favoriteDoctorToggle($btn)
		}
	})
}

function locateCurrentPosition() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(
			(position) => {
				const pos = {
					lat: position.coords.latitude,
					lng: position.coords.longitude,
				};
				
				searchMap.setCenter(pos);
				searchMap.setZoom(16);
				removeLoadingOverlay(resultsPanel);
			},
			() => {
				removeLoadingOverlay(resultsPanel);
				handleLocationError(true);
			}
		);
	} else {
		// Browser doesn't support Geolocation
		handleLocationError(false);
	}
}

function handleLocationError(browserHasGeolocation) {
	alert(
		browserHasGeolocation
		? "Error: Failed to load your geolocation."
		: "Error: Your browser doesn't support geolocation."
	)
}

google.maps.event.addDomListener(window, 'load', initialize);