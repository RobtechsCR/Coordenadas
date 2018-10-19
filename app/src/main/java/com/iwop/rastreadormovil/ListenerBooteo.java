package com.iwop.rastreadormovil;


import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;


@SuppressLint("NewApi")
@TargetApi(11)
public class ListenerBooteo extends BroadcastReceiver {
	static boolean wasBoot=false;

	//static SharedPreferences datosbarras=null;
	//static boolean isConnect=true;
	static SharedPreferences prefs=null;
	static Context cont;
	static SharedPreferences parameters=null;
	static SharedPreferences datosrastreo=null;
	static SharedPreferences.Editor editor=null;
	static CountDownTimer tm;
	@Override
	public void onReceive(final Context context, Intent intent) {
		cont=context;
		//Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG).show();
		if (prefs==null){
			prefs =context.getSharedPreferences("Seguridad", Context.MODE_MULTI_PROCESS);
		}
		if (editor==null){
			editor=prefs.edit();
		}
		if (parameters==null){
			parameters=context.getSharedPreferences("RastreoMovilParametros",Context.MODE_MULTI_PROCESS);
		}
		if (datosrastreo==null){
			datosrastreo=context.getSharedPreferences("DatosRastreo",Context.MODE_MULTI_PROCESS);
		}
		if (intent.getAction().contentEquals("android.intent.action.BOOT_COMPLETED")){
			/*if (android.os.Build.VERSION.SDK_INT<=Build.VERSION_CODES.JELLY_BEAN){
				//Settings.Secure.putInt(cont.getContentResolver(),Settings.Secure.ADB_ENABLED, 0);
				
			}
			else{
				Settings.Global.putInt(cont.getContentResolver(),Settings.Global.ADB_ENABLED, 0);
				
			}
			
			*/
			Intent startServiceIntent = new Intent(context, LocationService.class);

			Intent startServiceTestCommands = new Intent(context, TestCommands.class);
			//Intent startServiceBarrasTraseras = new Intent(context, DetectBarrasTraseras.class);
			context.startService(startServiceIntent);

			context.startService(startServiceTestCommands);
			//context.startService(startServiceBarrasTraseras);
			wasBoot=true;
			LocationService.consultaSaldo();
			return;
		}

		if (intent.getAction().contentEquals("android.intent.action.resetrastreoservice")){
			final Intent startServiceIntent = new Intent(context, LocationService.class);
			context.stopService(startServiceIntent);
			new Handler().postDelayed(new Runnable(){

				@Override
				public void run() {
					cont.startService(startServiceIntent);
				}

			}, 2000);


		}








		return;

		// TODO Auto-generated method stub

	}








}
