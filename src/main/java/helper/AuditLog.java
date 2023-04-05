package helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Will allow program to log the attempted logins, will state if successful or not, and user
 */
public class AuditLog {

    private static final String logFileName = "login_activity.txt";

    /**
     * This will log log-in attempts and write to file.
     * @param username
     * @param loginStatus
     * @throws IOException
     */
    public static void userActivityLogger (String username, Boolean loginStatus) throws IOException {

        try{
            String result = "Successful";
            if (!loginStatus) {
                result = "Unsuccessful";
            }

            //seting the second parameter to true to add new login attempts instead of replacing them
            BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFileName, true));
            logWriter.append("At time: " + ZonedDateTime.now(ZoneOffset.UTC).toString() + "-UTC\n"  +
                    "Attempted login by User: " + username + "\n" +
                    "Login attempt: " + result + "\n");

            //need to clear & close the file.
            logWriter.flush();;
            logWriter.close();
        } catch (IOException error) {
            error.printStackTrace();
        }

    }

}
