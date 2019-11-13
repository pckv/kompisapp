package me.pckv.kompisapp.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUser {

    private String displayName;
    private String email;
    private String password;
}
