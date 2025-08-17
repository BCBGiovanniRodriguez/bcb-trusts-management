"use strict";
$(() => {
    var _a;
    const localApiSystem = '/api/system', systemUserData = sessionStorage.getItem('systemUser'), spanNavUsernameJQuery = $("#spanNavUsername"), spanCardUsernameJQuery = $("#spanCardUsername"), spanCardProfileJQuery = $("#spanCardProfile"), spanCardUserCreatedJQuery = $("#spanCardUserCreated");
    if (systemUserData == undefined) {
        loadFrontendData();
    }
    else {
        const systemUser = JSON.parse(systemUserData);
        console.dir(systemUser);
        let username = (systemUser === null || systemUser === void 0 ? void 0 : systemUser.nickname) != undefined ? systemUser.nickname : '', profile = ((_a = systemUser === null || systemUser === void 0 ? void 0 : systemUser.profile) === null || _a === void 0 ? void 0 : _a.name) != undefined ? systemUser.profile.name : '', created = (systemUser === null || systemUser === void 0 ? void 0 : systemUser.created) != undefined ? systemUser.created : '';
        spanNavUsernameJQuery.text(username);
        spanCardUsernameJQuery.text(username);
        spanCardProfileJQuery.text(profile);
        spanCardUserCreatedJQuery.text(created);
    }
    function loadFrontendData() {
        const currentUserUrl = `${localApiSystem}/user/current`;
        $.ajax({
            method: 'GET',
            timeout: 5000,
            url: currentUserUrl
        }).fail((jqXHR, textStatus, error) => {
            console.dir(jqXHR);
            console.dir(textStatus);
            console.dir(error);
            $("#errorMessage").text("Error desconocido");
            serverErrorModal.modal('show');
            setTimeout(function () {
                serverErrorModal.modal('hide');
            }, 5000);
        }).then((result, textStatus, jqXHR) => {
            if (result != undefined) {
                const resultJson = JSON.parse(result);
                sessionStorage.setItem('systemUserData', result);
                if (resultJson.status == 1) {
                    const systemUser = resultJson.data;
                    console.dir(systemUser);
                    const nickname = systemUser.nickname, email = systemUser.email, person = systemUser.person, profile = systemUser.profile;
                    spanNavUsernameJQuery.text(nickname);
                    spanCardUsernameJQuery.text(systemUser.nickname);
                    spanCardProfileJQuery.text(profile.name);
                    spanCardUserCreatedJQuery.text(systemUser.created);
                }
                else {
                    resultNotFoundModal.modal('show');
                }
            }
            else {
                $("#errorMessage").text("Sin conexión al endpoint");
                serverErrorModal.modal('show');
            }
        });
    }
});
