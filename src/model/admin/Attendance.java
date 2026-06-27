package model.admin;

import java.util.Date;

public class Attendance  {
    private long emp_id;
    private Date date;
    private Date check_in;
    private Date check_out;
    private double hours;
    public Attendance(long emp_id, Date date, Date check_in, Date check_out, double hours) {
        this.emp_id = emp_id;
        this.date = date;
        this.check_in = check_in;
        this.check_out = check_out;
        this.hours = hours;
    }
    public  Attendance() {}

    public double getHours () { return hours; }
    public void setHours(double hours) {}
    public long getEmp_id () { return emp_id; }
    public void setEmp_id(long emp_id) {}

    public Date getDate () { return date; }
    public void setDate (Date date) { this.date = date; }
    public Date getCheck_in () { return check_in; }
    public void setCheck_in (Date check_in) { this.check_in = check_in; }
    public Date getCheck_out () { return check_out; }
    public void setCheck_out (Date check_out) { this.check_out = check_out; }
}
