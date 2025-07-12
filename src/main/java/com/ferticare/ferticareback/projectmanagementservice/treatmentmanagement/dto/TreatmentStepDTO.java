package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentStepDTO {
<<<<<<< HEAD
    
    @JsonProperty("step")
    private Integer step;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("duration")
    private String duration;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("activities")
    private List<String> activities;
} 
=======

    @JsonProperty("step")
    private Integer step;

    @JsonProperty("name")
    private String name;

    @JsonProperty("duration")
    private String duration;

    @JsonProperty("description")
    private String description;

    @JsonProperty("activities")
    private List<String> activities;
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
