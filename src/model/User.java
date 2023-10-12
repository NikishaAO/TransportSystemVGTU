package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User {
    protected int id;
    private String login;
    private String password;
    private String name;
    private String surname;
    private LocalDate birthDate;

    public User(String login, String password, String name, String surname, LocalDate birthDate) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
    }


}
