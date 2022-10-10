package kim.hsl.android_ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BesselCurve3 extends View {

    public BesselCurve3(Context context) {
        this(context, null);
    }

    public BesselCurve3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BesselCurve3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 初始化 曲线 和 画笔 实例对象
        Path path = new Path();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);

        // 设置起始点
        path.moveTo(0, getHeight() / 2F);

        // 设置控制点 和 终止点
        path.cubicTo(
                getWidth() / 4F, getHeight(),
                getWidth() * 3F / 4F, 0,
                getWidth(), getHeight() / 2F);

        // 绘制贝塞尔曲线
        canvas.drawPath(path, paint);
    }

    //曲线开始点
    private float mStartX, mStartY;
    //结束点
    private float mEndX, mEndY;
    //控制点
    private float mContorlX = 200, mContorlY = 60;//默认值
    private Paint mPaint,mLinePointPaint;
    private Path mPath;


    // 贝塞尔曲线控制点集合
    private ArrayList<PointF> mControlPoints = new ArrayList<>();

    /**
     * 贝塞尔曲线递归算法, 本方法计算 X 轴坐标值
     * @param i 贝塞尔曲线阶数
     * @param j 贝塞尔曲线控制点
     * @param u 比例 / 时间 , 取值范围 0.0 ~ 1.0
     * @return
     */
    private float BezierX(int i, int j, float u) {
        if (i == 1) {
            // 递归退出条件 : 贝塞尔曲线阶数 降为一阶
            // 一阶贝塞尔曲线点坐标 计算如下 :
            return (1 - u) * mControlPoints.get(j).x + u * mControlPoints.get(j + 1).x;
        }
        return (1 - u) * BezierX(i - 1, j, u) + u * BezierX(i - 1, j + 1, u);
    }

    /**
     * 贝塞尔曲线递归算法, 本方法计算 Y 轴坐标值
     * @param i 贝塞尔曲线阶数
     * @param j 贝塞尔曲线控制点
     * @param u 比例 / 时间 , 取值范围 0.0 ~ 1.0
     * @return
     */
    private float BezierY(int i, int j, float u) {
        if (i == 1) {
            // 递归退出条件 : 贝塞尔曲线阶数 降为一阶
            return (1 - u) * mControlPoints.get(j).y + u * mControlPoints.get(j + 1).y;
        }
        return (1 - u) * BezierY(i - 1, j, u) + u * BezierY(i - 1, j + 1, u);
    }

    /**
     * 构建贝塞尔曲线上的点集
     * @return
     */
    private ArrayList<PointF> collectBezierPoints() {
        // 使用 Path 实例对象存放贝塞尔曲线上的点集
        mPath.reset();

        // 用于存放贝塞尔曲线上点的集合
        ArrayList<PointF> points = new ArrayList<>();

        // 计算阶数 , 点的个数减去一 , 就是阶数 ;
        // 一阶贝塞尔曲线有 2 个点
        // 二阶贝塞尔曲线有 3 个点
        // n - 1 阶贝塞尔曲线有 n 个点
        int order = mControlPoints.size() - 1;

        // 贝塞尔曲线由 1000 个点组成 , 也就是 比例 u 每次增加 0.001
        // 贝塞尔曲线上的点的集合中收集 1000 个点
        float delta = 1.0f / 1000;

        // 每次累加 0.0001
        for (float u = 0; u <= 1; u += delta) {
            // Bezier点集
            PointF pointF = new PointF(BezierX(order, 0, u), BezierY(order, 0, u));
            points.add(pointF);
            if(points.size() == 1){
                mPath.moveTo(points.get(0).x,points.get(0).y);
            }else{
                mPath.lineTo(pointF.x,pointF.y);
            }

        }
        return points;
    }

}

