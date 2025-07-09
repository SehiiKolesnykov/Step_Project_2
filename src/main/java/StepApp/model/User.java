package StepApp.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static java.time.ZoneOffset.UTC;

public class User {

    private int id;
    private final String name;
    private final String surname;
    private final String gender;
    private final String avatarUrl;
    private final String profession;
    private Long lastVisit;
    private final String email;
    private final String password;

    public User(

            String name,
            String surname,
            String gender,
            String avatarUrl,
            String profession,
            Long lastVisit,
            String email,
            String password) {


        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.profession = profession;
        this.lastVisit = lastVisit;
        this.email = email;
        this.password = password;
    }

    public User(
            int id,
            String name,
            String surname,
            String gender,
            String avatarUrl,
            String profession,
            Long lastVisit,
            String email) {

        this.id = id;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.profession = profession;
        this.lastVisit = lastVisit;
        this.email = email;
        this.password = null;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getGender() { return gender; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getProfession() { return profession; }
    public Long getLastVisit() { return lastVisit; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setLastVisit(Long lastVisit) { this.lastVisit = lastVisit; }

    public String getLastVisitToString(){
        LocalDate date = Instant.ofEpochMilli(this.lastVisit).atZone(UTC).toLocalDate();
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public Long daysSinceLastVisit(){
        if (this.lastVisit == null) return (long) -1;
        LocalDate date = Instant.ofEpochMilli(this.lastVisit).atZone(UTC).toLocalDate();
        LocalDate today = LocalDate.now();
        return ChronoUnit.DAYS.between(date, today);
    }
}
