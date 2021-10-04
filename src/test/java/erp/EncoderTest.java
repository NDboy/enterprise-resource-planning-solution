package erp;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncoderTest {

    @Test
    void testEncode() {
        String user = "PHIBeat";
        System.out.println(user);
        System.out.println(new BCryptPasswordEncoder().encode(user));

    }
}
