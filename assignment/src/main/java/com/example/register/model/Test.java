package com.example.register.model;

import java.util.Objects;

public class Test {

    String firstName;
    String lastName;
    public  Test(String firstName,String lastName){
        this.firstName=firstName;
        this.lastName=lastName;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Test)) return false;
        Test test = (Test) o;
        return firstName.equals(test.firstName) && lastName.equals(test.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    public static void  main(String args){
        Test test = new Test("Hemant","Patel");
        Test test1=new Test("Patel","Hemant");
        System.out.println(test.hashCode());

        System.out.println(test1.hashCode());

    }
}
