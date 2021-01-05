package com.example.nerdlauncher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class NerdlauncherActivity    extends SingleFragmentActivity {


    @Override
    protected Fragment createFrag() {
        return nerdLAuncherFragment.newInstance();
    }
}