package jtechlog.jtechlogbootsecurity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from users where username not in ('user', 'admin')")
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testNotAuthenticated() {
        expectedException.expect(AuthenticationCredentialsNotFoundException.class);
        userService.addUser(new User("johndoe", "johndoe", "ROLE_USER"));
    }

    @Test
    @WithMockUser
    public void testNoRights() {
        expectedException.expect(AccessDeniedException.class);
        userService.addUser(new User("johndoe", "johndoe", "ROLE_USER"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testHasRights() {
        userService.addUser(new User("johndoe", "johndoe", "ROLE_USER"));
        List<User> users = userService.listUsers();
        assertEquals(List.of("admin", "johndoe", "user"), users.stream()
                .map(User::getUsername).collect(Collectors.toList()));
    }

    @Test
    @WithUserDetails("admin")
    public void testLoadAndHasRights() {
        userService.addUser(new User("johndoe", "johndoe", "ROLE_USER"));
        List<User> users = userService.listUsers();
        assertEquals(List.of("admin", "johndoe", "user"), users.stream()
                .map(User::getUsername).collect(Collectors.toList()));
    }
}
