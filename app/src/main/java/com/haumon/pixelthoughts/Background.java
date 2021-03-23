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
    private final Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);

    static int shadow = 50;

    static int radius = MainActivity.HEIGHT / 4 - shadow;

    static int numOfStars = 150;
    static int tinyMainStar = 3;
    double queueStars = 1;

    long timeStartZoomOut;
    float timeResizeMainStar = 100;
    float zoomOut = 0;
    float zoomOutSpeed = timeResizeMainStar * (radius - tinyMainStar) / MainActivity.timeMainStarDisappear;

    float distance = radius / 4f;
    float distanceSpeed = 0.007f;

    static Vector<Star> stars = new Vector<>();

    Random random = new Random();


    public Background(Context context) {
        super(context);
        paintText.setColor(Color.BLACK);
        paintMainStar.setShadowLayer(shadow, 0, 0, Color.parseColor("#FF8C00"));
        paintText.setTextAlign(Paint.Align.CENTER);
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
        setLayerType(LAYER_TYPE_SOFTWARE, paint);

        float r = radius - zoomOut >= tinyMainStar ? radius - zoomOut : tinyMainStar;

        if (y - zoomOut > -tinyMainStar) canvas.drawCircle(x, y - zoomOut, r, paint);
        else return;
        if (r == tinyMainStar) {
            zoomOut += 3f;
            r -= 2;
        }
//        System.out.println(zoomOutSpeed + " - " + zoomOut + " " + timeResizeMainStar + " " + (radius - tinyMainStar) + "/" +MainActivity.timeMainStarDisappear);
        if (MainActivity.start) {
//            System.out.println(System.currentTimeMillis() + "-" + timeStartZoomOut + "=" + (System.currentTimeMillis() - timeStartZoomOut) + " = " + timeResizeMainStar);
            if(System.currentTimeMillis() - timeStartZoomOut >= timeResizeMainStar){
                System.out.println(zoomOut + " - " + radius);
                zoomOut += zoomOutSpeed;
                timeStartZoomOut = System.currentTimeMillis();
            }
            distance -= distanceSpeed;
            paintText.setTextSize(r / 6);
            for (int i = 0; i < MainActivity.trouble.size(); i++) {
                canvas.drawText(MainActivity.trouble.get(i), x, y - zoomOut - MainActivity.trouble.size() * distance / 2 + distance * i, paintText);
            }
        }
    }

    private void addStars() {
        int size;
        while (stars.size() < queueStars) {
            size = random.nextInt(3) + 2;
            stars.addElement(new Star(random.nextInt(MainActivity.WIDTH), MainActivity.HEIGHT, size, size - 1));
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

