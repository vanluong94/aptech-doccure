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

    let $loadingOverlay = jQuery(`<div class="loading-overlay align-items-center d-flex justify-content-center position-absolute" style="top: 0;right: 0;bottom: 0;left: 0;z-index: 99;background-color: #ffffffcc;">
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
    $.ajax({
        url: `/ajax/favorite/${$btn.data('doctor')}`,
        method: 'post',
        headers: {
            ...getAjaxCsrfTokenHeader(),
        },
        beforeSend() {
            $btn.addClass('disabled');
        },
        success(resp) {
            callback(resp);
        },
        complete(xhr) {
            $btn.removeClass('disabled');

            if (xhr.responseJSON && !xhr.responseJSON.isSuccess && xhr.responseJSON.message) {
                alert(xhr.responseJSON.message);
            } 
        }
    });
}