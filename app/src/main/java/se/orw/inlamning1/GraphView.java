package se.orw.inlamning1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Marcus on 2014-09-27.
 */
public class GraphView extends View {
    private ArrayList<Float> values;
    private RectF rectf = new RectF(10, 10, 300, 300); // the size of the rectancle for the canvas.

    public GraphView(Context context, ArrayList<Float> values) {
        super(context);
        this.values = values;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int[] COLORS = {Color.RED, Color.CYAN};
        float startAngle = 0;

        for(int i = 0; i < values.size(); i++) {
            paint.setColor(COLORS[i]);
            if(i != 0) {
                startAngle += values.get(i - 1);
            }
            canvas.drawArc(rectf, startAngle, values.get(i), true, paint);
        }
    }
}