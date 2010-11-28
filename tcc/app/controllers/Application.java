package controllers;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.EntityManager;

import play.Play;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.JPASupport.JPAQuery;
import play.mvc.*;

import models.*;

@With(Secure.class)
public class Application extends Controller {
	
	@Before
	static void addDefaults() {
		List<Participant> participants = Participant.find("order by firstName").fetch();
	    renderArgs.put("buyers", participants);
	    int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	    renderArgs.put("today", today);
	}
	
	@Before
	static void getPreviousWinners() {
		 List<Win> wins = Win.find("order by winningDate").fetch();
		 List<String> winners = new ArrayList<String>();
		 for(int i = 0; i < 24; i++ ) {
			 winners.add(i, "");
		 }
		 for( int i = 0; i < wins.size(); i++) {
			 Date date = wins.get(i).winningDate;
			 Calendar cal = Calendar.getInstance();
			 cal.setTime(date);
			 int day = cal.get(Calendar.DAY_OF_MONTH);
			 if(day <= 24) {
				 winners.set( (day - 1),  wins.get(i).winner.getFirstName()) ;
			 }
		 }
		 renderArgs.put("winners", winners);
	}
	
    public static void index() {
    	render();
    }
   
    public static void draw(@Required @Min(1) Long buyer) {
    	if( validation.hasErrors()) {
    		render("Application/index.html");
    	}
    	
		Participant.setBuyer(buyer, true);
		JPAQuery jq = Participant.find( "Select p from Participant p where p.eligebleForDraw = true and p.boughtTodaysPresent = false" );
		List<Participant> participants = jq.fetch();
    	SecureRandom randomGenerator;
		try {
			randomGenerator = SecureRandom.getInstance("SHA1PRNG");
			if( participants.size() > 0 ) {
		    	randomGenerator.setSeed(System.currentTimeMillis());
		    	int winningNumber = randomGenerator.nextInt(participants.size());
		    	Participant winner = participants.get(winningNumber);
		    	winner.addWin();
		    	Participant.setBuyer(buyer, false);
		    	renderArgs.put("currentBuyerID", buyer);
		    	render( "Application/index.html", winner );
	    	}
	    	else {
	    		flash.error("Alle deltakere har vunnet");
	    		Participant.setBuyer(buyer, false);
	    		render("Application/index.html");
	    	}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}