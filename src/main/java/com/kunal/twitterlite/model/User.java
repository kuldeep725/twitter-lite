package com.kunal.twitterlite.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class User {

    private UUID userId;
    private String username;
    private String fullname;
    private String password;

}
