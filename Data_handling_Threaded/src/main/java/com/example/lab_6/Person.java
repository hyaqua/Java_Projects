package com.example.lab_6;

import java.time.LocalDate;

public class Person {
    int id;
    String firstName;
    String lastName;
    String email;
    String gender;
    String country;
    String domainName;
    LocalDate birthDate;

    public Person(int id, String firstName, String lastName,
                  String email, String gender, String country,
                  String domainName, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.country = country;
        this.domainName = domainName;
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public String getDomainName() {
        return domainName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
