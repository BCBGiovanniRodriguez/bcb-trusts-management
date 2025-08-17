"use strict";
$(() => {
    const milisecondsOnOneYear = 31557600000, apiLocation = "http://10.20.50.132:9002", localApiRequest = "/api/request", OPTION_YES = 1, OPTION_NO = 2, PERSON_TYPE_PERSON = 1, PERSON_TYPE_ENTERPRISE = 2, btnSaveRequestJQuery = $("#btnSaveRequest"), btnValidateJQuery = $("#btnValidate"), trustTypeIdJQuery = $("#trustTypeId"), personTypeJQuery = $("#personType"), rfcEnterpriseJQuery = $("#rfcEnterprise"), fullNameJQuery = $("#fullName"), firstNameJQuery = $("#firstName"), secondNameJQuery = $("#secondName"), lastNameJQuery = $("#lastName"), secondLastNameJQuery = $("#secondLastName"), genderJQuery = $("#gender"), birthDateJQuery = $("#birthDate"), curpJQuery = $("#curp"), rfcJQuery = $("#rfc"), zipcodeJQuery = $("#zipcode"), stateJQuery = $("#state"), townshipJQuery = $("#township"), colonyJQuery = $("#colony"), externalNumberJQuery = $("#externalNumber"), internalNumberJQuery = $("#internalNumber"), streetJQuery = $("#street"), phoneTypeJQuery = $("#phoneType"), phoneNumberJQuery = $("#phoneNumber"), emailTypeJQuery = $("#emailType"), emailJQuery = $("#email"), trustChangeJQuery = $("#trustChange"), trustChangeTrustJQuery = $("#trustChangeTrust"), wasReferedJQuery = $("#wasRefered"), wasReferedByJQuery = $("#wasReferedBy"), wasReferedByFullNameJQuery = $("#wasReferedByFullName"), foreignStatusJQuery = $("#foreignStatus"), resultNotFoundModal = $("#resultNotFoundModal"), serverErrorModal = $("#serverErrorModal"), errorValidationModalJQuery = $("#errorValidationModal"), successValidationModalJQuery = $("#successValidationModal");
    let errorList = [], partialAddress = "";
    $(".request-validate").on('change', function () {
        if (validateGeneralInformation() && validateAddress() && validateContact()) {
            btnSaveRequestJQuery.removeAttr('hidden');
            btnValidateJQuery.attr('hidden', 'hidden');
        }
        else {
            btnSaveRequestJQuery.attr('hidden', 'hidden');
            btnValidateJQuery.removeAttr('hidden');
        }
    });
    trustChangeJQuery.on('change', function () {
        let self = $(this), trustChange = Number(self.val());
        if (trustChange != null && trustChange == OPTION_YES) {
            $(".trustChange").removeAttr('hidden');
        }
        else {
            $(".trustChange").attr('hidden', 'hidden');
        }
    });
    wasReferedJQuery.on('change', function () {
        let self = $(this), wasRefered = self.val();
        if (wasRefered != null && wasRefered == OPTION_YES) {
            $(".wasRefered").removeAttr('hidden');
        }
        else {
            $(".wasRefered").attr('hidden', 'hidden');
        }
    });
    personTypeJQuery.on('change', function () {
        let self = $(this), personType = Number(self.val());
        if (PERSON_TYPE_PERSON == personType) {
            $("#personTypePerson").removeAttr("hidden");
            $("#personTypeEnterprise").attr("hidden", "hidden");
        }
        else {
            $("#personTypePerson").attr("hidden", "hidden");
            $("#personTypeEnterprise").removeAttr("hidden");
        }
    });
    zipcodeJQuery.on('change', function () {
        let self = $(this), zipcode = Number(self.val()), locationUrl = `${apiLocation}/api/location/byzipcode?zipcode=${zipcode}`;
        stateJQuery.empty();
        townshipJQuery.empty();
        colonyJQuery.empty();
        $.ajax({
            method: 'GET',
            timeout: 5000,
            url: locationUrl
        }).fail((jqXHR, textStatus, error) => {
            console.dir(jqXHR);
            console.dir(textStatus);
            console.dir(error);
            if (textStatus == "timeout") {
                $("#errorMessage").html("Timeout: Sin conexión al endpoint <strong>BCB Location</strong>");
            }
            else {
                $("#errorMessage").text("Error desconocido");
            }
            serverErrorModal.modal('show');
            setTimeout(function () {
                serverErrorModal.modal('hide');
            }, 5000);
        }).then((result, textStatus, jqXHR) => {
            console.log(textStatus);
            console.log(jqXHR);
            if (result != undefined) {
                if (result.state) {
                    let country = result.data.country, state = result.data.state, states = result.data.states, township = result.data.township, townships = result.data.townships, colonies = result.data.colonies;
                    let emptyOptionOne = $("<option>", { "value": 0, "text": "Seleccione Opción", "selected": "selected", "disabled": "disabled" });
                    partialAddress = township.name + ", " + state.name + ", " + country.name;
                    stateJQuery.append(emptyOptionOne);
                    $.each(states, function (i, item) {
                        let option = $("<option>", { "value": item.stateId, "text": item.name });
                        if (state.stateId == item.stateId) {
                            option.attr("selected", "selected");
                        }
                        stateJQuery.append(option);
                    });
                    stateJQuery.attr("disabled", "disabled");
                    let emptyOptionTwo = $("<option>", { "value": 0, "text": "Seleccione Opción", "selected": "selected", "disabled": "disabled" });
                    townshipJQuery.append(emptyOptionTwo);
                    $.each(townships, function (i, item) {
                        let option = $("<option>", { "value": item.townshipId, "text": item.name });
                        if (township.townshipId == item.townshipId) {
                            option.attr("selected", "selected");
                        }
                        townshipJQuery.append(option);
                    });
                    townshipJQuery.attr("disabled", "disabled");
                    let emptyOptionThree = $("<option>", { "value": 0, "text": "Seleccione Opción", "selected": "selected", "disabled": "disabled" });
                    colonyJQuery.append(emptyOptionThree);
                    $.each(colonies, function (i, item) {
                        let option = $("<option>", { "value": item.colonyId, "text": item.name });
                        colonyJQuery.append(option);
                    });
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
    });
    btnValidateJQuery.on('click', function () {
        errorList = [];
        if (validateGeneralInformation() && validateAddress() && validateContact()) {
            successValidationModalJQuery.modal('show');
            btnSaveRequestJQuery.removeAttr('hidden');
            btnValidateJQuery.attr('hidden', 'hidden');
        }
        else {
            errorValidationModalJQuery
                .find($("#errorDetail"))
                .html(errorList.join('<br />'));
            errorValidationModalJQuery.modal('show');
            btnSaveRequestJQuery.attr('hidden', 'hidden');
            btnValidateJQuery.removeAttr('hidden');
        }
    });
    function validateGeneralInformation() {
        var _a;
        let valid = true;
        if (trustTypeIdJQuery.val() == null || trustTypeIdJQuery.val() == "0") {
            valid && (valid = false);
            errorList.push("<strong>Tipo de Fideicomiso</strong> es requerido, seleccione una opción");
        }
        if (trustChangeJQuery.val() == null == undefined || trustChangeJQuery.val() == null || trustChangeJQuery.val() == "") {
            valid && (valid = false);
            errorList.push("<strong>Es cambio de Fiduciario?</strong> es requerido, seleccione una opción");
        }
        else {
            let trustChange = Number(trustChangeTrustJQuery.val());
            if (OPTION_YES === trustChange) {
                valid && (valid = false);
                errorList.push("<strong>Fiduciario Anterior</strong> es requerido, especifique");
            }
        }
        if (wasReferedJQuery.val() == undefined || wasReferedJQuery.val() == null || wasReferedJQuery.val() == "") {
            valid && (valid = false);
            errorList.push("<strong>Fue recomendado?</strong> es requerido, seleccione una opción");
        }
        else {
            let wasRefered = Number(wasReferedJQuery.val());
            if (OPTION_YES == wasRefered) {
                valid && (valid = false);
                errorList.push("<strong>Recomendado por</strong> es requerido, seleccione una opción");
                if (wasReferedByFullNameJQuery.val() == undefined || wasReferedByFullNameJQuery.val() == null || wasReferedByFullNameJQuery.val() == "") {
                    valid && (valid = false);
                    errorList.push("<strong>Nombre Completo</strong> es requerido, especifique");
                }
            }
        }
        if (trustTypeIdJQuery.val() == null || trustTypeIdJQuery.val() == "0") {
            valid && (valid = false);
            errorList.push("<strong>Tipo de Fideicomiso</strong> es requerido, seleccione una opción");
        }
        let personTypeObj = personTypeJQuery.val();
        if (personTypeJQuery.val() == null || personTypeJQuery.val() == "0") {
            valid = false;
            errorList.push("<strong>Tipo de Persona</strong> es requerido, seleccione una opción");
        }
        else {
            if (personTypeObj != null) {
                let personType = Number(personTypeObj);
                if (PERSON_TYPE_PERSON == personType) {
                    if (firstNameJQuery.val() == null || firstNameJQuery.val() == "") {
                        valid && (valid = false);
                        errorList.push("<strong>Primer Nombre</strong> es requerido");
                    }
                    if (lastNameJQuery.val() == null || lastNameJQuery.val() == "") {
                        valid && (valid = false);
                        errorList.push("<strong>Apellido Paterno</strong> es requerido");
                    }
                    if (genderJQuery.val() == null || genderJQuery.val() == "") {
                        valid && (valid = false);
                        errorList.push("<strong>Género</strong> es requerido");
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
                    if (curpJQuery.val() == null || curpJQuery.val() == "XAXX000000XXXXXX00") {
                        valid && (valid = false);
                        errorList.push("<strong>CURP</strong> es requerido, ingrese un valor válido");
                    }
                    if (rfcJQuery.val() == null || rfcJQuery.val() == "XAXX000000XX0") {
                        valid && (valid = false);
                        errorList.push("<strong>RFC</strong> es requerido, ingrese un valor válido");
                    }
                }
                else if (PERSON_TYPE_ENTERPRISE == personType) {
                    if (rfcEnterpriseJQuery.val() == null || rfcEnterpriseJQuery.val() == "XAXX000000XX0") {
                        valid && (valid = false);
                        errorList.push("<strong>RFC</strong> es requerido, ingrese un valor válido");
                    }
                    if (fullNameJQuery.val() == null || fullNameJQuery.val() == "") {
                        valid && (valid = false);
                        errorList.push("<strong>Razón Social</strong> es requerido, ingrese un valor válido");
                    }
                }
            }
        }
        if (foreignStatusJQuery.val() == null || foreignStatusJQuery.val() == "0") {
            valid && (valid = false);
            errorList.push("<strong>Estatus Migratorio</strong> es requerido, seleccione una opción");
        }
        return valid;
    }
    function validateAddress() {
        let valid = true;
        if (zipcodeJQuery.val() == null || zipcodeJQuery.val() == "") {
            valid = false;
            errorList.push("<strong>Código Postal</strong> es requerido");
        }
        if (colonyJQuery.val() == null || colonyJQuery.val() == "") {
            valid && (valid = false);
            errorList.push("<strong>Colonia</strong> es requerida, seleccione una opción");
        }
        if (externalNumberJQuery.val() == null || externalNumberJQuery.val() == "") {
            valid && (valid = false);
            errorList.push("<strong>Número Exterior</strong> es requerido");
        }
        if (internalNumberJQuery.val() == null || internalNumberJQuery.val() == "") {
            valid && (valid = false);
            errorList.push("<strong>Número Interior</strong> es requerido");
        }
        if (streetJQuery.val() == null || streetJQuery.val() == "") {
            valid && (valid = false);
            errorList.push("<strong>Calle</strong> es requerida");
        }
        return valid;
    }
    function validateContact() {
        let valid = true;
        if (phoneTypeJQuery.val() == null || phoneTypeJQuery.val() == "") {
            valid = false;
            errorList.push("<strong>Tipo de Teléfono</strong> es requerido, seleccione una opción válida");
        }
        if (phoneNumberJQuery.val() == null || phoneNumberJQuery.val() == "") {
            valid && (valid = false);
            errorList.push("<strong>Número Telefónico</strong> es requerido");
        }
        if (emailTypeJQuery.val() == null || emailTypeJQuery.val() == "") {
            valid && (valid = false);
            errorList.push("<strong>Tipo de Correo</strong> es requerido, seleccione una opción válida");
        }
        if (emailJQuery.val() == null || emailJQuery.val() == "") {
            valid && (valid = false);
            errorList.push("<strong>Correo Electrónico</strong> es requerido");
        }
        return valid;
    }
    btnSaveRequestJQuery.on('click', function () {
        var _a, _b, _c, _d, _e, _f, _g, _h, _j, _k, _l, _m, _o, _p, _q, _r, _s, _t, _u, _v, _w, _x;
        if (validateGeneralInformation() && validateAddress() && validateContact()) {
            let endpointRequest = localApiRequest + "/request", person = {}, address = {}, request = {}, colonyName = colonyJQuery.find('option:selected').text(), fullAddress;
            person.firstName = (_a = firstNameJQuery.val()) === null || _a === void 0 ? void 0 : _a.toString();
            person.secondName = (_b = secondNameJQuery.val()) === null || _b === void 0 ? void 0 : _b.toString();
            person.lastName = (_c = lastNameJQuery.val()) === null || _c === void 0 ? void 0 : _c.toString();
            person.secondLastName = (_d = secondLastNameJQuery.val()) === null || _d === void 0 ? void 0 : _d.toString();
            person.fullName = (_e = fullNameJQuery.val()) === null || _e === void 0 ? void 0 : _e.toString();
            person.gender = (_f = genderJQuery.val()) === null || _f === void 0 ? void 0 : _f.toString();
            person.birthDate = (_g = birthDateJQuery.val()) === null || _g === void 0 ? void 0 : _g.toString();
            person.curp = (_h = curpJQuery.val()) === null || _h === void 0 ? void 0 : _h.toString();
            person.rfc = (_j = rfcJQuery.val()) === null || _j === void 0 ? void 0 : _j.toString();
            person.type = (_k = personTypeJQuery.val()) === null || _k === void 0 ? void 0 : _k.toString();
            person.foreignStatus = (_l = foreignStatusJQuery.val()) === null || _l === void 0 ? void 0 : _l.toString();
            address.street = (_m = streetJQuery.val()) === null || _m === void 0 ? void 0 : _m.toString();
            address.externalNumber = (_o = externalNumberJQuery.val()) === null || _o === void 0 ? void 0 : _o.toString();
            address.internalNumber = (_p = internalNumberJQuery.val()) === null || _p === void 0 ? void 0 : _p.toString();
            address.zipcode = (_q = zipcodeJQuery.val()) === null || _q === void 0 ? void 0 : _q.toString();
            address.colonyId = (_r = colonyJQuery.val()) === null || _r === void 0 ? void 0 : _r.toString();
            fullAddress = streetJQuery.val() + ", " + internalNumberJQuery.val() + ", " + externalNumberJQuery.val() + ", Código Postal:" + zipcodeJQuery.val() + ", ";
            fullAddress += colonyName + ", " + partialAddress;
            address.fullAddress = fullAddress;
            request.type = (_s = trustTypeIdJQuery.val()) === null || _s === void 0 ? void 0 : _s.toString();
            request.person = person;
            request.address = address;
            request.trustChange = (_t = trustChangeJQuery.val()) === null || _t === void 0 ? void 0 : _t.toString();
            request.trustChangeTrust = (_u = trustChangeTrustJQuery.val()) === null || _u === void 0 ? void 0 : _u.toString();
            request.wasRefered = (_v = wasReferedJQuery.val()) === null || _v === void 0 ? void 0 : _v.toString();
            request.wasReferedBy = (_w = wasReferedByJQuery.val()) === null || _w === void 0 ? void 0 : _w.toString();
            request.wasReferedByFullName = (_x = wasReferedByFullNameJQuery.val()) === null || _x === void 0 ? void 0 : _x.toString();
            console.dir(JSON.stringify(request));
            $.ajax({
                method: 'POST',
                contentType: 'application/json',
                cache: false,
                url: endpointRequest,
                data: JSON.stringify(request)
            }).fail((jqXHR, textStatus, error) => {
                console.log(jqXHR);
                console.log(textStatus);
                console.log(error);
            })
                .then((result, textStatus, jqXHR) => {
                if (result != undefined) {
                    let resultJson = JSON.parse(result);
                    if (resultJson.status == 1) {
                        console.log("Registrado!");
                        confirmOperationModal.modal('show');
                        setTimeout(function () {
                            window.location.href = "/request/request?status=1";
                        }, 5000);
                    }
                    else if (resultJson.status == 0) {
                        console.log(result.message);
                    }
                }
            });
        }
    });
});
