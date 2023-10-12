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

public class Driver extends User{
    private LocalDate medicalCertificateDate;
    private String medicalCertificationNumber;
    private String driverLicense;

    public Driver(String login, String password, String name, String surname, LocalDate birthDate, LocalDate medicalCertificateDate, String medicalCertificationNumber, String driverLicense) {
        super(login, password, name, surname, birthDate);
        this.medicalCertificateDate = medicalCertificateDate;
        this.medicalCertificationNumber = medicalCertificationNumber;
        this.driverLicense = driverLicense;

    }

    public Driver(int id, String login, String password, String name, String surname, LocalDate birthDate, LocalDate medicalCertificateDate, String medicalCertificationNumber, String driverLicense) {
        super(id, login, password, name, surname, birthDate);
        this.medicalCertificateDate = medicalCertificateDate;
        this.medicalCertificationNumber = medicalCertificationNumber;
        this.driverLicense = driverLicense;
    }
}
