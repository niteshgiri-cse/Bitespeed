package com.Bitespeed.dto.Response;

import com.Bitespeed.dto.ContactData;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdentifyResponse {
    private ContactData contact;
}