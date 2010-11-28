package models;


import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class User extends Model {
 
    @Email
    @Required
    public String email;
   
    //TODO Implement sha1
    
    @Required
    @Password
    public String password;
    	
    public String fullname;
    public boolean isAdmin;
    
    public User(String email, String password, String fullname) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
    }
    
    public static User connect(String fullName, String password) {
        return User.find("Select u from User u where u.fullname = ? and u.password = ?", fullName, password).first();
    }
    
    public String toString() {
        return fullname;
    }
 
}
