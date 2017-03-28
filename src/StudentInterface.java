/**
 * Created by Cameron Stowell on 2/11/2017.
 */
import java.util.ArrayList;
public interface StudentInterface {
    void register(Course course);
    void withdraw(Course course);
    ArrayList<Course> getCourseList();
    boolean registered(Course course);
}
