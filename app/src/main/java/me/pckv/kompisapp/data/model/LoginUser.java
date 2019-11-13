package me.pckv.kompisapp.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUser {

    private String email;
    private String password;
}
