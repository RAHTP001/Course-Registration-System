/**
 * Created by Cameron Stowell on 2/11/2017.
 */
import java.io.Serializable;
public class User implements Serializable{

    private String username;
    private String password;
    private String firstName;
    private String lastName;

    public User(){
        this.username = "username";
        this.password = "password";
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String firstName, String lastName){
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }
}
