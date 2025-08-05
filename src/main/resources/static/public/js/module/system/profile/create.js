"use strict";
$(() => {
    const btnSaveProfileJQuery = $("#btnSaveProfile"), nameJQuery = $("#name"), membersJQuery = $("#members"), localApiSystem = "/api/system";
    btnSaveProfileJQuery.on('click', function () {
        var _a, _b;
        let self = $(this), endpointProfile = localApiSystem + "/profile", profileObject = {};
        profileObject.name = (_a = nameJQuery.val()) === null || _a === void 0 ? void 0 : _a.toString();
        profileObject.members = (_b = membersJQuery.val()) === null || _b === void 0 ? void 0 : _b.toString();
        $.ajax({
            method: 'POST',
            contentType: 'application/json',
            cache: false,
            url: endpointProfile,
            data: JSON.stringify(profileObject)
        }).fail((jqXHR, textStatus, error) => {
            console.log(jqXHR);
            console.log(textStatus);
            console.log(error);
        })
            .then((result, textStatus, jqXHR) => {
            if (result.status == 1) {
                console.log("Registrado!");
            }
            else if (result.status == 0) {
                console.log(result.message);
            }
        });
    });
});
