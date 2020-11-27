package it.polimi.db2.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class RegistrationUtils {
	// Standard e-Mail regex
	private static String emailRegex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
	
	public static boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile(emailRegex);
		// Check against the input e-mail
		Matcher matcher = pattern.matcher(email); 
		return matcher.matches();
	}
}
