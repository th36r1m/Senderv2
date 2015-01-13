package xor.technology.sender;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


/**
 * Created by Anton on 1/11/15.
 */
public class LoginActivity extends Activity {
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sender_login);

        // SET UP THE LOGIN FORM
        username = (EditText) findViewById(R.id.userNameLogin);
        password = (EditText) findViewById(R.id.userPasswordLogin);

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.edittext_action_login ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    Login();
                    return true;
                }
                return false;
            }
        });

        // Set up the submit button click handler
        Button actionButton = (Button) findViewById(R.id.loginBtnMember);
        actionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Login();
            }
        });
    }

    public void Login() {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

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
        validationErrorMessage.append(getString(R.string.error_end));

        // IF THERE IS A VALIDATION ERROR, DISPLAY THE MESSAGE TO THE USER
        if (validationError) {
            Toast.makeText(LoginActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        // SET UP A PROGRESS DIALOG
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage(getString(R.string.progress_signup));
        dialog.show();

        // SET UP A NEW PARSER USER
        ParseUser.logInInBackground(user, pass, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                dialog.dismiss();
                if (e != null) {
                    // SHOW THE ERROR MESSAGE
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    // START AN INTENT FOR THE DISPATCH ACTIVITY
                    Intent intent = new Intent(LoginActivity.this, DispatchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
