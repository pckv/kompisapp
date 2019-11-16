package me.pckv.kompisapp.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data class that captures user information for logged in users retrieved from UsersRepository
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoggedInUser {

    private String token;
    private long id;
    private String email;
    private String displayName;
}
