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

public class Checkpoint {
    private int id;
    private String title;
    private boolean longStop;
    private LocalDate dateOfArrival;

    public Checkpoint(String title, boolean longStop, LocalDate dateOfArrival) {
        this.title = title;
        this.longStop = longStop;
        this.dateOfArrival = dateOfArrival;
    }

    public Checkpoint(String title, boolean longStop) {
        this.title = title;
        this.longStop = longStop;
    }
}
