package com.purpleworldtechnology.sreesakthiengineeringcollege;

public class Subjects {
    int subject_id,mark_id,semester_id,internal_id,mark,department_id;
    String subject_name,batch,dept_short_form;

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getMark_id() {
        return mark_id;
    }

    public void setMark_id(int mark_id) {
        this.mark_id = mark_id;
    }

    public int getSemester_id() {
        return semester_id;
    }

    public void setSemester_id(int semester_id) {
        this.semester_id = semester_id;
    }

    public int getInternal_id() {
        return internal_id;
    }

    public void setInternal_id(int internal_id) {
        this.internal_id = internal_id;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getDept_short_form() {
        return dept_short_form;
    }

    public void setDept_short_form(String dept_short_form) {
        this.dept_short_form = dept_short_form;
    }
}
