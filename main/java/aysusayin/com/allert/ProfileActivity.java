package aysusayin.com.allert;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView mailTextView;
    private TextView allergensTextView;
    private String uid;
    private String email;
    private String username;
    private String allergens;
    private Button blackListButton;
    private Button favoritesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        usernameTextView = (TextView) findViewById(R.id.userNameTextView);
        mailTextView = (TextView) findViewById(R.id.emailTextView);
        allergensTextView = (TextView) findViewById(R.id.allergensTextView);
        blackListButton = (Button) findViewById(R.id.blackListButton);
        favoritesButton = (Button) findViewById(R.id.favoritesButton);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            uid = user.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users").child(uid);

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User value = dataSnapshot.getValue(User.class);
                    Log.d("USER BULUNDU", "Value is: " + value.getEmail());

                    email = value.getEmail();
                    username = value.getUsername();
                    allergens = value.getAllergens();

                    usernameTextView.setText(username);
                    mailTextView.setText(email);
                    allergensTextView.setText(allergens);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("USER DATA OKUNMADI", "Failed to read value.", error.toException());
                }
            });
        }

        blackListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, BlackListActivity.class));
            }
        });

        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, FavoritesActivity.class));
            }
        });

        allergensTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LayoutInflater li = LayoutInflater.from(ProfileActivity.this);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                userInput.setText(allergens);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        allergensTextView.setText(userInput.getText());

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef2 = database.getReference("users").child(uid);
                                        Map<String, Object> map = new HashMap<String, Object>();
                                        map.put("allergens", userInput.getText().toString());
                                        myRef2.updateChildren(map);

                                    }
                                })
                        .setNegativeButton(getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return false;
            }
        });
    }
}
