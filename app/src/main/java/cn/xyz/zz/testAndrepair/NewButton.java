package cn.xyz.zz.testAndrepair;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/14.
 */
public class NewButton extends Button {
    public NewButton(Context context) {
        super(context);
    }

    public NewButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if(text!=null&&text.length()>0){
            text ="hahahhahah:"+text;
        }
        super.setText(text, type);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(new MyClick(l));
    }

    public class MyClick implements OnClickListener{

        private OnClickListener l;

        public MyClick(OnClickListener l){

            this.l = l;
        }

        @Override
        public void onClick(View v) {
            l.onClick(v);
            Toast.makeText(getContext(), "111", Toast.LENGTH_SHORT).show();
        }
    }
}
