package edu.nku.csc450.realEstate.web.model;

import java.lang.*;

public class Person {
    int p_id;
    String e_mail;
    String f_name;
    String l_name;
    String u_name;
    int is_agent;
    int salary;

    public Person() {
        this.p_id = 0;
        this.e_mail = "";
        this.f_name = "";
        this.l_name = "";
        this.u_name = "";
        this.is_agent = 0;
        this.salary = 0;
    }

    public Person(String u_name) {
        this.p_id = 0;
        this.e_mail = "";
        this.f_name = "";
        this.l_name = "";
        this.u_name = u_name;
        this.is_agent = 0;
        this.salary = 0;
    }

    public Person(int p_id, String e_mail, String f_name,String l_name, String u_name, int is_agent, int salary) {
        this.p_id = p_id;
        this.e_mail = e_mail;
        this.f_name = f_name;
        this.l_name = l_name;
        this.u_name = u_name;
        this.is_agent = is_agent;
        this.salary = salary;
    }

    public int getID(){
        return this.p_id;
    }

    public String getUName(){
        return this.u_name;
    }

    public String getFName(){
        return this.f_name;
    }

    public String getLName(){
        return this.l_name;
    }

    public String getEMail(){
        return this.e_mail;
    }

    public int getIsAgent(){
        return this.is_agent;
    }

    public int getSalary(){
        return this.salary;
    }

    //overriding .equals() so we can do comparisons on these person objects
    public boolean equals(Person p){
        return (p.getUName()).equals(this.u_name);
    }
}