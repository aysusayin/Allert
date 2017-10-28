package aysusayin.com.allert;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.ArrayList;
import java.util.Collections;

public class ListeActivity extends AppCompatActivity {

    private ListView productListView;
    private ArrayList<Product> productsArrayList = new ArrayList<Product>();
    private Button signOutButton;
    private Button profileButton;
    private AutoCompleteTextView actv;
    private Button barcodeReaderButton;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);

        progressDialog = new ProgressDialog(ListeActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setIndeterminate(false);
        progressDialog.show();

        productListView = (ListView) findViewById(R.id.listView);
        signOutButton = (Button) findViewById(R.id.logOutButonu);
        barcodeReaderButton = (Button) findViewById(R.id.barcodeButton);
        profileButton = (Button) findViewById(R.id.myProfileButton);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("products");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Product model = postSnapShot.getValue(Product.class);
                    productsArrayList.add(model);
                }

                Collections.sort(productsArrayList);

                MyArrayAdapter adapter = new MyArrayAdapter(getApplicationContext(), productsArrayList);
                productListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                int size = productsArrayList.size();
                final String[] titlesArray = new String[size];
                for (int i = 0; i < size; i++) {
                    titlesArray[i] = productsArrayList.get(i).getName();
                }

                ArrayAdapter<String> actvAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, titlesArray);
                actv.setAdapter(actvAdapter);
                actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ListeActivity.this, ProductActivity.class);
                        String name = (String) parent.getItemAtPosition(position);
                        intent.putExtra("name", name);
                        int index = 0;
                        for (int i = 0; i < titlesArray.length; i++) {
                            if (titlesArray[i].equals(name)) {
                                index = i;
                                break;
                            }
                        }
                        intent.putExtra("contents", productsArrayList.get(index).getContent());
                        actv.setText("");
                        startActivity(intent);
                    }
                });
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("", "Failed to read value.", error.toException());
                progressDialog.dismiss();
                Toast.makeText(ListeActivity.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
            }
        });

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListeActivity.this, ProductActivity.class);
                intent.putExtra("name", productsArrayList.get(position).getName());
                intent.putExtra("contents", productsArrayList.get(position).getContent());
                startActivity(intent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ListeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        barcodeReaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ListeActivity.this, getString(R.string.newProperty), Toast.LENGTH_SHORT).show();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListeActivity.this, ProfileActivity.class));
            }
        });

    }
}
