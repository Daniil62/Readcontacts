package ru.job4j.readcontacts;

import androidx.fragment.app.Fragment;

public class MainActivator extends MainActivity {
    @Override
    public Fragment loadFragment() {
        return MainFragment.of(getIntent().getIntExtra(MainActivity.MAIN_FOR, 0));
    }
}
