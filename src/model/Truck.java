package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Truck {
    private int id;
    private String name;
    private String model;
    private int year;
    private double odometer;
    private double fuelTankCapacity;
    private TyreType TyreType;

    public Truck(String name, String model, int year, double odometer, double fuelTankCapacity, TyreType tyreType) {
        this.name = name;
        this.model = model;
        this.year = year;
        this.odometer = odometer;
        this.fuelTankCapacity = fuelTankCapacity;
        this.TyreType = tyreType;
    }
}
