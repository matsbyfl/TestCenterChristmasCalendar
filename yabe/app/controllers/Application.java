package controllers;

import java.util.List;

import models.Post;
import play.Play;
import play.mvc.*;

public class Application extends Controller {

	@Before
	static void addDefaults() {
	    renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
	    renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
	    
	}
	
    public static void index() {
    	 Post frontPost = Post.find("order by postedAt desc").first();
         List<Post> olderPosts = Post.find(
             "order by postedAt desc"
         ).from(1).fetch(10);
         render(frontPost, olderPosts);
     }
}