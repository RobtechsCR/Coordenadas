package com.iwop.rastreadormovil;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.xmlpull.v1.XmlPullParserException;



import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;


//import com.bugsense.trace.BugSenseHandler;

public class TestCommands extends Service{
	static Context context;
	static Handler handler;
	static Notification.Builder notibuilder=null;
	private UncaughtExceptionHandler defaultUEH;
	static AlarmManager alarmManager;
	private final String METHOD_NAME = "testCommand";
	static SharedPreferences prefsparametros;
	
	static int bus;
	PreparedStatement ps;
	ResultSet rs;
	static TestCommand testcommands;
	static boolean isTestingCommands=false;
	static Notification noti;
	static Connection conexion;
	static boolean isFirstRunning;
	//static String url="jdbc:mysql://186.177.142.34:3306/SOS?autoReconnect=true";
	static String libreria = "com.mysql.jdbc.Driver";
	static String user="AndroidUser";
	static long idconexion;
	static boolean changeId=false;
	static String pass="iwossqss70107";
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public void onCreate(){
		defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		/*Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
		        new Thread.UncaughtExceptionHandler() {
		            @Override
		            public void uncaughtException(Thread thread, Throwable ex) {
		            	
		                // here I do logging of exception to a db

						LocationService.logger.info(ex.getLocalizedMessage());
		                PendingIntent myActivity = PendingIntent.getService(getBaseContext(),
		                    192837, new Intent(getBaseContext(), TestCommands.class),
		                    PendingIntent.FLAG_ONE_SHOT);

		                
		                alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 
		                    0, myActivity );
		                System.exit(2);

		                // re-throw critical exception further to the os (important)
		                defaultUEH.uncaughtException(thread, ex);
		            }
		        };
		Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);*/
		context=this;
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notibuilder=new Notification.Builder(context);

		prefsparametros =getSharedPreferences("RastreoMovilParametros",Context.MODE_PRIVATE);
        bus=prefsparametros.getInt("Bus", Context.MODE_PRIVATE);
	    noti = notibuilder.setContentTitle("Detector de Comandos "+String.valueOf(bus)).setContentText("Cia de Inversiones La Tapachula").setSmallIcon(R.drawable.ic_launcher).setLargeIcon(null).build();
	    isFirstRunning=true;
	    handler=new Handler();
	    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SOL");
		wl.acquire();
		if (isFirstRunning){
		context=this;
		startForeground(3,noti);
		handler.postDelayed(new Runnable(){

			@Override
			public void run() {
				testCommands();
				
			}
			
		}, 10000);
		
		
		}
		
		
		
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		 super.onStartCommand(intent, flags, startId);	
		 if (isFirstRunning==false){
		defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		/*Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
			        new Thread.UncaughtExceptionHandler() {
			            @Override
			            public void uncaughtException(Thread thread, Throwable ex) {
			            	
			                // here I do logging of exception to a db

							LocationService.logger.info(ex.getLocalizedMessage());
							PendingIntent myActivity = PendingIntent.getService(getBaseContext(),
			                    192837, new Intent(getBaseContext(), TestCommands.class),
			                    PendingIntent.FLAG_ONE_SHOT);

			                
			                alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 
			                    0, myActivity );
			                System.exit(2);

			                // re-throw critical exception further to the os (important)
			                defaultUEH.uncaughtException(thread, ex);
			            }
			        };
		Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
*/
		
		 context=this;
		 PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		 PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SOL");
		 wl.acquire();
		 handler=new Handler();
		 prefsparametros =getSharedPreferences("RastreoMovilParametros",Context.MODE_MULTI_PROCESS);
		 bus=prefsparametros.getInt("Bus", Context.MODE_PRIVATE);
			 NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			 notibuilder=new Notification.Builder(context);
		 noti = notibuilder.setContentTitle("Detector de Comandos "+String.valueOf(bus)).setContentText("Cia de Inversiones La Tapachula").setSmallIcon(R.drawable.ic_launcher).setLargeIcon(null).build();
	     startForeground(3, noti);
	     handler.postDelayed(new Runnable(){

				@Override
				public void run() {
					testCommands();
					
				}
				
			}, 10000);
		
		 }
		 else{
			 isFirstRunning=false;
		 }
		
        return START_STICKY;
	}
	public  void testCommands(){
		
		if (isTestingCommands==false){
				/*handler.post(new Runnable(){

					@Override
					public void run() {
						Toast.makeText(context, "Select Consecutivo, Command from Commands where IdDispositivo='"+prefsparametros.getInt("IdDispositivo", 0)+"' and IsPending='1'", Toast.LENGTH_SHORT).show();
						
					}});*/
				prefsparametros =context.getSharedPreferences("RastreoMovilParametros",Context.MODE_MULTI_PROCESS);
				testcommands=new TestCommands().new TestCommand();
				testcommands.execute(prefsparametros.getInt("IdDispositivo", 0));
				try {
					testcommands.get(15, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					handler.postDelayed(new Runnable(){

						@Override
						public void run() {
							testCommands();
							
						}
						
					}, 60000);
				} catch (ExecutionException e) {
					handler.postDelayed(new Runnable(){

						@Override
						public void run() {
							testCommands();
							
						}
						
					}, 60000);
				} catch (TimeoutException e) {
					handler.postDelayed(new Runnable(){

						@Override
						public void run() {
							testCommands();
							
						}
						
					}, 60000);
				}
				catch (CancellationException e) {
					handler.postDelayed(new Runnable(){

						@Override
						public void run() {
							testCommands();
							
						}
						
					}, 60000);
				}
		
		}
	}
	
public class TestCommand extends AsyncTask<Integer,Integer,Boolean>{
		String msg;
		@Override
		protected Boolean doInBackground(Integer... arg0) {
			return false;
		               
		}
		protected void onPostExecute(Boolean rs) {
			isTestingCommands=false;
			//Toast.makeText(context, msg, 30).show();
			handler.postDelayed(new Runnable(){

				@Override
				public void run() {
					testCommands();
					
				}
				
			}, 60000);
			}

		}
}
