package org.alfonz.samples.alfonzgraphics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.alfonz.samples.R;
import org.alfonz.samples.alfonzadapter.AlfonzAdapterActivity;


public class AlfonzGraphicsActivity extends AppCompatActivity
{
	public static Intent newIntent(Context context)
	{
		Intent intent = new Intent(context, AlfonzGraphicsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return intent;
	}


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alfonz_graphics);
	}
}
