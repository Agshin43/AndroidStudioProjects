package com.apps.akaya.picnest.library;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * Author: Artemiy Garin
 * Date: 21.04.14
 */
@SuppressWarnings("unused")
public class QuickAction implements PopupWindow.OnDismissListener {

    private static final String PARAM_STATUS_BAR_HEIGHT = "status_bar_height";
    private static final String PARAM_DIMEN = "dimen";
    private static final String PARAM_ANDROID = "android";

    private Context context;
    private int screenWidth;
    private int screenHeight;

    private OnDismissListener onDismissListener;
    private PopupWindow popupWindow;
    private WindowManager windowManager;
    private RelativeLayout rootLayout;

    public QuickAction(Context context, int animationStyle, RelativeLayout rootLayout) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.context = context;
        this.rootLayout = rootLayout;

        initScreen();
        initPopupWindow(animationStyle);
    }

    @SuppressWarnings("deprecation")
    private void initScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
        } else {
            screenWidth = windowManager.getDefaultDisplay().getWidth();
            screenHeight = windowManager.getDefaultDisplay().getHeight();
        }
    }

    private void initPopupWindow(int animationStyle) {
        popupWindow = new PopupWindow(context);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(rootLayout);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setAnimationStyle(animationStyle);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                } else return false;
            }
        });
    }

    @SuppressWarnings("UnusedDeclaration")
    public void dismiss() {
        popupWindow.dismiss();
    }

    public void show(View anchor, int x, int y) {
        try {
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);

            Rect anchorRect = new Rect(location[0], location[1],
                    location[0] + anchor.getWidth(), location[1] + anchor.getHeight());

            if (rootLayout.getLayoutParams() == null) rootLayout.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rootLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            int rootHeight = rootLayout.getMeasuredHeight();
            int rootWidth = rootLayout.getMeasuredWidth();
            int offsetTop = anchorRect.top;
            int offsetBottom = screenHeight - anchorRect.bottom;
            boolean onTop = offsetTop > offsetBottom;

//            int x = calculateHorizontalPosition(anchor, anchorRect, rootWidth, screenWidth);
//            int y = calculateVerticalPosition(anchorRect, rootHeight, onTop, offsetTop);

            popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, x, y);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("","Couldn't show quick action");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private int calculateVerticalPosition(Rect anchorRect, int rootHeight, boolean onTop, int offsetTop) {
        int y;

        if (onTop) {
            if (rootHeight > offsetTop) y = getStatusBarHeight();
            else y = anchorRect.top - rootHeight;
        } else y = anchorRect.bottom;

        return y;
    }

    private int calculateHorizontalPosition(View anchor, Rect anchorRect, int rootWidth, int screenWidth) {
        int x;

        if ((anchorRect.left + rootWidth) > screenWidth) {
            x = anchorRect.left - (rootWidth - anchor.getWidth());
            if (x < 0) x = 0;
        } else {
            if (anchor.getWidth() > rootWidth) x = anchorRect.centerX() - (rootWidth / 2);
            else x = anchorRect.left;
        }

        return x;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(PARAM_STATUS_BAR_HEIGHT, PARAM_DIMEN, PARAM_ANDROID);
        if (resourceId > 0) result = context.getResources().getDimensionPixelSize(resourceId);

        return result;
    }

    @Override
    public void onDismiss() {
        if (onDismissListener != null) onDismissListener.onDismiss();
    }

    /**
     * Listeners
     */
    @SuppressWarnings("unused")
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void setMaxHeightResource(int heightResource) {
        int maxHeight = context.getResources().getDimensionPixelSize(heightResource);
        popupWindow.setHeight(maxHeight);
    }

}