package com.iwop.rastreadormovil;

public class DatosEstaticosRastreo {
	private static int sentido=0;
	private static int ramal_encontrado;
	private static int ultimo_ramal_encontrado;
	private static String nombre_ultimo_ramal_encontrado;
	private static int sentido_previo=0;
	private static int ramal_mayor_peso;
	private static String nombre_ramal_mayor_peso;
	private static boolean change_sentido;
	private static long generalentradaspe;
	private static long generalsalidaspe;
	private static long generalentradasps;
	private static long generalsalidasps;
	private static float contador_carreras;
	
	public static void setSentido(int i){
		if (sentido==0||sentido_previo==0){
			getDataFromShared();
		}
		if (i!=sentido_previo){
				change_sentido=true;
				sentido=sentido_previo=i;
				LocationService.editor.putInt("Sentido", sentido).apply();
				LocationService.editor.putInt("SentidoPrevio", sentido_previo).apply();
				LocationService.editor.putBoolean("ChangeSentido", change_sentido).apply();
				setContadoresGenerales();
				if (ultimo_ramal_encontrado==ramal_mayor_peso){
					contador_carreras=contador_carreras+(float)0.5;
					LocationService.editor.putFloat("ContadorCarreras", contador_carreras).apply();
					setContadoresGenerales();
				}
				else{
					contador_carreras=(float)0.5;
					LocationService.editor.putFloat("ContadorCarreras", contador_carreras).apply();
					setContadoresGenerales();
				}
				
			}
			else{
				sentido=i;
				LocationService.editor.putInt("Sentido", sentido).apply();
			}

		
	}
	public static void setNombreRamalMayorPeso(String s){
		nombre_ramal_mayor_peso=s;
		LocationService.editor.putString("Ramal", nombre_ramal_mayor_peso).apply();
	}
	public static String getNombreRamalMayorPeso(){
		return nombre_ramal_mayor_peso;
	}
	public static void setContadoresGenerales(){
		generalentradaspe= DataForSql.generalentradaspe;
		generalentradasps= DataForSql.generalentradasps;
		generalsalidaspe= DataForSql.generalsalidaspe;
		generalsalidasps= DataForSql.generalsalidasps;
		LocationService.editor.putLong("DatosGeneralEntradasPe", generalentradaspe).apply();
		LocationService.editor.putLong("DatosGeneralEntradasPs", generalentradasps).apply();
		LocationService.editor.putLong("DatosGeneralSalidasPe", generalsalidaspe).apply();
		LocationService.editor.putLong("DatosGeneralSalidasPs", generalsalidasps).apply();
		
	}
	public static float getContadorCarreras(){
		return contador_carreras;
	}
	public static void setContadoresSalidas(){
		generalsalidaspe= DataForSql.generalsalidaspe;
		generalsalidasps= DataForSql.generalsalidasps;
		LocationService.editor.putLong("DatosGeneralSalidasPe", generalsalidaspe).apply();
		LocationService.editor.putLong("DatosGeneralSalidasPs", generalsalidasps).apply();
	}
	public static void resetContadorCarreras(){
		contador_carreras=(float)0.5;
		LocationService.editor.putFloat("ContadorCarreras", contador_carreras).apply();
		
	}
	public static long getGeneralSalidas(){
		return generalsalidaspe+generalsalidasps;
	}
	public static long getGeneralEntradas(){
		return generalentradaspe;
	}
	public static long getSumatoriaContadoresGenerales(){
		return generalentradaspe+generalentradasps+generalsalidaspe+generalsalidasps;
	}
	public static int getSentido(){
		return sentido;
	}
	public static int getRamalMayorPeso(){
		return ramal_mayor_peso;
	}
	public static boolean changeSentido(){
		return change_sentido;
	}
	public static void setFalseChangeSentido(){
		change_sentido=false;
		LocationService.editor.putBoolean("ChangeSentido", change_sentido).apply();
	}
	public static int getRamalEncontrado(){
		return ramal_encontrado;
	}
	public static int getUltimoRamalEncontrado(){
		return ultimo_ramal_encontrado;
	}
	public static String getNombreUltimoRamal(){
		return nombre_ultimo_ramal_encontrado;
	}
	
	public static void setRamalEncontrado(int i){
		ramal_encontrado=i;
		LocationService.editor.putInt("RamalEncontradoGPS", ramal_encontrado).apply();
	}
	public static void setUltimoRamalEncontrado(int i, String s){
		ultimo_ramal_encontrado=i;
		nombre_ultimo_ramal_encontrado=s;
		LocationService.editor.putInt("UltimoRamal", ultimo_ramal_encontrado).apply();
		LocationService.editor.putString("UltimoRamalS", nombre_ultimo_ramal_encontrado).apply();
	}
	public static void setRamalMayorPeso(int i){
		ramal_mayor_peso=i;
		LocationService.editor.putInt("RamalInt", ramal_mayor_peso).apply();
	}
	
	public static void getDataFromShared(){
		sentido= LocationService.datosrastreo.getInt("Sentido", 0);
		sentido_previo= LocationService.datosrastreo.getInt("SentidoPrevio", sentido);
		contador_carreras= LocationService.datosrastreo.getFloat("ContadorCarreras", 1);
		ramal_encontrado= LocationService.datosrastreo.getInt("RamalEncontradoGPS", ramal_encontrado);
		ultimo_ramal_encontrado= LocationService.datosrastreo.getInt("UltimoRamal", ramal_encontrado);
		nombre_ultimo_ramal_encontrado= LocationService.datosrastreo.getString("UltimoRamalS", nombre_ultimo_ramal_encontrado);
		nombre_ramal_mayor_peso= LocationService.datosrastreo.getString("Ramal", nombre_ramal_mayor_peso);
		ramal_mayor_peso= LocationService.datosrastreo.getInt("RamalInt", ramal_encontrado);
		change_sentido= LocationService.datosrastreo.getBoolean("ChangeSentido", change_sentido);
		generalentradaspe= LocationService.datosrastreo.getLong("DatosGeneralEntradasPe", DataForSql.generalentradaspe);
		generalentradasps= LocationService.datosrastreo.getLong("DatosGeneralEntradasPs", DataForSql.generalentradasps);
		generalsalidaspe= LocationService.datosrastreo.getLong("DatosGeneralSalidasPe", DataForSql.generalsalidaspe);;
		generalsalidasps= LocationService.datosrastreo.getLong("DatosGeneralSalidasPs", DataForSql.generalsalidasps);;
		
	}
}
