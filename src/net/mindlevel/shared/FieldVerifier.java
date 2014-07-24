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

    private static final RegExp alphaNum = RegExp.compile("^([a-zA-Z0-9_\\-.?!]+[ ])*[a-zA-Z0-9_\\-.?!]+$");

    /**
     * Verifies that the specified name is valid for our service.
     *
     * @param name the name to validate
     * @return true if valid, false if invalid
     */
    public static boolean isValidName(String name) {
        if (name == null) {
            return false;
        }
        if (alphaNum.exec(name) == null) {
            return false;
        }
        return true;
    }

    public static boolean isValidUsername(String username) {
        return isValidName(username) && username.length() > 3 && username.length() < 21;
    }

    public static boolean isValidPassword(String password, String password2) {
        return password.equals(password2) && password.equals(password2) && password.length() >= 4;
    }

    public static String isValidMetaImage(MetaImage metaImage) {
        if(metaImage.getFilename().equals("")) {
            return "You forgot to send the picture";
        } else if (metaImage.getOwner().equals("")) {
            return "Something seems wrong with your login.";
        } else if (!isValidName(metaImage.getTitle())) {
            return "The title can only contain letters, numbers, space and dashes";
        }
        return "";
    }

    public static String isValidMission(Mission mission) {
        if(mission.getCategories().size()==0 ||
            mission.getDescription().equals("") ||
            mission.getName().equals("")) {
            return "You forgot to fill out one of the fields";
        } else if(!isValidName(mission.getName())) {
            return "The name can only contain letters, numbers and dashes.";
        }
        return "";
    }

    private static final RegExp rfc2822 = RegExp.compile(
            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
    );

    public static boolean isValidEmail(String email) {
        email = email.toLowerCase();
        if (rfc2822.exec(email) == null) {
            return false;
        }
        return true;
    }
}
