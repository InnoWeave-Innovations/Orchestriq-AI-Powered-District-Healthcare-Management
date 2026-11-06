package com.orchestriq.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class FacilityUpdateRequest {
    @NotBlank(message = "Facility name is required")
    @Size(max = 100, message = "Facility name cannot exceed 100 characters")
    private String facilityName;

    @NotBlank(message = "Facility type is required")
    @Pattern(regexp = "PHC|CHC",
            message = "Facility type must be PHC or CHC")
    private String facilityType;

    private String address;

    @Pattern(regexp = "^[0-9]{10}$",
            message = "Contact number must contain exactly 10 digits")
    private String contactNumber;

    public FacilityUpdateRequest() {
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
