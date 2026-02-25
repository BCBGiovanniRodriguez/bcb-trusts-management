"use strict";
$(() => {
    const milisecondsOnOneYear = 31557600000, btnSaveUserJQuery = $("#btnSaveUser"), firstNameJQuery = $("#firstName"), secondNameJQuery = $("#secondName"), lastNameJQuery = $("#lastName"), secondLastNameJQuery = $("#secondLastName"), genderJQuery = $("#gender"), birthDateJQuery = $("#birthDate"), curpJQuery = $("#curp"), rfcJQuery = $("#rfc"), emailJQuery = $("#email"), nicknameJQuery = $("#nickname"), confirmOperationModalJQuery = $("#confirmOperationModal"), serverErrorModalJQuery = $("#serverErrorModal"), divUserAlertJQuery = $("#divUserAlert"), userAlertJQuery = $("#userAlert"), userAlertStrongJQuery = $("#userAlertStrong"), userAlertSpanJQuery = $("#userAlertSpan"), localApiSystem = "/api/system";
    let errorList = [];
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
        if (curpJQuery.val() == null || curpJQuery.val() == "") {
            errorList.push("<strong>CURP</strong> es requerido");
        }
        if (rfcJQuery.val() == null || rfcJQuery.val() == "") {
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
        console.log("validatePerson: " + valid);
        return valid;
    }
    function validateEmail() {
        var _a;
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        const validateEmail = (email) => emailRegex.test(email);
        let valid = true;
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
        console.log("validateEmail: " + valid);
        return valid;
    }
    emailJQuery.on('change', function () {
        let self = $(this), email = self.val() != null ? self.val().toString() : "", nickname, uniqueEmail;
        uniqueEmail = false;
        if (email != "") {
            let url = localApiSystem + '/user/unique-email?email=' + self.val();
            $.getJSON(url, function (result) {
                if (result != undefined) {
                    if (result.status == 1) {
                        divUserAlertJQuery.removeAttr('hidden');
                        if (result.data == 1) {
                            uniqueEmail = true;
                            if (self.val() != null && self.val() != undefined && self.val() != "") {
                                email = self.val().toString();
                                nickname = email.split("@")[0];
                                nicknameJQuery.val(nickname);
                                userAlertJQuery.addClass("alert-success").removeClass("alert-danger").removeClass("alert-warning");
                                userAlertStrongJQuery.text("Correo Electrónico Disponible");
                                userAlertSpanJQuery.text("El correo electrónico que ingresó está disponible para registrarse");
                            }
                        }
                        else {
                            userAlertJQuery.addClass("alert-danger").removeClass("alert-warning");
                            userAlertStrongJQuery.text("Correo Electrónico No Disponible");
                            userAlertSpanJQuery.text("El correo electrónico que ingresó ya se encuentra registrado, por favor ingrese un correo electrónico diferente");
                            nicknameJQuery.val("");
                        }
                    }
                }
            });
        }
    });
    $(".form-control, .form-select").on('change', function () {
        let validado = validateEmail() && validatePerson();
        console.log("validado: " + validado);
        if (validado) {
            btnSaveUserJQuery.removeAttr('hidden');
        }
        else {
            btnSaveUserJQuery.attr('hidden', 'hidden');
        }
    });
    btnSaveUserJQuery.on('click', function () {
        var _a, _b, _c, _d, _e, _f, _g, _h, _j, _k;
        if (validatePerson() && validateEmail()) {
            let endpointUser = localApiSystem + "/user", personJson = {}, userJson = {};
            personJson.firstName = (_a = firstNameJQuery.val()) === null || _a === void 0 ? void 0 : _a.toString();
            personJson.secondName = (_b = secondNameJQuery.val()) === null || _b === void 0 ? void 0 : _b.toString();
            personJson.lastName = (_c = lastNameJQuery.val()) === null || _c === void 0 ? void 0 : _c.toString();
            personJson.secondLastName = (_d = secondLastNameJQuery.val()) === null || _d === void 0 ? void 0 : _d.toString();
            personJson.gender = (_e = genderJQuery.val()) === null || _e === void 0 ? void 0 : _e.toString();
            personJson.birthDate = (_f = birthDateJQuery.val()) === null || _f === void 0 ? void 0 : _f.toString();
            personJson.curp = (_g = curpJQuery.val()) === null || _g === void 0 ? void 0 : _g.toString();
            personJson.rfc = (_h = rfcJQuery.val()) === null || _h === void 0 ? void 0 : _h.toString();
            personJson.type = 1;
            userJson.person = personJson;
            userJson.email = (_j = emailJQuery.val()) === null || _j === void 0 ? void 0 : _j.toString();
            userJson.nickname = (_k = nicknameJQuery.val()) === null || _k === void 0 ? void 0 : _k.toString();
            $.ajax({
                method: 'POST',
                contentType: 'application/json',
                cache: false,
                url: endpointUser,
                data: JSON.stringify(userJson),
                success: ((result, textStatus, jqXHR) => {
                    if (result != undefined) {
                        let resultJson = JSON.parse(result);
                        if (resultJson.status == 1) {
                            confirmOperationModalJQuery.modal('show');
                            setTimeout(function () {
                                window.location.href = "/system/user?status=1";
                            }, 5000);
                        }
                        else if (resultJson.status == 0) {
                            setTimeout(function () {
                                console.log(resultJson.message);
                                $("#errorMessage").text(resultJson.message);
                                serverErrorModalJQuery.modal('show');
                            }, 5000);
                        }
                    }
                    else {
                        serverErrorModalJQuery.modal('show');
                    }
                }),
            }).fail((jqXHR, textStatus, error) => {
                console.log(jqXHR);
                console.log(textStatus);
                console.log(error);
            });
        }
    });
});
