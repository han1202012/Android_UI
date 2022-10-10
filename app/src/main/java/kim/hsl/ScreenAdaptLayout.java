package kim.hsl;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.lang.reflect.Field;

/**
 * 屏幕适配解决方案
 */
public class ScreenAdaptLayout extends ConstraintLayout {

    /**
     * 自定义布局中的子组件坐标数据是否需要进行换算
     */
    static boolean isCaculateFlag = true;
    /**
     * 屏幕适配参考宽度 / 设计稿宽度
     */
    public final float REFERENCE_WIDTH = 720;
    /**
     * 屏幕适配参考高度 / 设计稿高度
     * 这里注意标准高度是 1280, 去掉了状态栏后为 1232
     */
    public final float REFERENCE_HEIGHT = 1232;
    /**
     * 设备布局实际宽度
     */
    public float screenWidth;
    /**
     * 设备布局实际高度
     */
    public float screenHeight;
    /**
     * 上下文对象
     */
    public Context context;

    public ScreenAdaptLayout(Context context) {
        super(context);
        this.context = context;
    }

    public ScreenAdaptLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ScreenAdaptLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 初始化设备屏幕数据
        initMetrics();
        // isCaculateFlag 标志位, 保证该操作只执行一次
        if(isCaculateFlag){
            // 获取该 ViewGroup 子组件个数
            int childCount = this.getChildCount();
            // 获取 X 轴 设计稿 与 屏幕实际布局 缩放系数
            float xScale =  getXScaleValue();
            // 获取 Y 轴 设计稿 与 屏幕实际布局 缩放系数
            float yScale =  getYScaleValue();
            // 逐个遍历子组件
            for (int i = 0;i < childCount;i++){
                // 获取子组件
                View child = this.getChildAt(i);
                // 获取子组件的布局属性
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

                // 对子组件的 宽度 , 高度 , 左上右下的边距 进行换算
                layoutParams.width = (int) (layoutParams.width * xScale);
                layoutParams.height = (int) (layoutParams.height * yScale);

                layoutParams.leftMargin = (int) (layoutParams.leftMargin * xScale);
                layoutParams.topMargin = (int) (layoutParams.topMargin * yScale);
                layoutParams.rightMargin = (int) (layoutParams.rightMargin * xScale);
                layoutParams.bottomMargin = (int) (layoutParams.bottomMargin * yScale);
            }
            isCaculateFlag = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void initMetrics(){
        // 获取当前设备的屏幕信息
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        if(screenWidth == 0.0f || screenHeight == 0.0f){
            //获取状态框信息
            int statusBarHeight = getDimenValue(context,48);

            // 在屏幕真实宽高上减去状态栏高度
            if(displayMetrics.widthPixels > displayMetrics.heightPixels){
                // 宽度大于高度说明是横屏状态, 状态栏在左侧或者右侧
                this.screenWidth = displayMetrics.heightPixels;
                this.screenHeight = displayMetrics.widthPixels - statusBarHeight;
            }else{
                // 高度大于宽度说明是竖屏状态, 状态栏在上侧
                this.screenWidth = displayMetrics.widthPixels;
                this.screenHeight = displayMetrics.heightPixels - statusBarHeight;
            }
        }
    }

    /**
     * 获取设备实际宽度与参考宽度的比值
     * @return
     */
    public float getXScaleValue(){
        return screenWidth / REFERENCE_WIDTH;
    }

    /**
     * 获取沙设备实际高度与参考高度的比值
     * @return
     */
    public float getYScaleValue(){
        return screenHeight / REFERENCE_HEIGHT;
    }

    /**
     * 通过反射 com.android.internal.R$dimen 类, 获取其中的某些字段
     * @param context   上下文对象
     * @param defValue  如果没有成功获取指定字段, 这里返回一个默认值
     * @return
     */
    public int getDimenValue(Context context, int defValue) {
        try {
            // 反射 com.android.internal.R$dimen 类
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            // 创建 com.android.internal.R$dimen 实例对象
            Object instance = clazz.newInstance();
            // 获取指定的字段, 这里用于获取 system_bar_height, 也就是系统状态栏高度
            Field field = clazz.getField("system_bar_height");
            // 获取字段的值
            int fieldValue = (int) field.get(instance);
            // 获取的字段值是资源 ID, 需要转为实际的像素值
            return context.getResources().getDimensionPixelOffset(fieldValue);
        } catch (Exception e) {
            // 如果执行出现异常, 则返回默认值
            return defValue;
        }
    }
}