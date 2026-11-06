package com.orchestriq.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class FacilityCreateRequest {
    @NotBlank(message = "Facility name is required")
    @Size(
            max = 100,
            message = "Facility name cannot exceed 100 characters"
    )
    private String facilityName;

    @NotBlank(message = "Facility type is required")
    @Pattern(
            regexp = "PHC|CHC",
            message = "Facility Type must be either PHC or CHC"
    )
    private String facilityType;

    private String address;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Contact number contain exactly 10 digits"
    )
    private String contactNumber;

    public FacilityCreateRequest(){

    }

    public String getFacilityName(){
        return facilityName;
    }
    public String getAddress(){
        return address;
    }
    public String getFacilityType(){
        return facilityType;
    }
    public String getContactNumber(){
        return contactNumber;
    }
}
