package models;

import java.util.*;

import javax.persistence.*;
 
import play.Play;
import play.db.jpa.*;
import play.data.validation.*;
 

@Entity
public class Participant extends Model {
	
	@Required
	public Long partId;

	@Required
	public String firstName;
	
	@Required
	public String lastName;
	
	@Required
	public boolean eligebleForDraw;
	
	@Required
	public boolean boughtTodaysPresent;
	
	@OneToMany(mappedBy="winner", cascade=CascadeType.ALL)
    public List<Win> wins;
	
	public Participant(String firstName, String lastName) {
		this.id = Participant.find("max(id)").first();
		this.firstName = firstName;
		this.lastName = lastName;
		this.eligebleForDraw = true;
		this.boughtTodaysPresent = false;
		this.wins = new ArrayList<Win>();
	}
	
	public Participant addWin() {
        Win newWin = new Win(this).save();
        this.wins.add(newWin);
        if( this.wins.size() >= Integer.parseInt(Play.configuration.getProperty("tcc.winPerParticipant"))) {
        	this.eligebleForDraw = false; 
        }
        this.save();
        return this;
    }
	
	public static void setBuyer(Long id, boolean boughtPresent) {
    	Participant buyer = Participant.find("partId = ?", id ).first();
    	buyer.setBoughtTodaysPresent(boughtPresent);
    	buyer.save();
    }
	
	public static void resetBuyer() {
		EntityManager em = Participant.em();
    	em.createQuery("update Participant p set p.boughtTodaysPresent = false").executeUpdate();
	}
	
	public String toString() {
		return firstName;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public List<Win> getWins() {
		return wins;
	} 

	public boolean isBoughtTodaysPresent() {
		return boughtTodaysPresent;
	}

	public void setBoughtTodaysPresent(boolean boughtTodaysPresent) {
		this.boughtTodaysPresent = boughtTodaysPresent;
	}
	
	public Long getPartId() {
		return partId;
	}
}
