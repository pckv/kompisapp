package me.pckv.kompisapp.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data class that captures user information for logged in users retrieved from UsersRepository
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoggedInUser extends User {

    private String token;
    private String email;
}
