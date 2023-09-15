package com.mindhub.homebanking.utils;
import java.util.Random;

public final class CardUtils {

    private CardUtils() {
    }

    public static String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder("8666");

        for (int group = 0; group < 1; group++) {
            cardNumber.append(" ");
            for (int digit = 0; digit < 4; digit++) {
                cardNumber.append(new Random().nextInt(10));
            }
        }

        return cardNumber.toString();
    }

    public static String generateCvv() {
        Random random = new Random();
        int randomNumber = random.nextInt(900) + 001; // Genera un número aleatorio entre 100 y 999 (3 dígitos)
        return String.valueOf(randomNumber); // Convierte el número en una cadena de texto
    }



}
