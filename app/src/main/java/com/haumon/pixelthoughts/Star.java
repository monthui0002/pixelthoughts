package com.haumon.pixelthoughts;

import android.graphics.Canvas;
import android.graphics.Paint;

class Star {
    float x, y, size, speed;

    Star(float x, float y, float size, float speed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
    }

    void move() {
        this.y -= this.speed;
    }

    void update(Canvas canvas, Paint paint) {
        move();
        canvas.drawRect(x, y, x + size, y + size, paint);
        if (y < 0) Background.stars.removeElement(this);
    }
}
