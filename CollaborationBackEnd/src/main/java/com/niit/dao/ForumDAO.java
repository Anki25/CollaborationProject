package com.niit.dao;

import java.util.List;

import com.niit.model.Forum;
import com.niit.model.ForumComment;

public interface ForumDAO {

public boolean save(Forum forum); 
	
	public boolean update(Forum forum);
	
	public boolean delete(int forumID);
	
	public Forum get(int forumID);
	
	public Forum getName(String name);
	
	public List<Forum> list();

public boolean addComment(ForumComment forumcomment);
	
	public List<ForumComment> listComment(int id);
	
	public List<ForumComment> listOfAllComment();
	

}

