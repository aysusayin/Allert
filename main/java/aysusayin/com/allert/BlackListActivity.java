package aysusayin.com.allert;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BlackListActivity extends AppCompatActivity {

    private ListView blackListListView;
    private ArrayList<Product> blackProducts;
    private ProgressDialog progressDialog;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);

        progressDialog = new ProgressDialog(BlackListActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setIndeterminate(false);
        progressDialog.show();

        blackListListView = (ListView) findViewById(R.id.blackListListView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(user.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                blackProducts = user.blackList;
                Collections.sort(blackProducts);

                MyArrayAdapter adapter = new MyArrayAdapter(getApplicationContext(), blackProducts);
                blackListListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Failed", "on cancelled");
            }
        });

        blackListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BlackListActivity.this, ProductActivity.class);
                intent.putExtra("name", blackProducts.get(position).getName());
                intent.putExtra("contents", blackProducts.get(position).getContent());
                startActivity(intent);
            }
        });

        blackListListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int indexOfItem = position;
                LayoutInflater li = LayoutInflater.from(BlackListActivity.this);
                View promptsView = li.inflate(R.layout.are_you_sure_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        BlackListActivity.this);

                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int id) {
                                        blackProducts.remove(indexOfItem);
                                        Map<String, Object> map = new HashMap<String, Object>();
                                        map.put("blackList", blackProducts);
                                        myRef.updateChildren(map);
                                    }
                                })

                        .setNegativeButton(getString(R.string.no),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
            }
        });


    }
}
