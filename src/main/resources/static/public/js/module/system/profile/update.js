"use strict";
$(() => {
    const btnUpdateProfileJQuery = $("#btnSaveProfile"), profileIdJQuery = $("#profileId"), nameJQuery = $("#name"), membersJQuery = $("#members"), localApiSystem = "/api/system", permissionIds = [], permissionListJQuery = $("#permissionTable"), permissionTable = $("#permissionTable"), confirmOperationModal = $("#confirmOperationModal"), redirectionSpanJquery = $("#reditectionSpan");
    btnUpdateProfileJQuery.on('click', function () {
        var _a, _b;
        let self = $(this), endpointProfile = localApiSystem + "/profile/" + profileIdJQuery.val(), profileObject = {};
        permissionTable.find("tbody tr td input.permission:checked").each(function () {
            let value = String($(this).attr("permission"));
            permissionIds.push(value);
        });
        profileObject.name = (_a = nameJQuery.val()) === null || _a === void 0 ? void 0 : _a.toString();
        profileObject.members = (_b = membersJQuery.val()) === null || _b === void 0 ? void 0 : _b.toString();
        profileObject.permissionIds = permissionIds;
        $.ajax({
            method: 'PUT',
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
            let resultObj = JSON.parse(result);
            if (resultObj.status == 1) {
                console.dir(resultObj);
                console.log("Registrado!");
                confirmOperationModal.modal('show');
                setTimeout(function () {
                    window.location.href = "/system/profile";
                }, 5000);
            }
            else if (result.status == 0) {
                console.log(result.message);
            }
        });
    });
});
