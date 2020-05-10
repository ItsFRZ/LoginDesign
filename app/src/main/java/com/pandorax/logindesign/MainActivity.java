package com.pandorax.logindesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {




    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String emailPass ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{4,}$";
    CardView cardView1,cardView2;
    RelativeLayout loginbut,signupbut;
    EditText lid,lpas;
    EditText rid,rpas,rno,rname;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For Visisbility
        cardView1 = (CardView) findViewById(R.id.card1);
        cardView2 = (CardView) findViewById(R.id.card2);
        loginbut = (RelativeLayout) findViewById(R.id.loginbut);
        signupbut = (RelativeLayout) findViewById(R.id.signupbut);

        // For Login
        lid = (EditText) findViewById(R.id.lid);
        lpas = (EditText) findViewById(R.id.lpas);


        // Firebase Activity
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Information");
        firebaseAuth = FirebaseAuth.getInstance();


        //For Register
        rid = (EditText) findViewById(R.id.rid);
        rpas = (EditText) findViewById(R.id.rpas);
        rno = (EditText) findViewById(R.id.rno);
        rname = (EditText) findViewById(R.id.rname);


    }

    public void Signup(View view) {

        cardView1.setVisibility(View.GONE);
        cardView2.setVisibility(View.VISIBLE);
        signupbut.setVisibility(View.GONE);
        loginbut.setVisibility(View.VISIBLE);



    }

    public void Login(View view) {


        cardView1.setVisibility(View.VISIBLE);
        cardView2.setVisibility(View.GONE);
        signupbut.setVisibility(View.VISIBLE);
        loginbut.setVisibility(View.GONE);

    }

    public void LoginAction(View view) {

        String email = lid.getText().toString().trim();
        String pass = lpas.getText().toString().trim();

        if(email.matches(emailPattern) && pass != "")
        {
            Toast.makeText(this, "Valid Email Address Or Password", Toast.LENGTH_SHORT).show();
            firebaseAuth.signInWithEmailAndPassword(lid.getText().toString().trim(),lpas.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        Intent intent = new Intent(getApplicationContext(),Final.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(MainActivity.this, "No Match Or Invalid Id or Password", Toast.LENGTH_SHORT).show();
                }
            });

        }
         else{

            Snackbar snackbar = Snackbar.make(view,"Invalid Email Address Or Password",Snackbar.LENGTH_LONG);
            snackbar.show();
            (lid).setError("Invalid Email Address");
            (lpas).setError("Invalid Password");


            YoYo.with(Techniques.Flash)
                    .duration(700)
                    .repeat(1)
                    .playOn(findViewById(R.id.lid));


            YoYo.with(Techniques.Flash)
                    .duration(700)
                    .repeat(1)
                    .playOn(findViewById(R.id.lpas));

        }
    }


    public void RegisterAction(View view) {

        final String remail = rid.getText().toString().trim();
        final String rpass = rpas.getText().toString().trim();
        final String rnumber = rno.getText().toString().trim();
        final String rnames = rname.getText().toString().trim();

        if(remail.isEmpty())
        {
            rid.setError("Empty Email Field");
            YoYo.with(Techniques.Flash)
                    .duration(700)
                    .repeat(1)
                    .playOn(findViewById(R.id.rid));


        }
        else if(rpass.isEmpty())
        {
            rpas.setError("Empty Password Field");

            YoYo.with(Techniques.Flash)
                    .duration(700)
                    .repeat(1)
                    .playOn(findViewById(R.id.rpas));

        }

        else if(rnumber.isEmpty())
        {
            rno.setError("Empty Number Field");
            YoYo.with(Techniques.Flash)
                    .duration(700)
                    .repeat(1)
                    .playOn(findViewById(R.id.rno));

        }

        else if(rnames.isEmpty())
        {
            rname.setError("Empty Your Name Field");
            YoYo.with(Techniques.Flash)
                    .duration(700)
                    .repeat(1)
                    .playOn(findViewById(R.id.rname));

        }else{
            firebaseAuth.createUserWithEmailAndPassword(rid.getText().toString().trim(),rpas.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        HashMap<String,String> map = new HashMap<>();
                        map.put("email",rid.getText().toString().trim());
                        map.put("password",rpas.getText().toString().trim());
                        map.put("name",rname.getText().toString().trim());
                        map.put("number",rno.getText().toString().trim());
                        databaseReference.push().setValue(map);
                        Toast.makeText(MainActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(MainActivity.this, "Registration Failed :(", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }
}
