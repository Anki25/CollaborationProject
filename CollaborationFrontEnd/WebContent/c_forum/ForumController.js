/**
 * 
 */
'use strict';

app.controller('ForumController', [
		'$scope',
		'ForumService',
		'$location',
		'$rootScope',
		function($scope, ForumService, $location, $rootScope) {
			console.log("ForumController...")
			var self = this; // self is alias name instead directly use this
			self.forum = { // initialization
				forum_id : '',
				user_id : '',
				topic : '',
				content : '',
				creation_date : '',		
				errorCode:'',
				errorMessage:''
			};
			self.forums = [];
			
			self.forumcomment={
					id:'',
					comments:'',
					comment_date:'',
					user_id:'',
					user_name:'',
					forum_id:'',
					errorCode:'',
					errorMessage:''
			};
			self.forumcomments=[];

			
			/*GET SELECTED FORUM DETAILS*/

			self.getSelectedForum = getForum

			function getForum(id) {
				console.log("getting forum! " + id)
				ForumService.getForum(id).then(function(d) {
					self.forum = d;
					$location.path('/view_forum');
				}, function(errResponse) {
					console.error('Error while fetching forums');
				});
			};

			/* GET LIST OF ALL FORUMS */

			self.fetchAllForums = function() {
				console.log("getting list of forums")
				ForumService.fetchAllForums()
				.then(function(d) { // these methods are called call back methods
					self.forums = d;
				}, function(errResponse) {
					console.error('Error while fetching Forums');
				});
			};
			self.fetchAllForums();

			/* CREATE A FORUM */

			self.createForum = function(forum) {
				ForumService.createForum(forum).then(self.fetchAllForums,
						function(errResponse) {
							console.error('Error while creating Forums');
						});
			};

			/* UPDATE A FORUM */

			self.updateForum = function(forum) {
				ForumService.updateForum(forum).then(self.fetchAllForums,
						function(errResponse) {
							console.error('Error while updating Forums');
						});
			};

			/* DELETE A FORUM */

			self.deleteForum = function(id) {
				ForumService.deleteForum(id).then(self.fetchAllForums,
						function(errResponse) {
							console.error('Error while deleting Forums');
						});
			};

			/* ON CLICKING SUBMIT BUTTON */

			self.submit = function() {
				/*if (self.forum.forum_id == null) {*/
					console.log('Saving New Forum', self.forum);
					//self.forum.user_name = $rootScope.currentUser.user_id
					self.createForum(self.forum);
				//}
				self.reset();
			};
			
			self.reset=function(){
				console.log('resetting the forum',self.forum);
				self.forum={
						forum_id : '',
						forum_title : '',
						creation_date : '',
						info : '',
						user_name : '',
						errorCode:'',
						errorMessage:''
					};
				}

/* COMMENTING FOR BLOG BUTTON */
			
			self.createComment=function(id,forumcomment){
				ForumService.createComment(id,forumcomment).then(self.fetchAllForumComment(),
						function(errResponse) {
							console.error('Error while creating Forum Comments');
						});
			};
			
			self.doComment=function(id){
				self.createComment(id,self.forumcomment);
				self.resetComment();
			}


			self.resetComment=function(){
				console.log('resetting the forum comments',self.forumcomment);
				self.forumcomment={
						id:'',
						comments:'',
						comment_date:'',
						user_id:'',
						user_name:'',
						forum_id:'',
						errorCode:'',
						errorMessage:''
				};
				}
			
			/* GET LIST OF ALL FORUM COMMENTS*/

			self.fetchAllForumComment = function() {
				ForumService.fetchAllForumComment().then(function(d) { 
					self.forumcomments = d;
				}, function(errResponse) {
					console.error('Error while fetching Forum Comments');
				});
			};
			self.fetchAllForumComment();
			
			self.getSelectedForumComment = getForumComment

			function getForumComment(id) {
				console.log("getting Forumcomments in Controller! " + id)
				ForumService.fetchAllForumComments(id).then(function(d) {
					self.forumcomments = d;
					$location.path('/view_forum_comment');
				}, function(errResponse) {
					console.error('Error while fetching Forum comments in controller');
				});
			};


			/* END OF ALL */

		} ]);/**
 * 
 */