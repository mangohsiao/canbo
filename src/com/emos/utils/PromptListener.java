package com.emos.utils;

import com.emos.canbo.R;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;


public class PromptListener implements android.content.DialogInterface.OnClickListener {

	/**
	 */
	private String promptReply = null;
	
	/**
	 */
	View promptDialogView = null;
	/**
	 */
	TextView textView = null;
	
	public PromptListener(View inDialogView, TextView inTextView) {
		// TODO Auto-generated constructor stub
		promptDialogView = inDialogView;
		textView = inTextView;
	}
	
	@Override
	public void onClick(DialogInterface arg0, int buttonId) {
		// TODO Auto-generated method stub
		if(buttonId == DialogInterface.BUTTON_POSITIVE){
			textView.setText(Integer.toString(getPromptText()));
			promptReply = Integer.toString(getPromptText());
		}else{
			promptReply = null;
		}
	}

	private int getPromptText() {
		SeekBar skbar = (SeekBar)promptDialogView.findViewById(R.id.skbar_prompt);
		return skbar.getProgress();
	}
	
	/**
	 * @return
	 */
	public String getPromptReply() {
		return promptReply;
	}
}
