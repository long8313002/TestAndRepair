package cn.xyz.zz.testAndrepair;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/14.
 */
public class NewView extends View {
    public NewView(Context context) {
        super(context);
    }

    public NewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
