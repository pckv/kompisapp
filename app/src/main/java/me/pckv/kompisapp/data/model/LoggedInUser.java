package me.pckv.kompisapp.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Data
@AllArgsConstructor
public class LoggedInUser {

    private String token;
    private String email;
    private String displayName;
}
