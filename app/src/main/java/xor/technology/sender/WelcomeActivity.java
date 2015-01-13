package xor.technology.sender;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Activity which displays a registration screen to the user.
 */
public class WelcomeActivity extends Activity {

    private EditText username;
    private EditText password;
    private EditText passwordAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sender_welcome);

        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.userPassword);
        passwordAgain = (EditText) findViewById(R.id.userPasswordVerify);

        passwordAgain.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.edittext_action_signup ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    SignUp();
                    return true;
                }
                return false;
            }
        });

        // LOGIN BUTTON FOR ACTIVE USERS
        Button loginButton = (Button) findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // STARTS AN INTENT OF THE LOGIN ACTIVITY
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
            }
        });

        // JOIN BUTTON FOR NEW USERS
        Button signupButton = (Button) findViewById(R.id.joinBtn);
        signupButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SignUp();
            }
        });
    }

    public void SignUp() {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String passAgain = passwordAgain.toString().trim();

        // VALIDATE THE SIGN UP DATA
        boolean validationError = false;

        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));

        if (user.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_username));
        }
        if (user.length() > 20) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_username_too_long));
        }
        if (pass.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_password));
        }
        if (pass.length() > 20) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_password_too_long));
        }
        if (passAgain.length() > 20) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_password_too_long));
        }
        if (!pass.equals(passAgain)) {
            if (validationError)
                validationErrorMessage.append(getString(R.string.error_join));
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_mismatched_passwords));
        }
        validationErrorMessage.append(getString(R.string.error_end));

        // IF THERE IS A VALIDATION ERROR, DISPLAY THE MESSAGE TO THE USER
        if (validationError) {
            Toast.makeText(WelcomeActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        // SET UP A PROGRESS DIALOG
        final ProgressDialog dialog = new ProgressDialog(WelcomeActivity.this);
        dialog.setMessage(getString(R.string.progress_signup));
        dialog.show();

        // SET UP A NEW PARSER USER
        ParseUser pUser = new ParseUser();
        pUser.setUsername(user);
        pUser.setPassword(pass);

        // CALL THE PARSE SIGN UP METHOD
        pUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                if (e != null) {
                    // SHOW THE ERROR MESSAGE
                    Toast.makeText(WelcomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    // START AN INTENT FOR THE DISPATCH ACTIVITY
                    Intent intent = new Intent(WelcomeActivity.this, DispatchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
