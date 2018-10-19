package com.iwop.rastreadormovil;

import java.sql.Timestamp;
import java.text.Normalizer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.widget.Toast;

public class DataForSql {
	static String[] projection=new String[]{"stopcountbarras"};
	static ContentResolver cr;
	static String bateria;
	public static int bus;
	static double lat=0;
	static double lon=0;
	static Timestamp fecha;
	static Timestamp fechaprevia=null;
	static int sentido;
	static String networktype;
	static float contadorcarreras;
	static String datosfirstramal;
	static int marcasactual;
	//static Timestamp fechapreviabat=null;
	static double precision=0;
	static double velocidad=0;
	static int contador_reinicios=0;
	static int contadorentradaspe=0;
	static int contadorsalidaspe=0;
	static int contadorentradasps=0;
	static int contadorsalidasps=0;
	static int generalentradaspe=0;
	static int generalsalidaspe=0;
	static int generalentradasps=0;
	static int codigoconductor=0;
	static int contador_cambiosramal=0;
	static int generalsalidasps=0;
	static float metros_recorridos=0;
	static String imei="";
	static double velocidadexcedida=0;
	static Context context;
	static String lugar;
	static String ramal;
	static int cantidadvips=0;
	static String proveedor="";
	static String paradacercana;
	static int paradacercanaint;
	static int general=0;
	static int temperatura;
	static int contadorbloqueospe;
	static int contadorbloqueosps;
	static int generalbloqueospe;
	static int generalbloqueosps;
	static int ultimoramal;
	Intent batteryIntent;
	static SharedPreferences datosrastreo=null;
	static SharedPreferences.Editor editor=null;
	int level;
	int scale;
	int voltage;
	//static ModuloSQL ModuloSQL=new ModuloSQL();
	public DataForSql(Context c){
		context=c;
	}


public void setLatitud(double i){
	lat=i;
}
public void setLongitud(double i){
	lon=i;
}

public void setPrecision(double i){
	precision=i;
}

public void setProveedor(String s){
	proveedor=s;
}
public void setParadaCercanaInt(int i){
	paradacercanaint=i;
}

public void setKm_recorridos(float i){
	metros_recorridos=i/1000;
}
public void setLugar(String s){
	lugar=s;
}

public void setParadaCercana(String s){
	paradacercana=s;
}

public boolean saveData(){
	if (fechaprevia==null){
		fechaprevia=fecha;
		//setWifiTetheringEnabled(false);
	}
	if (cr==null){
		cr=context.getContentResolver();
	}
	/*if ((contadorentradaspe+contadorentradasps+contadorsalidaspe+contadorsalidasps+generalentradaspe+generalentradasps+generalsalidaspe+generalsalidasps)==0){
		contadorentradaspe=DetectBarrasDelanteras.contadorentradas;
		contadorentradasps=DetectBarrasDelanteras.contadorentradasps;
		contadorsalidaspe=DetectBarrasDelanteras.contadorsalidas;
		contadorsalidasps=DetectBarrasDelanteras.contadorsalidasps;
		generalentradaspe=DetectBarrasDelanteras.generalentradas;
		generalentradasps=DetectBarrasDelanteras.generalentradasps;
		generalsalidaspe=DetectBarrasDelanteras.generalsalidas;
		generalsalidasps=DetectBarrasDelanteras.generalsalidasps;
	}*/
	/*if (prefs==null){
		prefs=context.getSharedPreferences("TarjetasVip", Context.MODE_MULTI_PROCESS);
	}
	contadorentradaspe=prefs.getInt("ContadorEntPe", 0);
	//contadorentradasps=DetectBarrasDelanteras.contadorentradasps;
	contadorsalidaspe=prefs.getInt("ContadorSalPe", 0);
	//contadorsalidasps=DetectBarrasDelanteras.contadorsalidasps;
	generalentradaspe=prefs.getInt("ContadorGeneralEntradas", 0);
	//generalentradasps=DetectBarrasDelanteras.generalentradasps;
	generalsalidaspe=prefs.getInt("ContadorGeneralSalidas", 0);*/
	//generalsalidasps=DetectBarrasDelanteras.generalsalidasps;
	batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    voltage= batteryIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
    temperatura= batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
    /*if (temperatura>500&DetectBarrasDelanteras.isventiladoron==false){
    	Intent i=new Intent();
    	i.setAction("com.iwop.rastreadormovil.fanon");
    	context.sendBroadcast(i);
    	
    }*/
   /* if (temperatura<450&DetectBarrasDelanteras.isventiladoron){
    	Intent i=new Intent();
    	i.setAction("com.iwop.rastreadormovil.fanoff");
    	context.sendBroadcast(i);
    	
    }*/
    if(level == -1 || scale == -1) {
        
    }
    
    bateria= String.valueOf(((float)level / (float)scale) * 100.0f)+" / "+String.valueOf(voltage)+" / "+String.valueOf(temperatura);	   
    
	//if (fecha.getTime()>=fechaprevia.getTime()+4000){
	//	Toast.makeText(context, "SaveInSql", Toast.LENGTH_SHORT).show();
	//SharedPreferences prefs =context.getSharedPreferences("RastreoMovilParametros",Context.MODE_PRIVATE);
	//cantidadvips=prefs.getInt("ContadorVips", 0);
	String sentencia="insert into coordenadas (Latitud, Longitud, km,  Nombre_Parada_SOL, Exactitud, Proveedor, Lugar_Ref_Sol) values" +
			"('"+lat+"','"+lon+"','"+metros_recorridos+"','"+paradacercana+"','"+precision+"','"+proveedor+"','"+lugar+"')";
	sentencia= Normalizer.normalize(sentencia, Normalizer.Form.NFD);
	sentencia = sentencia.replaceAll("[^\\p{ASCII}]", "");
	final String s=sentencia;
	//LocationService.logger.info(sentencia);
	//Toast.makeText(LocationService.getContext(), sentencia, Toast.LENGTH_LONG).show();

	//fechaprevia=fecha;
	//if (ModuloSQL.isConnectionValid()){
	return ModuloSQL.Ejecutar(sentencia, true);
	//}
	//else{
		//ModuloSQL=new ModuloSQL();
		//if (ModuloSQL.isConnectionValid()){
			//return ModuloSQL.Ejecutar(sentencia,true);
		//}
		//else{
			//return false;
		//}
	//}
	
	
	//}
	//return false;
}
/*private void setWifiTetheringEnabled(boolean enable) {
    WifiManager wifiManager = (WifiManager)context.getSystemService(context.WIFI_SERVICE);

    Method[] methods = wifiManager.getClass().getDeclaredMethods();
    for (Method method : methods) {
        if (method.getName().equals("setWifiApEnabled")) {
            try {
                method.invoke(wifiManager, null, enable);
            } catch (Exception ex) {
            }
            break;
        }
    }
}*/
}
