/**
 * @author Giovanni Rodriguez <grodriguez@bcbcasadebolsa.com>
 */
$(() => {
    const btnUpdateProfileJQuery: JQuery = $("#btnSaveProfile"),
        profileIdJQuery: JQuery = $("#profileId"),
        nameJQuery: JQuery = $("#name"),
        membersJQuery: JQuery = $("#members"),
        localApiSystem: string = "/api/system",
        permissionIds: string[] = [],
        permissionTable = $("#permissionTable"),
        confirmOperationModal: JQuery = $("#confirmOperationModal");

        type SystemProfileObj = {
            name: string | undefined,
            members: string | undefined,
            permissionIds: string[]
        };

        btnUpdateProfileJQuery.on('click', function(this: any) {
            let self:JQuery = $(this),
                endpointProfile: string = localApiSystem + "/profile/" + profileIdJQuery.val(),
                profileObject = {} as SystemProfileObj;

            permissionTable.find("tbody tr td input.permission:checked").each(function(this: any) {
                let value = String($(this).attr("permission"));

                permissionIds.push(value);
            });

            profileObject.name = nameJQuery.val()?.toString();
            profileObject.members = membersJQuery.val()?.toString();
            profileObject.permissionIds = permissionIds;

            $.ajax({
                method: 'PUT',
                contentType: 'application/json',
                cache: false,
                url: endpointProfile,
                data: JSON.stringify(profileObject)
            }).fail((jqXHR, textStatus, error) => {
                console.log(jqXHR);
                console.log(textStatus);
                console.log(error);
            })
            .then((result, textStatus, jqXHR) => {
                let resultObj = JSON.parse(result);

                if(resultObj.status == 1) {
                    console.dir(resultObj);
                    console.log("Registrado!");
                    // @ts-ignore
                    confirmOperationModal.modal('show');
                    
                    setTimeout(function() {
                        window.location.href = "/system/profile";
                    }, 5000);
                    
                } else if(result.status == 0) {
                    console.log(result.message);
                }
            });
        });

});