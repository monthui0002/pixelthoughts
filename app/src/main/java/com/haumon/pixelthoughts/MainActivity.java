package com.haumon.pixelthoughts;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Vector;

public class MainActivity extends AppCompatActivity implements OnKeyboardVisibilityListener {
    static final int WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    static final int HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;

    RelativeLayout background_screen, main_screen, intro_screen, message_screen, info_screen, background_info_screen;

    TextView message, base_on;

    @SuppressLint("StaticFieldLeak")
    static EditText edit_text;

    Button btn_submit;

    MediaPlayer mediaPlayer;
    static int timeMedia = 0;

    static String[] messages = {"Take a deep breath in....", "....and breathe out", "Everything is okay", "Your life is okay",
            "Life is much grander than this thought", "The universe is over 93 billion light-years in distance", "Our galaxy is small",
            "Our sun is tiny", "The earth is minuscule", "Our cities are insignificant....", "....and you are microscopic",
            "This thought.... does not matter", "It can easily disappear", "and life will go on...."};

    int index = 0;

    static final long textAppearAndDisappear = 2000;
    static final long textStartOffset = 3000;
    static final long mainStarDuration = 1000;
    static final long timeMainStarDisappear = (messages.length - 1) * (textAppearAndDisappear * 2 + textStartOffset) - 2 * textAppearAndDisappear; //89s

    static boolean start = false;

    static Vector<String> trouble = new Vector<>();
    static int maxLine = 4;

    static int keyboardHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getId();

        mediaPlayer = MediaPlayer.create(this, R.raw.background);
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);

        final Background background = new Background(this);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        AlphaAnimation alphaAnimationMessageAppear = new AlphaAnimation(0.0f, 1.0f);
        AlphaAnimation alphaAnimationMessageDisappear = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(mainStarDuration);
        alphaAnimation.setStartOffset(textStartOffset);

        alphaAnimationMessageAppear.setDuration(textAppearAndDisappear);
        alphaAnimationMessageAppear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                message.setAlpha(1f);
                if (start) message.startAnimation(alphaAnimationMessageDisappear);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        alphaAnimationMessageDisappear.setDuration(textAppearAndDisappear);
        alphaAnimationMessageDisappear.setStartOffset(textStartOffset);
        alphaAnimationMessageDisappear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (index < messages.length) {
                    message.setText(messages[index++]);
                    message.startAnimation(alphaAnimationMessageAppear);
                } else {
                    message.setAlpha(0f);
                    main_screen.removeView(message_screen);

                    base_on.setMovementMethod(LinkMovementMethod.getInstance());
                    info_screen.animate().alpha(1f).setDuration(3000).setListener(null);
                    background_info_screen.animate().alpha(0.5f).setDuration(3000).setListener(null);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                message.setPadding(0, HEIGHT / 2 - Background.radius - Background.shadow - (int) message.getTextSize() * 2, 0, 0);
                message.startAnimation(alphaAnimationMessageAppear);


                edit_text.getLayoutParams().width = WIDTH / 2;
                edit_text.setX(WIDTH / 4f);
                edit_text.setY(HEIGHT - Background.radius - Background.shadow);

                edit_text.startAnimation(alphaAnimationMessageAppear);


                btn_submit.getLayoutParams().width = WIDTH / 4;
                btn_submit.setX(3 * edit_text.getX() / 2);
                btn_submit.setY(edit_text.getY() + 20);

                btn_submit.startAnimation(alphaAnimationMessageAppear);


                main_screen.setAlpha(0f);
                info_screen.setAlpha(0f);
                background_info_screen.setAlpha(0f);

                main_screen.addView(background);
                main_screen.addView(background_info_screen);
                main_screen.addView(info_screen);

                background_screen.removeView(intro_screen);
                background_screen.addView(main_screen);
                background_screen.addView(message_screen);

                main_screen.animate().alpha(1f).setDuration(3000).setListener(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        background_screen.removeAllViews();
        background_screen.addView(intro_screen);

        intro_screen.startAnimation(alphaAnimation);

        btn_submit.setOnClickListener(v -> {
            start = true;
            background.timeStartZoomOut = System.currentTimeMillis();
            getEditText();

            edit_text.animate().alpha(0f).setDuration(2000).setListener(null);
            btn_submit.animate().alpha(0f).setDuration(2000).setListener(null);
            message.startAnimation(alphaAnimationMessageDisappear);
        });
        setKeyboardVisibilityListener(this::onVisibilityChanged);
        setupUI(background_screen);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        timeMedia = mediaPlayer.getCurrentPosition();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.seekTo(timeMedia);
        mediaPlayer.start();
    }


    protected void getId() {
        intro_screen = findViewById(R.id.intro_screen);
        background_screen = findViewById(R.id.background_screen);
        main_screen = findViewById(R.id.main_screen);
        message_screen = findViewById(R.id.message_screen);
        background_info_screen = findViewById(R.id.background_info_screen);
        info_screen = findViewById(R.id.info_screen);

        base_on = findViewById(R.id.base_on);
        message = findViewById(R.id.message);
        edit_text = findViewById(R.id.edit_text);
        btn_submit = findViewById(R.id.btn_submit);
    }


    protected void getEditText() {
        String text = String.valueOf(edit_text.getText()).trim();
        Vector<String> strings = new Vector<>();
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ' || i + 1 == text.length()) {
                strings.add(string.toString() + (i + 1 == text.length() ? text.charAt(i) : ""));
                string = new StringBuilder();
            } else string.append(text.charAt(i));
        }

        Vector<String> result = new Vector<>();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            if ((str + strings.get(i)).length() < 20) {
                str.append(strings.get(i)).append(" ");
            } else {
                result.add(str.toString().trim());
                str = new StringBuilder(strings.get(i));
            }
        }

        if (!str.toString().equals("")) result.add(str.toString());
        for (int i = 0; i < Math.min(result.size(), maxLine); i++) {
            if (i == Math.min(result.size(), maxLine) - 1) {
                if (Math.min(result.size(), maxLine) == maxLine) {
                    trouble.add(result.get(i) + "...");
                    break;
                }
            }
            trouble.add(result.get(i));
        }
    }


    @Override
    public void onVisibilityChanged(boolean visible) {
        if (visible) {
            edit_text.setX(WIDTH / 4f);
            edit_text.setY(keyboardHeight);
        } else {
            edit_text.setX(WIDTH / 4f);
            edit_text.setY(HEIGHT - Background.radius - Background.shadow);
        }
    }

    public interface OnKeyboardVisibilityListener {
        void onVisibilityChanged(boolean visible);
    }

    private void setKeyboardVisibilityListener(final OnKeyboardVisibilityListener onKeyboardVisibilityListener) {
        final View parentView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private boolean alreadyOpen;
            private final int defaultKeyboardHeightDP = 100;
            private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
            private final Rect rect = new Rect();

            @Override
            public void onGlobalLayout() {
                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, parentView.getResources().getDisplayMetrics());
                parentView.getWindowVisibleDisplayFrame(rect);
                keyboardHeight = parentView.getRootView().getHeight() - (rect.bottom - rect.top);
                boolean isShown = keyboardHeight >= estimatedKeyboardHeight;

                if (isShown == alreadyOpen) {
                    Log.i("Keyboard state", "Ignoring global layout change...");
                    return;
                }
                alreadyOpen = isShown;
                onKeyboardVisibilityListener.onVisibilityChanged(isShown);
            }
        });
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setupUI(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideSoftKeyboard(MainActivity.this);
                return false;
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}
