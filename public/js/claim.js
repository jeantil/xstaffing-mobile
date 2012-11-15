var ClaimView = (function (args) {

    var claimView={

        showWhere:function(event){
            $('#status').html(where_template({position:{lat:latlng.lat(),lng:latlng.lng()}}));
        },


        claimClient:function(event){
            var target=$(event.currentTarget);
            var clientName=target.attr('data-id');
            alert(clientName);
            console.log(clientName);
        },
        render:function(el){
            el.html(args.clients_template({client:args.localClients}));
            $(".client").click(this.claimClient);
            $("#where").click(this.showWhere);
        }

    };
    return claimView;
});