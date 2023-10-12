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

public class Cargo {
    private int id;
    private String title;
    private double weight;
    private CargoType cargoType;
    private String description;
    private String customer;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;

    public Cargo(String title, double weight, CargoType cargoType, String description, String customer) {
        this.title = title;
        this.weight = weight;
        this.cargoType = cargoType;
        this.description = description;
        this.customer = customer;
        this.dateCreated = LocalDate.now();
    }
}
