package local.jarios.email.common.util;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

import java.util.ArrayList;
import java.util.List;

public class EmailValidator {

    public static List<String> getInvalidEmailsRFC(String commaSeparatedEmails) {
        List<String> invalids = new ArrayList<>();
        String[] emails = commaSeparatedEmails.split(",");

        for (String email : emails) {
            String trimmed = email.trim();
            if (!trimmed.isEmpty() && !isValidEmailRFC(trimmed)) {
                invalids.add(trimmed);
            }
        }

        return invalids;
    }

    public static boolean isValidEmailRFC(String email) {
        try {
            InternetAddress addr = new InternetAddress(email, true);
            addr.validate(); // lanza excepción si no es válido
            return true;
        } catch (AddressException e) {
            return false;
        }
    }
}
