package in.jatindhankhar.shorl.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    @BindView(R.id.complementary_drawable)
    View complementary_drawable;
    @BindView(R.id.date_text)
    TextView dateText;
    @BindView(R.id.parent_layout)
    LinearLayout parentLayout;

    public TimeagoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
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



    }

    private void initView()
    {
        setComplementaryDrawable();
        setDate();
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
    }

    public void setAgoDrawable(Drawable drawable)
    {
        mAgoDrawable = drawable;
    }

    public void setNormalDrawable(Drawable drawable)
    {
        mNormalDrawable = drawable;
    }

    public void setShowAgo(boolean val)
    {
        mShowAgo = val;
    }

    public void toggleView()
    {
        mShowAgo = !(mShowAgo); // Toggle the behavior
        // Then call new methods
        initView();
    }

    @OnClick(R.id.parent_layout)
    public void onClick() {
        toggleView();
    }
}
