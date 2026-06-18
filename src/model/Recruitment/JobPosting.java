package model.Recruitment;

import java.sql.Date;

public class JobPosting {
    private String title;
    private String description;
    private String requiment;
    private Date dayEnd;
    private double salary;

    public JobPosting() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequiment() {
        return requiment;
    }

    public void setRequiment(String requiment) {
        this.requiment = requiment;
    }

    public Date getDayEnd() {
        return dayEnd;
    }

    public void setDayEnd(Date dayEnd) {
        this.dayEnd = dayEnd;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public boolean checkValid() {

        return true;
    }

    public boolean save() {

        return true;
    }

    public boolean saveDraf() {

        return true;
    }
}
