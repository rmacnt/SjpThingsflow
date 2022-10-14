package com.sjp.framework.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;

public interface FragmentCycle {

    void onCreate(Bundle savedInstanceState);

    void onViewCreated(View view, @Nullable Bundle savedInstanceState);

    void onSaveInstanceState(Bundle outState);

    void onViewStateRestored(@Nullable Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onDestroyView();

    void onDestroy();

    void setUserVisibleHint(boolean isVisibleToUser);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    boolean onOptionsItemSelected(MenuItem item);

    void onPrepareOptionsMenu(Menu menu);
}
