package com.agshin.ipotekakalkulyatoru;

import com.agshin.ipotekakalkulyatoru.R;

import android.R.string;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;



public class MainActivity extends Activity {

	String m_qiymet;
	String m_kredit;
	String m_ilkin;
	String m_ayliq;
	
	Boolean canCalculate;
	int editTextBackId;
	int editTextBackId2;
	int calculatorState;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//	    ActionBar actionBar = getActionBar();
//	    actionBar.setDisplayHomeAsUpEnabled(true);
		calculatorState = 1;
		
		final Button btn_umumi = (Button) findViewById(R.id.btn_common_ipo);
		final Button btn_guzewt = (Button) findViewById(R.id.btn_prefer_ipo);
		final Spinner sp_muddet = (Spinner) findViewById(R.id.spinner_muddet);

		
		final EditText et_ilkin = (EditText) findViewById(R.id.editText_ilkin);
		final EditText et_faiz = (EditText) findViewById(R.id.editText_faiz);
		
		btn_umumi.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				calculatorState = 1;
				btn_umumi.setBackgroundResource(R.drawable.btn_toggled1);
				btn_guzewt.setBackgroundResource(R.drawable.btn_guzewtli_back);
				
				String array_spinner[]=new String[23];
				
				for(int i = 0; i < 23; i++)
				{
					array_spinner[i] = String.valueOf(i + 3);
				}

				ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,
				R.layout.list_item_view, array_spinner);
				sp_muddet.setAdapter(adapter);
				sp_muddet.setSelection(22);
				
				et_ilkin.setText("20");
				et_faiz.setText("8");
				
			}
		});
		
		btn_guzewt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				calculatorState = 2;
				btn_umumi.setBackgroundResource(R.drawable.btn_umumi_back);
				btn_guzewt.setBackgroundResource(R.drawable.btn_toggled2);
				
				String array_spinner[]=new String[28];

				
				for(int i = 0; i < 28; i++)
				{
					array_spinner[i] = String.valueOf(i + 3);
				}
				

				ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,
						R.layout.list_item_view, array_spinner);
				sp_muddet.setAdapter(adapter);
				sp_muddet.setSelection(27);
				
				et_ilkin.setText("15");
				et_faiz.setText("4");
				
			}
		});
		
		final TextView tv_alinan_evin_qiymeti = (TextView) findViewById(R.id.tv_alinan_evin_qiymeti_2);
		final TextView tv_kredit_meblegi = (TextView) findViewById(R.id.tv_Kreditin_meblegi_2);
		final TextView tv_ilkino_deniwe_minimal_teleb = (TextView) findViewById(R.id.tv_ilkin_odeniwe_min_teleb_2);
		final TextView tv_ayliq_odeniw = (TextView) findViewById(R.id.tv_ayliq_odeniw_2);
		
		
		final RelativeLayout baseLayout = (RelativeLayout) findViewById(R.id.lay_top);
		final RelativeLayout baseLayout2 = (RelativeLayout) findViewById(R.id.lay_botom);
		
		final EditText et_gelir = (EditText) findViewById(R.id.editText_gelir);
		final EditText et_aile_uzv_say = (EditText) findViewById(R.id.editText_aile);
		final EditText et_diger_ohd = (EditText) findViewById(R.id.editText_diger);
		
		et_diger_ohd.setText("30");
		et_ilkin.setText("20");
		et_faiz.setText("8");
		
		editTextBackId = R.drawable.login_text_edit_selector;
		editTextBackId2 = R.drawable.text_edit_empty;
		
		et_gelir.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				et_gelir.setBackgroundResource(editTextBackId);
				
			}
		});
		et_aile_uzv_say.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				et_aile_uzv_say.setBackgroundResource(editTextBackId);
				
			}
		});
		et_diger_ohd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				et_diger_ohd.setBackgroundResource(editTextBackId);
				
			}
		});
		et_ilkin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				et_ilkin.setBackgroundResource(editTextBackId);
				final ScrollView sv = (ScrollView) findViewById(R.id.scrollView);
				sv.scrollTo(0, sv.getBottom());
				
			}
		});
		et_faiz.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				et_faiz.setBackgroundResource(editTextBackId);
				
			}
		});
		
		final Button btn_geri = (Button) findViewById(R.id.btn_geri);
		
		
		final ImageButton btn_gelir_info = (ImageButton) findViewById(R.id.imageButton_gelir_info);
		final ImageButton btn_aile_info = (ImageButton) findViewById(R.id.imageButton_aile_info);
		final ImageButton btn_diger_info = (ImageButton) findViewById(R.id.imageButton_diger_info);
		final ImageButton btn_muddet_info = (ImageButton) findViewById(R.id.imageButton_muddet_info);
		final ImageButton btn_ilkin_info = (ImageButton) findViewById(R.id.imageButton_ilkin_info);
		final ImageButton btn_faiz_info = (ImageButton) findViewById(R.id.imageButton_faiz_info);
		
		///info buttons
		btn_gelir_info.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			    
			    builder.setTitle("Aylıq gəlir");
			    builder.setIcon(R.drawable.ic_info);
			    builder.setMessage("Aylıq ümumi rəsmi gəlir nəzərdə tutulur." +
										 "Bura maaşınızla bərabər, "+
										 "varsa digər gəlirləriniz də aiddir."+
										 "(Birgə borcalanlar varsa "+
										 "onların gəlirləri də daxildir)");
			    builder.setPositiveButton("Bağla", null);
			    builder.show();
			
			    

				
			    
			}
		});
		
		btn_aile_info.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			    
			    builder.setTitle("Ailə üzvlərinin sayı");
			    builder.setIcon(R.drawable.ic_info);
			    builder.setMessage("Borcalan və borcalanla birgə yaşayan (onun himayəsində olan) şəxslər");
			    builder.setPositiveButton("Bağla", null);
			    builder.show();
				
			}
		});
		
		btn_diger_info.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			    
			    builder.setTitle("Digər öhdəliklər");
			    builder.setIcon(R.drawable.ic_info);
			    builder.setMessage("İpoteka predmetinin  və Borcalanın həyat "+
										"sığortası üzrə ödənişlər,  borcalanın "+
										"mülkiyyətində olan/olacaq yaşayış sahəsi "+
										"ilə	bağlı  xərclər (texniki xidmət, kommunal "+
										"xidmətlər),	digər öhdəliklər üzrə ödənişlər"+
										"(kredit borcu, alimentlər və s.)");
			    builder.setPositiveButton("Bağla", null);
			    builder.show();
				
			}
		});
		
		btn_muddet_info.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			    
			    builder.setTitle("Kreditin müddəti (il)");
			    builder.setIcon(R.drawable.ic_info);
			    if(calculatorState == 1){
			    builder.setMessage("Kreditin verilmə müddətidir. "+
								   "İpoteka kreditinin verilmə şərtlərinə əsasən " +
								   "müddət 3 il ilə 25 il arasında ola bilər.");
			    }else
			    if(calculatorState == 2)
			    {
			    	builder.setMessage("Kreditin verilmə müddətidir. "+
							   "İpoteka kreditinin verilmə şərtlərinə əsasən " +
							   "müddət 3 il ilə 30 il arasında ola bilər.");
			    }
			    builder.setPositiveButton("Bağla", null);
			    builder.show();
				
			}
		});
		btn_ilkin_info.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			    
			    builder.setTitle("İlkin ödəniş (minimal)");
			    builder.setIcon(R.drawable.ic_info);
			    if(calculatorState == 1){
			    builder.setMessage("Almaq istədiyiniz evin dəyərinin "+
											"əvvəlcədən ödəyəcəyiniz hissəsi. "+
											"İpoteka kreditinin verilmə şərtlərinə "+
											"əsasən bu ən azı 20% -ni təşkil etməlidir. "+
											"Yəni, almaq istədiyiniz evin "+
											"minimum 20%-ni əvvəlcədən ödəməlisiniz. "+
											"Qalan 80% isə ipoteka krediti şəklində ödəyəcəksiniz "+
											"(maksimum 50000AZN olmaq şərtilə).");
			    }else if(calculatorState == 2)
			    {
			    	builder.setMessage("Almaq istədiyiniz evin dəyərinin "+
							"əvvəlcədən ödəyəcəyiniz hissəsi. "+
							"İpoteka kreditinin verilmə şərtlərinə "+
							"əsasən bu ən azı 15% -ni təşkil etməlidir. "+
							"Yəni, almaq istədiyiniz evin "+
							"minimum 15%-ni əvvəlcədən ödəməlisiniz. "+
							"Qalan 85% isə ipoteka krediti şəklində ödəyəcəksiniz "+
							"(maksimum 50000AZN olmaq şərtilə).");
			    }
			    builder.setPositiveButton("Bağla", null);
			    builder.show();
				
			}
		});
		
		btn_faiz_info.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			    
			    builder.setTitle("Kredit faizi (illik)");
			    if(calculatorState == 1){
			    builder.setMessage("İpoteka krediti üçün təyin edilən "+
			 		   "illik faiz nəzərdə tutulur. "+
					   "İpoteka kreditinin verilmə şərtlərinə "+
					   "əsasən faiz dərəcəsi 8%-dən çox ola bilməz");
			    }else if(calculatorState == 2)
			    {
			    builder.setMessage("İpoteka krediti üçün təyin edilən "+
					 		   "illik faiz nəzərdə tutulur. "+
							   "İpoteka kreditinin verilmə şərtlərinə "+
							   "əsasən faiz dərəcəsi 4%-dən çox ola bilməz");
			    }
			    builder.setPositiveButton("Bağla", null);
			    builder.show();
				
			}
		});
		

		////info buttons
		
		
		
		String array_spinner[]=new String[23];
		
		for(int i = 0; i < 23; i++)
		{
			array_spinner[i] = String.valueOf(i + 3);
		}


		ArrayAdapter adapter = new ArrayAdapter(this,
				R.layout.list_item_view, array_spinner);
		sp_muddet.setAdapter(adapter);
		sp_muddet.setSelection(22);
		
		sp_muddet.setOnItemSelectedListener(new OnItemSelectedListener() {


			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
		

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}


		});
		
		Button btn_calc = (Button) findViewById(R.id.btn_calc);
		btn_calc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				et_gelir.setBackgroundResource(editTextBackId);
				
				if(et_ilkin.getText().toString().length() > 0)
				{	
					if(calculatorState == 1){
						if(Integer.parseInt(et_ilkin.getText().toString()) < 20)
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						    
						    builder.setTitle("İlkin ödəniş");
						    builder.setIcon(R.drawable.ic_warning);
						    
						    builder.setMessage("İpoteka kreditlərinin verilnməsi şərtlərinə əsasən ilkin ödəniş 20%-dən az olmamalıdır.");
						    builder.setPositiveButton("Bağla", null);
						    builder.show();
						    et_ilkin.setText("20");
						    return;
						}else{
							if(Integer.parseInt(et_ilkin.getText().toString()) > 100)
							{
							    et_ilkin.setText("100");	
							}
						}
					}else if(calculatorState == 2)
					{
						if(Integer.parseInt(et_ilkin.getText().toString()) < 15)
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						    
						    builder.setTitle("İllik faiz");
						    builder.setIcon(R.drawable.ic_warning);
						    
						    builder.setMessage("İpoteka kreditlərinin verilnməsi şərtlərinə əsasən ilkin ödəniş 15%-dən az olmamalıdır.");
						    builder.setPositiveButton("Bağla", null);
						    builder.show();
						    et_ilkin.setText("15");
						    return;
						}else{
							if(Integer.parseInt(et_ilkin.getText().toString()) > 100)
							{
							    et_ilkin.setText("100");	
							}
						}
					}
				}
				if(et_faiz.getText().toString().length() > 0){
					if(calculatorState == 1){
						if(Integer.parseInt(et_faiz.getText().toString()) > 8)
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						    
						    builder.setTitle("Qeyd");
						    builder.setIcon(R.drawable.ic_warning);
						    builder.setMessage("İpoteka kreditlərinin verilnməsi şərtlərinə əsasən illik kredit faizi 8%-dən çox olmamalıdır.");
						    builder.setPositiveButton("Bağla", null);
						    builder.show();
						    et_faiz.setText("8");
						    return;
						}
					}else if(calculatorState == 2)
					{
						if(Integer.parseInt(et_faiz.getText().toString()) > 4)
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						    
						    builder.setTitle("Qeyd");
						    builder.setIcon(R.drawable.ic_warning);
						    builder.setMessage("İpoteka kreditlərinin verilnməsi şərtlərinə əsasən illik kredit faizi 4%-dən çox olmamalıdır.");
						    builder.setPositiveButton("Bağla", null);
						    builder.show();
						    et_faiz.setText("4");
						    return;
						}
					}
				}				
				canCalculate = true;
				

				
				if(et_gelir.getText().toString().length() == 0)
				{
					canCalculate = false;
					et_gelir.setBackgroundResource(editTextBackId2);
					InputMethodManager imm = (InputMethodManager)getSystemService(
							Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et_faiz.getWindowToken(), 0);
				}else 
				{
					et_gelir.setBackgroundResource(editTextBackId);
				}
				
				if(et_aile_uzv_say.getText().toString().length() == 0)
				{
					canCalculate = false;
					et_aile_uzv_say.setBackgroundResource(editTextBackId2);
				}else
				{
					et_aile_uzv_say.setBackgroundResource(editTextBackId);
				}
				
				if(et_diger_ohd.getText().toString().length() == 0)
				{
					canCalculate = false;
					et_diger_ohd.setBackgroundResource(editTextBackId2);
				}else
				{
					et_diger_ohd.setBackgroundResource(editTextBackId);
				}
				
				if(et_faiz.getText().toString().length() == 0)
				{
					canCalculate = false;
					et_faiz.setBackgroundResource(editTextBackId2);
				}else
				{
					et_faiz.setBackgroundResource(editTextBackId);
				}				
				if(et_ilkin.getText().toString().length() == 0)
				{
					canCalculate = false;
					et_ilkin.setBackgroundResource(editTextBackId2);
				}else
				{
					et_ilkin.setBackgroundResource(editTextBackId);
				}
				
				if(canCalculate){
				calculate(Float.parseFloat(et_gelir.getText().toString()),
						Float.parseFloat(et_aile_uzv_say.getText().toString()),
						Float.parseFloat(et_diger_ohd.getText().toString()),
						Float.parseFloat(sp_muddet.getSelectedItem().toString()),
						Float.parseFloat(et_ilkin.getText().toString()),
						Float.parseFloat(et_faiz.getText().toString()));
				
						tv_alinan_evin_qiymeti.setText(m_qiymet + " AZN");
						tv_kredit_meblegi.setText(m_kredit+ " AZN");
						tv_ilkino_deniwe_minimal_teleb.setText(m_ilkin+ " AZN");
						tv_ayliq_odeniw.setText(m_ayliq+ " AZN");
				
						InputMethodManager imm = (InputMethodManager)getSystemService(
								Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(et_faiz.getWindowToken(), 0);
						
						baseLayout.setVisibility(RelativeLayout.GONE);
						baseLayout2.setVisibility(RelativeLayout.GONE);
				}else
				{
					
				}
			}
		});
		
		btn_geri.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				baseLayout.setVisibility(RelativeLayout.ABOVE);
				baseLayout2.setVisibility(RelativeLayout.ABOVE);				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    //MenuInflater inflater = getMenuInflater();
	    //inflater.inflate(R.menu.main_activity_tabs, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	private void calculate(float gelir, float aile_uzv_say, float diger_ohd, float muddet, float ilkin_odeniw, float faiz)
	{
		float c4 = gelir;
		float c5 = aile_uzv_say;
		float c12 = 136; //min emekhaqqi;
		float c6 = diger_ohd;
		float c11h;
		
		if(c4 < 2001) 
		c11h = (float) (c4 * 0.17);
		else {
			c11h = (float) ((c4 - 2000) * 0.35 + 280 + (c4 * 0.03));
		}
				
		float c11 = Math.round(c11h);
		float c13 = c5 * c12;
		float c14h = c11h + c13 + c6;
		float c14 = Math.round(c14h);
		
		float c9 = (float) (faiz * 0.01);
		float g6 = 50000;
		float c7 = muddet;
		//		var $f8 = (($c9/12)*$g6)/(1-1/Math.pow(((1+$c9/12)),($c7*12)));

		float m1 = (c9 / 12) * g6;
		float m5 = 1 + (c9/12);
		float m6 = c7 * 12;
		float m2 = (float) Math.pow(m5, m6);
		float f8 = m1 / (1 - (1/m2)); 
//				(float) ((faiz / 12) * g6) / (1 - 1 / Math.pow(((1 + (faiz/12)), (muddet * 12)));
		float f18;
		if((c4 - c14h) > (c4 * 0.7)) 
			f18 = (float) (c4 * 0.7);
		else 
			f18 = c4 - c14h;
		
		float c18;
		if(f18 < 0) c18 = 0; 
		else {
			if(f18 <= f8) c18 = f18;
			else c18 = f8;
		}
		
		float c8 = (float) (ilkin_odeniw * 0.01);
		float c15  = (float) ((c18*(1 - Math.pow(1 +(c9/12), - c7*12) ) / (c9 /12)) / (1 - c8));
		float c15h = Math.round(c15);
		float c16 = (float) ((c18*(1 - Math.pow(1 +(c9/12), - c7*12) ) / (c9 /12)) );
		float c16h = Math.round(c16);
		float c17 = c15 - c16;
		float c17h = Math.round(c17);
		float c18h = Math.round(c18);
		float c19 = Math.round(c18 * c7 * 12);
		
		m_qiymet = String.valueOf(c15h);
		m_ilkin = String.valueOf(c17h);
		m_kredit = String.valueOf(c16h);
		m_ayliq = String.valueOf(c18);

//		Log.i("Alinan evin qiymeti: ", String.valueOf(c15h));
//		Log.i("Kredit meblegi     : ", String.valueOf(c16h));
//		Log.i("Ilkin odeniw       : ", String.valueOf(c17h));
//		Log.i("Ayliq odeniw       : ", String.valueOf(c18));

	}
	
	

}
