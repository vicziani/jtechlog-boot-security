package jtechlog.jtechlogbootsecurity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Sql(statements = "delete from users where username not in ('user', 'admin')")
class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    void testNotAuthenticated() {
        var user = new User("johndoe", "johndoe", "ROLE_USER");
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userService.addUser(user));
    }

    @Test
    @WithMockUser
    void testNoRights() {
        var user = new User("johndoe", "johndoe", "ROLE_USER");
        assertThrows(AccessDeniedException.class, () -> userService.addUser(user));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testHasRights() {
        userService.addUser(new User("johndoe", "johndoe", "ROLE_USER"));
        var users = userService.listUsers();
        assertThat(users)
                .extracting(User::getUsername)
                .containsExactly("admin", "johndoe", "user");
    }

    @Test
    @WithUserDetails("admin")
    void testLoadAndHasRights() {
        userService.addUser(new User("johndoe", "johndoe", "ROLE_USER"));
        var users = userService.listUsers();
        assertThat(users)
                .extracting(User::getUsername)
                .containsExactly("admin", "johndoe", "user");
    }
}
