package com.smart.zxingextend;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.smart.zxingextend.util.PhoneHelper;
import com.smart.zxingextend.util.ZXingUtils;

import static android.graphics.drawable.GradientDrawable.RECTANGLE;

/**
 * 本类的主要功能是 :  长按二维码结识别,结果点View的展示
 * 使用方法:
 * 1,和所需要识别的区域位置保持一致
 * 2,触发{@link #decodeQrcode(View)}
 *
 * @author jiangzhengyan  2024/3/29 10:24
 */
public class QrImageResultPointView extends FrameLayout {
    private static final String TAG = "QrImageResultPointView";

    private Result[] resultArr;
    private OnResultPointClickListener onResultPointClickListener;


    private int resultPointColor;//结果点的颜色
    private int resultPointStrokeColor;//结果点的线颜色
    private int resultPointWH;//结果点的宽高
    private int resultPointRadiusCorners;//结果点的圆角大小
    private int resultPointStrokeWidth;//结果点的线宽
    private TextView tv_cancle;
    private FrameLayout fl_container;


    public QrImageResultPointView(Context context) {
        this(context, null);
    }

    public QrImageResultPointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QrImageResultPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QrImageResultPointView, defStyleAttr, 0);

        //结果点的颜色
        resultPointColor = typedArray.getColor(R.styleable.QrImageResultPointView_pv_point_color, Color.parseColor("#CC22CE6A"));
        //结果点的线颜色
        resultPointStrokeColor = typedArray.getColor(R.styleable.QrImageResultPointView_pv_point_stroke_color, Color.parseColor("#FFFFFF"));
        //结果点的宽高
        resultPointWH = (int) typedArray.getDimension(R.styleable.QrImageResultPointView_pv_point_w_h, PhoneHelper.dip2px(getContext(), 40));
        //结果点的圆角大小
        resultPointRadiusCorners = (int) typedArray.getDimension(R.styleable.QrImageResultPointView_pv_point_radius_corner, PhoneHelper.dip2px(getContext(), 12));
        //结果点的线宽
        resultPointStrokeWidth = (int) typedArray.getDimension(R.styleable.QrImageResultPointView_pv_point_stroke_width, PhoneHelper.dip2px(getContext(), 4));

        typedArray.recycle();

        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.qr_image_result_point_view, null);
        tv_cancle = view.findViewById(R.id.tv_cancle);
        fl_container = view.findViewById(R.id.fl_container);

        tv_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onResultPointClickListener != null) {
                    onResultPointClickListener.onCancle();
                }
                cancel();
            }
        });

    }

    /**
     * 设置点位的参数配置
     *
     * @param resultPointWH            结果点的宽高
     * @param resultPointStrokeWidth   结果点的线宽
     * @param resultPointRadiusCorners 结果点的圆角大小
     * @param resultPointStrokeColor   结果点的线颜色
     * @param resultPointColor         结果点的颜色
     */
    public void setResultPointParam(int resultPointWH,
                                    int resultPointStrokeWidth,
                                    int resultPointRadiusCorners,
                                    String resultPointStrokeColor,
                                    String resultPointColor) {
        this.resultPointWH = resultPointWH;
        this.resultPointStrokeWidth = resultPointStrokeWidth;
        this.resultPointRadiusCorners = resultPointRadiusCorners;
        this.resultPointStrokeColor = Color.parseColor(resultPointStrokeColor);
        this.resultPointColor = Color.parseColor(resultPointColor);

    }


    /**
     * 解码,识别二维码
     *
     * @param viewCode 需要识别的区域
     */
    public void decodeQrcode(View viewCode) {
        if (viewCode == null) {
            cancel();
            return;
        }

        Rect crop = new Rect(0, 0, viewCode.getMeasuredWidth(), viewCode.getMeasuredHeight());
        this.resultArr = ZXingUtils.syncDecodeMultiQRCode(getDownscaledBitmapForView(viewCode, crop, 1));
        drawResultPoint();
    }

    /**
     * 获取View的裁剪区
     *
     * @param view            需要处理的View
     * @param crop            裁剪区域
     * @param downscaleFactor 缩放参数 1,不需要缩放
     * @return Bitmap
     */
    private Bitmap getDownscaledBitmapForView(View view, Rect crop, float downscaleFactor) {

        View cutView = view;

        int width = (int) (crop.width() * downscaleFactor);
        int height = (int) (crop.height() * downscaleFactor);
        float dx = -crop.left * downscaleFactor;
        float dy = -crop.top * downscaleFactor;
        if (width * height <= 0) {
            return null;
        }
        //1,准备图片
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //2,将bitmap作为绘制画布
        Canvas canvas = new Canvas(bitmap);
        Matrix matrix = new Matrix();
        matrix.preScale(downscaleFactor, downscaleFactor);
        matrix.postTranslate(dx, dy);
        //3,设置matrix
        canvas.setMatrix(matrix);
        //4,把View特定的区域绘制到canvas（bitmap）上去，
        cutView.draw(canvas);
        //5,最新的画布
        return bitmap;
    }

    public void removeAllPoints() {
        fl_container.removeAllViews();
    }

    /**
     * 取消
     */
    public void cancel() {
        removeAllPoints();
        setVisibility(GONE);
    }

    private void drawResultPoint() {
        Log.e(TAG, "drawResultPoint: 画点,开始...");
        removeAllPoints();
        if (resultArr == null || resultArr.length == 0) {
            if (onResultPointClickListener != null) {
                onResultPointClickListener.onCancle();
            }
            cancel();
            return;
        }
        setVisibility(VISIBLE);

        //展示取消按钮
        tv_cancle.setVisibility(View.VISIBLE);

        for (int i = 0; i < resultArr.length; i++) {
            final Result result = resultArr[i];
            ResultPoint[] points = result.getResultPoints();
            if (points == null || points.length == 0) {
                cancel();
                return;
            }
            //绘制码眼point
            for (int j = 0; j < points.length; j++) {
                ResultPoint point = points[j];

                int centerX = (int) point.getX();
                int centerY = (int) point.getY();

                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.OVAL);
                gradientDrawable.setColor(resultPointColor);

                ImageView imageView = new ImageView(getContext());
                imageView.setImageDrawable(gradientDrawable);
                RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(PhoneHelper.dip2px(getContext(), 8), PhoneHelper.dip2px(getContext(), 8));
                imageView.setLayoutParams(ivParams);

                imageView.setX(centerX);
                imageView.setY(centerY);

                fl_container.addView(imageView);
            }


            View pointView = LayoutInflater.from(getContext()).inflate(R.layout.qr_scan_result_point_view, null);
            RelativeLayout rl_root = pointView.findViewById(R.id.rl_root);
            ImageView iv_point_bg = pointView.findViewById(R.id.iv_point_bg);
            ImageView iv_point_arrow = pointView.findViewById(R.id.iv_point_arrow);

            //绘制中心
            if (points.length >= 2) {
                //二维码的最中心点
                int centerX = 0;
                int centerY = 0;
                ResultPoint point0 = points[0];
                ResultPoint point1 = points[1];
                if (points.length == 2) {
                    //两点的中点
                    centerX = (int) ((point0.getX() + point1.getX()) / 2);
                    centerY = (int) ((point0.getY() + point1.getY()) / 2);
                }
                if (points.length > 2) {
                    //取1,3点两点的中点
                    ResultPoint point2 = points[2];
                    centerX = (int) ((point0.getX() + point2.getX()) / 2);
                    centerY = (int) ((point0.getY() + point2.getY()) / 2);
                }

                //位置
                RelativeLayout.LayoutParams lpRoot = new RelativeLayout.LayoutParams(resultPointWH, resultPointWH);
                rl_root.setLayoutParams(lpRoot);

                rl_root.setX(centerX - resultPointWH / 2.0f);
                rl_root.setY(centerY - resultPointWH / 2.0f);


                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius(resultPointRadiusCorners);
                gradientDrawable.setShape(RECTANGLE);
                gradientDrawable.setStroke(resultPointStrokeWidth, resultPointStrokeColor);
                gradientDrawable.setColor(resultPointColor);

                iv_point_bg.setImageDrawable(gradientDrawable);

                //点的大小
                ViewGroup.LayoutParams pointBgLayoutParams = iv_point_bg.getLayoutParams();
                pointBgLayoutParams.width = resultPointWH;
                pointBgLayoutParams.height = resultPointWH;
                iv_point_bg.setLayoutParams(pointBgLayoutParams);

                //箭头大小
                ViewGroup.LayoutParams arrowLayoutParams = iv_point_arrow.getLayoutParams();
                arrowLayoutParams.width = resultPointWH / 2;
                arrowLayoutParams.height = resultPointWH / 2;
                iv_point_arrow.setLayoutParams(arrowLayoutParams);


                iv_point_bg.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onResultPointClickListener != null) {
                            onResultPointClickListener.onPointClick(result.getText());
                        }
                        cancel();
                    }
                });
                fl_container.addView(pointView);
            }
        }
        int childCount = fl_container.getChildCount();
        if (childCount <= 0) {
            //取消页面
            if (onResultPointClickListener != null) {
                onResultPointClickListener.onCancle();
            }
            cancel();
        }

        Log.e(TAG, "drawResultPoint: 画点,结束...");

    }


    public void setOnResultPointClickListener(OnResultPointClickListener onResultPointClickListener) {
        this.onResultPointClickListener = onResultPointClickListener;
    }

    public interface OnResultPointClickListener {
        void onPointClick(String result);

        void onCancle();
    }

}