package kim.hsl.android_ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CanvasTranslate extends View {

    public CanvasTranslate(Context context) {
        this(context, null);
    }

    public CanvasTranslate(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasTranslate(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘图用的画笔工具
        Paint paint = new Paint();

        // 要绘制的矩形 , 下面的坐标是 Canvas 绘制坐标系中的坐标
        RectF r = new RectF(0, 0, 666, 666);
        // 设置画笔颜色蓝色
        paint.setColor(Color.BLUE);
        // 绘制矩形
        canvas.drawRect(r, paint);

        // 将当前坐标保存到 状态栈 中
        canvas.save();
        // Canvas 绘图坐标分别在 X, Y 轴正向平移
        canvas.translate(111, 111);
        // 设置当前画笔颜色为红色
        paint.setColor(Color.RED);
        // 在 Canvas 画布平移的基础上再次进行绘制
        canvas.drawRect(r, paint);
        // 与上面的 save 方法对应
        canvas.restore();
    }

}

