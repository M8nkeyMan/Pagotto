package com.pag.socialz.CustomViews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pag.socialz.R;
import com.pag.socialz.Util.IdUtil;
import com.pag.socialz.Util.LogUtil;

public class MessageView extends RelativeLayout{

    private static final String TAG = MessageView.class.getSimpleName();

    private ImageView arrowImage;
    private RelativeLayout container;

    private TintedBitmapDrawable normalDrawable, pressedDrawable;

    private float cornerRadius, contentPadding, arrowMargin;
    private boolean showArrow;
    private ArrowPosition arrowPosition;
    private ArrowGravity arrowGravity;
    private int backgroundColor, backgroundColorPressed;

    public MessageView(Context context) {
        super(context);
        initView(null, 0);
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs,0);
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs,defStyleAttr);
    }

    private void initView(AttributeSet attributeSet, int defStyleAttr){
        TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.MessageView,defStyleAttr,0);

        showArrow = a.getBoolean(R.styleable.MessageView_mv_showArrow,true);
        arrowMargin = a.getDimension(R.styleable.MessageView_mv_arrowMargin,dpToPx(5));
        cornerRadius = a.getDimension(R.styleable.MessageView_mv_cornerRadius,0);
        contentPadding = a.getDimension(R.styleable.MessageView_mv_contentPadding,dpToPx(10));
        backgroundColor = a.getColor(R.styleable.MessageView_mv_backgroundColor, 0);
        backgroundColorPressed = a.getColor(R.styleable.MessageView_mv_backgroundColorPressed, 0);

        int intPosition = a.getInt(R.styleable.MessageView_mv_arrowPosition, ArrowPosition.LEFT.getValue());
        arrowPosition = ArrowPosition.getEnum(intPosition);

        int intGravity = a.getInt(R.styleable.MessageView_mv_arrowGravity, ArrowGravity.START.getValue());
        arrowGravity = ArrowGravity.getEnum(intGravity);

        a.recycle();
        initContent();
    }

    private void initContent(){
        arrowImage = new ImageView(getContext());
        arrowImage.setId(IdUtil.generateViewId());
        container = new RelativeLayout(getContext());
        container.setId(IdUtil.generateViewId());
        setShowArrow(showArrow);
        setContentPadding((int) contentPadding);
        super.addView(arrowImage, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        super.addView(container, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        updatePositionAndGravity();
        updateColors();
        this.setClickable(true);
    }

    public void setShowArrow(boolean showArrow) {
        this.showArrow = showArrow;
        if (!showArrow) {
            arrowImage.setVisibility(INVISIBLE);
        } else {
            arrowImage.setVisibility(VISIBLE);
        }
    }

    public void setArrowPosition(ArrowPosition position) {
        arrowPosition = position;
        updatePositionAndGravity();
    }

    public void setArrowGravity(ArrowGravity gravity) {
        arrowGravity = gravity;
        updatePositionAndGravity();
    }

    public void setContentPadding(int contentPadding) {
        this.contentPadding = contentPadding;
        container.setPadding(contentPadding, contentPadding, contentPadding, contentPadding);
    }

    private void updateColors() {
        MessageDrawable roundRectDrawable = new MessageDrawable(backgroundColor, cornerRadius);
        container.setBackground(roundRectDrawable);
        normalDrawable.setTint(backgroundColor);
        pressedDrawable.setTint(backgroundColorPressed);
        arrowImage.setImageTintList(ColorStateList.valueOf(backgroundColor));
        Drawable stateDrawable = new MessageStateDrawable(Color.TRANSPARENT) {
            @Override
            protected void onIsPressed(boolean isPressed) {
                MessageDrawable conRlBackground = (MessageDrawable) container.getBackground();
                if (isPressed) {
                    conRlBackground.setColor(backgroundColorPressed);
                    arrowImage.setImageDrawable(pressedDrawable);
                } else {
                    conRlBackground.setColor(backgroundColor);
                    arrowImage.setImageDrawable(normalDrawable);
                }
                container.invalidate();
                arrowImage.invalidate();
            }
        };
        setBackground(stateDrawable);
    }

    private void updatePositionAndGravity() {
        LayoutParams arrowParams = (LayoutParams) arrowImage.getLayoutParams();
        LayoutParams containerParams = (LayoutParams) container.getLayoutParams();
        invalidateRules(arrowParams);
        invalidateRules(containerParams);
        int arrowRotation;
        switch (arrowPosition) {
            case TOP: {
                arrowRotation = 270;
                arrowParams.addRule(ALIGN_PARENT_TOP, TRUE);
                arrowParams.setMargins((int) arrowMargin, 0, (int) arrowMargin, 0);
                containerParams.addRule(BELOW, arrowImage.getId());
                break;
            }
            case BOTTOM: {
                arrowRotation = 90;
                arrowParams.setMargins((int) arrowMargin, 0, (int) arrowMargin, 0);
                arrowParams.addRule(BELOW, container.getId());
                break;
            }
            case LEFT: {
                arrowRotation = 180;
                arrowParams.addRule(ALIGN_PARENT_LEFT, TRUE);
                arrowParams.setMargins(0, (int) arrowMargin, 0, (int) arrowMargin);
                containerParams.addRule(RIGHT_OF, arrowImage.getId());
                break;
            }
            default: {
                arrowRotation = 0;
                arrowParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
                arrowParams.setMargins(0, (int) arrowMargin, 0, (int) arrowMargin);
                containerParams.addRule(LEFT_OF, arrowImage.getId());
                break;
            }
        }

        switch (arrowGravity) {
            case START:
                if (arrowPosition == ArrowPosition.TOP || arrowPosition == ArrowPosition.BOTTOM) {
                    arrowParams.addRule(ALIGN_LEFT, container.getId());
                } else {
                    arrowParams.addRule(ALIGN_TOP, container.getId());
                }
                break;
            case CENTER:
                if (arrowPosition == ArrowPosition.TOP || arrowPosition == ArrowPosition.BOTTOM) {
                    arrowParams.addRule(CENTER_HORIZONTAL, TRUE);
                } else {
                    arrowParams.addRule(CENTER_VERTICAL, TRUE);
                }
                break;
            case END:
                if (arrowPosition == ArrowPosition.TOP || arrowPosition == ArrowPosition.BOTTOM) {
                    arrowParams.addRule(ALIGN_RIGHT, container.getId());
                } else {
                    arrowParams.addRule(ALIGN_BOTTOM, container.getId());
                }
                break;
            default:
                break;
        }
        LogUtil.logDebug(TAG, "updatePositionAndGravity: arrow: " + arrowParams.getRules().length);
        LogUtil.logDebug(TAG, "updatePositionAndGravity: container: " + containerParams.getRules().length);
        int arrowRes = R.drawable.mv_arrow;
        Bitmap source = BitmapFactory.decodeResource(this.getResources(), arrowRes);
        Bitmap rotateBitmap = rotateBitmap(source, arrowRotation);
        normalDrawable = new TintedBitmapDrawable(getResources(), rotateBitmap, backgroundColor);
        pressedDrawable = new TintedBitmapDrawable(getResources(), rotateBitmap, backgroundColorPressed);
        arrowImage.setImageDrawable(normalDrawable);
        arrowImage.setImageTintList(ColorStateList.valueOf(backgroundColor));
        arrowImage.setLayoutParams(arrowParams);
        container.setLayoutParams(containerParams);
    }

    private void invalidateRules(LayoutParams params) {
        params.addRule(ALIGN_PARENT_LEFT, 0);
        params.addRule(ALIGN_PARENT_TOP, 0);
        params.addRule(ALIGN_PARENT_BOTTOM, 0);
        params.addRule(ALIGN_PARENT_RIGHT, 0);

        params.addRule(CENTER_HORIZONTAL, 0);
        params.addRule(CENTER_VERTICAL, 0);

        params.addRule(RIGHT_OF, 0);
        params.addRule(BELOW, 0);
        params.addRule(ABOVE, 0);
        params.addRule(LEFT_OF, 0);

        params.addRule(ALIGN_BOTTOM, 0);
        params.addRule(ALIGN_TOP, 0);
        params.addRule(ALIGN_LEFT, 0);
        params.addRule(ALIGN_RIGHT, 0);
    }

    private int dpToPx(float dpValue){
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private enum ArrowPosition {

        LEFT(0), RIGHT(1), TOP(2), BOTTOM(3);

        int value;

        ArrowPosition(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ArrowPosition getEnum(int value) {
            switch (value) {
                case 0:
                    return LEFT;
                case 1:
                    return RIGHT;
                case 2:
                    return TOP;
                case 3:
                    return BOTTOM;
                default:
                    return LEFT;
            }
        }
    }

    private enum ArrowGravity {
        START(0), CENTER(1), END(2);

        int value;

        ArrowGravity(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ArrowGravity getEnum(int value) {
            switch (value) {
                case 0:
                    return START;
                case 1:
                    return CENTER;
                case 2:
                    return END;
                default:
                    return START;
            }
        }

    }

    private Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private final class TintedBitmapDrawable extends BitmapDrawable{
        private int tint;
        private int alpha;

        public TintedBitmapDrawable(final Resources res, final Bitmap bitmap, final int tint) {
            super(res, bitmap);
            this.tint = tint;
            this.alpha = Color.alpha(tint);
        }

        public TintedBitmapDrawable(final Resources res, final int resId, final int tint) {
            super(res, BitmapFactory.decodeResource(res, resId));
            this.tint = tint;
            this.alpha = Color.alpha(tint);
        }

        public void setTint(final int tint) {
            this.tint = tint;
            this.alpha = Color.alpha(tint);

            invalidateSelf();
        }

        @Override
        public void draw(final Canvas canvas) {
            final Paint paint = getPaint();
            if (paint.getColorFilter() == null) {
                paint.setColorFilter(new LightingColorFilter(tint, 0));
                paint.setAlpha(alpha);
            }
            super.draw(canvas);
        }
    }

    private final class MessageDrawable extends Drawable{

        private final Paint mPaint;
        private final RectF mBoundsF;
        private final Rect mBoundsI;
        private float mRadius;
        private float mPadding;
        private boolean mInsetForPadding;
        private boolean mInsetForRadius = true;

        public MessageDrawable(int backgroundColor, float radius) {
            mRadius = radius;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            mPaint.setColor(backgroundColor);
            mBoundsF = new RectF();
            mBoundsI = new Rect();
        }

        void setPadding(float padding, boolean insetForPadding, boolean insetForRadius) {
            if (padding == mPadding && mInsetForPadding == insetForPadding &&
                    mInsetForRadius == insetForRadius) {
                return;
            }
            mPadding = padding;
            mInsetForPadding = insetForPadding;
            mInsetForRadius = insetForRadius;
            updateBounds(null);
            invalidateSelf();
        }

        float getPadding() {
            return mPadding;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawRoundRect(mBoundsF, mRadius, mRadius, mPaint);
        }

        private void updateBounds(Rect bounds) {
            if (bounds == null) {
                bounds = getBounds();
            }
            mBoundsF.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
            mBoundsI.set(bounds);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            updateBounds(bounds);
        }

        @Override
        public void getOutline(Outline outline) {
            outline.setRoundRect(mBoundsI, mRadius);
        }

        @Override
        public void setAlpha(int alpha) {
            // not supported because older versions do not support
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            // not supported because older versions do not support
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        public float getRadius() {
            return mRadius;
        }

        void setRadius(float radius) {
            if (radius == mRadius) {
                return;
            }
            mRadius = radius;
            updateBounds(null);
            invalidateSelf();
        }

        public void setColor(int color) {
            mPaint.setColor(color);
            invalidateSelf();
        }

        public Paint getPaint() {
            return mPaint;
        }
    }

    private abstract class MessageStateDrawable extends ColorDrawable{
        private boolean mPressed;

        public MessageStateDrawable(int color) {
            super(color);
        }

        @Override
        protected boolean onStateChange(int[] state) {

            boolean pressed = isPressed(state);
            if (mPressed != pressed) {
                mPressed = pressed;
                onIsPressed(mPressed);
            }
            return true;
        }

        protected abstract void onIsPressed(boolean isPressed);

        @Override
        public boolean setState(int[] stateSet) {
            return super.setState(stateSet);
        }

        @Override
        public boolean isStateful() {
            return true;
        }

        private boolean isPressed(int[] state) {
            boolean pressed = false;
            for (int i = 0, j = state != null ? state.length : 0; i < j; i++) {
                if (state[i] == android.R.attr.state_pressed) {
                    pressed = true;
                    break;
                }
            }
            return pressed;
        }
    }
}
