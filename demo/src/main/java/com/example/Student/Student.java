package com.example.Student;

public class Student {
    private int id;
    private String name;
    private String dept, dept_head;
    private String college;

    Student(int id, String name, String dept, String dept_head, String college) {
        this.id = id;
        this.name = name;
        this.dept = dept;
        this.dept_head = dept_head;
        this.college = college;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDept() {
        return this.dept;
    }

    public String getDept_head() {
        return this.dept_head;
    }

    public String getCollege() {
        return this.college;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public void setDept_head(String dept_head) {
        this.dept_head = dept_head;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String toString() {
        return  "{ 'id':" + this.id + 
                ", 'name': " + this.name +
                ", 'dept': " + this.dept +
                ", 'dept_name': " + this.dept_head + "}";
    }
}
