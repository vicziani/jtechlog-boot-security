package jtechlog.jtechlogbootsecurity;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class BCryptTest {

    @Test
    void testEncode() {
        System.out.println(new BCryptPasswordEncoder().encode("user"));
    }

}
