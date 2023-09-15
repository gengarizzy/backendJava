package com.mindhub.homebanking;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class CardUtilsTest {

    @Test
    public void testGenerateCardNumber() {
        String cardNumber = CardUtils.generateCardNumber();

        // que la cadena generada tenga el formato correcto
        assertTrue(cardNumber.matches("^8666 \\d{4}$"));
    }

    @Test
    public void testGenerateCvv() {
        String cvv = CardUtils.generateCvv();

        // que el CVV tenga 3 dígitos
        assertTrue(cvv.matches("^\\d{3}$"));

        //verificar que CVV sea un numero valido (entre 001 y 999)
        int cvvNumber = Integer.parseInt(cvv);
        assertTrue(cvvNumber >= 001 && cvvNumber <= 999);
    }


}
