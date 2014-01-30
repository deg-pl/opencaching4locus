package org.bogus.android;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.bogus.geocaching.egpx.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

public class AndroidUtils
{
    public static void hideSoftKeyboard(Window window)
    {
        if (window == null){
            return ;
        }
        final InputMethodManager inputManager = (InputMethodManager)
                window.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        final View currentlyFocused = window.getCurrentFocus();
        if (currentlyFocused != null){
            inputManager.hideSoftInputFromWindow(currentlyFocused.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    public static void hideSoftKeyboard(Activity activity)
    {
        if (activity != null && activity.getWindow() != null){
            hideSoftKeyboard(activity.getWindow());
        }
    }

    public static void hideSoftKeyboard(Dialog dialog)
    {
        if (dialog != null && dialog.getWindow() != null){
            hideSoftKeyboard(dialog.getWindow());
        }
    }

    public static String toString(Object obj)
    {
        if (obj == null){
            return null;
        } else {
            return obj.toString();
        }
    }
    
    /**
     * Sets the specified image buttonto the given state, while modifying or
     * "graying-out" the icon as well
     * 
     * @param enabled The state of the menu item
     * @param item The image button to modify
     */
    public static void setImageButtonEnabled(boolean enabled, ImageButton item) {
        if (item.isEnabled() == enabled){
            return ;
        }
        Drawable originalImg = (Drawable)item.getTag(R.id.imageButtonOriginalImage);
        if (originalImg == null){
            originalImg = item.getDrawable();
            item.setTag(R.id.imageButtonOriginalImage, originalImg);
        }
        if (enabled){
            item.setImageDrawable(originalImg);
        } else {
            Drawable grayed = (Drawable)item.getTag(R.id.imageButtonGrayedImage);
            if (grayed == null){
                grayed = getGrayscaled(originalImg);
                item.setTag(R.id.imageButtonGrayedImage, grayed);
            }
            item.setImageDrawable(grayed);
        }
        item.setEnabled(enabled);
        //item.setClickable(enabled);
    }

    public static boolean setViewVisible(boolean visible, View view) {
        int vis = view.getVisibility();
        boolean currentlyVisible = vis == View.VISIBLE || vis == View.INVISIBLE;
        if (visible != currentlyVisible){
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
            return true;
        } else {
            return false;
        }
    }

    public static Drawable getGrayscaled(Drawable src) {
        Drawable res = src.mutate();
        res.setColorFilter(Color.GRAY, Mode.SRC_IN);
        return res;
    } 
    
    public static String formatFileSize(final int sizeKB)
        {
        if (sizeKB <= 1024){
            return sizeKB + " kB"; 
        } else {
            final NumberFormat nf = new DecimalFormat("####0.##");
            return nf.format(sizeKB/1024.0) + " MB";
        }
    }
}
