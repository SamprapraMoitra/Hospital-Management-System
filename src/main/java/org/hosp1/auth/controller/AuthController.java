package org.hosp1.auth.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.hosp1.auth.model.SessionManager;
import org.hosp1.auth.view.LoginView;
import org.hosp1.common.model.User;
import org.hosp1.common.repository.UserProvider;
import org.hosp1.common.util.PasswordUtil;


public class AuthController {

    private final List<UserProvider> providers;
    private final LoginView view;
    private final SessionManager session;
    private final Consumer<User> onLoginSuccess; // HospitalApp opens the right dashboard

    public AuthController(List<UserProvider> providers,
                          LoginView view,
                          SessionManager session,
                          Consumer<User> onLoginSuccess,
                          Runnable onRegisterRequested) {
        this.providers = providers;
        this.view = view;
        this.session = session;
        this.onLoginSuccess = onLoginSuccess;

        // connect the button clicks to controller methods
        view.setLoginAction(this::login);
        view.setRegisterAction(onRegisterRequested);
    }

    /** Opens the login screen. */
    public void start() {
        view.showView();
    }

    public void login() {
        String email = view.getEmail().trim();
        char[] password = view.getPassword();
        try {
            if (email.isEmpty() || password.length == 0) {
                view.showError("Please enter both email and password.");
                return;
            }

            Optional<User> user = authenticate(email, password);
            if (user.isPresent()) {
                session.setCurrentUser(user.get());
                view.clearPassword();
                view.hideView();
                onLoginSuccess.accept(user.get());
            } else {
                // same message for "no such user" and "wrong password"
                view.showError("Invalid email or password.");
                view.clearPassword();
            }
        } finally {
            Arrays.fill(password, '\0'); // wipe the password from memory
        }
    }

    public void logout() {
        session.clear();
        view.showView();
    }

    private Optional<User> authenticate(String email, char[] password) {
        for (UserProvider provider : providers) {
            Optional<User> found = provider.findByEmail(email);
            if (found.isPresent()
                    && PasswordUtil.matches(password, found.get().getPasswordHash())) {
                return found;
            }
        }
        return Optional.empty();
    }
}