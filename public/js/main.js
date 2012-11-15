var Clients = (function () {
    var Clients = {
        clients:[
            {name:"SG", position:{lat:48.8920, lng:2.2315}},
            {name:"EGencia", position:{lat:48.8892, lng:2.2404}},
            {name:"VSC", position:{lat:48.8915, lng:2.2411}},
            {name:"Xebia", position:{lat:48.8753, lng:2.3112}},
            {name:"Gare du nord", position:{lat:48.8805, lng:2.3551}},
            {name:"FinActive", position:{lat:48.8693, lng:2.3424}},
            {name:"Orange", position:{lat:48.8180, lng:2.3200}}
        ],
        closeBy:function (pos) {
            return this.clients.map(withDistance(pos)).filter(isNear)
        }
    };

    function withDistance(pos) {
        return function (client) {
            var clientPos = new google.maps.LatLng(client.position.lat, client.position.lng);
            client.distance = distanceTo(clientPos, pos) * 1000;
            return client;
        }
    }

    function isNear(client) {
        if (client.distance == "undefined") return false;
        return client.distance < 500
    }

    function distanceTo(source, dest, precision) {
        // default 4 sig figs reflects typical 0.3% accuracy of spherical model
        if (typeof precision == 'undefined') precision = 4;

        var R = 6371; //earth radius in km
        var lat1 = toRad(source.lat()), lon1 = toRad(source.lng());
        var lat2 = toRad(dest.lat()), lon2 = toRad(dest.lng());
        var dLat = lat2 - lat1;
        var dLon = lon2 - lon1;

        var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(lat1) * Math.cos(lat2) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        var d = R * c;
        return toPrecisionFixed(d, precision);
    }

    function toRad(number) {
        return number * Math.PI / 180;
    }

    function toPrecisionFixed(number, precision) {

        // use standard toPrecision method
        var n = number.toPrecision(precision);

        // ... but replace +ve exponential format with trailing zeros
        n = n.replace(/(.+)e\+(.+)/, function (n, sig, exp) {
            sig = sig.replace(/\./, '');       // remove decimal from significand
            l = sig.length - 1;
            while (exp-- > l) sig = sig + '0'; // append zeros from exponent
            return sig;
        });

        // ... and replace -ve exponential format with leading zeros
        n = n.replace(/(.+)e-(.+)/, function (n, sig, exp) {
            sig = sig.replace(/\./, '');       // remove decimal from significand
            while (exp-- > 1) sig = '0' + sig; // prepend zeros from exponent
            return '0.' + sig;
        });

        return n;
    }


    return Clients;
})();


