package com.niit.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.ForumDAO;
import com.niit.model.BlogComment;
import com.niit.model.Forum;
import com.niit.model.ForumComment;
import com.niit.model.User;

@RestController
public class ForumController {

private static final Logger logger	= LoggerFactory.getLogger(ForumController.class);
	
	@Autowired
	ForumDAO forumDAO;
	
	@RequestMapping(value="/forums",method=RequestMethod.GET)
	public ResponseEntity<List<Forum>> listAllForums(){
		logger.debug("calling method listAllForums");
		List<Forum> forum=forumDAO.list();
		if(forum.isEmpty()){
			return new ResponseEntity<List<Forum>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Forum>>(forum,HttpStatus.OK);
	}

	@RequestMapping(value="/forum/",method=RequestMethod.POST)
	public ResponseEntity<Forum> createForum(@RequestBody Forum forum,HttpSession httpSession){
		logger.debug("calling method createForum" + forum.getForum_id());
		if(forumDAO.get(forum.getForum_id())==null){
			Integer loogedInUserID=(Integer) httpSession.getAttribute("loggedInUserID");
			Date dt=new java.util.Date();
			forum.setCreation_date(dt.toString());
			User user=(User) httpSession.getAttribute("loggedInUser");
			forum.setUser_name(user.getUser_name());
			forum.setUser_id(loogedInUserID);
			forumDAO.save(forum);			
		}
		logger.debug("forum already exists with id:" + forum.getForum_id());
		forum.setErrorMessage("forum already exists with id:" + forum.getForum_id());
		return new ResponseEntity<Forum>(forum,HttpStatus.OK);
			}
	
	@RequestMapping(value="/forum/{id}",method=RequestMethod.PUT)
	public ResponseEntity<Forum> updateForum(@PathVariable("id") int forum_id,@RequestBody Forum forum){
		logger.debug("calling method updateForum" + forum.getForum_id());
		if(forumDAO.get(forum_id)==null){
			logger.debug("forum does not exists with id:" + forum.getForum_id());		
			forum=new Forum();
			forum.setErrorMessage("forum does not exists with id:" + forum.getForum_id());
			return new ResponseEntity<Forum> (forum,HttpStatus.NOT_FOUND);
		}
		forumDAO.update(forum);
		logger.debug("forum updated successfully");
		return new ResponseEntity<Forum> (forum,HttpStatus.OK);		
	}

	@RequestMapping(value="/forum/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<Forum> deleteForum(@PathVariable("id") int forum_id){
		logger.debug("calling method deleteForum for forum id: " + forum_id);
		Forum forum=forumDAO.get(forum_id);
		if(forum==null){
			logger.debug("forum does not exists with id:" + forum_id);
			forum=new Forum();
			forum.setErrorMessage("forum does not exists with id:" + forum_id);
			return new ResponseEntity<Forum> (forum,HttpStatus.NOT_FOUND);	
		}
		forumDAO.delete(forum_id);
		logger.debug("forum deleted successfully");
		return new ResponseEntity<Forum> (forum,HttpStatus.OK);		
	}
	
	@RequestMapping(value="/forum/{id}",method=RequestMethod.GET)
	public ResponseEntity<Forum> getForum(@PathVariable("id") int forum_id){
		logger.debug("calling method getForum for forum id: " + forum_id);
		Forum forum=forumDAO.get(forum_id);
		if(forum==null){
			logger.debug("forum does not exists with id:" + forum_id);
			forum=new Forum();
			forum.setErrorMessage("forum does not exists with id:" + forum_id);
			return new ResponseEntity<Forum> (forum,HttpStatus.NOT_FOUND);
		}
		logger.debug("forum exists with id:" + forum_id);
		return new ResponseEntity<Forum> (forum,HttpStatus.OK);
	}

	@RequestMapping(value="/forumcomment/{id}",method=RequestMethod.POST)
	public ResponseEntity<ForumComment> createForumComment(@PathVariable("id") int forum_id,@RequestBody ForumComment forumcomment,HttpSession httpSession){
		logger.debug("calling method createForumComment" + forumcomment.getForum_id());
		Integer loogedInUserID=(Integer) httpSession.getAttribute("loggedInUserID");
		User username=(User) httpSession.getAttribute("loggedInUser");
		forumcomment.setUser_id(loogedInUserID);
		Date dt=new java.util.Date();
		forumcomment.setComment_date(dt.toString());
		forumcomment.setUser_name(username.getUser_name());
		forumcomment.setForum_id(forum_id);
		if(forumDAO.addComment(forumcomment)==true){
			forumcomment.setErrorCode("200");
			forumcomment.setErrorMessage("Comment saved");
		
		}else{
			return new ResponseEntity<ForumComment>(forumcomment,HttpStatus.BAD_REQUEST);
		}
				
		return new ResponseEntity<ForumComment>(forumcomment,HttpStatus.OK);
			}

	@RequestMapping(value="/forumscommentlistperforum/{id}",method=RequestMethod.GET)
	public ResponseEntity<List<ForumComment>> listAllForumsCommentsPerForum(@PathVariable("id") int forum_id){
		logger.debug("calling method listAllForumcomments");
		List<ForumComment> forumcomment=forumDAO.listComment(forum_id);
		if(forumcomment.isEmpty()){
			return new ResponseEntity<List<ForumComment>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<ForumComment>>(forumcomment,HttpStatus.OK);
	}


	@RequestMapping(value="/forumscommentlist/",method=RequestMethod.GET)
	public ResponseEntity<List<ForumComment>> listAllForumsComments(){
		logger.debug("calling method listAllForums");
		List<ForumComment> forumcomment=forumDAO.listOfAllComment();
		if(forumcomment.isEmpty()){
			return new ResponseEntity<List<ForumComment>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<ForumComment>>(forumcomment,HttpStatus.OK);
	}

	

}




