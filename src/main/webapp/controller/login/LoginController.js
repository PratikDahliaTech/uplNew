upl_survey.controller("LoginController",['$scope', '$location', 'LoginService', function($scope, $location, LoginService) {
			console.log("In controller");
			$scope.LoginUser = function() {
				console.log("In function");
				LoginService.loginUser($scope.password,$scope.password ).then(function(response) {
					console.log(response);
					$scope.userDetails = response.data.items;
					if($scope.userDetails.type ='ADMIN'){
						$location.url('/userManagement.html');
					} else if(($scope.userDetails.type ='USER') || ($scope.userDetails.type ='SURVEYOR')){
						$location.url('/surveyList.html');
					}
					// $window.location.href = 'view/SurveyList.html';
				}, function(response) {
					$scope.errors = response.data.errorMessages;
				})
			}
		} ]);
		
upl_survey.service('LoginService', ['$http',
		function($http) {

			this.loginUser = function loginUser(email, password) {
				return $http({
					method : 'GET',
					url : '/upl_survey/loginUser',
					params : {
						email : email,
						password : password
					}
				});
			}
}
]);
