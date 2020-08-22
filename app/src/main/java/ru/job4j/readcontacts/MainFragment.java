package ru.job4j.readcontacts;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainFragment extends Fragment {
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private EditText et;
    private String string = "";
    static MainFragment of(int value) {
        MainFragment main = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.MAIN_FOR, value);
        main.setArguments(bundle);
        return main;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        RecyclerView recycler = view.findViewById(R.id.phones);
        et = view.findViewById(R.id.editText);
        string = et.getText().toString();
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        loadDic(phones);
        return view;
    }
    private void loadDic(List<String> phones) {
        try (Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(
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
