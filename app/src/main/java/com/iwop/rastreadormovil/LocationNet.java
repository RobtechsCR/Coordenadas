package com.iwop.rastreadormovil;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class LocationNet implements LocationListener{
	private static BlockingQueue<Runnable> mDecodeWorkQueue=null;
	
	private static ThreadPoolExecutor scheduler =
		    null;
	static double latitud;
	static boolean isGettingParada=false;
	static double longitud;
	static Runnable threadgetparada;
	static Runnable threadsenttosql;
	static float acu;	
	static boolean sendContadoresParadas=false;
	static Context context;
	static Timestamp fecha=null;
	static Location locationparada=new Location("Parada");
	static boolean isfirst=true;	
	static long fechaprevia=0;	
	static Location previa=null;
	static long fechagpslost=0;	
	static int paradaencontrada=-2;
	static boolean isSafe=false;
	static long startAhorro=0;
	static Calendar c=null;	
	static int sentido=-1;
	static boolean resetVips=false;
	static double[] latitudesparadas1_2={9.935750,9.936127,9.933737,9.932975,9.933763,9.930707,9.927870,9.932642,9.921962,9.919940,9.918197,9.916210,9.913543,9.912045,9.909120,9.905113,9.904997,9.904977,9.904842,9.904325, 9.851583};
	static double[] longitudesparadas1_2={-84.08555333,-84.09090833,-84.09610667,-84.10089667,-84.12856333,-84.132105,-84.13403,-84.135695,-84.13782833,-84.139045,-84.13917167,-84.13921167,-84.13933667,-84.13932,-84.13873833,-84.13837167,-84.13595,-84.134275,-84.13126,-84.12814,-83.902790};
	static double[] latitudesparadas2_1={9.9043,9.904901667,9.905018333,9.905065,9.905923333,9.908558333,9.91197,9.913753333,9.915775,9.916961667,9.917015,9.91899,9.92036,9.920168333,9.925126667,9.927641667,9.930793333,9.931838333,9.933688333,9.932785,9.932883333,9.935096667,9.935868333,9.93575};
	static double[] longitudesparadas2_1={-84.12869194,-84.13112167,-84.13429833,-84.13554667,-84.13853333,-84.13838333,-84.13925,-84.13929667,-84.13926667,-84.13974667,-84.14068833,-84.14090333,-84.14005667,-84.13776,-84.13538333,-84.13405167,-84.13174667,-84.13007,-84.12830333,-84.10174,-84.09608,-84.09085,-84.09236,-84.08555333};
	static String[] nombresparadas1_2={"Parada Final, Calle 16 Avenida 0 y 1","Peana detras del pali del Paseo Colon.","Caseta costado sur parque Maria Auxiliadora","Caseta frente al patinodromo de La Sabana","Frente al Centro Comercial Trejos Montealegre","Junto a rotulo de autoservicio de KFC","Diagonal al Automercado AM","Caseta frente a Tapiceria Vega","Autoservicio J.J.M","Costado Este de la Iglesia de San Miguel de Escazu","Veterinaria el Brujo Cubano","50 metros Norte del Centro de Acabados Maderas Camacho","Entrada al Colegio","Entrada a La Pajarera, 50m Norte de Carnes Rafa","Frente al Centro Educativo San Antonio de Padua","Frente a Marisqueria El Descanso","Frente Delegacion de Fuerza Publica de San Antonio","Frente a Escuela Juan XXIII","Frente a Cantina La Guaria","Parada Terminal, Diagonal a Predio La Tapachula","Casa Esteban"};
	static String[] nombresparadas2_1={"Parada Inicial","Esquina con cerca de malla, costado Este Cantina La Guaria","Costado Sur de Iglesia de San Antonio, junto a gradas","Esquina costado sur de la cancha de basquetball","Caseta junto a telefono publico","Frente a Panalera Tuti, frente a entrada Calle El Man","Frente a Condominios Rocas de la Montana, entrada a La Pajarera","Entrada al Colegio","Junto a tienda Arcari frente a Centro de Acabados Maderas Camacho","Frente a apartamentos porton negro, 75m Oeste de Farmacia Escazu","Frente a distribuidora San Miguel, junto a casa blanco con celestes","Costado Oeste Escuela Republica de Venezuela","Caseta frente a entrada principal Municipalidad de Escazu","Junto a entrada principal El Sol Naciente","Caseta frente a Topografia y Catastro Gustavo Porras","Contiguo al Centro Comercial Escazu, AutoMercado AM, con bahia","Caseta frente a Mc Donalds, San Rafael de Escazu","Caseta frente a Vivero Exotica","Casetas y bahia Centro Comercial Trejos Montealegre","Caseta frente a Controloria General de la Republica","Avenida 6, Calles 36 y 38","Calle 26, Avenidas 4 y 6","Calle 26, Avenida 0 y 2","Parada Final, Calle 16 Avenidas 0 y 1"};
	static Location locationactual;
	
	@Override
	public void onLocationChanged(final android.location.Location arg0) {	
		
		context=LocationService.getContext();
		/*if(LocationService.isMyServiceRunning(DetectBarrasDelanteras.class)==false){
			Intent i=new Intent();
			i.setAction("com.rastreomovil.startservicedetectbarrasdelanteras");
			context.sendBroadcast(i);
		}*/
		LocationService.handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context,"Ubicacion actualizda",Toast.LENGTH_SHORT).show();
			}
		});
		c=Calendar.getInstance();
		if (context!=null){
			if (mDecodeWorkQueue==null){mDecodeWorkQueue = new LinkedBlockingQueue<Runnable>(20);}
			//Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
			//Toast.makeText(context, String.valueOf(batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)), Toast.LENGTH_SHORT).show();
			if (scheduler==null){
					RejectedExecutionHandler block = new RejectedExecutionHandler() {
					  

					@Override
					public void rejectedExecution(Runnable arg0,
							ThreadPoolExecutor arg1) {
							//LocationService.sendSMS("Reject Pool Execution."+ arg0.toString());
							arg1.execute(arg0);
							return;
						 
						
					}
					};
					
				 scheduler = new ThreadPoolExecutor(5,20,100,TimeUnit.MILLISECONDS,mDecodeWorkQueue);
				 scheduler.setRejectedExecutionHandler(block);
				 scheduler.allowCoreThreadTimeOut(true);
			}
		locationactual=arg0;
		if (isfirst){
			fechaprevia=arg0.getTime();
			fechagpslost=arg0.getTime();
			isfirst=false;
		}
		if (LocationService.km==0)
		{
			LocationService.km=LocationService.getMetrosRecorridos();
		}
		if (previa==null)
		{
			previa=new Location("Previa");
			previa=arg0;
		}
		
		fecha=new Timestamp(arg0.getTime());
		LocationService.fecha=fecha;
		if (threadgetparada==null){
			threadgetparada=new Runnable(){
				@Override
				public void run(){
					getParadaCercana();
				}
			};
			
		}
		if (threadgetparada==null){
			threadgetparada=new Runnable(){
				@Override
				public void run(){
					getParadaCercana();
				}
			};
			
		}
		if (threadsenttosql==null){
			threadsenttosql=new Runnable(){
				@Override
				public void run(){
					LocationService.sendToServer(locationactual);
				}
			};
			
		}
		/*if (c.get(Calendar.HOUR_OF_DAY)==23 & c.get(Calendar.MINUTE)==55 & resetVips==false)
		{
			
			resetVips=true;
		}*/
		
		
		if (fechaprevia==0){
			fechaprevia=arg0.getTime();
		}
		/*if (sendContadoresParadas){
			LocationService.sendToServer(arg0);
			fechaprevia=fecha.getTime();
			
		}*/
		//if (arg0.getSpeed()<7&arg0.distanceTo(previa)>2&sendContadoresParadas==false){
		/*if (sendContadoresParadas==false){
			//getParadaCercana();
			if (paradaencontrada!=-1){
				sendContadoresParadas=true;}
				LocationService.editor.putBoolean("SendContadoresParadas", sendContadoresParadas).commit();
			//counter++;
			//}
		}*/
		if (isGettingParada==false){
			
			isGettingParada=true;
			scheduler.remove(threadgetparada);
			scheduler.submit(threadgetparada);
			
		}
		scheduler.execute(threadsenttosql);

		if (fecha.getTime()>=fechagpslost+60000)
			{			
			fechagpslost=fecha.getTime();
			LocationService.selectLocationProvider(5);
			}
		
		//if (LocationService.datosrastreo.getBoolean("isConnect", ListenerBooteo.isConnect)==false){
			
		//}

		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		
		 
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderEnabled(String arg0) {

		// TODO Auto-generated method stub
		
	}
	public void getParadaCercana(){
		isGettingParada=true;
		//Toast.makeText(context, "GET PARADA CERCANA", Toast.LENGTH_SHORT).show();
		float distance2=110;
		int i=0;
		int rango=100;
		if (sentido==1)
			{
				//paradaencontrada=-1;
				//Toast.makeText(context, "GET PARADA CERCANA", Toast.LENGTH_SHORT).show();
				while(i<=latitudesparadas1_2.length-1)
				{
					locationparada.setLatitude(latitudesparadas1_2[i]);
					locationparada.setLongitude(longitudesparadas1_2[i]);
					distance2=locationactual.distanceTo(locationparada);
					//Toast.makeText(context, String.valueOf(distance2), Toast.LENGTH_SHORT).show();
					if ((int)distance2<=rango)
					{
						//Toast.makeText(context, "GET PARADA CERCANA", Toast.LENGTH_SHORT).show();
						if (i!=paradaencontrada)
						{
							rango=(int)distance2;
							paradaencontrada=i;
							LocationService.editor.putInt("ParadaEncontradaInt", paradaencontrada).commit();
							LocationService.editor.putString("ParadaEncontradaS", nombresparadas1_2[paradaencontrada]).commit();
							//Toast.makeText(context, nombresparadas2_1[paradaencontrada], 30).show();
						}
						/*else
						{
							break;
						}*/
				
					}
					
					i++;
				}
			}
			else
			{
				if (sentido==2){
					//paradaencontrada=-1;
				while(i<=latitudesparadas2_1.length-1)
				{
					
					locationparada.setLatitude(latitudesparadas2_1[i]);
					locationparada.setLongitude(longitudesparadas2_1[i]);
					distance2=locationactual.distanceTo(locationparada);
					//Toast.makeText(context, String.valueOf(distance2), 30).show();
					if ((int)distance2<=rango)
					{
						if (i!=paradaencontrada)
						{
							rango=(int)distance2;
							paradaencontrada=i;
							LocationService.editor.putInt("ParadaEncontradaInt", paradaencontrada).commit();
							LocationService.editor.putString("ParadaEncontradaS", nombresparadas2_1[paradaencontrada]).commit();
							//Toast.makeText(context, nombresparadas2_1[paradaencontrada], 30).show();
						}
						/*else
						{
							break;
						}*/
					
					}
					
					i++;
				}
			}}
		//}
		
		isGettingParada=false;
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		
		// TODO Auto-generated method stub
		
	}
	public double[] getCoordinates(){
		double[] coordenadas=new double[2];
		coordenadas[0]=latitud;
		coordenadas[1]=longitud;
		return coordenadas;
	}
	public float getAccuracy(){
		return acu;
	}
	/*public void getParadaCercana(){
		//Toast.makeText(context, "GET PARADA CERCANA", Toast.LENGTH_SHORT).show();
		float distance2=0;
		distance2=(int)50;
		
		if (paradaencontrada!=-1){
		paradaencontrada=-1;
		LocationService.editor.putInt("ParadaEncontradaInt", paradaencontrada).commit();
		LocationService.editor.putString("ParadaEncontradaS", "No es parada").commit();}
		if ((int)distance2>=50)
		{
			
			
			int i=0;
			int rango=50;
			if (sentido==1)
			{
				//Toast.makeText(context, "GET PARADA CERCANA", Toast.LENGTH_SHORT).show();
				while(i<=latitudesparadas1_2.length-1)
				{
					locationparada.setLatitude(latitudesparadas1_2[i]);
					locationparada.setLongitude(longitudesparadas1_2[i]);
					distance2=locationactual.distanceTo(locationparada);
					//Toast.makeText(context, String.valueOf(distance2), Toast.LENGTH_SHORT).show();
					if ((int)distance2<=rango)
					{
						//Toast.makeText(context, "GET PARADA CERCANA", Toast.LENGTH_SHORT).show();
						if (i!=paradaencontrada)
						{
							rango=(int)distance2;
							paradaencontrada=i;
							LocationService.editor.putInt("ParadaEncontradaInt", paradaencontrada).commit();
							LocationService.editor.putString("ParadaEncontradaS", nombresparadas1_2[paradaencontrada]).commit();
							//Toast.makeText(context, nombresparadas2_1[paradaencontrada], 30).show();
						}
						else
						{
							break;
						}
				
					}
					
					i++;
				}
			}
			else
			{
				if (sentido==2){
				while(i<=latitudesparadas2_1.length-1)
				{
					
					locationparada.setLatitude(latitudesparadas2_1[i]);
					locationparada.setLongitude(longitudesparadas2_1[i]);
					distance2=locationactual.distanceTo(locationparada);
					//Toast.makeText(context, String.valueOf(distance2), 30).show();
					if ((int)distance2<=rango)
					{
						if (i!=paradaencontrada)
						{
							rango=(int)distance2;
							paradaencontrada=i;
							LocationService.editor.putInt("ParadaEncontradaInt", paradaencontrada).commit();
							LocationService.editor.putString("ParadaEncontradaS", nombresparadas2_1[paradaencontrada]).commit();
							//Toast.makeText(context, nombresparadas2_1[paradaencontrada], 30).show();
						}
						else
						{
							break;
						}
					
					}
					
					i++;
				}
			}}
		}
		
		
	}*/

	
	
	
}


