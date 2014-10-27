package se.orw.inlamning1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Marcus on 2014-10-22.
 */
public class LoginButton extends Button {
    private String label = "";
    private Bitmap image;

    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        int imageID = 0;
        int labelID = 0;

        for(int i = 0; i < attrs.getAttributeCount(); i++) {
            if(attrs.getAttributeName(i).equals("label")) {
                labelID = attrs.getAttributeResourceValue(i, 0);
            } else if(attrs.getAttributeName(i).equals("imageID")) {
                imageID = attrs.getAttributeResourceValue(i, 0);
            }
        }
        if (imageID == 0) {
            imageID = R.drawable.blank;
        }
        if(labelID != 0) {
            label = getResources().getString(labelID);
        }
        image = BitmapFactory.decodeResource(context.getResources(), imageID);
    }

    /**
     * Render the view (button)
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint text = new Paint();
        text.setColor(Color.BLACK);
        canvas.drawBitmap(image, 0, 0, null);
        text.setTextSize(32);
        canvas.drawText(label, 100, 60, text);
    }
}
