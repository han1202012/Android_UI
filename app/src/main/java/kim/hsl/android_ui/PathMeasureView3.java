package kim.hsl.android_ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

public class PathMeasureView3 extends View {

    public PathMeasureView3(Context context) {
        this(context, null);
    }

    public PathMeasureView3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathMeasureView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.getSaveCount();

        // 触发下一次绘制
        invalidate();
    }

    /**
     * 将 dp 屏幕像素 值转为 px 真实像素值, 目的是使用 dp 为单位在手机中显示相同的效果
     * @param dp
     * @return
     */
    public float dp2px(float dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }

}

