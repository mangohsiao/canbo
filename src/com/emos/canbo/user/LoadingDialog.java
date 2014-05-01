package com.emos.canbo.user;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog extends ProgressDialog {

	public LoadingDialog(Context context, String title) {
		super(context);
		setTitle(title);
	}

}
