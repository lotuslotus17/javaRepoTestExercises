package com.vorotilin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
//Client user representation class, aswel as xml annonation for JAXB
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
    @XmlElement(name = "name")
    private String firstName;
    @XmlElement(name = "secondname")
    private String lastName;
    @XmlElement(name = "message")
    private String message;
    @XmlElement(name = "date")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date date;

    public User(String firstName, String lastName, String message) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.message = message;
        this.date = new Date();
    }

    public User() {
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
