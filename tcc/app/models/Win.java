package models;

import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Win extends Model {
	
	@Required
	@ManyToOne
	public Participant winner;
	
	@Required
	public Date winningDate;
	
	public Win( Participant winner ) {
		this.winner = winner;
		this.winningDate = new Date();
	}
}
