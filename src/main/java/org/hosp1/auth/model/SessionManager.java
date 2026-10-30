package org.hosp1.auth.model;

import java.util.Optional;

import org.hosp1.common.model.User;


public class SessionManager {

    private User currentUser;
    public void setCurrentUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        this.currentUser = user;
    }

    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }


    public boolean isLoggedInAs(Class<? extends User> type) {
        return type.isInstance(currentUser);
    }

    public void clear() {
        this.currentUser = null;
    }
}