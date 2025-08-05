/**
 * @author Giovanni Rodriguez <grodriguez@bcbcasadebolsa.com>
 */
$(() => {
    const btnSaveProfileJQuery: JQuery = $("#btnSaveProfile"),
        nameJQuery: JQuery = $("#name"),
        membersJQuery: JQuery = $("#members"),
        localApiSystem: string = "/api/system";

        type SystemProfile = {
            name: string | undefined,
            members: string | undefined,
        };

    btnSaveProfileJQuery.on('click', function(this: any){
        let self:JQuery = $(this),
            endpointProfile: string = localApiSystem + "/profile",
            profileObject = {} as SystemProfile;

            profileObject.name = nameJQuery.val()?.toString();
            profileObject.members = membersJQuery.val()?.toString();
        
        $.ajax({
            method: 'POST',
            contentType: 'application/json',
            cache: false,
            //dataType: 'application/json',
            url: endpointProfile,
            data: JSON.stringify(profileObject)
        }).fail((jqXHR, textStatus, error) => {
            console.log(jqXHR);
            console.log(textStatus);
            console.log(error);
        })
        .then((result, textStatus, jqXHR) => {
            if(result.status == 1) {
                console.log("Registrado!");
            } else if(result.status == 0) {
                console.log(result.message);
            }
        });
    });
});