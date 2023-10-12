package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Manager extends User{
    private String email;
    private LocalDate employmentDate;
    private Boolean isAdmin;

    public Manager(String login, String password, String name, String surname, LocalDate birthDate, String email, LocalDate employmentDate, boolean isAdmin) {
        super(login, password, name, surname, birthDate);
        this.email = email;
        this.employmentDate = employmentDate;
        this.isAdmin = isAdmin;
    }

    public Manager(int id, String login, String password, String name, String surname, LocalDate birthDate, String email, LocalDate employmentDate, Boolean isAdmin) {
        super(id, login, password, name, surname, birthDate);
        this.email = email;
        this.employmentDate = employmentDate;
        this.isAdmin = isAdmin;
    }
}
