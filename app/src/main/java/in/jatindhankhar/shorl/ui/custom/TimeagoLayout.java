package in.jatindhankhar.shorl.ui.custom;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.utils.AnimationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.jatindhankhar.shorl.R;
import in.jatindhankhar.shorl.utils.Utils;

/**
 * Created by jatin on 1/6/17.
 */

public class TimeagoLayout extends LinearLayout {
    private boolean  mShowAgo;
    private Drawable mAgoDrawable;
    private Drawable mNormalDrawable;
    private String mTargetDate;
    private Context mContext;
    @BindView(R.id.complementary_drawable)
    View complementary_drawable;
    @BindView(R.id.date_text)
    TextView dateText;
    @BindView(R.id.parent_layout)
    LinearLayout parentLayout;

    public TimeagoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimeagoLayout, 0, 0);
        try {
            mShowAgo = a.getBoolean(R.styleable.TimeagoLayout_showAgoDefault, false);
            mAgoDrawable = a.getDrawable(R.styleable.TimeagoLayout_agoDrawable);
            mNormalDrawable = a.getDrawable(R.styleable.TimeagoLayout_normalDrawable);
            mTargetDate = a.getString(R.styleable.TimeagoLayout_targetDate);

        } finally {
            a.recycle();
        }

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.time_ago_layout,this,true);
        ButterKnife.bind(this);
        setComplementaryDrawable();
        initView();
        //setDate(); // calling setDate causes Inflation Exception. No idea. Maybe because if targetDate is not specified inside


    }

    private void initView()
    {
        setComplementaryDrawable();
        if(mTargetDate != null)
        {setDate();} // Avoid crash
    }

    private void setComplementaryDrawable()
    {

        if(mShowAgo)
        {
            complementary_drawable.setBackgroundDrawable(mAgoDrawable);
        }
        else
        {
            complementary_drawable.setBackgroundDrawable(mNormalDrawable);
        }
    }

    private void setDate()
    {

        if(mShowAgo)
        {
            dateText.setText(Utils.getRelativeTime(mTargetDate));
        }

        else
        {
            dateText.setText(Utils.getReadbleDate(mTargetDate));
        }
    }
    public void setTargetDate(String date)
    {
        mTargetDate = date;
        setDate();
    }

    public void setAgoDrawable(Drawable drawable)
    {
        mAgoDrawable = drawable;
        setComplementaryDrawable();
    }

    public void setNormalDrawable(Drawable drawable)
    {
        mNormalDrawable = drawable;
        setComplementaryDrawable();
    }

    public void setShowAgo(boolean val)
    {
        mShowAgo = val;
    }

    private void toggleView()
    {
        mShowAgo = !(mShowAgo); // Toggle the behavior
        // Then call new methods
        initView();
        animateView();

    }

    private void animateView()
    {
        /*Animation slideInRight = AnimationUtils.loadAnimation(mContext,R.anim.slide_in_right);
        slideInRight.setDuration(500);
        parentLayout.setAnimation(slideInRight);*/

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(300);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
               // parentLayout.setVisibility(View.GONE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        Animation showIn = new AlphaAnimation(0, 1);
        showIn.setInterpolator(new AccelerateInterpolator());
        showIn.setDuration(300);

        showIn.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                // parentLayout.setVisibility(View.GONE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        parentLayout.startAnimation(fadeOut);
        parentLayout.setAnimation(showIn);

    }

    @OnClick(R.id.parent_layout)
    public void onClick() {
        toggleView();
    }
}
