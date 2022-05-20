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

const themeUtils = {
    renderApmtStatusBadge(status) {
        switch (status) {
            case 0:
                return `<div class="btn bg-warning-light btn-sm">Pending</div>`;
            case 1:
                return `<div class="btn bg-info-light btn-sm">Confirm</div>`;
            case 2:
                return `<div class="btn bg-success-light btn-sm">Completed</div>`;
            case 3:
                return `<div class="btn bg-danger-light btn-sm">Cancelled</div>`;
        }
    },
    renderApmtStatusPill(status) {
        switch (status) {
            case 0:
                return `<span class="badge badge-pill bg-warning-light">Pending</span>`;
            case 1:
                return `<span class="badge badge-pill bg-info-light">Confirm</span>`;
            case 2:
                return `<span class="badge badge-pill bg-success-light">Completed</span>`;
            case 3:
                return `<span class="badge badge-pill bg-danger-light">Cancelled</span>`;
        }
    },
    renderApmtLogContent(content) {
        return content.replace(/\[([^\]]+)\]/g, '<mark>$1</mark>');
    },
    renderUserTd(user) {
        return `<h2 class="table-avatar">` +
            `<a href="${user.url}" class="avatar avatar-sm mr-2">` +
                `<img class="avatar-img rounded-circle" src="${user.avatar}" alt="${user.title}">` +
            `</a>` +
            `<a href="${user.url}">${user.title}<span>${user.subtitle}</span></a>` +
        `</h2>`;
    }
}