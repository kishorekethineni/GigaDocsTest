package com.gigadocs.gigadocstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gigadocs.gigadocstest.Extras.SwipeToDeleteCallback;
import com.gigadocs.gigadocstest.Extras.Utils;
import com.gigadocs.gigadocstest.RoomPersistance.UserDao;
import com.gigadocs.gigadocstest.RoomPersistance.UserRoomDatabase;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ImageView imageView=null;
    private List<User> users=new ArrayList<>();
    UserDao dao;
    UserAdapter adapter;

    private RecyclerView recyclerView;
    private boolean ageFlag=false,dateFlag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        UserRoomDatabase db = Room.databaseBuilder(getApplicationContext(),
                UserRoomDatabase.class, "user_database").allowMainThreadQueries().build();
        dao=db.userDao();
        users.addAll(dao.getAllUsers());
        adapter=new UserAdapter(users);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        findViewById(R.id.filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFilterDialog();
            }
        });

        enableSwipeToDeleteAndUndo();

        EditText searchFilter = findViewById(R.id.search_input);
        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                filter(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.form_dialog_layout, (ViewGroup) findViewById(R.id.layout_root));

                final TextInputEditText name = (TextInputEditText) layout.findViewById(R.id.name);
                final TextInputEditText mobile = (TextInputEditText) layout.findViewById(R.id.mobile);
                final TextInputEditText email = (TextInputEditText) layout.findViewById(R.id.email);
                final TextInputEditText age = (TextInputEditText) layout.findViewById(R.id.age);
                final TextInputEditText gender = layout.findViewById(R.id.gender);
                final ImageView image = layout.findViewById(R.id.imageView);

                imageView=image;

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PackageManager pm = getPackageManager();
                        int hasCamPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
                        int hasStorePerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
                        if (hasCamPerm == PackageManager.PERMISSION_GRANTED && hasStorePerm == PackageManager.PERMISSION_GRANTED) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            builder.setTitle("Choose a source");
                            String[] animals = {"Camera", "Gallery"};
                            builder.setItems(animals, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(takePicture, 1123);
                                            break;
                                        case 1:
                                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            startActivityForResult(pickPhoto, 1124);
                                            break;
                                    }
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else{
                            Toast.makeText(HomeActivity.this, "Permission Required try approve permission manually", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setView(layout);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Utils.isEmpty(new TextInputEditText[]{name,age,mobile,gender},HomeActivity.this)&&Utils.isValidEmail(email,HomeActivity.this)) {
                            String imageBase64 = Utils.imageToBase64(imageView);
                            String date = Utils.getCurrentTimeMillis();
                            User user = new User(name.getText().toString(), mobile.getText().toString(), email.getText().toString(), gender.getText().toString(), Integer.parseInt(age.getText().toString()), imageBase64, date);
                            dao.insert(user);
                            users.add(user);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(HomeActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(HomeActivity.this, "Cancelled By User", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 1123:
                if(resultCode == RESULT_OK){
                    Bitmap bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    if (imageView!=null){
                        imageView.setImageBitmap(bitmap);
                    }
                }
                break;
            case 1124:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    if (imageView!=null)
                        imageView.setImageURI(selectedImage);
                }
                break;
        }
    }
    private void filter(String text) {
        ArrayList<User> newList = new ArrayList<>();
        if (text.length()!=0) {
            for (User item : users) {
                if (item.getName().contains(text)) {
                    Log.i("Contains", text);
                    newList.add(item);
                }
            }
        }else{
            newList.addAll(users);
        }
        adapter.setFilter(newList);
    }


    private void getFilterDialog(){

        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater factory = LayoutInflater.from(HomeActivity.this);
        final View view = factory.inflate(R.layout.filter_alert_dialog, null);
        LinearLayout dateLayout=view.findViewById(R.id.date_sort_layout);
        LinearLayout ageLayout=view.findViewById(R.id.age_sort_layout);
        ImageView dateArrow = view.findViewById(R.id.date_arrow);
        ImageView ageArrow = view.findViewById(R.id.age_arrow);
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFlag= !dateFlag;
                dateArrow.setImageResource(dateFlag?R.drawable.ic_baseline_arrow_downward_24:R.drawable.ic_baseline_arrow_upward_24);
                adapter.sortByDate(dateFlag?1:0);
            }
        });

        ageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ageFlag= !ageFlag;
                ageArrow.setImageResource(ageFlag?R.drawable.ic_baseline_arrow_downward_24:R.drawable.ic_baseline_arrow_upward_24);
                adapter.sortByAge(ageFlag?1:0);
            }
        });
        alert.setView(view);
        alert.show();
    }


    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final User item = adapter.getData().get(position);

                adapter.removeItem(position);
                dao.deleteUser(item);

                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.parent), "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                        dao.insert(item);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}