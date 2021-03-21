package com.haumon.pixelthoughts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Random;
import java.util.Vector;

class Background extends View {
    private final Paint paintMainStar = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintStars = new Paint(Paint.ANTI_ALIAS_FLAG);

    static int shadow = 50;
    static int radius = MainActivity.HEIGHT / 4 - shadow; //  height/4 = r + s <> width / 3
    float zoomOut = 0.0f;

    static Vector<Star> stars = new Vector<>();

    static int numOfStars = 150;
    static int tinyMainStar = 3;
    double queueStars = 1;

    Random random = new Random();

    float distance = 40;

    public Background(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paintMainStar.setColor(Color.WHITE);
        paintStars.setColor(Color.WHITE);

        drawStars(canvas, paintStars);

        drawMainStar(MainActivity.WIDTH / 2f, MainActivity.HEIGHT / 2f, canvas, paintMainStar);
    }

    private void drawMainStar(float x, float y, Canvas canvas, Paint paint) {
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(shadow, 0, 0, Color.parseColor("#FF8C00"));
        setLayerType(LAYER_TYPE_SOFTWARE, paint);

        float r = radius - zoomOut >= tinyMainStar ? radius - zoomOut : tinyMainStar;

        if (y - zoomOut > -tinyMainStar) canvas.drawCircle(x, y - zoomOut, r, paint); else return;
        if (r == tinyMainStar){
            zoomOut += 3f;
            r -= 2;
        }
        if (MainActivity.start){
            zoomOut += 0.05f;
            distance -= 0.01f;
            paint.setColor(Color.BLACK);
            paint.setTextSize(r/6);
            paint.setTextAlign(Paint.Align.CENTER);
            for(int i = 0; i < MainActivity.trouble.size(); i++){
                canvas.drawText(MainActivity.trouble.get(i), x, y - MainActivity.trouble.size()*distance/2 + distance*i - zoomOut, paint);
            }
        }
    }

    private void addStars() {
        int speed;
        while (stars.size() < queueStars) {
            speed = random.nextInt(3) + 2;
            stars.addElement(new Star(random.nextInt(MainActivity.WIDTH), MainActivity.HEIGHT, speed, speed - 1));
        }
        queueStars += queueStars < numOfStars ? 0.5 : 0;
    }

    private void drawStars(Canvas canvas, Paint paint) {
        addStars();
        for (int i = 0; i < stars.size(); i++) {
            stars.elementAt(i).update(canvas, paint);
        }
    }
}

