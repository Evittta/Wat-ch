package com.example.vita.wat_ch.fraqments.drawer;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vita.wat_ch.R;
import com.example.vita.wat_ch.TTSManager;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class BusSelectionFragment extends Fragment implements View.OnClickListener {
	private View view;
	private ImageButton imageButtonCamera, imageButtonVoice;
	private ImageView mImageView;
	private static final int REQUEST_CAMERA = 1;
	private static final String TAG = "QWERTY";
	private TTSManager ttsManager = null;
	
	private String q;
	
	private Uri imageUri;
	private TextView detectedTextView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_bus_selection, container, false);
		imageButtonCamera = (ImageButton) view.findViewById(R.id.imageBtnCamera);
		mImageView = (ImageView) view.findViewById(R.id.imageViewCamera);
		imageButtonVoice = (ImageButton) view.findViewById(R.id.imageBtnVoice);
		detectedTextView = (TextView) view.findViewById(R.id.detected_text);
		detectedTextView.setMovementMethod(new ScrollingMovementMethod());
		
		
		imageButtonCamera.setOnClickListener(this);
		imageButtonVoice.setOnClickListener(this);
		ttsManager = new TTSManager();
		ttsManager.init(getActivity());
		return view;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.imageBtnCamera:
				String filename = System.currentTimeMillis() + ".jpg";
				
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.TITLE, filename);
				values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
				imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				
				Intent intent = new Intent();
				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, REQUEST_CAMERA);
				
				break;
			case R.id.imageBtnVoice:
				ttsManager.initQueue("Bus number " + q);
				break;
			
		}
	}
	
	private void inspect(Uri uri) {
		InputStream is = null;
		Bitmap bitmap = null;
		try {
			is = getActivity().getContentResolver().openInputStream(uri);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			options.inSampleSize = 2;
			options.inScreenDensity = DisplayMetrics.DENSITY_LOW;
			bitmap = BitmapFactory.decodeStream(is, null, options);
			mImageView.setImageURI(uri);
			inspectFromBitmap(bitmap);
		} catch (FileNotFoundException e) {
			Log.w(TAG, "Failed to find the file: " + uri, e);
		} finally {
			if (bitmap != null) {
				bitmap.recycle();
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Log.w(TAG, "Failed to close InputStream", e);
				}
			}
		}
	}
	
	private void inspectFromBitmap(Bitmap bitmap) {
		
		try {
			TextRecognizer textRecognizer =  new TextRecognizer.Builder(getActivity()).build();
			if (!textRecognizer.isOperational()) {
				new AlertDialog.
						Builder(getActivity()).
						setMessage("Text recognizer could not be set up on your device").show();
				return;
			}
			
			Frame frame = new Frame.Builder().setBitmap(bitmap).build();
			SparseArray<TextBlock> origTextBlocks = textRecognizer.detect(frame);
			List<TextBlock> textBlocks = new ArrayList<>();
			for (int i = 0; i < origTextBlocks.size(); i++) {
				TextBlock textBlock = origTextBlocks.valueAt(i);
				textBlocks.add(textBlock);
			}
			Collections.sort(textBlocks, new Comparator<TextBlock>() {
				@Override
				public int compare(TextBlock o1, TextBlock o2) {
					int diffOfTops = o1.getBoundingBox().top - o2.getBoundingBox().top;
					int diffOfLefts = o1.getBoundingBox().left - o2.getBoundingBox().left;
					if (diffOfTops != 0) {
						return diffOfTops;
					}
					return diffOfLefts;
				}
			});
			
			StringBuilder detectedText = new StringBuilder();
			for (TextBlock textBlock : textBlocks) {
				if (textBlock != null && textBlock.getValue() != null) {
					detectedText.append(textBlock.getValue());
					detectedText.append("\n");
				}
			}
			detectedTextView.setText(detectedText);
			q = detectedText.toString();
		} catch (Exception e){
			Log.d("qwerty" , e + "");
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_CAMERA:
				if (resultCode == RESULT_OK) {
					if (imageUri != null) {
						inspect(imageUri);
					}
				}
				break;
			default:
				super.onActivityResult(requestCode, resultCode, data);
				break;
		}
	}
}
