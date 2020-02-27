package com.vorotilin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

//Response from server representation class, aswel as xml annonation for JAXB
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class Response {
    @XmlElement(name = "message")
    private String message;
    private User user;
    @XmlElement(name = "date")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date date;

    public Response(User user) {
        this.message = "Добрый день, " + user.getFirstName() + " " + user.getLastName() + ", Ваше сообщение успешно обработано!";
        this.date = new Date();
    }

    public Response() {
    }
    public String getMessage()
    {
        return message;
    }
}
