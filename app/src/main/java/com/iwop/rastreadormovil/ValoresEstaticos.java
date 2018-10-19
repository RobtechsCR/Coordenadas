package com.iwop.rastreadormovil;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class ValoresEstaticos {
	//VARIABLES DE CONTROL:

	//public final static String URL = "http://192.168.1.10:8084/WebService/WbSrv?wsdl";
	//public final static String SOAP_ACTION = "\"192.168.1.10:8084/WebService/WbSrv\"";
	static int CANTIDAD_TRANSMISIONES_LUGARES_DETENIDO=15;
	static int ESPERA_ENVIO_SQL=4000; //milisegundos
	static int ESPERA_ENVIO_LOCAL=15000; //milisegundos
	static int Timeout_sendingsql_sendingdiferido=120000; //milisegundos
	static boolean fanon=true;
	static boolean changefanstate=true;
	static int bus;
	static String imei;
	static double latitude=9.849592;
	static double longitude=-83.903532;
	static boolean enableSimulating=false;
	static int ramalencontrado=0;
	static public int pin_entradas_barradelantera=11;
	static public int pin_salidas_barradelantera=12;
	static public int pin_salidas_barrastraseras=13;
	static public int pin_entradas_barrastraseras=14;
	static public int pin_ventilador=7;
	static public int contadorentradaspe=0;
	static public int contadorentradasps=0;
	static public int contadorsalidaspe=0;
	static public int temperatura;
	static public float versionFirmare=(float)31.5;
	static public String action_getdata="com.iwop.rastreadormovil.getdatabarras";
	static public String action_stop_count_barras="";
	private static final String uri =
		    "content://com.iwop.rastreadormovil.android.contentproviders/marcas";
		 
	public static final Uri CONTENT_URI = Uri.parse(uri);
	
public void setBus(int i){
	bus=i;
}
public int getPinEntradasPe(){
	return pin_entradas_barradelantera;
}
public int getPinEntradasPs(){
	/*if (getBus()==345 || getBus()==291){
		return pin_salidas_barrastraseras;
	}
	else{*/
		return pin_entradas_barrastraseras;
	//}
	
}
public int getPinSalidasPe(){
	/*if (getBus()==345 || getBus()==291){
		return pin_entradas_barrastraseras;
	}
	else{*/
		return pin_salidas_barradelantera;
	//}
}
public int getPinVentilador(){
	return pin_ventilador;
}
public int getPinSalidasPs(){
	return pin_salidas_barrastraseras;
}
public int getEntradasPe(){
	return contadorentradaspe;
}
public void setEntradasPe(int i){
	contadorentradaspe=i;
}
public int getEntradasPs(){
	return contadorentradasps;
}
public boolean getVentiladorOn(){
	return fanon;
}
public int getTemperatura(){
	temperatura=DataForSql.temperatura;
	return temperatura;
}

public void setVentiladorOn(boolean b){
	fanon=b;
}
public void setChangeFanState(boolean b){
	changefanstate=b;
}
public boolean getChangeFanState(){
	return changefanstate;
}
public void setEntradasPs(int i){
	contadorentradasps=i;
}

public void setImei(String s){
	imei=s;
}
public static int getBus(){
	if (LocationService.context!=null){
	SharedPreferences prefsparametros =LocationService.context.getSharedPreferences("RastreoMovilParametros",Context.MODE_PRIVATE);
	bus=prefsparametros.getInt("Bus", 0);
	return bus;
	}
	else{
		return 0;
	}
}
public String getImei(){
	return imei;
}
public void setLatitude(double d){
latitude=d;	
}
public void setLongitude(double d){
	longitude=d;	
}
public void enableSimulate(boolean b){
	enableSimulating=b;
}
public boolean getSimulate(){
	return enableSimulating;
}
public void setRamalEncontrado(int i){
	ramalencontrado=i;
}
public static double getLatitude(){
	return latitude;
}
public static double getLongitude(){
	return longitude;
}
public int getRamalEncontrado(){
	return ramalencontrado;
}
public int geMinStop(){
	return 0;
}
}