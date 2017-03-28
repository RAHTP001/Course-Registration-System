import java.io.*;
import java.util.*;

/**
 * Created by Cameron Stowell on 2/11/2017.
 *
 */
public class Program implements Serializable{

    private static ArrayList<Course> courseList = new ArrayList<>();
    private static ArrayList<Student> studentList = new ArrayList<>();
    private static Admin admin = new Admin("Admin", "Admin001");
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) throws IOException{
        deserializeArrayLists();
        Login();
        serializeArrayLists();

    }

    /*
    No longer useful, was used to import the CSV file. Here so you can see how I did it.
     */
    public static void createCourseList(){
        try (BufferedReader inFile = new BufferedReader(new FileReader("MyUniversityCourses.csv"))){
            String line = inFile.readLine();
            while(line != null){
                String[] data = line.split(",");
                Course newCourse = new Course(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]), new ArrayList<>(), data[5], Integer.parseInt(data[6]), data[7]);
                courseList.add(newCourse);
                System.out.print("Done with course\n");
                line = inFile.readLine();
            }
            inFile.close();
        } catch (FileNotFoundException e){
            System.out.print("File not found\n");
        } catch (IOException e) {
            System.out.print("IO exception\n");
        }
    }

    /*
    Logs in the user as either a student or an admin.
     */
    public static void Login() throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Are you a:\n1. Student\n2. Admin\n");
        String username, password;
        switch (Integer.parseInt(input.readLine())) {
            case 1:
                System.out.print("Enter your username:\n");
                username = input.readLine();
                System.out.print("Enter your password:\n");
                password = input.readLine();
                for(Student s : studentList){
                    if(username.equals(s.getUsername()) && password.equals(s.getPassword())) {
                        runStudentUI(s);
                        break;
                    }
                }
                break;
            case 2:
                System.out.print("Enter your username:\n");
                username = input.readLine();
                System.out.print("Enter your password:\n");
                password = input.readLine();
                if (username.equals(admin.getUsername()) && password.equals(admin.getPassword())){
                    runAdminUI();
                }
                break;
            default:
                System.out.print("Invalid entry\n");
                Login();
        }
    }

    /*
    Runs the UI for Students
     */
    public static void runStudentUI(Student student) throws IOException{
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Course course;
        boolean foundCourse;
        String courseName;
        int courseSection;
        BufferedReader studentUI = new BufferedReader(new FileReader("studentui.txt"));
        String line = studentUI.readLine();
        while(line != null){
            System.out.print(line + "\n");
            line  = studentUI.readLine();
        }
        switch (Integer.parseInt(input.readLine())) {
            /*Prints all courses*/
            case 1:
                for (Course c : courseList) {
                    System.out.print(c.toString() + "\n");
                }
                break;

            /*Prints courses with open slots remaining*/
            case 2:
                for (Course c : courseList) {
                    if (c.getCurrentStudents() < c.getMaxStudents()) {
                        System.out.print(c.toString() + "\n");
                    }
                }
                break;

            /*Registers a student on a course*/
            case 3:
                foundCourse = false;
                while (!foundCourse) {
                    System.out.print("Enter a course name\n");
                    courseName = input.readLine();
                    System.out.print("Enter course section\n");
                    courseSection = Integer.parseInt(input.readLine());
                    course = findCourse(courseName, courseSection);
                    if(course != null){
                        if(!student.registered(course)){
                            student.register(course);
                            course.registerStudent(student);
                            foundCourse = true;
                            System.out.print(String.format("You are now registered in %s", course.getName()));
                        } else{
                            System.out.print("You're already registered on that course!\n");
                            foundCourse = true;
                        }
                    } else {
                        System.out.print("Cannot find course!\n");
                    }
                }
                break;

            /*Withdraws a student from a course*/
            case 4:
                foundCourse = false;
                while(!foundCourse) {
                    System.out.print("Enter a course name\n");
                    courseName = input.readLine();
                    course = findCourse(courseName);
                    if(course != null){
                        if(student.registered(course)) {
                            student.withdraw(course);
                            course.withdrawStudent(student);
                            foundCourse = true;
                            System.out.print(String.format("You are now withdrawn from %s", course.getName()));
                        } else {
                            System.out.print("You are not registered in that course!\n");
                        }
                    } else {
                        System.out.print("Cannot find course!\n");
                    }
                }
                break;

            /*Prints list of courses a user is registered in*/
            case 5:
                for(Course c : student.getCourseList()){
                    System.out.print(String.format("%s, %s, %s, %d, %s", c.getName(), c.getID(), c.getInstructor(),  c.getSectionNumber(), c.getLocation()));
                }
                break;

            /*Exits and serializes the arrays*/
            case 6:
                System.out.println("Thanks for using!");
                serializeArrayLists();
                System.exit(1);
        }
        serializeArrayLists();
        System.out.print("\n----------\n");
        runStudentUI(student);
    }

    /*
    Runs the UI for the Admin
     */
    public static void runAdminUI() throws IOException{
        boolean foundCourse;
        String line;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Would you like:\n1.Course Management\n2.Reports\n3.Exit\n");
        switch(Integer.parseInt(input.readLine())){
            /*Course Management UI*/
            case 1:
                BufferedReader adminCMUI = new BufferedReader(new FileReader("adminCourseManagementUI.txt"));
                line = adminCMUI.readLine();
                while(line != null){
                    System.out.print(line + "\n");
                    line  = adminCMUI.readLine();
                }
                switch (Integer.parseInt(input.readLine())){
                    /*Create a new course*/
                    case 1:
                        Course course = new Course();
                        editCourseUI(course);
                        courseList.add(course);
                        break;

                    /*Delete a course*/
                    case 2:
                        foundCourse = false;
                        while (!foundCourse) {
                            System.out.print("Enter a course name\n");
                            String courseName = input.readLine();
                            System.out.print("Enter course section\n");
                            int courseSection = Integer.parseInt(input.readLine());
                            course = findCourse(courseName, courseSection);
                            if(course != null){
                                courseList.remove(course);
                                for(Student s : studentList){
                                    if(s.getCourseList().contains(course)){
                                        s.withdraw(course);
                                    }
                                }
                                foundCourse = true;
                            } else {
                                System.out.print("Cannot find course!\n");
                            }
                        }
                        break;

                    /*Edit a course*/
                    case 3:
                        foundCourse = false;
                        while (!foundCourse) {
                            System.out.print("Enter a course name\n");
                            String courseName = input.readLine();
                            System.out.print("Enter course section\n");
                            int courseSection = Integer.parseInt(input.readLine());
                            course = findCourse(courseName, courseSection);
                            if(course != null){
                                editCourseUI(course);
                                foundCourse = true;
                            } else {
                                System.out.print("Cannot find course!\n");
                            }
                        }
                        break;

                    /*Display course info by id*/
                    case 4:
                        foundCourse = false;
                        while (!foundCourse) {
                            System.out.print("Enter a course ID\n");
                            String courseID = input.readLine();
                            ArrayList<Course> listOfCourses = findCourseByID(courseID);
                            if(listOfCourses != null){
                                for(Course c : listOfCourses){
                                    System.out.print(c.toString() + "\n");
                                }
                                foundCourse = true;
                            } else {
                                System.out.print("Cannot find course!\n");
                            }
                        }
                        break;

                    /*Register a new student*/
                    case 5:
                        studentList.add(admin.createStudent());
                        break;

                    /*Exit*/
                    case 6:
                        serializeArrayLists();
                        break;
                }

                break;

            /*Reports UI*/
            case 2:
                BufferedReader adminRUI = new BufferedReader(new FileReader("adminReportsUI.txt"));
                line = adminRUI.readLine();
                while(line != null){
                    System.out.print(line + "\n");
                    line  = adminRUI.readLine();
                }

                switch (Integer.parseInt(input.readLine())){
                    /*View all courses*/
                    case 1:
                        for(Course c : courseList){
                            System.out.print(String.format("%s, %s, Current: %d, Max: %d\n", c.getName(), c.getID(), c.getCurrentStudents(), c.getMaxStudents()));
                        }
                        break;

                    /*View full courses*/
                    case 2:
                        for(Course c : courseList){
                            if(c.getCurrentStudents() >= c.getMaxStudents()){
                                System.out.print(c.toString() + "\n");
                            }
                        }
                        break;

                    /*Write a file with list of full files*/
                    case 3:
                        PrintWriter writer = new PrintWriter("fullcourses.txt");
                        for(Course c : courseList){
                            if(c.getCurrentStudents() >= c.getMaxStudents()){
                                writer.println(c.toString());
                            }
                        }
                        break;

                    /*View the student list for a course*/
                    case 4:
                        foundCourse = false;
                        while (!foundCourse) {
                            System.out.print("Enter a course name\n");
                            String courseName = input.readLine();
                            System.out.print("Enter course section\n");
                            int courseSection = Integer.parseInt(input.readLine());
                            Course course = findCourse(courseName, courseSection);
                            if(course != null){
                                for(Student s : course.getStudentList()){
                                    System.out.print(s.toString() + "\n");
                                }
                                foundCourse = true;
                            } else {
                                System.out.print("Cannot find course!\n");
                            }
                        }
                        break;

                    /*View courses by student*/
                    case 5:
                        boolean foundStudent = false;
                        while (!foundStudent) {
                            System.out.print("Enter a first name\n");
                            String firstName = input.readLine();
                            System.out.print("Enter last name\n");
                            String lastName = input.readLine();
                            Student student = findStudent(firstName, lastName);
                            if(student != null){
                                for(Course c : student.getCourseList()){
                                    System.out.print(c.toString() + "\n");
                                }
                                foundStudent = true;
                            } else {
                                System.out.print("Cannot find student!\n");
                            }
                        }
                        break;

                    /*Sort courses by number of students registered*/
                    case 6:
                        Collections.sort(courseList);
                        for(Course c : courseList){
                            System.out.print(c.toString() + "\n");
                        }
                        break;

                    /*Exit*/
                    case 7:
                        serializeArrayLists();
                        break;

                }

                break;

            /*Exit*/
            case 3:
                serializeArrayLists();
                System.exit(1);


        }
        serializeArrayLists();
        System.out.print("\n----------\n");
        runAdminUI();
    }

    public static void editCourseUI(Course course) throws IOException{
        String line;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader editCourseUI = new BufferedReader(new FileReader("editCourseUI.txt"));
        line = editCourseUI.readLine();
        while(line != null){
            System.out.print(line + "\n");
            line  = editCourseUI.readLine();
        }
        switch(Integer.parseInt(input.readLine())){
            /*Course Name*/
            case 1:
                System.out.print(String.format("Current: %s", course.getName()));
                course.setName(input.readLine());
                break;

            /*Course ID*/
            case 2:
                System.out.print(String.format("Current: %s", course.getID()));
                course.setID(input.readLine());
                break;

            /*Max Students*/
            case 3:
                System.out.print(String.format("Current: %d", course.getMaxStudents()));
                course.setMaxStudents(Integer.parseInt(input.readLine()));
                break;

            /*Current Students*/
            case 4:
                System.out.print(String.format("Current: %d", course.getCurrentStudents()));
                course.setCurrentStudents(Integer.parseInt(input.readLine()));
                break;

            /*Instructor*/
            case 5:
                System.out.print(String.format("Current: %s", course.getInstructor()));
                course.setInstructor(input.readLine());
                break;

            /*Section Number*/
            case 6:
                System.out.print(String.format("Current: %d", course.getSectionNumber()));
                course.setSectionNumber(Integer.parseInt(input.readLine()));
                break;

            /*Location*/
            case 7:
                System.out.print(String.format("Current: %s", course.getLocation()));
                course.setLocation(input.readLine());
                break;

            /*Exit*/
            case 8:
                serializeArrayLists();
                return;
        }
        editCourseUI(course);
    }

    public static Course findCourse(String name){
        for(Course c : courseList){
            if(name.equals(c.getName())){
                return c;
            }
        }
        return null;
    }

    public static Course findCourse(String name, int sectionNumber){
        for(Course c : courseList){
            if(name.equals(c.getName()) && sectionNumber == c.getSectionNumber()){
                return c;
            }
        }
        return null;
    }

    public static ArrayList<Course> findCourseByID(String id){
        ArrayList<Course> list = new ArrayList<>();
        for(Course c : courseList){
            if(id.equals(c.getID())){
                list.add(c);
            }
        }
        return list;
    }

    public static Student findStudent(String firstName, String lastName){
        for(Student s : studentList){
            if(firstName.equals(s.getFirstName()) && lastName.equals(s.getLastName())){
                return s;
            }
        }
        return null;
    }

    /*
    Serializes the the Course and Student ArrayLists. Always called at the end of Program.
     */
    public static void serializeArrayLists(){
        try{
            /* serializes courseList */
            FileOutputStream courseFileOut= new FileOutputStream("courselist");
            ObjectOutputStream courseObjectOut= new ObjectOutputStream(courseFileOut);
            courseObjectOut.writeObject(courseList);
            courseObjectOut.close();
            courseFileOut.close();

            /* serializes studentList */
            FileOutputStream studentFileOut = new FileOutputStream("studentlist");
            ObjectOutputStream studentObjectOut = new ObjectOutputStream(studentFileOut);
            studentObjectOut.writeObject(studentList);
            studentObjectOut.close();
            studentFileOut.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    /*
    De-serializes the Course and Student ArrayLists. Always called at the start of Program.
     */
    public static void deserializeArrayLists(){
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();
        try
        {
            /* for courseList */
            FileInputStream courseFileInput = new FileInputStream("courselist");
            ObjectInputStream courseObjectInput = new ObjectInputStream(courseFileInput);
            courses = (ArrayList) courseObjectInput.readObject();
            courseObjectInput.close();
            courseFileInput.close();

            /* for studentList */
            FileInputStream studentFileInput = new FileInputStream("studentlist");
            ObjectInputStream studentObjectInput = new ObjectInputStream(studentFileInput);
            students = (ArrayList) studentObjectInput.readObject();
            studentObjectInput.close();
            studentFileInput.close();
        }catch(IOException ioe){
            System.out.print("IO exception on deserialization\n");
            ioe.printStackTrace();
        }catch(ClassNotFoundException c){
            System.out.print("Class not found\n");
            c.printStackTrace();
        }

        courseList = courses;
        studentList = students;
    }

}
