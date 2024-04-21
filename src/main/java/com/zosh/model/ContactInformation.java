package com.zosh.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformation {


    private String email;
    private String mobile;
    private String twitter;
    private String instagram;
}
