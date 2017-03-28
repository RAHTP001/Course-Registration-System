/**
 * Created by Cameron Stowell on 2/11/2017.
 */
import java.util.Scanner;
public class Admin extends User implements AdminInterface{

    public Admin(String username, String password) {
        super(username, password);
    }

    public Student createStudent(){
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a first name:\n");
        String firstName = input.nextLine();
        System.out.print("Enter a last name:\n");
        String lastName = input.nextLine();
        System.out.print("Enter a username:\n");
        String username = input.nextLine();
        System.out.print("Enter a password:\n");
        String password = input.nextLine();
        Student student = new Student(username, password, firstName, lastName);
        return student;
    }

}
