const getAjaxCsrfTokenHeader = () => {
    return {
        [
            document.querySelector('meta[name="_csrf_header"]').content
        ]: document.querySelector('meta[name="_csrf"]').content
    };
}

// jQuery(function($){
//     $(document).ajaxSend(function(e, xhr, options) {
//         xhr.setRequestHeader(
//             document.querySelector('meta[name="_csrf_header"]').content, 
//             document.querySelector('meta[name="_csrf"]').content
//         );
//     });
// })

const addLoadingOverlay = ($el) => {

    let $loadingOverlay = jQuery(`<div class="loading-overlay align-items-center d-flex justify-content-center position-absolute" style="top: 0;right: 0;bottom: 0;left: 0;z-index: 99999;background-color: #ffffffcc;">
        <div class="spinner-border text-secondary" role="status">
        <span class="sr-only">Loading...</span>
        </div>
    </div>`);

    if (!($el instanceof jQuery)) {
        $el = $($el);
    }
    
    $el.addClass('position-relative').css('min-height', '100px');
    $el.append($loadingOverlay);

}

const removeLoadingOverlay = ($el) => {
    if (!($el instanceof jQuery)) {
        $el = $($el);
    }

    $el.removeClass('position-relative');
    $el.find('.loading-overlay').remove();
}

/**
 * Handle favorite toggle feature
 * @param {*} $btn 
 * @param {*} callback 
 */
const favoriteDoctorToggle = ($btn, callback) => {
    let body = document.getElementsByTagName('body')[0];
    if (body.classList.contains('ROLE_GUEST')) { 
        window.location.href = '/login';
    } else if (!body.classList.contains('ROLE_PATIENT')) {
        alert('Only Patient can be able to add Favorite Doctor');
    } else {
        let doctorId = $btn.data('doctor');
        if (doctorId) {
            $.ajax({
                url: `/ajax/favorite/${doctorId}`,
                method: 'post',
                headers: {
                    ...getAjaxCsrfTokenHeader(),
                },
                beforeSend() {
                    $btn.addClass('disabled');
                },
                success(resp) {
                    $btn.toggleClass('fav-active', resp.data.isFavorite);
                    if (callback instanceof Function) {
                        callback(resp);
                    }
                },
                complete(xhr) {
                    $btn.removeClass('disabled');
        
                    if (xhr.responseJSON && !xhr.responseJSON.isSuccess && xhr.responseJSON.message) {
                        alert(xhr.responseJSON.message);
                    } 
                }
            });
        }
    }
}

const gMapHelpers = {
    parsePlaceComponents(place) {
        return place.address_components.reduce((components, component) => {
            components[component.types[0]] = component;
            return components;
        }, {})
    },
    parseAddressFromComponents(components) {
        return {
            addressLine1: components.route ? ((components.street_number ? components.street_number.short_name + ' ' : '') + components.route.long_name) : '',
            addressLine2: '',
            city        : components.locality ? components.locality.short_name : (components.administrative_area_level_2 ? components.administrative_area_level_2.short_name : ''),
            state       : components.administrative_area_level_1.short_name,
            postalCode  : components.postal_code ? (components.postal_code.long_name + (components.postal_code_suffix ? `-${components.postal_code_suffix}` : '')): '',
            country     : components.country.short_name,
        }
    },
    parseAddress(place) {
        const components = this.parsePlaceComponents(place);
        return this.parseAddressFromComponents(components);
    }
}