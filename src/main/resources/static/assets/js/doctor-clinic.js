const clinicMapInit = () => {

	const mapCont = document.getElementById('clinic_location_map');
	const formCard = document.getElementById('clinicForm').querySelector('.card');

	let clinicInputLat = document.getElementById('clinicInputLat'), 
		clinicInputLong = document.getElementById('clinicInputLong')
	let clinicHasLocated = clinicInputLat.value && clinicInputLong.value;

	let map, infoWindow, marker, mapCenter = { lat: -34.401133113281986, lng: 150.87797718048094 };

	let autocomplete;
	let searchAddress = document.querySelector('#searchAddress');

	let addressLine1Field = document.getElementById('addressLine1'),
	    addressLine2Field = document.getElementById('addressLine2'),
	    cityField         = document.getElementById('city'),
	    stateField        = document.getElementById('state'),
	    postalCodeField   = document.getElementById('postalCode'),
	    countryField      = document.getElementById('country');

	const clinicPage = {

		isLoading: false,

		/**
		 * Initialization functions
		 */
		initMap() {
	
			// anyway, locate current position first
			this.locateHere();
			
			if (clinicHasLocated) {
				mapCenter = this.getCurrentPos();
			} else {
				this.showLoading(); // show loading till current position is located
			}
		
			map = new google.maps.Map(mapCont, {
				center: mapCenter,
				zoom: clinicHasLocated ? 16 : 14,
				mapTypeId: google.maps.MapTypeId.ROADMAP
			});
			
			infoWindow = new google.maps.InfoWindow();
		
			this.initMarker();
			this.initAutocomplete();

		},

		initMarker() {
			marker = new google.maps.Marker({
				position: map.getCenter(),
				draggable: true
			});
			
			google.maps.event.addListener(marker, 'dragend', (evt) => {
				// reset this
				searchAddress.value = '';

				this.setCurrentPos({
					lat: marker.getPosition().lat(),
					lng: marker.getPosition().lng()
				})
				
				countryField.disabled = true;
				this.parseCurrentAddress(() => {
					countryField.disabled = false;
				});
			});
			
			// map.setCenter(marker.position);
			marker.setMap(map);
		},

		initAutocomplete() {
	
			// Create the autocomplete object, restricting the search predictions to
			// addresses in the US and Canada.
			autocomplete = new google.maps.places.Autocomplete(searchAddress, {
				componentRestrictions: { country: countryField.value ? [countryField.value] : [] },
				fields: ["address_components", "geometry", "adr_address", "formatted_address"],
				types: ["address"],
				language: "en"
			});
			
			autocomplete.addListener('place_changed', () => {
				const place = autocomplete.getPlace();

				this.setCurrentPos({
					lat: place.geometry.location.lat(),
					lng: place.geometry.location.lng()
				})

				this.recenterMap();
				this.recenterMarker();

				if (place.address_components) {
					this.fillAddress(place);
				}
			});
		},

		/**
		 * Event
		 */
		onCountryChanged(e, keepAddrFields) {

			if (!keepAddrFields) {
				addressLine1Field.value = '';
				addressLine2Field.value = '';
				cityField.value         = '';
				stateField.value        = '';
				postalCodeField.value   = '';
			}

			autocomplete.setComponentRestrictions({ country: [countryField.value] });

		},

		/**
		 * Helper functions
		 */
		locateHere() {
			if (navigator.geolocation) {

				navigator.geolocation.getCurrentPosition(
					(position) => {

						let pos = {
							lat: position.coords.latitude,
							lng: position.coords.longitude,
						};

						if (!clinicHasLocated) {
							this.setCurrentPos(pos);
							this.recenterMap();
							this.recenterMarker();
							this.parseCurrentAddress(() => {
								this.maybeHideLoading();
							});
						} else {
							this.maybeHideLoading()
						}
						
					},
					() => {
						this.maybeHideLoading();
						alert('Error: The Geolocation service failed.');
					}
				);

			} else {
				// Browser doesn't support Geolocation
				alert('Error: Your browser doesn\'t support geolocation.');
			}
		},

		fillAddress(place) {

			let components = gMapHelpers.parsePlaceComponents(place);
			let address = gMapHelpers.parseAddressFromComponents(components);

			
			/**
			 * Override some fields of address 
			 */

			// 1. Get international city name workaround
			if (place.plus_code) {
				address.state = place.plus_code.compound_code.split(', ').at(-2);
			} else if (searchAddress.value) {
				address.state = searchAddress.value.split(', ').at(-2);
			}

			// 2. Get the rest of address
			let parts = place.formatted_address
				.split(', ')
				.slice(0, -2); // remove country and state from parts
			
			// detect city 
			if (components.locality) { 
				// address.city = components.locality;
				address.city = parts.pop();
			} else {
				address.city = address.state;
			}
			address.addressLine1 = parts.shift();
			address.addressLine2 = [...parts].join(', ');

			/**
			 * Start setting value
			 */
			addressLine1Field.value = address.addressLine1;
			addressLine2Field.value = address.addressLine2;
			cityField.value         = address.city;
			stateField.value        = address.state;
			postalCodeField.value   = address.postalCode;
			
			if (countryField.value != address.country) {
				countryField.value = address.country;
				jQuery(countryField).trigger('change', keepAddrFields = true)
			}
		},

		parseCurrentAddress(completeCallback) {
			geocoder = new google.maps.Geocoder();

			geocoder.geocode({ latLng: this.getCurrentPos() }, (places) => {
				if (places) {
					this.fillAddress(places[0])
				}
				if (completeCallback) {
					completeCallback();
				}
			});
		},

		setCurrentPos(pos) {
			clinicInputLat.value = pos.lat;
			clinicInputLong.value = pos.lng;
		},

		getCurrentPos() {
			return {
				lat: parseFloat(clinicInputLat.value),
				lng: parseFloat(clinicInputLong.value)
			};
		},

		recenterMarker() {
			marker.setPosition(this.getCurrentPos());
		},

		recenterMap() {
			map.setCenter(this.getCurrentPos());
			map.setZoom(16);
		},

		showLoading() {
			this.isLoading = true;
			addLoadingOverlay(formCard);
		},

		hideLoading() {
			removeLoadingOverlay(formCard);
		},

		maybeHideLoading() {
			if (this.isLoading) {
				this.hideLoading()
			}
		}

	}

	clinicPage.initMap();
	countryField.onchange = clinicPage.onCountryChanged;
}

window.googleMapInit = clinicMapInit;