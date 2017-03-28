import java.util.ArrayList;
import java.io.*;


/**
 * Created by Cameron Stowell on 2/10/2017.
 * Used to create and define courses
 */
public class Course implements Serializable, Comparable<Course>{

    private String name;
    private String id;
    private int maxStudents;
    private int currentStudents;
    private ArrayList<Student> students = new ArrayList<>();
    private String instructor;
    private int sectionNumber;
    private String location;
    private static final long serialVersionUID = 1L;

    public Course() {
        this.name = "New Course";
    }

    public Course(String name, String id, int maxStudents, int currentStudents, ArrayList<Student> students, String instructor, int sectionNumber, String location){
        this.name = name;
        this.id = id;
        this.maxStudents = maxStudents;
        this.currentStudents = currentStudents;
        this.students = students;
        this.instructor = instructor;
        this.sectionNumber = sectionNumber;
        this.location = location;
    }

    public void registerStudent(Student student){
        students.add(student);
        currentStudents++;
    }

    public void withdrawStudent(Student student) {
        students.remove(student);
        currentStudents--;
    }

    public ArrayList<Student> getStudentList(){
        return this.students;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getID(){
        return this.id;
    }

    public void setID(String id){
        this.id = id;
    }

    public int getMaxStudents(){
        return this.maxStudents;
    }

    public void setMaxStudents(int maxStudents){
        this.maxStudents = maxStudents;
    }

    public int getCurrentStudents(){
        return this.currentStudents;
    }

    public void setCurrentStudents(int currentStudents){
        this.currentStudents = currentStudents;
    }

    public String getInstructor(){
        return this.instructor;
    }

    public void setInstructor(String instructor){
        this.instructor = instructor;
    }

    public int getSectionNumber(){
        return this.sectionNumber;
    }

    public void setSectionNumber(int sectionNumber){
        this.sectionNumber = sectionNumber;
    }

    public String getLocation(){
        return this.location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String toString(){
        return String.format("%s, %s, %d, %d, %s, %d, %s", name, id, maxStudents, currentStudents, instructor, sectionNumber, location);
    }

    public int compareTo(Course course){
        return this.currentStudents - course.getCurrentStudents();
    }

}
