/**
 * @author Giovanni Rodriguez <grodriguez@bcbcasadebolsa.com>
 */
$(() => {
    const milisecondsOnOneYear: number = 31557600000, // 1000 * 60 * 60 * 24 * 365 // Miliseconds on one year
        btnValidateJQuery: JQuery = $("#btnValidate"),
        btnSaveUserJQuery: JQuery = $("#btnSaveUser"),
        profileJQuery: JQuery = $("#profile"),
        firstNameJQuery: JQuery = $("#firstName"),
        secondNameJQuery: JQuery = $("#secondName"),
        lastNameJQuery: JQuery = $("#lastName"),
        secondLastNameJQuery: JQuery = $("#secondLastName"),
        genderJQuery: JQuery = $("#gender"),
        birthDateJQuery: JQuery = $("#birthDate"),
        curpJQuery: JQuery = $("#curp"),
        rfcJQuery: JQuery = $("#rfc"),
        emailJQuery: JQuery = $("#email"),
        nicknameJQuery: JQuery = $("#nickname"),
        errorValidationModalJQuery: JQuery = $("#errorValidationModal"),
        successValidationModalJQuery: JQuery = $("#successValidationModal"),
        confirmOperationModalJQuery: JQuery = $("#confirmOperationModal"),
        serverErrorModalJQuery: JQuery = $("#serverErrorModal"),
        resultFoundModalJQuery: JQuery = $("#resultFoundModal"),
        localApiSystem: string = "/api/system";

    let errorList: string[] = [];

    type Person = {
        firstName: string | undefined,
        secondName: string | undefined,
        lastName: string | undefined,
        secondLastName: string | undefined,
        gender: string | undefined,
        birthDate: string | undefined,
        curp: string | undefined,
        rfc: string | undefined,
        personType: string | undefined,
    }

    type User = {
        email: string | undefined,
        nickname: string | undefined,
        person: Person | undefined,
        profileId: number | undefined,
        permissionList: number[]
    }

    btnValidateJQuery.on('click', function() {
        errorList = [];

        if(validateProfileAndPermissions() && validatePerson()) {
            // @ts-ignore
            successValidationModalJQuery.modal('show');
            btnSaveUserJQuery.removeAttr('hidden');
            btnValidateJQuery.attr('hidden', 'hidden');
        } else {
            errorValidationModalJQuery
                .find($("#errorDetail"))
                .html(errorList.join('<br />'));
            // @ts-ignore
            errorValidationModalJQuery.modal('show');
            btnSaveUserJQuery.attr('hidden', 'hidden');
            btnValidateJQuery.removeAttr('hidden');
        }

    });

    profileJQuery.on('change', function(this: any) {
        let self = $(this),
            profileId = Number(self.val()),
            tbody = $("#profilePermissionTable").find("tbody");

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
            if(result != undefined) {
                let resultJson = JSON.parse(result);
    
                if(resultJson.status == 1) {
                    setTimeout(function() {
                        // @ts-ignore
                        resultFoundModalJQuery.modal('show');
                    }, 1000);

                    let data = resultJson.data;
                    tbody.empty();
                    $.each(data, function(i, item) {
    
                        let tr = $('<tr>'), 
                            tdId = $('<td>', {'text': item.permissionId}),
                            tdModule = $('<td>', {'text': item.moduleAsString}),
                            tdCode = $('<td>', {'text': item.code}),
                            tdName = $('<td>', {'text': item.name});
    
                        tr.append(tdId).append(tdModule).append(tdCode).append(tdName);
                        tbody.append(tr);
                    });
    
                } else if(resultJson.status == 0) {
                    console.log(resultJson.message);
                }

            } else {
                // @ts-ignore
                serverErrorModalJQuery.modal('show');
            }
        });

        validatePerson();
    });

    function validatePerson() {
        let valid: boolean = true;

        if(firstNameJQuery.val() == null || firstNameJQuery.val() == "") {
            valid = false;
            errorList.push("<strong>Primer Nombre</strong> es requerido");
        }

        if(lastNameJQuery.val() == null || lastNameJQuery.val() == "") {
            valid &&= false;
            errorList.push("<strong>Apellido Paterno</strong> es requerido");
        }

        if(genderJQuery.val() == null) {
            valid &&= false;
            errorList.push("<strong>Género</strong> es requerido");
        }

        if(curpJQuery.val() == null || curpJQuery.val() == "" || curpJQuery.val() == "XAXX000000XXXXXX00") {
            errorList.push("<strong>CURP</strong> es requerido");
        }

        if(rfcJQuery.val() == null || rfcJQuery.val() == "" || rfcJQuery.val() == "XAXX000000XX0") {
            valid &&= false;
            errorList.push("<strong>RFC</strong> es requerido");
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

        return valid;
    }
    
    function validateProfileAndPermissions() {
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        const validateEmail = (email: string) => emailRegex.test(email);

        let valid: boolean = true;

        if(profileJQuery.val() == null || profileJQuery.val() == undefined) {
            valid &&= false;
            errorList.push("<strong>Perfil de Sistema</strong> es requerido, seleccione una opción");
        }

        if(emailJQuery.val() == null || emailJQuery.val() == undefined) {
            valid &&= false;
            errorList.push("<strong>Correo Electrónico</strong> es requerido");
        } else {
            let emailString = emailJQuery.val()?.toString() != undefined ? String(emailJQuery.val()): ''; 
            if(! validateEmail(emailString)) {
                valid &&= false;
                errorList.push("<strong>Correo Electrónico</strong> es requerido, el valor no parece ser válido");
            } 
        }

        return valid;
    }

    emailJQuery.on('change', function(this: any) {
        let self = $(this),
            email: string,
            nickname: string;
        
        if(self.val() != null && self.val() != undefined && self.val() != "") {
            email = self.val().toString();
            nickname = email.split("@")[0];
            nicknameJQuery.val(nickname);
        }
    });

    btnSaveUserJQuery.on('click', function() {
        if(validatePerson() && validateProfileAndPermissions()) {
            let endpointUser: string = localApiSystem + "/user",
                personJson: Person = {} as Person,
                userJson: User = {} as User;

            personJson.firstName = firstNameJQuery.val()?.toString();
            personJson.secondName = secondNameJQuery.val()?.toString();
            personJson.lastName = lastNameJQuery.val()?.toString();
            personJson.secondLastName = secondLastNameJQuery.val()?.toString();
            personJson.gender = genderJQuery.val()?.toString();
            personJson.birthDate = birthDateJQuery.val()?.toString();
            personJson.curp = curpJQuery.val()?.toString();
            personJson.rfc = rfcJQuery.val()?.toString();
            
            userJson.person = personJson;
            userJson.email = emailJQuery.val()?.toString();
            userJson.nickname = nicknameJQuery.val()?.toString();
            userJson.profileId = Number(profileJQuery.val());

            console.dir(userJson);

            $.ajax({
                method: 'POST',
                contentType: 'application/json',
                cache: false,
                //dataType: 'application/json',
                url: endpointUser,
                data: JSON.stringify(userJson)
            }).fail((jqXHR, textStatus, error) => {
                console.log(jqXHR);
                console.log(textStatus);
                console.log(error);
            })
            .then((result, textStatus, jqXHR) => {
                if(result != undefined) {
                    let resultJson = JSON.parse(result);
    
                    if(resultJson.status == 1) {
                        // @ts-ignore
                        confirmOperationModalJQuery.modal('show');
                        
                        setTimeout(function() {
                            window.location.href = "/system/user?status=1";
                        }, 5000);
    
                    } else if(resultJson.status == 0) {
                        console.log(resultJson.message);
                        $("#errorMessage").text(resultJson.message);
                        // @ts-ignore
                        serverErrorModalJQuery.modal('show');
                    }
                } else {
                    // @ts-ignore
                    serverErrorModalJQuery.modal('show');
                }
            });
        }
    });
});