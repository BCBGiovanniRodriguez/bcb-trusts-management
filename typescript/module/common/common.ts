/**
 * @author Giovanni Rodriguez <grodriguez@bcbcasadebolsa.com>
 */
$(() => {
    const localApiSystem: string = '/api/system',
        systemUserData = sessionStorage.getItem('systemUser'),
        spanNavUsernameJQuery: JQuery = $("#spanNavUsername"),
        spanCardUsernameJQuery: JQuery = $("#spanCardUsername"),
        spanCardProfileJQuery: JQuery = $("#spanCardProfile"),
        spanCardUserCreatedJQuery: JQuery = $("#spanCardUserCreated");

    type Profile = {
        profileId: string | undefined,
        name: string | undefined,
        members: string | undefined,
        status: string | undefined,
        created: string | undefined
    }

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
        userId: string | undefined,
        nickname: string | undefined,
        email: string | undefined,
        profile: Profile | undefined,
        person: Person | undefined,
        status: string | undefined,
        created: string | undefined
    }

    if(systemUserData == undefined) {
        loadFrontendData();
    } else {
        const systemUser= JSON.parse(systemUserData);
        console.dir(systemUser);
        
        let username: string = systemUser?.nickname != undefined ? systemUser.nickname : '',
            profile: string = systemUser?.profile?.name != undefined ? systemUser.profile.name : '',
            created: string = systemUser?.created != undefined ? systemUser.created : '';

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
                // @ts-ignore
                serverErrorModal.modal('show');

                setTimeout(function() {
                    // @ts-ignore
                    serverErrorModal.modal('hide');
                }, 5000);

        }).then((result, textStatus, jqXHR) => {
            if(result != undefined) {
                const resultJson = JSON.parse(result);
                sessionStorage.setItem('systemUserData', result);
                
                if(resultJson.status == 1) {
                    const systemUser = resultJson.data;
                    console.dir(systemUser);


                    const nickname = systemUser.nickname,
                        email = systemUser.email,
                        person = systemUser.person,
                        profile = systemUser.profile;

                    spanNavUsernameJQuery.text(nickname);
                    spanCardUsernameJQuery.text(systemUser.nickname);
                    spanCardProfileJQuery.text(profile.name);
                    spanCardUserCreatedJQuery.text(systemUser.created);

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
    }
});