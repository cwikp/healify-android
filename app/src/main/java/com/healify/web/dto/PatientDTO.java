package com.healify.web.dto;


import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class PatientDTO implements Serializable{
    private String id;
    private String beaconId;

    private String firstName;
    private String lastName;
    private String address;
    private String pesel;
    private String birthDate;

    private String in;
    private String out;

    private String disease;
    private String doctor;
    private HealthStateDTO healthState;
    private List<CheckUpDTO> checkUps;
    private List<DrugDTO> drug;
}
