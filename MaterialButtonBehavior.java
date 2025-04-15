package <your package>;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ImanX. and developed by rezatutor475
 */
public class MaterialButtonBehavior extends CoordinatorLayout.Behavior<MaterialButton> {
    private ValueAnimator valueAnimator = new ValueAnimator();
    private int expandWidth;
    private int collapseWidth;
    private boolean isExpanded = true;
    private int animationDuration = 170;
    private final Map<MaterialButton, Integer> buttonOriginalWidths = new HashMap<>();

    public MaterialButtonBehavior() {}

    public MaterialButtonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull MaterialButton child,
                                       @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, MaterialButton child, View dependency) {
        if (expandWidth == 0 && child.getMeasuredWidth() != 0) {
            expandWidth = child.getMeasuredWidth();
            buttonOriginalWidths.put(child, expandWidth);
        }

        if (collapseWidth == 0 && child.getMinWidth() != 0) {
            collapseWidth = child.getMinWidth();
        }

        return dependency instanceof RecyclerView;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, MaterialButton child, View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dy) {
        if (dyConsumed < 0 && !isExpanded) {
            animateButtonWidth(child, expandWidth);
            isExpanded = true;
        } else if (dyConsumed > 0 && isExpanded) {
            animateButtonWidth(child, collapseWidth);
            isExpanded = false;
        }
    }

    private void animateButtonWidth(final View view, int targetWidth) {
        if (valueAnimator != null && valueAnimator.isRunning()) return;

        int currentWidth = view.getMeasuredWidth();
        valueAnimator = ValueAnimator.ofInt(currentWidth, targetWidth);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(animationDuration);
        valueAnimator.addUpdateListener(anim -> {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = (int) anim.getAnimatedValue();
            view.setLayoutParams(params);
        });
        valueAnimator.start();
    }

    public void expand(MaterialButton button) {
        animateButtonWidth(button, expandWidth);
        isExpanded = true;
    }

    public void collapse(MaterialButton button) {
        animateButtonWidth(button, collapseWidth);
        isExpanded = false;
    }

    public void toggle(MaterialButton button) {
        if (isExpanded) {
            collapse(button);
        } else {
            expand(button);
        }
    }

    public void instantExpand(MaterialButton button) {
        setButtonWidth(button, expandWidth);
        isExpanded = true;
    }

    public void instantCollapse(MaterialButton button) {
        setButtonWidth(button, collapseWidth);
        isExpanded = false;
    }

    public void setButtonWidth(MaterialButton button, int width) {
        ViewGroup.LayoutParams params = button.getLayoutParams();
        params.width = width;
        button.setLayoutParams(params);
    }

    public void calibrateWidth(MaterialButton button) {
        if (isExpanded) {
            expandWidth = button.getMeasuredWidth();
        } else {
            collapseWidth = button.getMeasuredWidth();
        }
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setAnimationDuration(int duration) {
        this.animationDuration = duration;
    }

    public int getAnimationDuration() {
        return animationDuration;
    }

    public void show(MaterialButton button) {
        button.setVisibility(View.VISIBLE);
    }

    public void hide(MaterialButton button) {
        button.setVisibility(View.INVISIBLE);
    }

    public void fadeIn(MaterialButton button, int duration) {
        button.setAlpha(0f);
        button.setVisibility(View.VISIBLE);
        button.animate().alpha(1f).setDuration(duration).start();
    }

    public void fadeOut(MaterialButton button, int duration) {
        button.animate().alpha(0f).setDuration(duration)
              .withEndAction(() -> button.setVisibility(View.INVISIBLE)).start();
    }

    public void toggleVisibility(MaterialButton button) {
        if (button.getVisibility() == View.VISIBLE) {
            hide(button);
        } else {
            show(button);
        }
    }

    public boolean isAnimationRunning() {
        return valueAnimator != null && valueAnimator.isRunning();
    }

    public void stopAnimation() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
    }

    public void saveState(MaterialButton button, SparseArray<Parcelable> container) {
        button.saveHierarchyState(container);
    }

    public void restoreState(MaterialButton button, SparseArray<Parcelable> container) {
        button.restoreHierarchyState(container);
    }

    public void enable(MaterialButton button) {
        button.setEnabled(true);
    }

    public void disable(MaterialButton button) {
        button.setEnabled(false);
    }

    public boolean isEnabled(MaterialButton button) {
        return button.isEnabled();
    }
} 
