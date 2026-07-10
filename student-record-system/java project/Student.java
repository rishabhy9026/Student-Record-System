public class Student {
    int roll;
    String name;
    double marks;

    public Student(int roll, String name, double marks) {
        this.roll = roll;
        this.name = name;
        this.marks = marks;
    }

    public int getRoll() { return roll; }
    public String getName() { return name; }
    public double getMarks() { return marks; }

    public String toString() {
        return roll + "," + name + "," + marks;
    }
}
