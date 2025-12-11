package org.example.hotel_management.util;

import io.github.cdimascio.dotenv.Dotenv;

public class DotEnvUtil {

    public static Dotenv getDotenv() {
        return Dotenv.configure().load();
    }
}
