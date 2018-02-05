package com.system.cutline.Utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {

    public static void hideSoftKeyboard(AppCompatActivity activity) {
        View focusedView = activity.getCurrentFocus();
        if(focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    private KeyboardUtil() {}
}