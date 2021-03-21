package com.haumon.pixelthoughts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Vector;


public class MainActivity extends AppCompatActivity {
    static final int WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    static final int HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;

    RelativeLayout background_screen, main_screen, intro_screen, message_screen, info_screen, background_info_screen;

    TextView message, base_on;

    static EditText edit_text;

    Button btn_submit;

    MediaPlayer mediaPlayer;

    static String[] messages = {"Take a deep breath in....", "....and breathe out", "Everything is okay", "Your life is okay",
            "Life is much grander than this thought", "The universe is over 93 billion light-years in distance", "Our galaxy is small",
            "Our sun is tiny", "The earth is minuscule", "Our cities are insignificant....", "....and you are microscopic",
            "This thought.... does not matter", "It can easily disappear", "and life will go on...."};

    int index = 0;

    static long textAppearAndDisappear = 2000;
    static long textStartOffset = 2000;
    static long mainStarDuration = 1000;

    static long lifeOfMessages = (textAppearAndDisappear + textStartOffset)*messages.length;

    static boolean start = false;

    static Vector<String> trouble = new Vector<>();
    static int maxLine = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();

        System.out.println("time---------------------" + lifeOfMessages);
        mediaPlayer = MediaPlayer.create(this, R.raw.background);

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
                }
                else{
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
                mediaPlayer.start();

                message.setPadding(0, HEIGHT / 2 - Background.radius - Background.shadow - (int) message.getTextSize() * 2, 0, 0);
                message.startAnimation(alphaAnimationMessageAppear);

                edit_text.getLayoutParams().width= WIDTH/2;
                edit_text.setX(WIDTH/4f);
                edit_text.setY(HEIGHT - Background.radius - Background.shadow);

                edit_text.startAnimation(alphaAnimationMessageAppear);

                btn_submit.getLayoutParams().width = WIDTH/4;
                btn_submit.setX(3*edit_text.getX()/2);
                btn_submit.setY(edit_text.getY() + 20);


                btn_submit.startAnimation(alphaAnimationMessageAppear);

                main_screen.addView(background);
                main_screen.setAlpha(0f);
                info_screen.setAlpha(0f);
                background_info_screen.setAlpha(0f);
                intro_screen.setAlpha(0.0f);

                main_screen.addView(background_info_screen);
                main_screen.addView(info_screen);

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

            String text = String.valueOf(edit_text.getText()).trim();
            Vector<String> strings = new Vector<>();
            String string = "";
            for(int i = 0; i < text.length(); i++){
                if(text.charAt(i) == ' ' || i + 1 == text.length()){
                    strings.add(string + (i + 1 == text.length() ? text.charAt(i) : ""));
                    string = "";
                }
                else string += text.charAt(i);
            }
            Vector<String> result = new Vector<>();
            String str = "";
            for(int i = 0; i < strings.size(); i++){
                if((str + strings.get(i)).length() < 20){
                    str += strings.get(i) + " ";
                }
                else{
                    result.add(str.trim());
                    str = strings.get(i);
                }
            }
            if(str != "") result.add(str);
            System.out.println("maxx" + Math.min(result.size(), maxLine));
            for(int i=0; i< Math.min(result.size(), maxLine); i++){
                if(i == Math.min(result.size(), maxLine) - 1){
                    if(Math.min(result.size(), maxLine) == maxLine){
                        trouble.add(result.get(i) + "...");
                    }
                }
                else{
                    trouble.add(result.get(i));
                }
            }

            edit_text.animate().alpha(0f).setDuration(2000).setListener(null);
            btn_submit.animate().alpha(0f).setDuration(2000).setListener(null);
            message.startAnimation(alphaAnimationMessageDisappear);
        });

    }

    void AnhXa(){
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
}
