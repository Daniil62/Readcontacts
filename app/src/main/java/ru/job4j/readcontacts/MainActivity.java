package ru.job4j.readcontacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private EditText et;
    private String string = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        RecyclerView recycler = findViewById(R.id.phones);
        et = findViewById(R.id.editText);
        string = et.getText().toString();
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        List<String> phones = new ArrayList<>();
        adapter = new PhoneAdapter(phones);
        recycler.setAdapter(adapter);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                string = et.getText().toString();
                phones.clear();
                loadDic(phones);
            }
        });
        if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .READ_CONTACTS}, 0);
        }
        loadDic(phones);
    }
    private void loadDic(List<String> phones) {
        try (Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                        + " like '%" + string + "%'",
                null, null)) {
            assert cursor != null;
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract
                        .CommonDataKinds.Phone.DISPLAY_NAME));
                String phone = cursor.getString(cursor.getColumnIndex(ContactsContract
                        .CommonDataKinds.Phone.NUMBER));
                phones.add(name + " " + phone);
            }
            adapter.notifyDataSetChanged();
        }
    }
    public static final class PhoneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final List<String> phones;
        PhoneAdapter(List<String> phones) {
            this.phones = phones;
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.phone, parent, false)
            ) {};
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            TextView text = holder.itemView.findViewById(R.id.name);
            text.setText(phones.get(position));
        }
        @Override
        public int getItemCount() {
            return phones.size();
        }
    }
}
