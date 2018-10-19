package com.iwop.rastreadormovil;

import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
//import android.widget.Toast;
//import android.widget.Toast;
//import android.widget.Toast;

public class ModuloSQL {
	static public String url="jdbc:mysql://192.168.43.153:3306/prueba";
	//static public String url="jdbc:mysql://186.176.33.157:3306/prueba";
	static String libreria = "com.mysql.jdbc.Driver";
	static String user="test";
	static String pass="test";
	static Connection conexion;
	static SQLExecute sqlexecute=null;
	static boolean isConnected;
	//static int sentidoold=LocationService.sentido;
	//static int sentidonuevo=LocationService.sentido;
	
	public static boolean testLogin(){
		
		new Thread(){			
		public void run(){
						try {								
						Class.forName(libreria).newInstance();
						//conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://186.176.175.203:1433/SAGO", "sa", "Ciltcr9769");
						conexion=DriverManager.getConnection(url,user,pass);
						//Statement stmt = conexion.createStatement();
						
						if (conexion!=null){
							isConnected=true;
						}
						else {
							isConnected=false;
						}
						
						
						
					} catch (InstantiationException e) {
						
						isConnected=false;

						
					} catch (IllegalAccessException e) {
						
						isConnected=false;
						
					} catch (ClassNotFoundException e) {
						isConnected=false;
						
					} catch (SQLException e) {
						isConnected=false;
					}
					
					
				}}.start();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					
				}
				return isConnected;
				
			
			
			
	}
		

	

	public static void closeConnection(){
		
		if (conexion!=null){
		try {
			if (conexion.isClosed()==false){
				conexion.close();
				conexion=null;
				
			}
			conexion=null;
			
		} catch (SQLException e) {
			conexion=null;
			
		}}
		conexion=null;
		
		
	}


	public static boolean Ejecutar(final String s, boolean settransmision){

		try {
		sqlexecute=new ModuloSQL().new SQLExecute();
		sqlexecute.execute(s);
		return sqlexecute.get(30, TimeUnit.SECONDS);
		
			
		}catch (InterruptedException e) {
			LocationService.isisSendingtoSQL=false;
			sqlexecute.cancel(true);
			
			return false;
		}
		catch (CancellationException  e){
			LocationService.isisSendingtoSQL=false;
			return false;
		}
		catch (ExecutionException e) {
			LocationService.isisSendingtoSQL=false;
			sqlexecute.cancel(true);
			
			return false;
		} catch (TimeoutException e) {
			LocationService.isisSendingtoSQL=false;
			sqlexecute.cancel(true);
			
			return false;
		}
		catch (NullPointerException e) {
			LocationService.isisSendingtoSQL=false;
			sqlexecute.cancel(true);
			return false;
		}
		
					
				
				
				
		}



	
	public class SQLExecute extends AsyncTask<String,Integer,Boolean>{
		String msg;
		
		@Override
		protected Boolean doInBackground(final String... arg0) {
			try{
				if (conexion==null){
					Class.forName(libreria).newInstance();
					//conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://186.176.175.203:1433/SAGO", "sa", "Ciltcr9769");
					conexion=DriverManager.getConnection(url,user,pass);
				}
				if (conexion.isValid(5)==false){
					Class.forName(libreria).newInstance();
					//conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://186.176.175.203:1433/SAGO", "sa", "Ciltcr9769");
					conexion=DriverManager.getConnection(url,user,pass);
				}
				LocationService.handler.post(new Runnable() {
					@Override
					public void run() {
						//Toast.makeText(LocationService.context, arg0[0], Toast.LENGTH_SHORT).show();
					}
				});
				PreparedStatement p=conexion.prepareStatement(arg0[0]);

				return p.executeUpdate()>0;
			               
		
	}
	catch (final NullPointerException e) {
		LocationService.handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(LocationService.context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}
		});
		return false;
	} catch (final IllegalAccessException e) {
				LocationService.handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(LocationService.context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
					}
				});
			} catch (final InstantiationException e) {
				LocationService.handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(LocationService.context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
					}
				});
			} catch (final SQLException e) {
				LocationService.handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(LocationService.context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
					}
				});
			} catch (final ClassNotFoundException e) {
				LocationService.handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(LocationService.context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
					}
				});
			}

		return false;
		}
		@Override
		protected void onPostExecute(Boolean rs) {
			//LocationService.isisSendingtoSQL=false;
			
		}
		@Override
		protected void onCancelled() {
			LocationService.isisSendingtoSQL=false;
			

	    }
		}








}

