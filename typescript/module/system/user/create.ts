/**
 * @author Giovanni Rodriguez <grodriguez@bcbcasadebolsa.com>
 */
$(() => {
    const milisecondsOnOneYear: number = 31557600000, // 1000 * 60 * 60 * 24 * 365 // Miliseconds on one year
        btnSaveUserJQuery: JQuery = $("#btnSaveUser"),
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
        confirmOperationModalJQuery: JQuery = $("#confirmOperationModal"),
        serverErrorModalJQuery: JQuery = $("#serverErrorModal"),
        divUserAlertJQuery: JQuery = $("#divUserAlert"),
        userAlertJQuery: JQuery = $("#userAlert"),
        userAlertStrongJQuery: JQuery = $("#userAlertStrong"),
        userAlertSpanJQuery: JQuery = $("#userAlertSpan"),
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
        type: number | undefined,
    }

    type User = {
        email: string | undefined,
        nickname: string | undefined,
        person: Person | undefined
    }

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

        if(curpJQuery.val() == null || curpJQuery.val() == "") {
            errorList.push("<strong>CURP</strong> es requerido");
        }

        if(rfcJQuery.val() == null || rfcJQuery.val() == "") {
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

        console.log("validatePerson: " + valid);

        return valid;
    }
    
    function validateEmail() {
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        const validateEmail = (email: string) => emailRegex.test(email);

        let valid: boolean = true;

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

        console.log("validateEmail: " + valid);

        return valid;
    }

    emailJQuery.on('change', function(this: any) {
        let self = $(this),
            email: string = self.val() != null ? self.val().toString() : "",
            nickname: string,
            uniqueEmail: boolean;

        uniqueEmail = false;

        if(email != "") {
            let url: string = localApiSystem + '/user/unique-email?email=' + self.val();

            $.getJSON(url, function(result) {
                if(result != undefined) {
                    if(result.status == 1) {
                        divUserAlertJQuery.removeAttr('hidden');
                        if(result.data == 1) {
                            uniqueEmail = true;
                            if(self.val() != null && self.val() != undefined && self.val() != "") {
                                email = self.val().toString();
                                nickname = email.split("@")[0];
                                nicknameJQuery.val(nickname);
                
                                userAlertJQuery.addClass("alert-success").removeClass("alert-danger").removeClass("alert-warning");
                                userAlertStrongJQuery.text("Correo Electrónico Disponible");
                                userAlertSpanJQuery.text("El correo electrónico que ingresó está disponible para registrarse");
                            }
                        } else {
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

    $(".form-control, .form-select").on('change', function() {
        let validado: boolean = validateEmail() && validatePerson();
        console.log("validado: " + validado);

        if(validado) {
            btnSaveUserJQuery.removeAttr('hidden');
        } else {
            btnSaveUserJQuery.attr('hidden', 'hidden');
        }
    });

    btnSaveUserJQuery.on('click', function() {
        if(validatePerson() && validateEmail()) {
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
            personJson.type = 1;
            
            userJson.person = personJson;
            userJson.email = emailJQuery.val()?.toString();
            userJson.nickname = nicknameJQuery.val()?.toString();

            $.ajax({
                method: 'POST',
                contentType: 'application/json',
                cache: false,
                url: endpointUser,
                data: JSON.stringify(userJson),
                success: ((result, textStatus, jqXHR) => {
                    if(result != undefined) {
                        let resultJson = JSON.parse(result);
        
                        if(resultJson.status == 1) {
                            // @ts-ignore
                            confirmOperationModalJQuery.modal('show');
                            
                            setTimeout(function() {
                                window.location.href = "/system/user?status=1";
                            }, 5000);
        
                        } else if(resultJson.status == 0) {
                            setTimeout(function() {
                                console.log(resultJson.message);
                                $("#errorMessage").text(resultJson.message);
                                // @ts-ignore
                                serverErrorModalJQuery.modal('show');
                            }, 5000);
                        }
                    } else {
                        // @ts-ignore
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