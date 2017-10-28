package aysusayin.com.allert;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView contentsTextView;
    private TextView warningTextView;
    private String allergens = " ";
    private String title = "";
    private ArrayList<String> allergenList = new ArrayList<>();
    private String contents;
    private ImageButton blackListButton;
    private ImageButton favoritesButton;
    private Product currentProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        titleTextView = (TextView) findViewById(R.id.ProductNameTextView);
        contentsTextView = (TextView) findViewById(R.id.textView7);
        warningTextView = (TextView) findViewById(R.id.warningTextView);
        favoritesButton = (ImageButton) findViewById(R.id.favouritesImageButton);
        blackListButton = (ImageButton) findViewById(R.id.blackListImageButton);

        Intent intent = getIntent();
        title = intent.getStringExtra("name");
        contents = intent.getStringExtra("contents");
        currentProduct = new Product(title, contents);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users").child(user.getUid());
        titleTextView.setText(title);
        contentsTextView.setMovementMethod(new ScrollingMovementMethod());
        contentsTextView.setText(contents);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                allergens = value.getAllergens();
                seperate(allergens);
                boolean check = true;
                for (int i = 0; i < allergenList.size(); i++) {
                    if (contents.toLowerCase().contains(allergenList.get(i))) {
                        warningTextView.setText(getString(R.string.warning));
                        warningTextView.setTextColor(Color.RED);
                        check = false;
                        break;
                    }
                }
                if (check) {
                    warningTextView.setText(getString(R.string.safe));
                    warningTextView.setTextColor(Color.parseColor("#01DF01"));
                }

                ArrayList<Product> fav = value.favorites;
                ArrayList<Product> black = value.blackList;

                if (fav.contains(currentProduct)) {
                    favoritesButton.setImageResource(android.R.drawable.btn_star_big_on);
                }
                if (black.contains(currentProduct)) {
                    blackListButton.setImageResource(R.drawable.warningred);
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("", "Failed to read value.", error.toException());
                Toast.makeText(ProductActivity.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
            }
        });


        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user_ = dataSnapshot.getValue(User.class);
                        if (!(user_.favorites.contains(currentProduct))) {
                            user_.favorites.add(currentProduct);
                            myRef.setValue(user_);
                            favoritesButton.setImageResource(android.R.drawable.btn_star_big_on);
                        }
                        Toast.makeText(ProductActivity.this, "Favorilere eklendi", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("", "Failed to read value.", databaseError.toException());
                    }
                });
            }
        });

        blackListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user_ = dataSnapshot.getValue(User.class);
                        if (!(user_.blackList.contains(currentProduct))) {
                            user_.blackList.add(currentProduct);
                            myRef.setValue(user_);
                            blackListButton.setImageResource(R.drawable.warningred);
                        }
                        Toast.makeText(ProductActivity.this, "Kara Listeme eklendi", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("", "Failed to read value.", databaseError.toException());
                    }
                });
            }
        });

    }

    public String deleteSpaces(String allergensWithSpace) {
        String s = "";
        for (int i = 0; i < allergensWithSpace.length(); i++) {
            if (allergensWithSpace.charAt(i) != ' ') {
                s += allergensWithSpace.charAt(i);
            }
        }
        return s;
    }

    public void seperate(String allergens) {
        String allergensWithoutSpace = deleteSpaces(allergens);
        if (allergensWithoutSpace.contains(",")) {
            while (allergensWithoutSpace.contains(",")) {
                allergenList.add(allergensWithoutSpace.substring(0, allergensWithoutSpace.indexOf(',')).toLowerCase());
                allergensWithoutSpace = allergensWithoutSpace.substring(allergensWithoutSpace.indexOf(',') + 1);
            }
            allergenList.add(allergensWithoutSpace.toLowerCase());
        } else {
            allergenList.add(allergensWithoutSpace.toLowerCase());
        }
    }


}
