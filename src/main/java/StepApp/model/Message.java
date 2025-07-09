package StepApp.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Message {

    private final Long id;
    private final String src;
    private final String dst;
    private final String message;
    private final Long date;

    public Message(Long id, String src, String dst, String message, Long date) {
        this.id = id;
        this.src = src;
        this.dst = dst;
        this.message = message;
        this.date = date;
    }

    public Long getId() { return id; }
    public String getSrc() { return src; }
    public String getDst() { return dst; }
    public String getMessage() { return message; }
    public Long getDate() { return date; }

    public String getFormattedDate(){
        LocalDateTime dateTime = Instant.ofEpochMilli(this.date).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return dateTime.format(DateTimeFormatter.ofPattern("MM dd, HH:mm"));
    }
}
