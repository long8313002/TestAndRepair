package cn.xyz.zz.testAndrepair;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/13.
 */
public class NewRadioButton extends RadioButton {
    public NewRadioButton(Context context) {
        super(context);
    }

    public NewRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    public void toggle() {

        Toast.makeText(getContext(),getClass().getSimpleName(),1).show();
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return NewRadioButton.class.getName();
    }
}
