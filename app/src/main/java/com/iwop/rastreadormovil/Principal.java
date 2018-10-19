package com.iwop.rastreadormovil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
//import android.widget.Toast;

public class Principal extends Activity {
	
	Button iniciar;
	//TextView numerobus;
	//TextView serie;
	static Context contexto;
	TextView etiquetabus;
	
	//EditText txtbus;
	//TextView busactual;
	//EditText pass;
	//CheckBox enableSimulating;
	//Button io;
	//Button mandarp;
	//Button setRamal;
	//EditText ramal;
	//static int bus=0;
	static double lat;
	static double lon;
	static Timestamp fecha;
	static double precision;
	static double velocidad;
	static int context;
	static String imei;
	static boolean isrunning=false;
	static int counterpass=0;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		//if (connectInternet()){
		prefs =getSharedPreferences("RastreoMovilParametros",Context.MODE_PRIVATE);
		iniciar=(Button)findViewById(R.id.btniniciar);


		
		//DataForSql data=new DataForSql(getApplicationContext());
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		imei=telephonyManager.getDeviceId();
		contexto=this;
		editor=prefs.edit();
		
		//status.setEnabled(false);
		
		//boolean isHack=prefs.getBoolean("", defValue)
		if (prefs.getBoolean("Hack", false)){
			iniciar.setEnabled(false);
		}
		else{
			iniciar.setEnabled(true);
		}
		iniciar.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View arg0) {
				
				
				//SharedPreferences prefs =getSharedPreferences("RastreoMovilParametros",Context.MODE_PRIVATE);
				//SharedPreferences.Editor editor = prefs.edit();
				
				//TestLogin testLogin=new TestLogin();
				//sentencia="select * from MonitoreoMovil_Imei Where Imei='"+imei+"'";
				
				//testLogin.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sentencia);
				//boolean test=false;
				//try {
				//	test = testLogin.get();
				//} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
				//	e1.printStackTrace();
				//} catch (ExecutionException e1) {
					// TODO Auto-generated catch block
				//	e1.printStackTrace();
				//}
				//if (test)
				//{
							//	
							 	LocationService.isFromPrincipal=true;
											
							 	//Toast.makeText(contexto, "start", Toast.LENGTH_SHORT).show();
								if (isrunning==false){
								
								//SharedPreferences prefs =getSharedPreferences("RastreoMovilParametros",Context.MODE_PRIVATE);
								//SharedPreferences.Editor editor = prefs.edit();
								
								//Toast.makeText(contexto, "start", Toast.LENGTH_SHORT).show();
								editor.putString("Imei", imei);
								
								editor.putBoolean("IsAuthentic", true).commit();
								editor.commit();
								
								stopService(new Intent(getApplicationContext(), LocationService.class));
						        Intent i=new Intent(getApplicationContext(),LocationService.class);

						        Intent startServiceTestCommands = new Intent(getApplicationContext(), TestCommands.class);
								//Intent startServiceBarrasTraseras = new Intent(getApplicationContext(), DetectBarrasTraseras.class);
						        startService(i);  				       
						        

								startService(startServiceTestCommands);

									/*try {

										Process p=Runtime.getRuntime().exec("su");
										DataOutputStream d=new DataOutputStream(p.getOutputStream());
										d.writeBytes("reboot \n");
										d.flush();
										d.close();
										p.waitFor();
									}
									catch (IOException e) {
										//logger.info("SetAlarm. Error en reinicio de dispositivo. ContadorAlarmas= " + String.valueOf(contadoralarm));
									} catch (InterruptedException e) {
										//logger.info("SetAlarm. Error en reinicio de dispositivo. ContadorAlarmas= " + String.valueOf(contadoralarm));
									}*/
								//startService(startServiceBarrasTraseras);
						        /*try {
						        	
						        	Process proc = Runtime.getRuntime()
						                    .exec(new String[]{ "su", "-c", "busybox killall system_server"});
						            proc.waitFor();
								} catch (IOException e) {
									return;
								} catch (InterruptedException e) {
									return;
								}*/
						        		}
									//
								};});}
	//}
							
					
				//}.start();
			
				
				
			//}
				
				
		
		
		
		
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.principal, menu);
		return true;
	}
	public boolean connectInternet(){
		 TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		 int data=tm.getDataState();
		 if (data==TelephonyManager.DATA_CONNECTED){return true;}
		 WifiManager wm=(WifiManager)getSystemService(Context.WIFI_SERVICE);
		 int wifi=wm.getWifiState();
		 WifiInfo wminfo=wm.getConnectionInfo();
		 int netid=wminfo.getNetworkId();	 
		 if (wifi==WifiManager.WIFI_STATE_ENABLED & netid!=-1){return true;}
		 else{return false;}
	}
	
		
		
	}
	 
	 
			
	

