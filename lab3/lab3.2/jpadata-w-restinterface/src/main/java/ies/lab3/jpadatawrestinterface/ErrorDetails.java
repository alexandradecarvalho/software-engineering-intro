package ies.lab3.jpadatawrestinterface;

import java.util.Date;

public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;

    public ErrorDetails(Date timestamp, String message, String details){
        super();
        this.timestamp = timestamp;
        this.details = details;
        this.message = message;
    }

    public Date getTimestamp(){
        return timestamp;
    }

    public String getDetails(){
            return details;
    }
}
