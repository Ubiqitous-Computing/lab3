package com.example.sensors;

import androidx.activity.ComponentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ComponentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate()");

        setContentView(R.layout.activity_main);

        /* Variables */
        EditText name = findViewById(R.id.editName);
        EditText password = findViewById(R.id.editPassword);
        EditText telephone = findViewById(R.id.editPhone);
        EditText email = findViewById(R.id.editEmail);
        Button submit = findViewById(R.id.btnSubmit);

        /* Action Listener for submitButton */
        submit.setOnClickListener(v -> {

            String nameText = name.getText().toString().trim();
            String passText = password.getText().toString().trim();
            String teleNo = telephone.getText().toString().trim();
            String emailText = email.getText().toString().trim();

            boolean isValid = true;

            /* Form Validations */

            /* NAME */
            if (nameText.isEmpty()) {
                name.setError("Name is Required");
                isValid = false;
            } else if (!nameText.matches("^[a-zA-Z]+$")) {
                name.setError("Name must contain letters only!");
                isValid = false;
            }

            /* PASSWORD */
            if (passText.isEmpty()) {
                password.setError("Password is Required");
                isValid = false;
            } else if (passText.length() < 3) {
                password.setError("Password must be 3 or more characters!");
                isValid = false;
            }

            /* TELEPHONE */
            if (teleNo.isEmpty()) {
                telephone.setError("Telephone Number is Required");
                isValid = false;
            } else if (!teleNo.matches("^[0-9]+$")) {
                telephone.setError("You can only use numbers 0 - 9");
                isValid = false;
            } else if (teleNo.length() < 3) {
                telephone.setError("Phone Number Not Long Enough!");
                isValid = false;
            }

            /* EMAIL */
            if (emailText.isEmpty()) {
                email.setError("Email is required");
                isValid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                email.setError("Enter a valid email");
                isValid = false;
            }

            /* If everything works */
            if (isValid) {
                Intent intent = new Intent(MainActivity.this, IntentActivity.class);
                /* Pass it to Intent */
                intent.putExtra("name", nameText);
                startActivity(intent);
            }
        });
    }
}