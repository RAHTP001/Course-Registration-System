import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Cameron Stowell on 2/11/2017.
 */
public class Student extends User implements Serializable, StudentInterface{

    private ArrayList<Course> courses = new ArrayList<>();
    private static final long serialVersionUID = 1L;



    public Student(String username, String password, String firstName, String lastName){
        super(username, password, firstName, lastName);
    }

    public void register(Course course){
        courses.add(course);
    }

    public void withdraw(Course course){
        courses.remove(course);
    }

    public ArrayList<Course> getCourseList(){
        return this.courses;
    }

    public boolean registered(Course course){
        return courses.contains(course);
    }

    public String toString(){
        return String.format("%s %s", getFirstName(), getLastName());
    }
}
