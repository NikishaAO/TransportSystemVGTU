package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Delivery {
    private int id;
    private String startPoint;
    private String endPoint;
    private long startLn;
    private long startLt;
    private long endLn;
    private long endLt;
    private int driver;
    private LocalDate dateOfDeparture;
    private LocalDate dateofArrival;
    private int truck;
    private int cargo;
    private boolean isInProcess;
    private List<Checkpoint> checkpointList;
    private List<Manager> responsibleManagers;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;

    public Delivery(String startPoint, String endPoint, long startLn, long startLt, long endLn, long endLt, List<Checkpoint> checkpointList, List<Manager> responsibleManagers) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startLn = startLn;
        this.startLt = startLt;
        this.endLn = endLn;
        this.endLt = endLt;
        this.checkpointList = checkpointList;
        this.responsibleManagers = responsibleManagers;
    }

    public Delivery(String startPoint, String endPoint, long startLn, long startLt, long endLn, long endLt, int truck, int cargo, List<Checkpoint> checkpointList, List<Manager> responsibleManagers) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startLn = startLn;
        this.startLt = startLt;
        this.endLn = endLn;
        this.endLt = endLt;
        this.truck = truck;
        this.cargo = cargo;
        this.checkpointList = checkpointList;
        this.responsibleManagers = responsibleManagers;
    }

    public Delivery(String startPoint, String endPoint, long startLn, long startLt, long endLn, long endLt, int driver, LocalDate dateOfDeparture, LocalDate dateofArrival, int truck, int cargo, boolean isInProcess) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startLn = startLn;
        this.startLt = startLt;
        this.endLn = endLn;
        this.endLt = endLt;
        this.driver = driver;
        this.dateOfDeparture = dateOfDeparture;
        this.dateofArrival = dateofArrival;
        this.truck = truck;
        this.cargo = cargo;
        this.isInProcess = isInProcess;
    }

    public Delivery(int id, String startPoint, String endPoint, long startLn, long startLt, long endLn, long endLt, int driver, LocalDate dateOfDeparture, LocalDate dateofArrival, int truck, int cargo, boolean isInProcess) {
        this.id = id;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startLn = startLn;
        this.startLt = startLt;
        this.endLn = endLn;
        this.endLt = endLt;
        this.driver = driver;
        this.dateOfDeparture = dateOfDeparture;
        this.dateofArrival = dateofArrival;
        this.truck = truck;
        this.cargo = cargo;
        this.isInProcess = isInProcess;
    }

    public Delivery(String startPoint, String endPoint, long startLn, long startLt, long endLn, long endLt, LocalDate dateOfDeparture, LocalDate dateofArrival, int cargo, boolean isInProcess) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startLn = startLn;
        this.startLt = startLt;
        this.endLn = endLn;
        this.endLt = endLt;
        this.dateOfDeparture = dateOfDeparture;
        this.dateofArrival = dateofArrival;
        this.cargo = cargo;
        this.isInProcess = isInProcess;
    }
}

