package ies.lab3.jpadatawrestinterface;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {
    private long id;
    private String firstName;
    private String lastName;
    private String emailId;

    public Employee(){}

    public Employee(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    @Column(name = "first_name", nullable = false)
    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    @Column(name = "last_name", nullable = false)
    public String getLastName(){
        return lastName;
    }

    public void setLastName(String name) {
        this.lastName = name;
    }

    @Column(name = "email_address", nullable = false)
    public String getEmailId(){
        return emailId;
    }

    public void setEmailId(String email) {
        this.emailId = email;
    }

    @Override
    public String toString() {
        return "Employee [id = " + id + ", firstName = " + firstName+ ", lastName = " + lastName +
                ", emailId = " + emailId + "]";
    }
}
