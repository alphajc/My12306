package com.neuedu.my12306.stationlist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Gavin on 2015/12/30.
 */
public class LetterIndexView extends View {
    String []letter = {"#","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    Paint paint = new Paint();
    int choose = -1;
    OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    public LetterIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LetterIndexView(Context context) {
        super(context);
    }

    public LetterIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int singleHeight = height/letter.length;

        paint.setColor(Color.DKGRAY);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);
        paint.setTextSize(40);

        for (int i=0; i<letter.length; i++) {
            float x = width/2 - paint.measureText(letter[i])/2;
            float y = i*singleHeight+singleHeight;
            if (i == choose){
                paint.setColor(Color.GREEN);
                paint.setFakeBoldText(true);
            }
            canvas.drawText(letter[i],x,y,paint);

            paint.reset();
            paint.setColor(Color.DKGRAY);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(30);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int c = (int) (y/getHeight()*letter.length);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                choose = c;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (c < 0 || c > 26) break;
                if (onTouchingLetterChangedListener != null) {
                    onTouchingLetterChangedListener.onTouchingLetterChanged(letter[c]);
                    choose = -1;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                choose = c;
                invalidate();
                break;
            default:
        }

        return true;
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String choose);
    }
}
