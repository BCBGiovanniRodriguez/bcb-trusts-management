/**
 * @author Giovanni Rodriguez <grodriguez@bcbcasadebolsa.com>
 */
$(() => {
    const btnSaveRequestJQuery: JQuery = $("#btnSaveRequest"),
        personTypeIdJQuery: JQuery = $("#personTypeId"),   
        zipcodeJQuery: JQuery = $("#zipcode"),
        apiLocation: string = "http://10.20.50.132:9002",
        stateJQuery: JQuery = $("#state"),
        townshipJQuery: JQuery = $("#township"),
        colonyJQuery: JQuery = $("#colony"),
        resultNotFoundModal: JQuery = $("#resultNotFoundModal");

    personTypeIdJQuery.on('change', function(this: any){
        let self: JQuery = $(this),
            personTypeId = Number(self.val());

            if(personTypeId == 1) {
                $("#personType").removeAttr("hidden");
                $("#enterpriseType").attr("hidden", "hidden");
            } else {
                $("#personType").attr("hidden", "hidden");
                $("#enterpriseType").removeAttr("hidden");
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
            url: locationUrl
        }).then((result, textStatus, jqXHR) => {
            //console.dir(result);
            if(result.state) {
                let country = result.data.country,
                    state = result.data.state,
                    states = result.data.states,
                    township = result.data.township,
                    townships = result.data.townships,
                    colonies = result.data.colonies;
                
                let emptyOptionOne = $("<option>", {"value": 0, "text": "Seleccione Opción", "selected":"selected", "disabled":"disabled"});
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
        });
    });


});
