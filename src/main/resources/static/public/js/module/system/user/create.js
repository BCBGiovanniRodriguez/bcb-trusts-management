"use strict";
$(() => {
    const milisecondsOnOneYear = 31557600000, btnValidateJQuery = $("#btnValidate"), btnSaveUserJQuery = $("#btnSaveUser"), profileJQuery = $("#profile"), firstNameJQuery = $("#firstName"), secondNameJQuery = $("#secondName"), lastNameJQuery = $("#lastName"), secondLastNameJQuery = $("#secondLastName"), genderJQuery = $("#gender"), birthDateJQuery = $("#birthDate"), curpJQuery = $("#curp"), rfcJQuery = $("#rfc"), emailJQuery = $("#email"), nicknameJQuery = $("#nickname"), errorValidationModalJQuery = $("#errorValidationModal"), successValidationModalJQuery = $("#successValidationModal"), localApiSystem = "/api/system";
    let errorList = [];
    btnValidateJQuery.on('click', function () {
        errorList = [];
        if (validateProfileAndPermissions() && validatePerson()) {
            successValidationModalJQuery.modal('show');
            btnSaveUserJQuery.removeAttr('hidden');
            btnValidateJQuery.attr('hidden', 'hidden');
        }
        else {
            errorValidationModalJQuery
                .find($("#errorDetail"))
                .html(errorList.join('<br />'));
            errorValidationModalJQuery.modal('show');
            btnSaveUserJQuery.attr('hidden', 'hidden');
            btnValidateJQuery.removeAttr('hidden');
        }
    });
    profileJQuery.on('change', function () {
        let self = $(this), profileId = Number(self.val()), tbody = $("#profilePermissionTable").find("tbody");
        $.ajax({
            method: 'GET',
            contentType: 'application/json',
            cache: false,
            url: localApiSystem + '/profile/permission/' + profileId
        }).fail((jqXHR, textStatus, error) => {
            console.log(jqXHR);
            console.log(textStatus);
            console.log(error);
        })
            .then((result, textStatus, jqXHR) => {
            let resultJson = JSON.parse(result);
            if (resultJson.status == 1) {
                let data = resultJson.data;
                tbody.empty();
                $.each(data, function (i, item) {
                    let tr = $('<tr>'), tdId = $('<td>', { 'text': item.permissionId }), tdModule = $('<td>', { 'text': item.moduleAsString }), tdCode = $('<td>', { 'text': item.code }), tdName = $('<td>', { 'text': item.name });
                    tr.append(tdId).append(tdModule).append(tdCode).append(tdName);
                    tbody.append(tr);
                });
            }
            else if (resultJson.status == 0) {
                console.log(resultJson.message);
            }
        });
        validatePerson();
    });
    function validatePerson() {
        var _a;
        let valid = true;
        if (firstNameJQuery.val() == null || firstNameJQuery.val() == "") {
            valid = false;
            errorList.push("<strong>Primer Nombre</strong> es requerido");
        }
        if (lastNameJQuery.val() == null || lastNameJQuery.val() == "") {
            valid && (valid = false);
            errorList.push("<strong>Apellido Paterno</strong> es requerido");
        }
        if (genderJQuery.val() == null) {
            valid && (valid = false);
            errorList.push("<strong>Género</strong> es requerido");
        }
        if (curpJQuery.val() == null || curpJQuery.val() == "" || curpJQuery.val() == "XAXX000000XXXXXX00") {
            errorList.push("<strong>CURP</strong> es requerido");
        }
        if (rfcJQuery.val() == null || rfcJQuery.val() == "" || rfcJQuery.val() == "XAXX000000XX0") {
            valid && (valid = false);
            errorList.push("<strong>RFC</strong> es requerido");
        }
        if (birthDateJQuery.val() == null || birthDateJQuery.val() == "") {
            valid && (valid = false);
            errorList.push("<strong>Fecha de Nacimiento</strong> es requerida");
        }
        else {
            let birthDateStr = (_a = birthDateJQuery.val()) === null || _a === void 0 ? void 0 : _a.toString();
            if (birthDateStr != null || birthDateStr != undefined) {
                let today = new Date(), birthDate = new Date(birthDateStr), milisecondsBetweenDates = today.getTime() - birthDate.getTime(), years = milisecondsBetweenDates / milisecondsOnOneYear;
                if (years < 18) {
                    valid && (valid = false);
                    errorList.push("<strong>Fecha de Nacimiento</strong> es menor a 18 años");
                }
            }
            else {
                valid && (valid = false);
                errorList.push("<strong>Fecha de Nacimiento</strong> ocurrio un error al transformar la fecha de nacimiento");
            }
        }
        return valid;
    }
    function validateProfileAndPermissions() {
        var _a;
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        const validateEmail = (email) => emailRegex.test(email);
        let valid = true;
        if (profileJQuery.val() == null || profileJQuery.val() == undefined) {
            valid && (valid = false);
            errorList.push("<strong>Perfil de Sistema</strong> es requerido, seleccione una opción");
        }
        if (emailJQuery.val() == null || emailJQuery.val() == undefined) {
            valid && (valid = false);
            errorList.push("<strong>Correo Electrónico</strong> es requerido");
        }
        else {
            let emailString = ((_a = emailJQuery.val()) === null || _a === void 0 ? void 0 : _a.toString()) != undefined ? String(emailJQuery.val()) : '';
            if (!validateEmail(emailString)) {
                valid && (valid = false);
                errorList.push("<strong>Correo Electrónico</strong> es requerido, el valor no parece ser válido");
            }
        }
        return valid;
    }
    emailJQuery.on('change', function () {
        console.log('Change');
        let self = $(this), email, nickname;
        if (self.val() != null && self.val() != undefined && self.val() != "") {
            email = self.val().toString();
            nickname = email.split("@")[0];
            nicknameJQuery.val(nickname);
        }
    });
    btnSaveUserJQuery.on('click', function () {
        var _a, _b, _c, _d, _e, _f, _g, _h, _j, _k;
        if (validatePerson() && validateProfileAndPermissions()) {
            let endpointUser = localApiSystem + "/user", personJson = {}, userJson = {};
            personJson.firstName = (_a = firstNameJQuery.val()) === null || _a === void 0 ? void 0 : _a.toString();
            personJson.secondName = (_b = secondNameJQuery.val()) === null || _b === void 0 ? void 0 : _b.toString();
            personJson.lastName = (_c = lastNameJQuery.val()) === null || _c === void 0 ? void 0 : _c.toString();
            personJson.secondLastName = (_d = secondLastNameJQuery.val()) === null || _d === void 0 ? void 0 : _d.toString();
            personJson.gender = (_e = genderJQuery.val()) === null || _e === void 0 ? void 0 : _e.toString();
            personJson.birthDate = (_f = birthDateJQuery.val()) === null || _f === void 0 ? void 0 : _f.toString();
            personJson.curp = (_g = curpJQuery.val()) === null || _g === void 0 ? void 0 : _g.toString();
            personJson.rfc = (_h = rfcJQuery.val()) === null || _h === void 0 ? void 0 : _h.toString();
            userJson.person = personJson;
            userJson.email = (_j = emailJQuery.val()) === null || _j === void 0 ? void 0 : _j.toString();
            userJson.nickname = (_k = nicknameJQuery.val()) === null || _k === void 0 ? void 0 : _k.toString();
            userJson.profileId = Number(profileJQuery.val());
            console.dir(userJson);
            $.ajax({
                method: 'POST',
                contentType: 'application/json',
                cache: false,
                url: endpointUser,
                data: JSON.stringify(userJson)
            }).fail((jqXHR, textStatus, error) => {
                console.log(jqXHR);
                console.log(textStatus);
                console.log(error);
            })
                .then((result, textStatus, jqXHR) => {
                let resultJson = JSON.parse(result);
                if (resultJson.status == 1) {
                    console.log("Registrado!");
                }
                else if (resultJson.status == 0) {
                    console.log(result.message);
                }
            });
        }
    });
});
