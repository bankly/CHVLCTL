package stepbystep.careful;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import stepbystep.careful.util.BinaryFile;
import stepbystep.careful.util.CheckVnLang;
import stepbystep.careful.util.DatabaseIndexInfo;
import stepbystep.careful.util.DictProtocol;
import stepbystep.careful.util.Engine;
import stepbystep.careful.util.FileMeaning;
import stepbystep.careful.util.HandlingStringEV;
import stepbystep.careful.util.IndexInfo;
import stepbystep.careful.util.IndexInfoOperations;
import stepbystep.careful.util.MyAdapter;
import stepbystep.careful.util.Stemmer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener, OnScrollListener,
        OnItemSelectedListener {

	private EditText mEditSearch;
	private ListView mListIndex;
	private MyAdapter mMyAdapter;
	private WebView mWebMeaning;

	private Engine mEngine;

	private boolean mAtHome;

	private DictApplication mDictApp;
	private Spinner mSpinnerDictType;
	private IndexInfoOperations indexOperations;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_custom_title_bar);

		mDictApp = (DictApplication) getApplicationContext();
		mDictApp.setDictType(DictApplication.arrDictType[0]);
		mEngine = new Engine(this);
		indexOperations = new IndexInfoOperations(getApplicationContext());

		File database = getApplicationContext().getDatabasePath(DatabaseIndexInfo.DATABASE_NAME);
		if (database.exists() != true) {
			indexOperations.createDataBase("E-V");
		}

		mEngine.openDict(mDictApp.getDictType());
		mListIndex = (ListView) findViewById(R.id.list_index);

		mMyAdapter = new MyAdapter(this, mEngine);
		mListIndex.setAdapter(mMyAdapter);

		mSpinnerDictType = (Spinner) findViewById(R.id.spinner_dict_type);
		ArrayAdapter<String> adapterSpinnerDictType = new ArrayAdapter<String>(this,
		        android.R.layout.simple_spinner_item);
		adapterSpinnerDictType
		        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterSpinnerDictType.addAll(DictApplication.arrDictType);
		mSpinnerDictType.setAdapter(adapterSpinnerDictType);
		mWebMeaning = (WebView) findViewById(R.id.web_meaning);
		mWebMeaning.getSettings().setFixedFontFamily("SANS_SERIF");
		mWebMeaning.setWebViewClient(new MyWebViewClient());

		mEditSearch = (EditText) findViewById(R.id.edit_search);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/DejaVuSans.ttf");
		mEditSearch.setTypeface(tf);

		setEvents();

	}

	private void setEvents() {
		mEditSearch.addTextChangedListener(new MyTextWatcher());
		mListIndex.setOnItemClickListener(this);
		mListIndex.setOnScrollListener(this);
		mSpinnerDictType.setOnItemSelectedListener(this);
	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			if (url.equals("about:blank")) {
				view.loadUrl(url);
			} else {
				IndexInfo indexInfo = mEngine.getMeaning(url);
				if (indexInfo == null) {
					indexInfo = mEngine.getMeaningLoose(url);
				}

				boolean newDict = false;
				String newTypeDict = null;
				if (indexInfo == null) {

					newTypeDict = DictApplication.arrDictType[0] == mDictApp.getDictType() ? DictApplication.arrDictType[1]
					        : DictApplication.arrDictType[0];
					mEngine.close();
					mEngine.openDict(newTypeDict);
					indexInfo = mEngine.getMeaning(url);
					newDict = true;
				}

				if (indexInfo == null) {
					indexInfo = mEngine.getMeaningLoose(url);
				}

				if (indexInfo != null) {
					if (newDict == false) {
						loadMeaning(indexInfo, mDictApp.getDictType());
					} else {
						loadMeaning(indexInfo, newTypeDict);
					}
				}

				if (indexInfo == null) {
					mEngine.close();
					mEngine.openDict(DictApplication.arrDictType[0]);
					String result;
					if ((result = Stemmer.getOriginalWord(url)) != null) {
						indexInfo = mEngine.getMeaning(result);
					}

					if (indexInfo != null) {
						loadMeaning(indexInfo, DictApplication.arrDictType[0]);
					}
				}

				mEngine.close();
				mEngine.openDict(mDictApp.getDictType());
			}
			return true;
		}
	}

	private class MyTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

			if (s.length() > 0 && s.charAt(s.length() - 1) != ' ') {
				int posItem = 0;
				if (mDictApp.getDictType().startsWith("V")) {
					if (CheckVnLang.isVietLang(s.toString())) {
						posItem = mEngine.getEntryIdVnLang(s.toString());
					} else {
						posItem = mEngine.getEntryIdUnsigned(s.toString());
					}

				} else {
					posItem = mEngine.getEntryId(s.toString());
				}
				if (posItem != -1) {
					mListIndex.setSelection(posItem);
				}
			}
		}
	}

	private void setView(boolean value) {
		if (value == true) {
			mEditSearch.setVisibility(View.VISIBLE);
			mListIndex.setVisibility(View.VISIBLE);
			mWebMeaning.setVisibility(View.GONE);
			mSpinnerDictType.setVisibility(View.VISIBLE);
		} else {
			mEditSearch.setVisibility(View.GONE);
			mListIndex.setVisibility(View.GONE);
			mSpinnerDictType.setVisibility(View.GONE);
			mWebMeaning.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		hideKeyboard();
		IndexInfo indexInfo = new IndexInfo();
		indexInfo = mEngine.getMeaning(position + 1);
		loadMeaning(indexInfo, mDictApp.getDictType());

		mEditSearch.setText("");
		mEditSearch.append(indexInfo.getWord());
		setView(false);
		mAtHome = false;
		hideKeyboard();
		setView(false);
		mAtHome = false;

		// mWebMeaning.loadDataWithBaseURL(null,
		// DictProtocol.convertToDictProtocol(mEngine.getEntryAll()), "text/html", "utf-8", null);

		// mEngine.close();
		// mEngine.openDict1("E-V");
		//
		// long begin = System.currentTimeMillis();
		// mEngine.getEntryAllContent();
		// long end = System.currentTimeMillis();
		// Log.i("MY", "time: " + (end - begin));

		/*
		 * String meaning = FileMeaning.readMeaning(9328, 66, "E-V"); String handledMeaning =
		 * HandlingStringEV.formatString(meaning);
		 * 
		 * mWebMeaning.loadDataWithBaseURL(null, handledMeaning, "text/html", "utf-8", null);
		 */
		// long begin = System.currentTimeMillis();
		// DictProtocol.convertPos();
		// long end = System.currentTimeMillis();
		// Log.i("MY", "time: " + (end - begin));

		// String meaning = FileMeaning.readMeaning(
		// MyConvert.convertBase64ToDecimal("8"),
		// MyConvert.convertBase64ToDecimal("B3"), "E-V");
		// String handledMeaning = HandlingStringEV.formatString(meaning);
		//
		// mWebMeaning.loadDataWithBaseURL(null, handledMeaning, "text/html", "utf-8", null);
		// setView(false);

		// long begin = System.currentTimeMillis();
		// DictProtocol.convertIndexFull();
		// long end = System.currentTimeMillis();
		// Log.i("MY", "time: " + (end - begin));

		// mEngine.close();
		// mEngine.openDict1("E-V");
		// mEngine.updateUnsignedWord();

		// mWebMeaning.loadDataWithBaseURL(null, BinaryFile.readGzip(), "text/html", "utf-8", null);
		// setView(false);

		// GZIPOutputStream gos;
		// try {
		// gos = new GZIPOutputStream(new FileOutputStream(new File(DictApplication.PATH_SDCARD
		// + File.separator + DictApplication.FOLDER + File.separator + "E-V"
		// + File.separator + DictApplication.FILE_MEANING)));
		// FileInputStream fis = new FileInputStream(new File(DictApplication.PATH_SDCARD
		// + File.separator + DictApplication.FOLDER + File.separator + "E-V"
		// + File.separator + "dict.dict"));
		// BufferedInputStream bis = new BufferedInputStream(fis);
		// byte[] buff = new byte[8192];
		// while(bis.read(buff) != -1) {
		// gos.write(buff);
		// }
		//
		// gos.flush();
		// gos.finish();
		// gos.close();
		// bis.close();
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		
//		BinaryFile.convertToBinFile("E-V");

	}

	private void loadMeaning(IndexInfo indexInfo, String typeDict) {
		long s = System.currentTimeMillis();
		String meaning = FileMeaning.readMeaning(indexInfo.getStart(), indexInfo.getLength(),
		        typeDict);
		long e = System.currentTimeMillis();
		Log.i("MY", "time: " + (e - s));
//		Toast.makeText(getApplicationContext(), "time: " + (e - s) ,Toast.LENGTH_LONG).show();
		String handledMeaning = HandlingStringEV.formatString(meaning);

		mWebMeaning.loadDataWithBaseURL(null, handledMeaning, "text/html", "utf-8", null);
//		 mWebMeaning.loadDataWithBaseURL(null, meaning, "text/html", "utf-8", null);
	}

	private void hideKeyboard() {
		InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethod.hideSoftInputFromWindow(mEditSearch.getWindowToken(),
		        InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mAtHome != true) {
				mAtHome = true;
				setView(true);
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
	        int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == 1) {
			hideKeyboard();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		mDictApp.setDictType(DictApplication.arrDictType[position]);
		mEngine.close();
		mEngine.openDict(DictApplication.arrDictType[position]);
		mListIndex.invalidateViews();
		mListIndex.setSelection(0);

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
