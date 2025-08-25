/**
 * @author Giovanni Rodriguez <grodriguez@bcbcasadebolsa.com>
 */
$(() => {
    const milisecondsOnOneYear: number = 31557600000, // 1000 * 60 * 60 * 24 * 365 // Miliseconds on one year
        apiLocation: string = "http://10.20.50.132:9002",
        localApiRequest: string = "/api/request",
        OPTION_YES = 1,
        OPTION_NO = 2,
        PERSON_TYPE_PERSON = 1,
        PERSON_TYPE_ENTERPRISE = 2,
        btnSaveRequestJQuery: JQuery = $("#btnSaveRequest"),
        btnValidateJQuery: JQuery = $("#btnValidate"),
        trustTypeIdJQuery: JQuery = $("#trustTypeId"),
        personTypeJQuery: JQuery = $("#personType"),
        rfcEnterpriseJQuery: JQuery = $("#rfcEnterprise"),
        fullNameJQuery: JQuery = $("#fullName"),
        firstNameJQuery: JQuery = $("#firstName"),
        secondNameJQuery: JQuery = $("#secondName"),
        lastNameJQuery: JQuery = $("#lastName"),
        secondLastNameJQuery: JQuery = $("#secondLastName"),
        genderJQuery: JQuery = $("#gender"),
        birthDateJQuery: JQuery = $("#birthDate"),
        curpJQuery: JQuery = $("#curp"),
        rfcJQuery: JQuery = $("#rfc"),
        zipcodeJQuery: JQuery = $("#zipcode"),
        stateJQuery: JQuery = $("#state"),
        townshipJQuery: JQuery = $("#township"),
        colonyJQuery: JQuery = $("#colony"),
        externalNumberJQuery: JQuery = $("#externalNumber"),
        internalNumberJQuery: JQuery = $("#internalNumber"),
        streetJQuery: JQuery = $("#street"),
        phoneTypeJQuery: JQuery = $("#phoneType"),
        phoneNumberJQuery: JQuery = $("#phoneNumber"),
        emailTypeJQuery: JQuery = $("#emailType"),
        emailJQuery: JQuery = $("#email"),
        trustChangeJQuery: JQuery = $("#trustChange"),
        trustChangeTrustJQuery: JQuery = $("#trustChangeTrust"),
        wasReferedJQuery: JQuery = $("#wasRefered"),
        wasReferedByJQuery: JQuery = $("#wasReferedBy"),
        wasReferedByFullNameJQuery: JQuery = $("#wasReferedByFullName"),
        foreignStatusJQuery: JQuery = $("#foreignStatus"),
        resultNotFoundModal: JQuery = $("#resultNotFoundModal"),
        serverErrorModal: JQuery = $("#serverErrorModal"),
        errorValidationModalJQuery: JQuery = $("#errorValidationModal"),
        successValidationModalJQuery: JQuery = $("#successValidationModal");
    let errorList: string[] = [],
        partialAddress: string = "";

    type Person = {
        firstName: string | undefined,
        secondName: string | undefined,
        lastName: string | undefined,
        secondLastName: string | undefined,
        fullName: string | undefined,
        gender: string | undefined,
        birthDate: string | undefined,
        curp: string | undefined,
        rfc: string | undefined,
        type: string | undefined,
        foreignStatus: string | undefined,
    }

    type Address = {
        street: string | undefined,
        externalNumber: string | undefined,
        internalNumber: string | undefined,
        zipcode: string | undefined,
        colonyId: string | undefined,
        fullAddress: string | undefined
    }

    type Request = {
        number: string | undefined,
        type: string | undefined,
        person: Person | undefined,
        address: Address | undefined,
        registeredBy: string | undefined,
        trustChange: string | undefined,
        trustChangeTrust: string | undefined,
        wasRefered: string | undefined,
        wasReferedBy: string | undefined,
        wasReferedByFullName: string | undefined,
    }

    $(".request-validate").on('change', function() {
        if(validateGeneralInformation() && validateAddress() && validateContact()) {
            btnSaveRequestJQuery.removeAttr('hidden');
            btnValidateJQuery.attr('hidden', 'hidden');
        } else {
            btnSaveRequestJQuery.attr('hidden', 'hidden');
            btnValidateJQuery.removeAttr('hidden');
        }
    });

    trustChangeJQuery.on('change', function(this: any) {
        let self = $(this),
            trustChange = Number(self.val());

        if(trustChange != null && trustChange == OPTION_YES) {
            $(".trustChange").removeAttr('hidden');
        } else {
            $(".trustChange").attr('hidden', 'hidden');
        }
    });

    wasReferedJQuery.on('change', function(this: any) {
        let self = $(this),
            wasRefered = self.val();
        
        if(wasRefered != null && wasRefered == OPTION_YES) {
            $(".wasRefered").removeAttr('hidden');
        } else {
            $(".wasRefered").attr('hidden', 'hidden');
        }
    });

    personTypeJQuery.on('change', function(this: any) {
        let self: JQuery = $(this),
            personType = Number(self.val());

        if(PERSON_TYPE_PERSON == personType) {
            $("#personTypePerson").removeAttr("hidden");
            $("#personTypeEnterprise").attr("hidden", "hidden");
        } else {
            $("#personTypePerson").attr("hidden", "hidden");
            $("#personTypeEnterprise").removeAttr("hidden");
        }
    });

    zipcodeJQuery.on('change', function(this: any) {
        let self: JQuery = $(this),
        zipcode: number = Number(self.val()),
        locationUrl: string = `${apiLocation}/api/location/byzipcode?zipcode=${zipcode}`;

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
                if(textStatus == "timeout") {
                    $("#errorMessage").html("Timeout: Sin conexión al endpoint <strong>BCB Location</strong>");
                } else {
                    $("#errorMessage").text("Error desconocido");
                }
                // @ts-ignore
                serverErrorModal.modal('show');

                setTimeout(function() {
                    // @ts-ignore
                    serverErrorModal.modal('hide');
                }, 5000);

        }).then((result, textStatus, jqXHR) => {
            if(result != undefined) {
                if(result.state) {
                    let country = result.data.country,
                        state = result.data.state,
                        states = result.data.states,
                        township = result.data.township,
                        townships = result.data.townships,
                        colonies = result.data.colonies;
                    
                    let emptyOptionOne = $("<option>", {"value": 0, "text": "Seleccione Opción", "selected":"selected", "disabled":"disabled"});
                    partialAddress = township.name + ", " + state.name + ", " + country.name;
                    stateJQuery.append(emptyOptionOne);
                    $.each(states, function(i, item) {
                        let option = $("<option>", {"value": item.stateId, "text": item.name});
                        if(state.stateId == item.stateId) {
                            option.attr("selected", "selected");
                        }
                        stateJQuery.append(option);
                    });
                    stateJQuery.attr("disabled", "disabled");

                    let emptyOptionTwo = $("<option>", {"value": 0, "text": "Seleccione Opción", "selected":"selected", "disabled":"disabled"});
                    townshipJQuery.append(emptyOptionTwo);
                    $.each(townships, function(i, item){
                        let option = $("<option>", {"value": item.townshipId, "text": item.name});
                        if(township.townshipId == item.townshipId) {
                            option.attr("selected", "selected");
                        }
                        townshipJQuery.append(option);
                    });
                    townshipJQuery.attr("disabled", "disabled");

                    let emptyOptionThree = $("<option>", {"value": 0, "text": "Seleccione Opción", "selected":"selected", "disabled":"disabled"});
                    colonyJQuery.append(emptyOptionThree);
                    $.each(colonies, function(i, item) {
                        let option = $("<option>", {"value": item.colonyId, "text": item.name});
                        colonyJQuery.append(option);
                    });
                } else {
                    // @ts-ignore
                    resultNotFoundModal.modal('show');
                }
            } else {
                $("#errorMessage").text("Sin conexión al endpoint");
                // @ts-ignore
                serverErrorModal.modal('show');
            }
            
            
        });
    });

    btnValidateJQuery.on('click', function() {
        errorList = [];

        if(validateGeneralInformation() && validateAddress() && validateContact()) {
            // @ts-ignore
            successValidationModalJQuery.modal('show');
            btnSaveRequestJQuery.removeAttr('hidden');
            btnValidateJQuery.attr('hidden', 'hidden');
        } else {
            errorValidationModalJQuery
                .find($("#errorDetail"))
                .html(errorList.join('<br />'));

            // @ts-ignore
            errorValidationModalJQuery.modal('show');
            btnSaveRequestJQuery.attr('hidden', 'hidden');
            btnValidateJQuery.removeAttr('hidden');
        }
    });

    function validateGeneralInformation() {
        let valid: boolean = true;

        if (trustTypeIdJQuery.val() == null || trustTypeIdJQuery.val() == "0") {
            valid &&= false;
            errorList.push("<strong>Tipo de Fideicomiso</strong> es requerido, seleccione una opción");
        }

        if (trustChangeJQuery.val() == null == undefined || trustChangeJQuery.val() == null || trustChangeJQuery.val() == "") {
            valid &&= false;
            errorList.push("<strong>Es cambio de Fiduciario?</strong> es requerido, seleccione una opción");
        } else {
            let trustChange = Number(trustChangeTrustJQuery.val());
            if (OPTION_YES === trustChange) {
                valid &&= false;
                errorList.push("<strong>Fiduciario Anterior</strong> es requerido, especifique");
            }
        }

        if (wasReferedJQuery.val() == undefined || wasReferedJQuery.val() == null || wasReferedJQuery.val() == "") {
            valid &&= false;
            errorList.push("<strong>Fue recomendado?</strong> es requerido, seleccione una opción");
        } else {
            let wasRefered = Number(wasReferedJQuery.val());
            if(OPTION_YES == wasRefered) {
                valid &&= false;
                errorList.push("<strong>Recomendado por</strong> es requerido, seleccione una opción");
                
                if(wasReferedByFullNameJQuery.val() == undefined || wasReferedByFullNameJQuery.val() == null || wasReferedByFullNameJQuery.val() == "") {
                    valid &&= false;
                    errorList.push("<strong>Nombre Completo</strong> es requerido, especifique");
                }
            }

        }

        if (trustTypeIdJQuery.val() == null || trustTypeIdJQuery.val() == "0") {
            valid &&= false;
            errorList.push("<strong>Tipo de Fideicomiso</strong> es requerido, seleccione una opción");
        }

        let personTypeObj = personTypeJQuery.val();
        if (personTypeJQuery.val() == null || personTypeJQuery.val() == "0") {
            valid = false;
            errorList.push("<strong>Tipo de Persona</strong> es requerido, seleccione una opción");
        } else {
            if(personTypeObj != null) {
                let personType: number = Number(personTypeObj);

                if(PERSON_TYPE_PERSON == personType) {
                    if(firstNameJQuery.val() == null || firstNameJQuery.val() == "") {
                        valid &&= false;
                        errorList.push("<strong>Primer Nombre</strong> es requerido");
                    }

                    if(lastNameJQuery.val() == null || lastNameJQuery.val() == "") {
                        valid &&= false;
                        errorList.push("<strong>Apellido Paterno</strong> es requerido");
                    }

                    if(genderJQuery.val() == null || genderJQuery.val() == "") {
                        valid &&= false;
                        errorList.push("<strong>Género</strong> es requerido");
                    }

                    if(birthDateJQuery.val() == null || birthDateJQuery.val() == "") {
                        valid &&= false;
                        errorList.push("<strong>Fecha de Nacimiento</strong> es requerida");
                    } else {
                        let birthDateStr = birthDateJQuery.val()?.toString();

                        if (birthDateStr != null || birthDateStr != undefined) {
                            let today = new Date(),
                                birthDate = new Date(birthDateStr),
                                milisecondsBetweenDates = today.getTime() - birthDate.getTime(),
                                years = milisecondsBetweenDates / milisecondsOnOneYear;

                            if(years < 18) { // 18 years legal age on México
                                valid &&= false;
                                errorList.push("<strong>Fecha de Nacimiento</strong> es menor a 18 años");
                            }
                        } else {
                            valid &&= false;
                            errorList.push("<strong>Fecha de Nacimiento</strong> ocurrio un error al transformar la fecha de nacimiento");
                        }
                    }

                    if(curpJQuery.val() == null || curpJQuery.val() == "XAXX000000XXXXXX00") {
                        valid &&= false;
                        errorList.push("<strong>CURP</strong> es requerido, ingrese un valor válido");
                    }

                    if(rfcJQuery.val() == null || rfcJQuery.val() == "XAXX000000XX0") {
                        valid &&= false;
                        errorList.push("<strong>RFC</strong> es requerido, ingrese un valor válido");
                    }

                } else if(PERSON_TYPE_ENTERPRISE == personType) {
                    if(rfcEnterpriseJQuery.val() == null || rfcEnterpriseJQuery.val() == "XAXX000000XX0") {
                        valid &&= false;
                        errorList.push("<strong>RFC</strong> es requerido, ingrese un valor válido");
                    }

                    if(fullNameJQuery.val() == null || fullNameJQuery.val() == "") {
                        valid &&= false;
                        errorList.push("<strong>Razón Social</strong> es requerido, ingrese un valor válido");
                    }
                }
            }
        }

        if(foreignStatusJQuery.val() == null || foreignStatusJQuery.val() == "0") {
            valid &&= false;
            errorList.push("<strong>Estatus Migratorio</strong> es requerido, seleccione una opción");
        }

        return valid;
    }

    function validateAddress() {
        let valid: boolean = true;

        if(zipcodeJQuery.val() == null || zipcodeJQuery.val() == "") {
            valid = false;
            errorList.push("<strong>Código Postal</strong> es requerido");
        }

        if(colonyJQuery.val() == null || colonyJQuery.val() == "") {
            valid &&= false;
            errorList.push("<strong>Colonia</strong> es requerida, seleccione una opción");
        }

        if(externalNumberJQuery.val() == null || externalNumberJQuery.val() == "") {
            valid &&= false;
            errorList.push("<strong>Número Exterior</strong> es requerido");
        }

        if(internalNumberJQuery.val() == null || internalNumberJQuery.val() == "") {
            valid &&= false;
            errorList.push("<strong>Número Interior</strong> es requerido");
        }

        if(streetJQuery.val() == null || streetJQuery.val() == "") {
            valid &&= false;
            errorList.push("<strong>Calle</strong> es requerida");
        }

        return valid;
    }

    function validateContact() {
        let valid: boolean = true;

        if(phoneTypeJQuery.val() == null || phoneTypeJQuery.val() == "") {
            valid = false;
            errorList.push("<strong>Tipo de Teléfono</strong> es requerido, seleccione una opción válida");
        }

        if(phoneNumberJQuery.val() == null || phoneNumberJQuery.val() == "") {
            valid &&= false;
            errorList.push("<strong>Número Telefónico</strong> es requerido");
        }

        if(emailTypeJQuery.val() == null || emailTypeJQuery.val() == "") {
            valid &&= false;
            errorList.push("<strong>Tipo de Correo</strong> es requerido, seleccione una opción válida");
        }

        if(emailJQuery.val() == null || emailJQuery.val() == "") {
            valid &&= false;
            errorList.push("<strong>Correo Electrónico</strong> es requerido");
        }

        return valid;
    }

    btnSaveRequestJQuery.on('click', function () {
        if(validateGeneralInformation() && validateAddress() && validateContact()) {
            let endpointRequest: string = localApiRequest + "/request",
                person = {} as Person,
                address = {} as Address,
                request = {} as Request,
                colonyName: string = colonyJQuery.find('option:selected').text(),
                fullAddress: string;

            person.firstName = firstNameJQuery.val()?.toString();
            person.secondName = secondNameJQuery.val()?.toString();
            person.lastName = lastNameJQuery.val()?.toString();
            person.secondLastName = secondLastNameJQuery.val()?.toString();
            person.fullName = fullNameJQuery.val()?.toString();
            person.gender = genderJQuery.val()?.toString();
            person.birthDate = birthDateJQuery.val()?.toString();
            person.curp = curpJQuery.val()?.toString();
            person.rfc = rfcJQuery.val()?.toString();
            person.type = personTypeJQuery.val()?.toString();
            person.foreignStatus = foreignStatusJQuery.val()?.toString();

            address.street = streetJQuery.val()?.toString();
            address.externalNumber = externalNumberJQuery.val()?.toString();
            address.internalNumber = internalNumberJQuery.val()?.toString();
            address.zipcode = zipcodeJQuery.val()?.toString();
            address.colonyId = colonyJQuery.val()?.toString();
            fullAddress = streetJQuery.val()+ ", " + internalNumberJQuery.val() + ", " + externalNumberJQuery.val() + ", Código Postal: " + zipcodeJQuery.val() + ", ";
            fullAddress += colonyName + ", " + partialAddress;
            address.fullAddress = fullAddress;

            //request.number 
            request.type = trustTypeIdJQuery.val()?.toString();
            request.person = person;
            request.address = address;
            request.trustChange = trustChangeJQuery.val()?.toString();
            request.trustChangeTrust = trustChangeTrustJQuery.val()?.toString();
            request.wasRefered = wasReferedJQuery.val()?.toString();
            request.wasReferedBy = wasReferedByJQuery.val()?.toString();
            request.wasReferedByFullName = wasReferedByFullNameJQuery.val()?.toString();

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
                if(result != undefined) {
                    let resultJson = JSON.parse(result);

                    if(resultJson.status == 1) {
                        console.log("Registrado!");
                        // @ts-ignore
                        confirmOperationModal.modal('show');
                        setTimeout(function() {
                            window.location.href = "/request/request?status=1";
                        }, 5000);
                    } else if(resultJson.status == 0) {
                        console.log(result.message);
                    }
                }
                
            });
        }
        

    }); 

});
