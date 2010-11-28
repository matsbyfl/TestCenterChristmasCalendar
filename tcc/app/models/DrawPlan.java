package models;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class DrawPlan extends Model {
	
	@Required
	public Integer day;
	
	@Required
	public Boolean enabled;
	
	public DrawPlan( Integer day, Boolean enabled ) {
		this.day = day;
		this.enabled = enabled;
	}
	
}
