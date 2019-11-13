package me.pckv.kompisapp.ui.user.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class exposing authenticated user details to the UI.
 */
@AllArgsConstructor
class LoggedInUserView {

    @Getter
    private String displayName;
    //... other data fields that may be accessible to the UI
}
