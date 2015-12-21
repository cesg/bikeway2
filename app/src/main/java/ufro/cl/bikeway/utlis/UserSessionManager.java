package ufro.cl.bikeway.utlis;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by cristian on 17-12-15.
 */
public class UserSessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    private static final String KEY_PREF = "AUTH";
    private static final String KEY_AUTH_CHECK = "logginCheck";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";

    public UserSessionManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(KEY_PREF, 0);
        this.editor = sharedPreferences.edit();
    }

    public void createSession(String name, String email) {
        editor.putBoolean(KEY_AUTH_CHECK, true);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(KEY_AUTH_CHECK, false);
    }
}
