package kim.hsl.android_ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class PathMeasureView2 extends View {

    /**
     * 左眼距离左边的距离（控件宽度＊EYE_PERCENT_W），
     * 右眼距离右边的距离（控件宽度＊EYE_PERCENT_W）
     */
    private static final float EYE_PERCENT_W = 0.35F;
    /**
     *眼睛距离top的距离（控件的高度＊EYE_PERCENT_H）
     */
    private static final float EYE_PERCENT_H = 0.38F;

    /**
     * 嘴巴左边跟右边距离top的距离（控件的高度＊MOUCH_PERCENT_H）
     */
    private static final float MOUCH_PERCENT_H = 0.55F;
    /**
     * 嘴中间距离顶部的距离, 控件的高度 ＊ MOUCH_PERCENT_H2
     */
    private static final float MOUCH_PERCENT_H2 = 0.7F;
    /**
     * 嘴巴左边跟右边距离边缘的位置（控件宽度＊MOUCH_PERCENT_W）
     */
    private static final float MOUCH_PERCENT_W = 0.23F;

    /**
     * 眼镜嘴巴上下摆动的范围
     */
    private static final float DURATION_AREA = 0.15F;

    /**
     * 动画持续时长
     */
    private static final int DURATION = 500;



    /**
     * 进度条动画
     */
    Animation mAnimation2 = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            setProgress(interpolatedTime);
            postInvalidate();
        }
    };



    private Paint reachedPaint;
    private Paint unreachedPaint;
    private Path reachedPath;
    private Path unreachedPath;
    private Path mouthPath=new Path();

    private float mProgress = 0.1f;
    private float lineWidth = dp2px(2);

    private float mRadius;

    private float mMouchH = MOUCH_PERCENT_H;

    private float mMouchH2 = MOUCH_PERCENT_H2;

    private float mEyesH = EYE_PERCENT_H;

    /**
     * 动画启动标志位
     */
    private boolean isStart=true;

    /**
     * 脸部摆动动画
     */
    Animation mAnimation = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            // 计算上下移动的幅度
            float offset = interpolatedTime * DURATION_AREA;
            // 计算左眼高度
            mMouchH = MOUCH_PERCENT_H + offset;
            // 计算右眼高度
            mMouchH2 = MOUCH_PERCENT_H2 + offset;
            // 计算嘴巴高度
            mEyesH=EYE_PERCENT_H + offset;

            // 设置进度条进度
            mProgress = interpolatedTime;
            // 更新 UI
            postInvalidate();
        }
    };

    public PathMeasureView2(Context context) {
        this(context, null);
    }

    public PathMeasureView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathMeasureView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 开启进度条动画
     */
    private void startAnimation() {
        mAnimation.setDuration(DURATION);
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        startAnimation(mAnimation);
    }

    private void initView() {
        reachedPaint=new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        reachedPaint.setStyle(Paint.Style.STROKE);
        reachedPaint.setStrokeWidth(lineWidth);
        reachedPaint.setColor(Color.WHITE);
        reachedPaint.setStrokeJoin(Paint.Join.ROUND);
        reachedPaint.setStrokeCap(Paint.Cap.ROUND);

        unreachedPaint=new Paint(reachedPaint);
        unreachedPaint.setColor(Color.GRAY);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(isStart){
            startAnimation();
            isStart=false;
        }
        mRadius = getWidth()/7F/2;
        if(unreachedPath == null){
            unreachedPath = new Path();
        }
        unreachedPath.addRoundRect(new RectF(lineWidth,lineWidth,w-lineWidth,h-lineWidth),w/6,w/6, Path.Direction.CCW);

        if(reachedPath==null){
            reachedPath=new Path();
        }
        reachedPath.addRoundRect(new RectF(lineWidth,lineWidth,w-lineWidth,h-lineWidth),w/6,w/6, Path.Direction.CW);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        canvas.save();

        drawFace(canvas);
        drawReachedRect(canvas);

        canvas.restore();
    }

    /**
     * draw face
     */
    private void drawFace(Canvas canvas) {
        unreachedPaint.setStyle(Paint.Style.FILL);
        //画左边的眼睛
        canvas.drawCircle(getWidth()*EYE_PERCENT_W,getHeight()*mEyesH-mRadius,mRadius,unreachedPaint);
        //画右边的眼睛
        canvas.drawCircle(getWidth()*(1-EYE_PERCENT_W),getHeight()*mEyesH-mRadius,mRadius,unreachedPaint);
        mouthPath.reset();
        //画嘴巴
        mouthPath.moveTo(getWidth()*MOUCH_PERCENT_W,getHeight()*mMouchH);
        mouthPath.quadTo(getWidth()/2,getHeight()*mMouchH2,getWidth()*(1-MOUCH_PERCENT_W),getHeight()*mMouchH);
        unreachedPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mouthPath,unreachedPaint);
    }

    private void drawReachedRect(Canvas canvas) {
        unreachedPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(unreachedPath,unreachedPaint);

        PathMeasure measure=new PathMeasure(reachedPath,false);
        float length = measure.getLength();
        //获取当前path长度
        float currLength=length * mProgress;
        Path path = new Path();

        /**
         * 因为uc的起始位置是在顶部的位置,而我们的path的起始位置是在左下的位置，
         * 所以我们需要加上一个length*1/3f偏移量
         */
        measure.getSegment(length*1/3f,currLength+length*1/3f,path,true);

        canvas.drawPath(path, reachedPaint);
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

    public void setProgress(float progress){
        Log.d("TAG",""+progress);
        /*if(progress < mProgress){
            return;
        }*/

        // 设置当前进度条进度
        this.mProgress = progress;
        postInvalidate();
    }

    public void release() {
        mAnimation.cancel();
        mAnimation2.cancel();
        clearAnimation();
        setVisibility(View.GONE);
    }

}

