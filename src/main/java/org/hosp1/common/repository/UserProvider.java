package org.hosp1.common.repository;

import org.hosp1.common.model.User;

import java.util.Optional;

/**
 * Anything that can look up a user by email, so AuthController can log in
 * admins, staff and patients without knowing which repository holds them.
 */
public interface UserProvider {

    /** Returns the user with this email (case-insensitive), or empty if none exists. */
    Optional<User> findByEmail(String email);
}