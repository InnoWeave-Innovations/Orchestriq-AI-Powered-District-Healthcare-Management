package com.orchestriq.backend.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class FacilityResponse {
    private UUID facilityId;
    private String facilityName;
    private String facilityType;
    private String address;
    private String contactNumber;
    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public FacilityResponse(
            UUID facilityId,
            String facilityName,
            String facilityType,
            String address,
            String contactNumber,
            String status,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ){
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.facilityType = facilityType;
        this.address = address;
        this.contactNumber = contactNumber;
        this.status = status;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }
}
