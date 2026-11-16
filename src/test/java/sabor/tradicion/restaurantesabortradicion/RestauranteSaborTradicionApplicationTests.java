package sabor.tradicion.restaurantesabortradicion;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
class RestauranteSaborTradicionApplicationTests {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {

        List<String> passwords = List.of("alonso123", "nuevaClave123", "soyfabianita123","soyjuanito");

        for (String password : passwords) {
            String hashedPassword = passwordEncoder.encode(password);
            System.out.println("Contraseña: " + password + " | Hash: " + hashedPassword);
        }
    }

}
