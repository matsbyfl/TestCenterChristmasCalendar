import models.DrawPlan;
import models.Participant;
import models.User;
import models.Win;

import org.junit.*;
import java.util.*;
import play.test.*;


public class BasicTest extends UnitTest {

	 @Before
	 public void setup() {
		 Fixtures.deleteAll();
	 }
	 
	 @Test
	 public void createAndRetrieveParticipant() {
		 new Participant(1, "Test participant").save();
		 Participant testpart = Participant.find("byPartId", 1).first();
		 assertEquals(1, Participant.count());
		 assertNotNull(testpart);
		 assertEquals((Integer)1, (Integer)testpart.partId);
		 
		 new Participant(1, "TP2").save();
		 Participant tp2 = Participant.find("byName", "TP2").first();
		 assertNotNull(tp2);
		 assertEquals("TP2", tp2.firstName);		 
	 }
	 
	 @Test
	 public void createWin() {
		 Participant part = new Participant(1, "TP2").save();
		 new Win(part).save();
		 Win testWin = Win.find("byWinner", part).first();
		 assertNotNull(testWin);
		 assertEquals("TP2", testWin.winner.firstName);
		 assertEquals(1, Win.count());
	 }
	 
	 @Test
	 public void AddWinToParticipantWithRelation() {
		 Participant part = new Participant(1, "TP2").save();
		 part.addWin();
		 assertEquals(1, part.getWins().size());
		 
		 Participant part2 = new Participant(1, "TP3").save();
		 part2.addWin();
		 part2.addWin();
		 assertEquals(2, part2.getWins().size());
		 
		 assertEquals(3, Win.count());
		 
		 part2.delete();
		 assertEquals(1, Win.count());
		 
	 }
	 
	 @Test
	 public void addDrawPlan() {
		 new DrawPlan(1, true).save();
		 new DrawPlan(2, false).save();
		 new DrawPlan(2, true).save();		 
		 
		 assertEquals(2, DrawPlan.count("enabled = ?", true));
		 
	 }	 
	 
	@Test
	public void createAndRetrieveUser() {
	    // Create a new user and save it
	    new User("bob@gmail.com", "secret", "Bob").save();
	    
	    // Retrieve the user with e-mail address bob@gmail.com
	    User bob = User.find("byEmail", "bob@gmail.com").first();
	    
	    // Test 
	    assertNotNull(bob);
	    assertEquals("Bob", bob.fullname);
	}
	
	@Test
	public void tryConnectAsUser() {
	    // Create a new user and save it
	    new User("bob@gmail.com", "secret", "Bob").save();
	    
	    // Test 
	    assertNotNull(User.connect("bob@gmail.com", "secret"));
	    assertNull(User.connect("bob@gmail.com", "badpassword"));
	    assertNull(User.connect("tom@gmail.com", "secret"));
	}	
}
