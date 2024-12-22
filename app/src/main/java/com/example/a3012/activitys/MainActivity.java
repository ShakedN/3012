package com.example.a3012.activitys;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.Navigation;

import com.example.a3012.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import moudles.Students;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth=FirebaseAuth.getInstance();
    }

    public void login(View view) {

        String email = ((EditText)findViewById(R.id.inputUserName)).getText().toString();
        String password = ((EditText)findViewById(R.id.inputPass)).getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();

                            // Create a Bundle to pass the email
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);  // Store the email in the Bundle

                            // Navigate and pass the Bundle to fragmentthree
                            Navigation.findNavController(view).navigate(R.id.action_fragmentOne_to_fragmentthree, bundle);

                        } else {
                            Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void reg(View view) {
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        String password = ((EditText)findViewById(R.id.password1)).getText().toString();
        Log.d("result",email+" "+password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,"reg ok",Toast.LENGTH_LONG).show();
                            view.post(() -> Navigation.findNavController(view).navigate(R.id.action_fragmentTwo_to_fragmentOne));

                        } else {
                            Toast.makeText(MainActivity.this,"reg fail",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
    void func(View view){
        Intent intent = new Intent(this, mainActivitySecond.class);
        startActivity(intent);

    }
    public  void addDATA(){
        String phone=((EditText)findViewById(R.id.phone)).getText().toString();
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user").child(phone);
        Students students = new Students(email,phone);
        myRef.setValue(students);
    }
    public void getStudent(String phone){
        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(phone);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Students value = dataSnapshot.getValue(Students.class);
                Toast.makeText(MainActivity.this, value.getEmail(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to read value: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}