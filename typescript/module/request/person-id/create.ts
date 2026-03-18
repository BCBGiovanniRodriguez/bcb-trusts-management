/**
 * @author Giovanni Rodriguez <grodriguez@bcbcasadebolsa.com>
 */
$(() => {
    //apiLocation: string = "http://10.20.50.132:9002",
    const milisecondsOnOneYear: number = 31557600000, // 1000 * 60 * 60 * 24 * 365 // Miliseconds on one year
        apiLocation: string = "http://localhost:9002",
        localApiRequest: string = "/api/request",
        RFC_ENTERPRISE_DEFAULT = "XAX000000XX0",
        RFC_PERSON_DEFAULT = "XAXX000000XX0",
        CURP_DEFAULT = "XAXX000000XXXXXX00",
        FOREIGN_STATUS_NATIONAL = 1,
        FOREIGN_STATUS_FOREIGNER = 2,
        PHONE_NATIONAL = 1,
        PHONE_FOREIGN = 2,
        PERSON_TYPE_PERSON = 1,
        PERSON_TYPE_ENTERPRISE = 2,

        btnSavePersonJQuery: JQuery = $("#btnSavePerson"),
        btnAddAddressJQuery: JQuery = $("#btnAddAddress"),

        btnIncludePhoneJQuery: JQuery = $("#btnIncludePhone"),
        btnIncludeEmailJQuery: JQuery = $("#btnIncludeEmail"),
        btnIncludeAddressJQuery: JQuery = $("#btnIncludeAddress"),

        addressTableJQuery: JQuery = $("#addressTable"),

        personTypeJQuery: JQuery = $("#personType"),
        maritalStatusJQuery: JQuery = $("#maritalStatus"),
        foreignStatusJQuery: JQuery = $("#foreignStatus"),
        nationalityIdJQuery: JQuery = $("#nationalityId"),
        // Personal data
        fullNameJQuery: JQuery = $("#fullName"),
        firstNameJQuery: JQuery = $("#firstName"),
        secondNameJQuery: JQuery = $("#secondName"),
        lastNameJQuery: JQuery = $("#lastName"),
        secondLastNameJQuery: JQuery = $("#secondLastName"),
        genderJQuery: JQuery = $("#gender"),
        birthDateJQuery: JQuery = $("#birthDate"),
        curpJQuery: JQuery = $("#curp"),
        rfcJQuery: JQuery = $("#rfc"),
        rfcEnterpriseJQuery: JQuery = $("#rfcEnterprise"),
        // Address data
        zipcodeJQuery: JQuery = $("#zipcode"),
        stateJQuery: JQuery = $("#state"),
        townshipJQuery: JQuery = $("#township"),
        colonyJQuery: JQuery = $("#colony"),
        externalNumberJQuery: JQuery = $("#externalNumber"),
        internalNumberJQuery: JQuery = $("#internalNumber"),
        streetJQuery: JQuery = $("#street"),
        // Phone data
        phoneLocationJQuery: JQuery = $("#phoneLocation"),
        phoneTypeJQuery: JQuery = $("#phoneType"),
        phoneOwnJQuery: JQuery = $("#phoneOwn"),
        phoneNumberJQuery: JQuery = $("#phoneNumber"),
        // Email data
        emailTypeJQuery: JQuery = $("#emailType"),
        emailJQuery: JQuery = $("#email"),

        addressModalJQuery: JQuery = $("#addressModal"),

        confirmModal: JQuery = $("#confirmModal"),
        resultNotFoundModal: JQuery = $("#resultNotFoundModal"),
        serverErrorModal: JQuery = $("#serverErrorModal"),
        errorValidationModalJQuery: JQuery = $("#errorValidationModal"),
        successValidationModalJQuery: JQuery = $("#successValidationModal"),
        phoneLocationContainerJQuery: JQuery = $("#phoneLocationContainer"),
        btnModalConfirmJQuery: JQuery = $("#btnModalConfirm");

        console.log("Default Id:" + defaultNationalityId);

    personTypeJQuery.on("change", function(){
        let self = $(this),
            personType = self.val();

        if(PERSON_TYPE_ENTERPRISE == personType) {
            maritalStatusJQuery.val(0);
            maritalStatusJQuery.removeAttr("required");
            maritalStatusJQuery.attr("disabled", "disabled");
        } else if (PERSON_TYPE_PERSON == personType) {
            maritalStatusJQuery.val(0);
            maritalStatusJQuery.attr("required", "required");
            maritalStatusJQuery.removeAttr("disabled");
        }
    });

    foreignStatusJQuery.on("change", function(){
        let self = $(this),
            foreignStatus = self.val();

        if(FOREIGN_STATUS_FOREIGNER == foreignStatus) {
            nationalityIdJQuery.val(0);
            nationalityIdJQuery.removeAttr("disabled");
        } else if(FOREIGN_STATUS_NATIONAL == foreignStatus) {
            nationalityIdJQuery.val(defaultNationalityId);
            nationalityIdJQuery.attr("disabled", "disabled");
        }
    });


    $.getJSON(bcbApiNationalityEndpoint, function(result) {
        if(result != undefined) {
            if(result.status == 1) {
                nationalityIdJQuery.empty();
                let defaultOption = $("<option>", {"text": "Seleccionar Opción", "value": 0});
                nationalityIdJQuery.append(defaultOption);

                $.each(result.data, function(index, item) {
                    //console.dir(item);
                    let optionNationality = $("<option>", {"text": item.name, "value": item.nationalityId});

                    if(Number(defaultNationalityId) === Number(item.nationalityId)) {
                        console.log("Found!:");
                        console.dir(item);
                        optionNationality.attr("selected", "selected");
                    }

                    nationalityIdJQuery.append(optionNationality);
                });

            }
        }
    });

    let errorList: string[] = [],
        partialAddress: string = "";

    type Person = {
        type: string | undefined,
        maritalStatus: string | undefined,
        foreignStatus: string | undefined,
        nationalityId: string | undefined,
        firstName: string | undefined,
        secondName: string | undefined,
        lastName: string | undefined,
        secondLastName: string | undefined,
        fullName: string | undefined,
        gender: string | undefined,
        birthDate: string | undefined,
        curp: string | undefined,
        rfc: string | undefined,
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

    type Phone = {
        type: string | undefined,
        number: string | undefined,
        own: string | undefined,
    }
    
    type Email = {
        type: string | undefined,
        address: string | undefined,
        own: string | undefined,
    }

    type RequestData = {
        person: Person | undefined,
        address: Address | undefined,
        phone: Phone[] | undefined,
        email: Email[] | undefined,
    }

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

    function validatePersonInformation() {
        let valid: boolean = true;

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

                    if(secondLastNameJQuery.val() == null || secondLastNameJQuery.val() == "") {
                        valid &&= false;
                        errorList.push("<strong>Apellido Materno</strong> es requerido");
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

    function validatePhone() {
        let valid: boolean = true;

        if(phoneLocationJQuery.val() == null || phoneLocationJQuery.val() == "") {

        } else {
            let phoneLocation: number = Number(phoneLocationJQuery.val());
            
            if(phoneLocation != PHONE_NATIONAL && phoneLocation != PHONE_FOREIGN) {
                valid = false;
                errorList.push("<strong>Ubicación del Teléfono</strong> es requerida, seleccione una opción válida");
            }
        }

        if(phoneTypeJQuery.val() == null || phoneTypeJQuery.val() == "") {
            valid = false;
            errorList.push("<strong>Tipo de Teléfono</strong> es requerido, seleccione una opción válida");
        }

        if(phoneOwnJQuery.val() == null || phoneOwnJQuery.val() == "") {
            valid = false;
            errorList.push("<strong>Pertenencia</strong> es requerida, seleccione una opción válida");
        }

        if(phoneNumberJQuery.val() == null || phoneNumberJQuery.val() == "") {
            valid &&= false;
            errorList.push("<strong>Número Telefónico</strong> es requerido");
        }

        return valid;
    }

    function validateEmail() {
        let valid: boolean = true;

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

    phoneLocationJQuery.on('change', function(this: any) {
        let self: JQuery = $(this),
            phoneLocation = Number(self.val());
        
        if(phoneLocation == PHONE_FOREIGN) {
            phoneLocationContainerJQuery.removeAttr("hidden");
        } else {
            phoneLocationContainerJQuery.attr("hidden", "hidden");
        }
    });

    btnAddAddressJQuery.on('click', function() {
        if(validateAddress()) {
            let address = {} as Address,
                fullAddress: string = streetJQuery.val() + ", " + internalNumberJQuery.val() + ", " + externalNumberJQuery.val() + ", Código Postal: " + zipcodeJQuery.val() + ", ";
            fullAddress += colonyJQuery.find('option:selected').text() + ", " + partialAddress;
            $("#fullAddress").val(fullAddress);
        }
    });

    $(".address").on('change', function() {
        if(validateAddress()) {
            btnIncludeAddressJQuery.removeAttr("hidden");
        } else {
            btnIncludeAddressJQuery.attr("hidden", "hidden");
        }
    });

    $(".phone").on('change', function() {
        if(validatePhone()) {
            btnIncludePhoneJQuery.removeAttr("hidden");
        } else {
            btnIncludePhoneJQuery.attr("hidden", "hidden");
        }
    });

    $(".email").on('change', function() {
        if(validateEmail()) {
            btnIncludeEmailJQuery.removeAttr("hidden");
        } else {
            btnIncludeEmailJQuery.attr("hidden", "hidden");
        }
    });

    btnIncludeAddressJQuery.on('click', function() {
        let address = {} as Address;
        let partialAddress: string = "Calle: ";

        address.street = streetJQuery.val()?.toString();
        address.externalNumber = externalNumberJQuery.val()?.toString();
        address.internalNumber = internalNumberJQuery.val()?.toString();
        address.zipcode = zipcodeJQuery.val()?.toString();
        address.colonyId = colonyJQuery.val()?.toString();

        partialAddress += address.street + ", Número Exterior: " + address.externalNumber + ", Número Interior: " + address.internalNumber + ", Código Postal: " + address.zipcode + ", Colonia: ";
        partialAddress += colonyJQuery.find('option:selected').text() + ", Municipio: " + townshipJQuery.find('option:selected').text() + ", Estado: " + stateJQuery.find('option:selected').text();
        address.fullAddress = partialAddress;
        //

        addressTableJQuery.find("tbody").append(
            $("<tr>").append(
                $("<td>").text(address.street + ""),
                $("<td>").text(address.internalNumber + ""),
                $("<td>").text(address.externalNumber + ""),
                $("<td>").text(colonyJQuery.find('option:selected').text()),
                $("<td>").text(address.zipcode + ""),
                $("<td>").text(townshipJQuery.find('option:selected').text())
            )
        );

        // @ts-ignore
        addressModalJQuery.modal('hide');

    });

    btnSavePersonJQuery.on('click', function () {
        // @ts-ignore
        confirmModal.modal("show");
    });

    btnModalConfirmJQuery.on("click", function(){
        if(validatePersonInformation()) {
            let personIdEndpoint: string = requestEndpoint + "/person-id",
                person = {} as Person,
                fullName: string;
                
            person.type = personTypeJQuery.val()?.toString();
            person.maritalStatus = maritalStatusJQuery.val()?.toString();
            person.foreignStatus = foreignStatusJQuery.val()?.toString();
            person.nationalityId = nationalityIdJQuery.val()?.toString();

            if(PERSON_TYPE_ENTERPRISE == Number(person.type)) {
                person.firstName = "";
                person.secondName = "";
                person.lastName = "";
                person.secondLastName = "";
                person.fullName = fullNameJQuery.val()?.toString();
                person.gender = "0";
                person.birthDate = "1990-01-01"; // Reconstruir en base al RFC de la persona moral
                person.curp = CURP_DEFAULT;
                person.rfc = RFC_PERSON_DEFAULT;
            } else if(PERSON_TYPE_PERSON == Number(person.type)) {
                person.firstName = firstNameJQuery.val()?.toString();
                person.secondName = secondNameJQuery.val()?.toString();
                person.lastName = lastNameJQuery.val()?.toString();
                person.secondLastName = secondLastNameJQuery.val()?.toString();
                fullName = person.lastName + " " + person.secondLastName + ", " + person.firstName + " " + person.secondName;
                person.fullName = fullName;
                person.gender = genderJQuery.val()?.toString();
                person.birthDate = birthDateJQuery.val()?.toString();
                person.curp = curpJQuery.val()?.toString();
                person.rfc = rfcJQuery.val()?.toString();
            }

            console.dir(JSON.stringify(person));

            $.ajax({
                method: 'POST',
                contentType: 'application/json',
                cache: false,
                url: personIdEndpoint,
                data: JSON.stringify(person)
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
                        confirmModal.modal('show');
                        setTimeout(function() {
                            window.location.href = "/request/person-id-front?status=1";
                        }, 5000);
                    } else if(resultJson.status == 0) {
                        console.log(result.message);
                    }
                }
            });
        }
    });

});
