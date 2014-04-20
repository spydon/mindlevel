package net.mindlevel.shared;

import com.google.gwt.regexp.shared.RegExp;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is not translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {

    /**
     * Verifies that the specified name is valid for our service.
     *
     * In this example, we only require that the name is at least four
     * characters. In your application, you can use more complex checks to ensure
     * that usernames, passwords, email addresses, URLs, and other fields have the
     * proper syntax.
     *
     * @param name the name to validate
     * @return true if valid, false if invalid
     */
    public static boolean isValidUsername(String name) {
        if (name == null) {
            return false;
        }
        return name.length() > 3 && name.length() < 21;
    }

    public static boolean isValidPassword(String password, String password2) {
        return password.equals(password2) && password.length() >= 4;
    }

    public static boolean isValidMetaImage(MetaImage metaImage) {

        return !metaImage.getFilename().equals("") && !metaImage.getOwner().equals("");
    }

    public static boolean isValidMission(Mission mission) {
        // TODO Auto-generated method stub
        return true;
    }

    private static final RegExp rfc2822 = RegExp.compile(
            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
    );

    public static boolean isValidEmail(String email) {
        if (rfc2822.exec(email) == null) {
            return false;
        }
        return true;
    }
}
