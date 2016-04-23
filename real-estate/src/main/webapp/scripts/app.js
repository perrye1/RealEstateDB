var App = angular.module('real-estate', ['ngRoute']);

	App.config(function($routeProvider) {
        $routeProvider

            // route for the home page
            .when('/', {
                templateUrl : 'home.html',
                controller  : 'mainController'
            })

            .when('/homePage', {
                templateUrl : 'home.html',
                controller  : 'mainController'
            })

            // route for the about page
            .when('/markSold', {
                templateUrl : 'markSold.html',
                controller  : 'mainController'
            })

            // route for the about page
            .when('/newListing', {
                templateUrl : 'newListing.html',
                controller  : 'mainController'
            })

            .when('/uploadImage', {
                templateUrl : 'uploadImage.html',
                controller  : 'mainController'
            })

            .when('/showImage', {
                templateUrl : 'showImage.html',
                controller  : 'mainController'
            })

            // route for the contact page
            .when('/updateListing', {
                templateUrl : 'updateListing.html',
                controller  : 'mainController'
            });
    });

    // create the controller and inject Angular's $scope
    App.controller('mainController', function($scope, $rootScope, $http) {

        // create a message to display in our view
        $scope.homePageMessage = 'Table will go here';



        $scope.whoAmI = function() {
        	$http.get("/real-estate/people?action=getCurrentPerson")
	            .then(function(resp) {
	                var me = resp.data;
	                $scope.me = me;
	            });
    	}

    	$scope.getPeople = function() {
	        $http.get("/real-estate/people?action=findAll")
	            .then(function(resp) {
	                $scope.people = resp.data;
	            });
	        setTimeout($scope.getPeople, 2000);
    	}

    	$scope.getAllListings = function() {
	        $http.get("/real-estate/listing?action=findAll")
	            .then(function(resp) {
	                $scope.listings = resp.data;
	            });
    	}

    	$scope.searchListings = function() {
	        $http.post("/real-estate/listing", {}, {
	            headers: {
	                'Content-Type': 'application/x-www-form-urlencoded'
	            },
	            params: {
	                "action": "search",
	                "min_price": $scope.searchMinPrice,
	                "max_price": $scope.searchMaxPrice,
	            }
	        })
	        	.then(function(resp) {
	 				$scope.listings = resp.data;
	            });
    	}

    	$scope.addNewListing = function() {
	        $http.post("/real-estate/listing", {}, {
	            headers: {
	                'Content-Type': 'application/x-www-form-urlencoded'
	            },
	            params: {
	                "action": "add",
	                "s_id": $scope.newListingSeller,
	                "agent_id": $scope.me.p_id,
	                "list_price": $scope.newListingPrice,
	                "address": $scope.newListingAddress,
	                "city": $scope.newListingCity,
	                "state": $scope.newListingState,
	                "zip": $scope.newListingZip,
	                "pool": $scope.newListingPool
	            }
	        })
	            .then(function(resp) {
	 				window.location = "/real-estate/";
	            });
    	}

    	$scope.updateListing = function() {
	        $http.post("/real-estate/listing", {}, {
	            headers: {
	                'Content-Type': 'application/x-www-form-urlencoded'
	            },
	            params: {
	                "action": "updateInfo",
	                "t_id" : $rootScope.currentListingID,
	                "list_price": $scope.updateListingPrice,
	                "address": $scope.updateListingAddress,
	                "city": $scope.updateListingCity,
	                "state": $scope.updateListingState,
	                "zip": $scope.updateListingZip,
	                "pool": $scope.updateListingPool
	            }
	        })
	            .then(function(resp) {
	 				window.location = "/real-estate/";
	            });
    	}

    	$scope.markListingSold = function() {
	        $http.post("/real-estate/listing", {}, {
	            headers: {
	                'Content-Type': 'application/x-www-form-urlencoded'
	            },
	            params: {
	                "action": "updateSold",
	                "t_id" : $rootScope.currentListingID,
	                "b_id": $scope.soldListingBuyer,
	                "sold_price": $scope.soldListingPrice,

	            }
	        })
	            .then(function(resp) {
	 				window.location = "/real-estate/";
	            });
    	}

    	$scope.upload = function() {
    		var t_id = $rootScope.currentListingID;
	        var file = document.getElementById("filePicker").files[0];
	        var formdata = new FormData();
	        formdata.append('action', 'bulkUpload');
	        formdata.append('t_id', t_id);
	        formdata.append('file', file);
	        var xhr = new XMLHttpRequest();       
	        xhr.open('POST','/real-estate/upload',true);
	        xhr.send(formdata);

	        window.location = "/real-estate/";
	    }

	    $scope.retrieveImage = function() {
	        $http.post("/real-estate/listing", {}, {
	            headers: {
	                'Content-Type': 'application/x-www-form-urlencoded'
	            },
	            params: {
	                "action": "retrieveImage",
	                "t_id" : $rootScope.currentListingID,
	            }
	        })
	        	.then(function(resp) {
	        		$scope.imageData = "data:image/jpg;base64," + resp.data.image;
	            });
    	}

    	$scope.promoteMe = function() {
	        $http.post("/real-estate/people", {}, {
	            headers: {
	                'Content-Type': 'application/x-www-form-urlencoded'
	            },
	            params: {
	                "action": "promoteUser",
	                "p_id" : $scope.me.p_id
	            }
	        })
	            .then(function(resp) {
	            	$scope.logout();
	            });
    	}

    	$scope.logout = function() {
        $http.post("/real-estate/authenticate", {}, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            params: {
                "action": "logout",
            }
        })
        .then(function(resp) {
            var json = resp.data;
            console.log(json.result)
            if (json.result == "logoutSuccess") {
                window.location = "/real-estate/login.html";
            }
        });
    }

    	$scope.$watch('currentListingID', function(newValue, oldValue) {
       		if (newValue != undefined){
       			$scope.radioClicked=true;
       		}
 		});

        $scope.usStates = [{"name":"Alabama","abbreviation":"AL"},{"name":"Alaska","abbreviation":"AK"},{"name":"AmericanSamoa","abbreviation":"AS"},{"name":"Arizona","abbreviation":"AZ"},{"name":"Arkansas","abbreviation":"AR"},{"name":"California","abbreviation":"CA"},{"name":"Colorado","abbreviation":"CO"},{"name":"Connecticut","abbreviation":"CT"},{"name":"Delaware","abbreviation":"DE"},{"name":"DistrictOfColumbia","abbreviation":"DC"},{"name":"FederatedStatesOfMicronesia","abbreviation":"FM"},{"name":"Florida","abbreviation":"FL"},{"name":"Georgia","abbreviation":"GA"},
			{"name":"Guam","abbreviation":"GU"},{"name":"Hawaii","abbreviation":"HI"},{"name":"Idaho","abbreviation":"ID"},{"name":"Illinois","abbreviation":"IL"},{"name":"Indiana","abbreviation":"IN"},{"name":"Iowa","abbreviation":"IA"},{"name":"Kansas","abbreviation":"KS"},{"name":"Kentucky","abbreviation":"KY"},{"name":"Louisiana","abbreviation":"LA"},{"name":"Maine","abbreviation":"ME"},{"name":"MarshallIslands","abbreviation":"MH"},{"name":"Maryland","abbreviation":"MD"},{"name":"Massachusetts","abbreviation":"MA"},{"name":"Michigan","abbreviation":"MI"},
			{"name":"Minnesota","abbreviation":"MN"},{"name":"Mississippi","abbreviation":"MS"},{"name":"Missouri","abbreviation":"MO"},{"name":"Montana","abbreviation":"MT"},{"name":"Nebraska","abbreviation":"NE"},{"name":"Nevada","abbreviation":"NV"},{"name":"NewHampshire","abbreviation":"NH"},{"name":"NewJersey","abbreviation":"NJ"},{"name":"NewMexico","abbreviation":"NM"},{"name":"NewYork","abbreviation":"NY"},{"name":"NorthCarolina","abbreviation":"NC"},{"name":"NorthDakota","abbreviation":"ND"},{"name":"NorthernMarianaIslands","abbreviation":"MP"},
			{"name":"Ohio","abbreviation":"OH"},{"name":"Oklahoma","abbreviation":"OK"},{"name":"Oregon","abbreviation":"OR"},{"name":"Palau","abbreviation":"PW"},{"name":"Pennsylvania","abbreviation":"PA"},{"name":"PuertoRico","abbreviation":"PR"},{"name":"RhodeIsland","abbreviation":"RI"},{"name":"SouthCarolina","abbreviation":"SC"},{"name":"SouthDakota","abbreviation":"SD"},{"name":"Tennessee","abbreviation":"TN"},{"name":"Texas","abbreviation":"TX"},{"name":"Utah","abbreviation":"UT"},{"name":"Vermont","abbreviation":"VT"},{"name":"VirginIslands","abbreviation":"VI"},
			{"name":"Virginia","abbreviation":"VA"},{"name":"Washington","abbreviation":"WA"},{"name":"WestVirginia","abbreviation":"WV"},{"name":"Wisconsin","abbreviation":"WI"},{"name":"Wyoming","abbreviation":"WY"}];

    });

    App.controller('LoginController', function($scope, $http) {

    $scope.login = function() {
        $http.post("/real-estate/authenticate", {}, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            params: {
                "action": "login",
                "u_name": $scope.u_name
            }
        })
            .then(function(resp) {
                var json = resp.data;
                console.log(json.result)
                if (json.result == "loginSuccess") {
                    window.location = "/real-estate/index.html";
                } else {
                    $scope.showReg = true;
                }
            });
    }

    $scope.register = function() {
        $http.post("/real-estate/authenticate", {}, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            params: {
                "action": "register",
                "e_mail": $scope.e_mail,
                "f_name": $scope.f_name,
                "l_name": $scope.l_name,
                "u_name": $scope.u_name,
            }
        })
            .then(function(resp) {
                var json = resp.data;
                if (json.result == "registerSuccess") {
                    window.location = "/real-estate/index.html";
                } else if (json.result == "alreadyRegistered") {
                    window.location = "/real-estate/index.html";
                }
            });
    }

});