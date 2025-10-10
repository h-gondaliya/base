package com.netcarat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for client information used in invoice generation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private String name;
    private String address;
    private String email;
    private String phone;
}