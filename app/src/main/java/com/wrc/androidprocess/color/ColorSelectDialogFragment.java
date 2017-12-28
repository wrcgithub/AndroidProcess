package com.wrc.androidprocess.color;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wrc.androidprocess.R;
import com.wrc.androidprocess.color.core.ColorPalette;


/**
 * 颜色选择器：DialogFragment
 */
public class ColorSelectDialogFragment extends DialogFragment {


    private ColorPalette colorPalette;
    private Button cancelBtn,confirmBtn;
    private int color;

    private OnColorSelectListener onColorSelectListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        LinearLayout rootLayout= (LinearLayout) inflater.inflate(R.layout.fragment_picker_color,container,false);
        colorPalette= (ColorPalette) rootLayout.findViewById(R.id.fragment_color_palette);

        colorPalette.setLastColor(color);
        colorPalette.setCurrColor(color);

        cancelBtn= (Button) rootLayout.findViewById(R.id.cancel_color_btn);
        confirmBtn= (Button) rootLayout.findViewById(R.id.confirm_color_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onColorSelectListener!=null){
                    color=colorPalette.getSelectColor();
                    onColorSelectListener.onSelectFinish(color);
                }
                dismiss();
            }
        });

        return rootLayout;
    }

    public static interface OnColorSelectListener {
        public void onSelectFinish(int color);
    }

    public OnColorSelectListener getOnColorSelectListener() {
        return onColorSelectListener;
    }

    public void setOnColorSelectListener(OnColorSelectListener onColorSelectListener) {
        this.onColorSelectListener = onColorSelectListener;
    }

    public void setLastColor(int color){
        this.color=color;
    }
}
