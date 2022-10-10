package kim.hsl.android_ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PathMeasureView extends View {

    /**
     * 画笔工具
     * 线性渐变渲染 需要设置给该 画笔工具
     */
    private Paint mPaint;

    /**
     * 曲线上的点
     */
    private float[] pos = {0F, 0F};

    /**
     * 曲线上点的切点
     */
    private float[] tan = {0F, 0F};

    /**
     * 前进百分比, 0F ~ 1F
     */
    private float mProgress;

    public PathMeasureView(Context context) {
        this(context, null);
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 初始化 画笔工具, 主要是设置该画笔的渲染
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 每次前进千分之五
        mProgress += 0.005;
        // 到达结束点后, 继续循环运动
        if (mProgress >= 1) mProgress = 0;

        canvas.drawColor(Color.WHITE);
        canvas.translate(getWidth() / 2, getHeight() / 2);

        // 绘制的 Path
        Path path = new Path();

        // 绘制圆形
        path.addCircle(0, 0, 300, Path.Direction.CW);
        canvas.drawPath(path, mPaint);

        // 圆形曲线测量
        PathMeasure pathMeasure = new PathMeasure(path, false);

        // 获取特定点的 坐标 以及 切点
        pathMeasure.getPosTan(pathMeasure.getLength() * mProgress, pos, tan);

        // 在该特定点绘制圆形
        canvas.drawCircle(pos[0], pos[1], 20, mPaint);

        // 触发下一次绘制
        invalidate();
    }

}

