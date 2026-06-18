package model.Recruitment;

import model.User;

import java.sql.Date;

public class Employer extends User {

    public Employer(){

    }
    public JobPosting getJobPostingDraf() {
        JobPosting j = new JobPosting();
        j.setTitle("abc");
        j.setDescription("abc");
        j.setRequiment("abc");
        j.setDayEnd(new Date(2000,10,15));
        j.setSalary(9000000);

        return j;
    }

}
