const clinicInit = () => {

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
				fields: ["address_components", "geometry"],
				types: ["address"],
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
					this.fillAddress(place.address_components);
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

		fillAddress(addressComponents) {
			components = addressComponents.reduce((components, component) => {
				components[component.types[0]] = component;
				return components;
			}, {});

			// console.log(components);

			addressLine1Field.value = components.route ? ((components.street_number ? components.street_number.short_name + ' ' : '') + components.route.long_name) : '';
			addressLine2Field.value = '';
			cityField.value         = components.locality ? components.locality.short_name : (components.administrative_area_level_2 ? components.administrative_area_level_2.short_name : '');
			stateField.value        = components.administrative_area_level_1.short_name;
			postalCodeField.value   = components.postal_code ? (components.postal_code.long_name + (components.postal_code_suffix ? `-${components.postal_code_suffix}` : '')) : '';
			
			if (countryField.value != components.country.short_name) {
				countryField.value = components.country.short_name;
				jQuery(countryField).trigger('change', keepAddrFields = true)
			}
		},

		parseCurrentAddress(completeCallback) {
			geocoder = new google.maps.Geocoder();

			geocoder.geocode({ latLng: this.getCurrentPos() }, (responses) => {
				if (responses) {
					this.fillAddress(responses[0].address_components)
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

window.clinicInit = clinicInit;