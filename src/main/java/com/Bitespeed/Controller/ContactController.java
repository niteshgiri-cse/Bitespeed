package com.Bitespeed.Controller;

import com.Bitespeed.dto.Request.IdentifyRequest;
import com.Bitespeed.dto.Response.IdentifyResponse;
import com.Bitespeed.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identify")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<IdentifyResponse> identify(@RequestBody IdentifyRequest request) {
        return ResponseEntity.ok(contactService.identify(request));
    }
}