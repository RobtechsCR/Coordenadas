package com.iwop.rastreadormovil;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


//import android.widget.Toast;

public class LocationService extends Service {
	//VARIABLES PARA DETECTAR QUE CUALQUIERA DE LAS ASYNCTASK ESTAN EN EJECUCION;
	//static boolean isSearchingRamal=false;
	static boolean isTestingDispositivo=false;
	static Logger logger = Logger.getAnonymousLogger();
	static boolean isSavingToLocal=false;
	static boolean isSetDataOn=false;
	static boolean isSendingDiferido=false;
	static boolean isisSendingtoSQL=false;
	static boolean testlasttransmision=false;
	static boolean resetdatabarras;
	static boolean changeId;
	static FileHandler fh;

	static BaseSQLLocal baselocal_guarda=null;
	static SQLiteDatabase db_guarda=null;
	static BaseSQLLocal baselocal_diferido=null;
	static SQLiteDatabase db_diferido=null;
	static Cursor cursor_lugares=null;
	static String sentencia;
	static boolean setFirstRamal2=false;
	//static long timecommands;
	static int contadorsalidas;
	static int contadorentradas;
	//static int contadoractualprevio=-1;
	//static int contadoractual;
	static long idconexion;
	static boolean setFirstRamal=false;
	static int bus;
	static Handler handler;
	//static boolean isNotBusy=true;
	static Intent getdatabarras=new Intent();
	static AlarmManager alarmManager;
	//static AlarmManager alarmReceteoBarras;
	//static PendingIntent receteobarras;
	static String imei;
	static boolean isFromPrincipal=false;

	//static int sentido;
	//static int sentidoprevio2;
	static LocationManager lmanager;
	static boolean provider_gps;
	static boolean provider_net;
	static boolean GPS_ENABLE=false;

	static Notification.Builder notibuilder=null;
	public static LocationListener listenergps;
	public static LocationListener listenernet;

	private UncaughtExceptionHandler defaultUEH;
	static Notification noti;
	static int contador_cambiosramal;
	//static boolean isAhorro=false;
	static Connection conexion=null;
	static String url="jdbc:mysql://108.61.150.108:3306/SOS?autoReconnect=true";
	static String libreria = "com.mysql.jdbc.Driver";
	static String user="AndroidUser";
	static String pass="iwossqss70107";
	//static int minstop=0;
	static int contadoralarm=0;
	//ramales originales
	//static double[] latitudesramales={9.935436,9.904267,9.920020,9.912733,9.902330,9.909232,9.897018,9.902061,9.909728,9.891262,9.919610,9.932340,9.904544,9.943013,9.950642, 9.931623,9.947913,9.963427,9.965992,9.957927,9.943772,9.912587,9.905637,9.888648,9.915740,9.914292,9.936337, 9.933853};
	//static double[] longitudesramales={-84.085560,-84.128477,-84.140757,-84.149500,-84.140563,-84.132678,-84.141603,-84.157446,-84.127493,-84.127772,-84.126903,-84.085515,-84.127316,-84.152617,-84.163908,-84.169572,-84.177897,-84.189605,-84.187287,-84.204395,-84.224353,-84.179278,-84.182075,-84.171853,-84.195991,-84.241733,-84.222271,-84.181072};
	//TERMINAL-SAN ANTONIO-CENTRO-CORAZON-VISTA ORO-STA TERESA-CARMEN-BEBEDERO-LOTES-CURIO-BELLO-TERMINALSJ-TERMINALSANANTONIO-MULTIPLAZA-GUACHIPELIN-CALLE VIEJA-PISTA-POZOS IMAS-PAVICEN-LINDORA-LOS ANGELES-SALITRAL-MONTOYA-MATINILLA-BARRIO ESPANA-CIUDAD COLON-PREDIO BRASIL-MUSMANNI
	//static double[] latitudesramales={9.935436,9.904267,9.920020,9.912733,9.85172,9.909232,9.897018,9.902061,9.909728,9.891262,9.919610,9.932340,9.904544,9.943013,9.950642, 9.931623,9.947913,9.963427,9.965992,9.957927,9.943772,9.912587,9.905637,9.888648,9.915740,9.914292,9.936337, 9.933853};
	//static double[] longitudesramales={-84.085560,-84.128477,-84.140757,-84.149500,-83.9029,-84.132678,-84.141603,-84.157446,-84.127493,-84.127772,-84.126903,-84.085515,-84.127316,-84.152617,-84.163908,-84.169572,-84.177897,-84.189605,-84.187287,-84.204395,-84.224353,-84.179278,-84.182075,-84.171853,-84.195991,-84.241733,-84.222271,-84.181072};
	//static int ramalencontrado=30;
	//static double latituddetectaramal;
	//static double longituddetectaramal;
	//static boolean setTimeCarrera=false;
	//static Location detectoRamal=new Location("DetectoRamal");
	//static float distanciadetectoramal=0;
	//static Location locationramal=new Location("Ramal");
	//static boolean isInRamal=true;
	//static boolean sendData=true;
	//static boolean setSentido=true;
	static int counterpredio=0;
	static CountDownTimer contador=null;
	static boolean isFirstRunning=true;

	static SQLiteDatabase db=null;
	static BaseLugaresLocal helperlugares;
	static SQLiteDatabase lugares;
	static Cursor cursor=null;
	static float meters=2000;
	static float distancia=3000;
	static DataForSql data=null;
	static boolean stopcountbarras=false;
	//static TestCommands testcommands=null;
	//static BusquedaRamal busqueda=null;
	static SharedPreferences prefsparametros=null;
	static SharedPreferences datosrastreo=null;
	//static Location location;

	private static int contadorsql=0; 
	static ConnectivityManager cm;
	static NetworkInfo net;
	//static Thread getprov=null;
	//static Location localprevia=null;
	static int contadorlugares=0;
	static String lugarprevio=null;
	static long timesendsql=0;
	//static boolean isConnect=true;
	//static int contadorgeneralentradasprevio;
	//static int contadorgeneralsalidasprevio;
	static boolean IsInTerminal;
	//static boolean setcontadorgeneralsalidasprevio;
	//static float contadorcarreras;
	static long ultimatransmision=0;
	//static boolean isEmpty=true;
	static Timestamp fechadif=null;
	static float km=0;
	//static float metrossql=0;
	static float difkm=0;
	static float kmprevio=0;
	//static float kmprevio2=0;
	static String updateramal;
	static boolean updateRamal=false;
	static float difkm2=0;
	public static SharedPreferences.Editor editor=null;
	static boolean enPredioSanAntonio=false;
	static boolean enPredioBrasil=false;
	static Location predioSanAntonio=null;
	static Location predioBrasil=null;
	static boolean sendLugar=true;
	static boolean sendSeguro=false;
	static boolean lugarseguro=false;
	static int sentidoprevio=0;
	static final String [] setAirplaneOn=new String[]{"settings put global airplane_mode_on" + " " + 1, "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state" + " " + 1};
	static final String [] setAirplaneOff=new String[]{"settings put global airplane_mode_on" + " " + 0, "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state" + " " + 0};
	static long tiempocarrera=0;
	static boolean sendDatosramal=true;



	private static BlockingQueue<Runnable> mDecodeWorkQueue=null;

	public static ThreadPoolExecutor scheduler =
			null;
	static int counter=0;
	static Location locationParadaEncontrada=null;
	static boolean isGettingParada=false;
	static boolean isGettingRamal=false;
	static Location testPointescazu1;
	static Location testPointescazu2;
	static Location testPointescazu3;
	static Location testPointsa1;
	static Location testPointsa2;
	static Location testPointsa3;
	static boolean isSetBarrasOff=false;
	static int max_speed=100;
	static int paradaencontrada=-2;
	static Location locationactual;
	static boolean addlistener=false;
	static Timestamp fecha=null;
	static long fechaprevia=0;
	static long fechaprevia3=0;
	static long fechaprevia6=0;
	static double latitudprevia=0;
	static double longitudprevia=0;
	static double mLastLocationMills=0;
	static Location mLastLocation=null;
	static boolean isGPSfix=false;
	static Context context=null;
	//static boolean isAhorro=false;
	static boolean isNetSelected=false;
	static long activethread=0;
	static Calendar c=null;
	static Location location_multiplaza_testpoint;
	static int cantidadsatelites=0;
	static String lugar="Desconocido";

	static float metrosr=0;
	static boolean resetVips=false;
	//static int sentido=-1;
	//static boolean setSentido=false;
	static long fechaprevia2=0;
	static long fechaprevia4=0;
	static long fechaprevia5=0;
	static Location previa=null;
	static float distance=0;
	static int segstop=0;
	static Location locationramal=new Location("Ramal");
	static Location locationparada=new Location("Parada");
	static boolean calculatekm=true;
	static boolean isInRamal=true;
	static boolean sendData=true;
	//static int ramalencontrado=30;
	static long fechapreviadetectoramal=0;
	static boolean setTimeCarrera;
	static boolean sendContadoresParadas=false;
	static double latituddetectaramal;
	static double longituddetectaramal;
	//static Location detectoRamal=new Location("DetectoRamal");
	static float distanciadetectoramal=0;
	static double[] latitudesparadas1_2={9.935750, 9.936127,9.933737, 9.932975, 9.933763, 9.930707, 9.927870, 9.932642, 9.921962, 9.919940, 9.918197, 9.916210, 9.913543, 9.912045, 9.909120, 9.905113, 9.904997, 9.904977, 9.904842, 9.904325, 9.932292, 9.929162, 9.929698, 9.930373, 9.930953, 9.930715, 9.930422, 9.928858, 9.927560, 9.925652, 9.923532, 9.921077, 9.920373, 9.918510, 9.917022, 9.915568, 9.915313, 9.914487, 9.913597, 9.912972, 9.913497, 9.913492, 9.913382, 9.913163, 9.912455, 9.912482, 9.910957, 9.909818, 9.935750, 9.932407, 9.932650, 9.932815, 9.933132, 9.933362, 9.933602, 9.933465, 9.933373, 9.931117, 9.934982, 9.932390, 9.932222, 9.922097, 9.920653, 9.919608, 9.920452, 9.921053, 9.923455, 9.925387, 9.927797, 9.932222, 9.936170, 9.938930, 9.940062, 9.942650, 9.944155, 9.945822, 9.948653, 9.951255, 9.954505, 9.956147, 9.958962, 9.962053, 9.963293, 9.964500, 9.926327, 9.923470, 9.922133, 9.917022, 9.90035, 9.908713, 9.908090, 9.908287, 9.905527, 9.903682, 9.901755, 9.900083, 9.898175, 9.89645, 9.896020, 9.895852, 9.897312, 9.898880, 9.90036, 9.901960, 9.903683, 9.902910, 9.901742, 9.900465, 9.898810, 9.897198, 9.896603, 9.896603, 9.896890, 9.895248, 9.893467, 9.892152, 9.936127, 9.932362, 9.965313, 9.966383, 9.966383, 9.967060, 9.919798, 9.920168, 9.927642, 9.932883, 9.932292, 9.915272, 9.912693, 9.911255, 9.910570, 9.093005, 9.904747, 9.903132, 9.901972, 9.901036, 9.899925, 9.898515, 9.896550, 9.898328, 9.918795, 9.917437, 9.917420, 9.917308, 9.916730, 9.916355, 9.917680, 9.919033, 9.921123,  9.921123, 9.919538, 9.920340, 9.920117, 9.918913, 9.917530, 9.91730 , 9.91533, 9.914000, 9.912077, 9.911717, 9.911760, 9.911192, 9.910643, 9.909848, 9.851583,9.934019,9.933382,9.931921,9.931341,9.931106,9.930937,9.931499,9.933363,9.933933,9.934265,9.935922,9.936644,9.938850,9.940311,9.942843,9.944669,9.946526,9.947655,9.948742,9.952369,9.949684,9.955023,9.957325,9.959666,9.960805,9.962081,9.963075,9.934723,9.937423,9.942262,9.939551,9.949469,9.951611,9.954183,9.955896,9.957803,9.958107,9.958035,9.958053,9.958049,9.958040,9.933624,9.933558,9.933514,9.933528,9.932995,9.932164,9.929557,9.927655,9.927671,9.928726,9.928023,9.928845,9.928876,9.930248,9.929245,9.931942,9.933858,9.932371,9.929817,9.925722,9.924728,9.923257,9.921126,9.919964,9.918934,9.917214,9.929849,9.928274,9.925687,9.924752,9.923708,9.922360,9.920621,9.919251,9.917963,9.916653,9.916068,9.916260,9.936244,9.937262,9.938614,9.941264,9.942924,9.943292,9.928114,9.928921,9.929864,9.928438,9.927075,9.925959,9.925762,9.926345,9.925357,9.925522,9.925827,9.923840,9.919909,9.922424,9.924603,9.927932,9.929929,9.930490,9.930718,9.931067,9.931947,9.933679,9.934256,9.934610,9.961528,9.961721,9.943979,9.943761,9.938529,9.940575,9.941645,9.942730,9.944450,9.945550,9.948024,9.946688,9.946482,9.945909,9.945831,9.932775,9.933016,9.933575,9.934084,9.934734,9.934344,9.933850,9.932752,9.931849,9.928674,9.928805,9.930010,9.930708,9.933086,9.931975,9.930821,9.928427,9.927527,9.926619,9.925576,9.924603,9.923495,9.922644,9.921078,9.920882,9.920860,9.908065,9.930080,9.929457,9.928101,9.924517,9.923014,9.920981,9.919382,9.917970,9.917077,9.915279,9.914099,9.911947,9.910427,9.909379,9.907665,9.906651,9.905163,9.929685,9.928755,9.928178,9.926314,9.925523,9.924636,9.922645,9.920162,9.919176,9.918560,9.917638,9.902575,9.900333,9.898967,9.898148,9.895396,9.893833,9.891816,9.890548,9.889650,9.889288,9.889644,9.889249,9.888552,9.906880,9.906354,9.905617,9.905662,9.915554,9.914931,9.914487,9.913695,9.912913,9.913570,9.912565,9.911468};
	static double[] longitudesparadas1_2={-84.08555333, -84.09090833, -84.09610667, -84.10089667, -84.12856333, -84.132105, -84.13403, -84.135695, -84.13782833, -84.139045, -84.13917167, -84.13921167, -84.13933667, -84.13932, -84.13873833, -84.13837167, -84.13595, -84.134275, -84.13126, -84.12814, 84.08544, -84.13434667, -84.137735, -84.14186333, -84.14599167, -84.14942833, -84.15063833, -84.14936167, -84.14716833, -84.14570167, -84.14523333, -84.14514167, -84.143365, -84.14337667, -84.14351167, -84.14434, -84.14511, -84.14755333, -84.14838167, -84.149295, -84.14954167, -84.15045167, -84.15120333, -84.15318667, -84.15401167, -84.15586667, -84.15819, -84.1568, -84.08555333, -84.09923, -84.10177167, -84.10354333, -84.10665833, -84.10887833, -84.11101, -84.114205, -84.11867833, -84.12070833, -84.12053167, -84.12512944, -84.12786, -84.14059167, -84.14165833, -84.14201167, -84.14330833, -84.14510667, -84.14518167, -84.147455, -84.147455, -84.15001, -84.15176333, -84.153615, -84.15480833, -84.15743333, -84.15927, -84.16056333, -84.16238333, -84.164075, -84.16698833, -84.168675, -84.1711, -84.17382667, -84.17563167, -84.17967833, -84.14046667, -84.14075333, -84.14138667, -84.14351167, -84.14351167, -84.14135833, -84.14220167, -84.14100167, -84.140285, -84.14032833,-84.14082833, -84.140765 , -84.1417, -84.14363, -84.14497667, -84.14866333, -84.15135833, -84.15273833,-84.15451667,  84.15746333, -84.13457833, -84.13533333, -84.13464833, -84.13353, -84.13310667, -84.13207333, -84.12989333, -84.12989333, -84.12895333, -84.12830333, -84.12792, -84.12782167, -84.09090833, -84.15001025, -84.18283667, -84.18729167, -84.18729167, -84.18981833, -84.14079333, -84.13776, -84.13405167, -84.09608, -84.08544, -84.13708333, -84.135375, -84.136305, -84.13692333, -84.13805833, -84.138845, -84.13882167, -84.13891667, -84.13857167, -84.13854333, -84.30744556, -84.14041833, -84.142355, -84.13741833, -84.13677167, -84.13585, -84.13399167, -84.13354833, -84.13169, -84.130675, -84.12988, -84.12771667,-84.12771667,-84.12687333, -84.14024167, -84.13529167, -84.13431, -84.13375167, -84.13399167, -84.13336333, -84.13364833, -84.13359, -84.13192167, -84.131, -84.129885, -84.12902833, -84.12759333,-83.902790,-84.185799,-84.188257,-84.186728,-84.183984,-84.181472,-84.179629,-84.178352,-84.178173,-84.182971,-84.183441,-84.183697,-84.183918,-84.184302,-84.184680,-84.184847,-84.186437,-84.187637,-84.188456,-84.188838,-84.189616,-84.189180,-84.190155,-84.190495,-84.190740,-84.190908,-84.188745,-84.188498,-84.188574,-84.188624,-84.189694,-84.188927,-84.192663,-84.193508,-84.194515,-84.195179,-84.195901,-84.201683,-84.202625,-84.203473,-84.204341,-84.203888,-84.190291,-84.191480,-84.194162,-84.196153,-84.200329,-84.201867,-84.204981,-84.208256,-84.209981,-84.213392,-84.212276,-84.217052,-84.215225,-84.219319,-84.218714,-84.219607,-84.220139,-84.200425,-84.200788,-84.199018,-84.198687,-84.198193,-84.196951,-84.196612,-84.196433,-84.196581,-84.203883,-84.202771,-84.201091,-84.200540,-84.200010,-84.199161,-84.198695,-84.198393,-84.198129,-84.197740,-84.197424,-84.196093,-84.220371,-84.220459,-84.220544,-84.222769,-84.223646,-84.224134,-84.218974,-84.220495,-84.222550,-84.226336,-84.227942,-84.229018,-84.230616,-84.231626,-84.232192,-84.234039,-84.235906,-84.240312,-84.241351,-84.240903,-84.240336,-84.240393,-84.240440,-84.239418,-84.236304,-84.232724,-84.231438,-84.229742,-84.226116,-84.222967,-84.193121,-84.195015,-84.193089,-84.191877,-84.135672,-84.143331,-84.144971,-84.146481,-84.148897,-84.152619,-84.159230,-84.173632,-84.182095,-84.187611,-84.191176,-84.103534,-84.106152,-84.110723,-84.154230,-84.157704,-84.163763,-84.165363,-84.167410,-84.171616,-84.171621,-84.173537,-84.174575,-84.176756,-84.190624,-84.190323,-84.189762,-84.188700,-84.187920,-84.187402,-84.186265,-84.185357,-84.184866,-84.184477,-84.183250,-84.181874,-84.180566,-84.178782,-84.180098,-84.180461,-84.180556,-84.180082,-84.180223,-84.180472,-84.180533,-84.179742,-84.179339,-84.179562,-84.179649,-84.179094,-84.178412,-84.178181,-84.177934,-84.177678,-84.177246,-84.175536,-84.175693,-84.176113,-84.176770,-84.177073,-84.177621,-84.178315,-84.178829,-84.178852,-84.178904,-84.179162,-84.176236,-84.175024,-84.174829,-84.174639,-84.173603,-84.174066,-84.174573,-84.172851,-84.172908,-84.172797,-84.172529,-84.172116,-84.171981,-84.179237,-84.180015,-84.180229,-84.180941,-84.144392,-84.145606,-84.147622,-84.148312,-84.149462,-84.150521,-84.155677,-84.156880};
	static double[] latitudesparadas2_1={9.909818333, 9.910865, 9.912436667, 9.912318333, 9.913178333, 9.913453333, 9.913453333, 9.913579167, 9.912765, 9.913635, 9.914545, 9.914805, 9.915416667, 9.916756667, 9.918698333, 9.920451667, 9.921053333, 9.923455, 9.925386667,9.927796667, 9.929041667 , 9.930723333, 9.930791667, 9.930346667, 9.929953333, 9.92903, 9.93071, 9.931756667, 9.933688333, 9.932785, 9.932883333, 9.932291667, 9.9645, 9.963408333, 9.962222222, 9.958948333, 9.956416667, 9.953888889, 9.951071667, 9.931666667, 9.948361667, 9.944373333 , 9.942476667, 9.423485, 9.938235, 9.936743333, 9.93272, 9.928858333, 9.92756, 9.925651667, 9.923531667, 9.921076667, 9.920571667, 9.92034, 9.920183333, 9.925126667, 9.927641667, 9.930793333, 9.931838333, 9.932323333, 9.932345, 9.934301667, 9.9333, 9.931161667, 9.9333, 9.933326667, 9.916675, 9.933048333, 9.932915, 9.932558333, 9.932201667, 9.932921667, 9.935096667, 9.935868333, 9.93575, 9.966715, 9.966248333, 9.96685166, 9.968166667, 9.9043, 9.904901667, 9.905018333, 9.905065, 9.905923333, 9.908558333, 9.91197, 9.913753333, 9.915555556, 9.916961667, 9.917015, 9.91899, 9.92036, 9.898328333, 9.898166667, 9.89999, 9.901661667, 9.902163333, 9.903523333, 9.909936667, 9.910501667, 9.911408333, 9.919538333, 9.921118333, 9.920995, 9.919181667, 9.916908333, 9.916533333, 9.917222222, 9.917508333, 9.917551667, 9.909066667, 9.91142, 9.9129, 9.914456667, 9.916253333, 9.919425, 9.918666667, 9.909848333, 9.910678333, 9.911261667, 9.91185, 9.912605, 9.912343333, 9.915196667, 9.92034, 9.93575, 9.946508,9.963031,9.962131,9.960744,9.959778,9.957279,9.955114,9.952347,9.951199,9.949672,9.948877,9.946227,9.922936,9.944590,9.942452,9.940275,9.938787,9.936633,9.935831,9.934125,9.934019,9.933382,9.931921,9.931341,9.931106,9.930937,9.931499,9.933363,9.933933,9.957882,9.957910,9.957931,9.957964,9.957958,9.955849,9.954120,9.951736,9.950601,9.949658,9.949161,9.945416,9.942219,9.939502,9.937297,9.934850,9.917227,9.918920,9.919973,9.921147,9.924769,9.925743,9.929933,9.932513,9.930243,9.933841,9.931915,9.928949,9.928718,9.928773,9.928480,9.927897,9.927526,9.927522,9.929520,9.932045,9.932644,9.933424,9.933460,9.933454,9.933459,9.916322,9.916135,9.916686,9.918003,9.919270,9.920671,9.922458,9.923739,9.924756,9.925691,9.928269,9.929885,9.935305,9.934690,9.934297,9.933095,9.932091,9.931064,9.930778,9.930601,9.929257,9.927727,9.924839,9.922236,9.918338,9.916795,9.914310,9.911814,9.910473,9.911938,9.914549,9.916354,9.917964,9.921512,9.923892,9.925821,9.925402,9.925469,9.926337,9.925567,9.925996,9.927033,9.928487,9.929949,9.928916,9.927812,9.942660,9.943306,9.942669,9.938986,9.937090,9.937939,9.936064,9.935635,9.935787,9.960330,9.961557,9.961454,9.944463,9.944062,9.948072,9.945938,9.947235,9.946625,9.943970,9.941771,9.940244,9.937128,9.935645,9.930657,9.929684,9.928498,9.928578,9.932611,9.933613,9.934995,9.934661,9.934019,9.930728,9.930696,9.930283,9.930252,9.929802,9.888646,9.889322,9.889587,9.889383,9.889737,9.890636,9.891987,9.893940,9.895459,9.898259,9.899029,9.900495,9.902697,9.905336,9.906750,9.907763,9.909506,9.910546,9.912348,9.913814,9.915040,9.917335,9.917903,9.919202,9.920569,9.922830,9.924281,9.928630,9.918262,9.919156,9.920318,9.922653,9.924485,9.925255,9.926567,9.927846,9.929118,9.930207,9.905595,9.905846,9.906542,9.907150,9.908261,9.920923,9.921173,9.922708,9.923552,9.924715,9.925684,9.926734,9.927555,9.928332,9.931007,9.932141,9.933188,9.910862,9.912495,9.913503,9.912795,9.913595,9.914538,9.914824,9.915429,9.916340,9.913234};
	static double[] longitudesparadas2_1={-84.1568, -84.15645667, -84.15583667, -84.15434667, -84.15306667, -84.150465,  -84.15046, -84.14969167, -84.14941667, -84.14821167, -84.14747333, -84.16099333 , 84.14432667, -84.14338167, -84.14342, -84.14330833, -84.14510667, -84.14518167, -84.14545333, -84.147455, -84.14963333, -84.15009667, -84.146055, -84.14146167, -84.13834, -84.13514167,-84.131845, -84.13015833, -84.12830333, -84.10174, -84.09608, -84.08544, -84.17967833, -84.17571667, -84.173935, -84.171305, -84.16877, -84.16683667, -84.16419833,-84.16243, -84.16036667, -84.15959, -84.15724667, -84.154875, -84.15305833, -84.15208667, -84.15013833, -84.14936167, -84.14716833, -84.14570167, -84.14523333, -84.14514167, -84.14288833, -84.14024167, -84.13776333, -84.13538333, -84.13405167, -84.13174667, -84.13007, -84.12824833, -84.12501167, -84.12110833, -84.119325, -84.12051167, -84.119325, -84.114545, -84.11051167, -84.10669667, -84.10506, -84.10190944, -84.09860167, -84.09592, -84.09085, -84.09236, -84.08555333, -84.18606444, -84.187405, -84.18610167, -84.18400833, -84.12869194, -84.13112167, -84.13429833, -84.13554667, -84.13853333, -84.13838333, -84.13925, -84.13929667, -84.13926667, -84.13974667, -84.14068833, -84.14090333, -84.14005667, -84.142355, -84.14176333, -84.14077833, -84.140755, -84.13951167, -84.13881, -84.13775833, -84.13721444, -84.13597667, -84.12687333, -84.12766667, -84.12879167, -84.129765, -84.13133889, -84.13329167, -84.13453333, -84.13565167, -84.13712667, -84.13266833, -84.133425, -84.13379611, -84.13334333, -84.13331667, -84.13896722, -84.14060333, -84.12759333, -84.12903167, -84.12981833, -84.13089722, -84.13423167,-84.13531167, -84.13697833, -84.14024167, -84.08555333, -84.183566,-84.188572,-84.188784,-84.191047,-84.190888,-84.190605,-84.190300,-84.189766,-84.189559,-84.189317,-84.189034,-84.187502,-84.197813,-84.186566,-84.184862,-84.184803,-84.184387,-84.184043,-84.183574,-84.183562,-84.185799,-84.188257,-84.186728,-84.183984,-84.181472,-84.179629,-84.178352,-84.178173,-84.182971,-84.204173,-84.203456,-84.202566,-84.201675,-84.196306,-84.195306,-84.194637,-84.193739,-84.193308,-84.192966,-84.192759,-84.191183,-84.189803,-84.189022,-84.188720,-84.188728,-84.196511,-84.196333,-84.196498,-84.196837,-84.198593,-84.198952,-84.200634,-84.200370,-84.219454,-84.220191,-84.219726,-84.218623,-84.217061,-84.215370,-84.213309,-84.212283,-84.209970,-84.208209,-84.204842,-84.201807,-84.200702,-84.196095,-84.194336,-84.191470,-84.190716,-84.196012,-84.197368,-84.197687,-84.198044,-84.198304,-84.198603,-84.199116,-84.199901,-84.200462,-84.201002,-84.202698,-84.203804,-84.221545,-84.224210,-84.227598,-84.230338,-84.231527,-84.233659,-84.237327,-84.239911,-84.240756,-84.240550,-84.240454,-84.241216,-84.241702,-84.241860,-84.241637,-84.241303,-84.240551,-84.240397,-84.240586,-84.240843,-84.240975,-84.241279,-84.240155,-84.235817,-84.233745,-84.232025,-84.231463,-84.230446,-84.228816,-84.227721,-84.226168,-84.222861,-84.220716,-84.218857,-84.224390,-84.225539,-84.227774,-84.227136,-84.227512,-84.227501,-84.226626,-84.224585,-84.222969,-84.196830,-84.194943,-84.193034,-84.193463,-84.191376,-84.177262,-84.186933,-84.161238,-84.152667,-84.148690,-84.145564,-84.143218,-84.135218,-84.128145,-84.177223,-84.174381,-84.173627,-84.171480,-84.167505,-84.165518,-84.162861,-84.157994,-84.154479,-84.150210,-84.146134,-84.141267,-84.139890,-84.138217,-84.171906,-84.171995,-84.172587,-84.172757,-84.172886,-84.172754,-84.174611,-84.173971,-84.173461,-84.174583,-84.174716,-84.175005,-84.176132,-84.177209,-84.177620,-84.177851,-84.178112,-84.178361,-84.179139,-84.179510,-84.179429,-84.179173,-84.179542,-84.180450,-84.180436,-84.180092,-84.179963,-84.180386,-84.178781,-84.178670,-84.178705,-84.178168,-84.177626,-84.177037,-84.176586,-84.176182,-84.175503,-84.175453,-84.180912,-84.180058,-84.179695,-84.178453,-84.178746,-84.181913,-84.183315,-84.184429,-84.184767,-84.185338,-84.186263,-84.187380,-84.187827,-84.188503,-84.189725,-84.190281,-84.190524,-84.156125,-84.155697,-84.150428,-84.149493,-84.148300,-84.147445,-84.145677,-84.144295,-84.143379,-84.151873};
	static String[] nombresparadas1_2={"Parada Final Calle 16 Avenida 0 y 1", "Peana detras del Pali del Paseo Colon", "Caseta costado sur parque Maria Auxiliadora", "Caseta frente al patinodromo de La Sabana", "Frente al Centro Comercial Trejos Montealegre", "Junto a rotulo de autoservicio de KFC", "Diagonal al Automercado AM", "Caseta frente a Tapiceria Vega", "Autoservicio JJM", "Costado Este de la Iglesia de San Miguel de Escazu", "Veterinaria el Brujo Cubano", "50 metros Norte del Centro de Acabados Maderas Camacho", "Entrada al Colegio", "Entrada a La Pajarera 50m Norte de Carnes Rafa", "Frente al Centro Educativo San Antonio de Padua", "Frente a Marisqueria El Descanso", "Frente Delegacion de Fuerza Publica de San Antonio", "Frente a Escuela Juan XXIII", "Frente a Cantina La Guaria", "Parada Terminal Diagonal a Predio La Tapachula", "Terminal 30 mts oeste de caseta Parque Solon Nunez Frutos", "Frente entrada principal Mas x Menos", "Caseta junto a Outlet de Tienda Extremos", "Bahia centro comercial plaza country", "Caseta condominio el cortijo", "Caseta a centro comercial guachipelin", "Costado oeste de multicentro Paco", "Junto a fuente de Condominio los prados", "Condomio condado del Country", "Caseta con publicidad de Hitachi diagonal a super herrera", "Frente a clinica veterinaria 24 horas", "Junto a entrada principal bar Snoopy", "25 mts al sur de la violeta", "Junto a casas gemelas color crema con cafe", "50 mts antes Carnes el Country frente al Colegio", "Caseta de Madera Rustica debajo de higueron", "Junto al telefono publico rotulo Barrio los Cuetes", "Contiguo a la escuela corazon de Jesus", "Porton verde frente a Iglesia junto a abastecedor", "20 mts este del puente Parada Final Corazon de Jesus", "Poste CNFL 25073", "Poste CNFL 25069", "Frente a Rotulo de Iglesia biblica bautista", "Junto a propiedad con cerca de malla plateada", "Junto a poste CNFL 25113", "Frente con tapia color terracota con palmera alta", "Junto a villa Carrizal", "Parada Final frente a Casa de Madera Piedra grande", "Parada Final Calle 16 Avenida 1 y 3", "Caseta frente a grupo Comunica", "Caseta frente a Contraloria General de la Republica", "Caseta frente a AMPM la sabana","Caseta frente a antigua Aresep", "Caseta en bahia frente al edificio de Apartamentos donde esta Amnet", "Peana frente al Cafe Entrepans la sabana", "Junto a telefono publico de la entrada principal de Ucimed", "Caseta frente al Laboratorio de Fitoproteccion del MAC", "Entra a bajo los Anonos, contiguo a Villa Anonos", "Caseta frente a Euromotor junto a puente que lleva a pavas", "Frente a Caseta de Distribuidora Santa Barbara Bello Horizonte", "Centro Comercial los Anonos junto a Antiguo Hitachi", "Junto a pollo asado Parri pollo", "25 mts norte del AyA san miguel de escazu", "Junto a tapia de block expuesto porton negro techo tejas", "Frente al costado este de la violeta", "Contiguo a la asociacion de bienestar social de escazu", "Junto clinica veterinaria 24 horas", "Junto a super herrera", "Esquina suroeste de condominio condados del Country", "Pulperia el Llano", "Junto a AMPM", "Frente al Laboratorio Saenz Renauld", "Junto a plaza mundo", "Caseta tipo parabus 21499 frente a tapia de cerro alto", "Junto a Gimnasio de la Escuela Guachipelin", "Junto a taller mecanico contiguo a importadora GP", "Bahia y caseta con publicidad West College frente a bodegas el almendro", "Junto a casa de familia Soto Jimenez frente al Saint Mary School", "Junto a club cubano jose marti", "Junto a tapia cafe claro con rejas negras y saran rotulo de tajo", "Frente a hacienda el Cocoi","Esquina junto a la entrada principal de Residencial Pinar del Rio", "Frente a casa de dos pisos con muro beige alero de tejas", "Junto a telefono publico condominio la Reserva Parada Final Guachipeli", "Frente a Condomio la Molina","Junto al poste, frente al super san Martin", "Frente a rotulo de Cruz Roja", "Carnes el Country, 50 mts antes frente al colegio", "25 mts del minisuper Minor", "Frente a entrada principal del Condominio Riveras del Monte", "Frente a Entrada de Calle a Lotes Peru", "25 mts despues del poste CNFL 11312a6", "Poste CNFL 26215", "Junto al poste con rotulo de mirador tiquicia frente a tanque del AyA", "Poste CNFL 26237", "Trapiche viejo", "15 mts al sur de super la florecilla frente al rotulo de mirador tiquicia", "Junto a casa amarilla con cerca de poste de concreto frente a caseta de parada","Junto a telefono publico de la iglesia nuestra senora del Carmen", "Junto a embajada de Uruguay", "20 mts antes del poste CNFL 26150", "Junto a cerca de bambu contiguo a dos propiedades con portones de malla", "25 mts despues de la entrada a Calle Mirador Tiquicia", "Parada final con caseta costado norte de la iglesia de bebedero", "Frente a entrada principal de condominio la vita", "Junto a casa blanca porton negro frente a porton azul", "25 mts antes poste CNFL 26522", "Junto a malla electrosoldada azul casa blanca", "Frente a porton negro grande tapia amarilla", "25 mts despues de poste CNFL 26531", "Junto a Verdureria el Hipermito", "Junto a pulperia la primavera", "Entrada a calle avalos poste CNFL 26549", "Lote baldio contiguo a tapia celeste propiedad aya", "Frente a taller mecanico porton azul", "Parada terminal poste CNFL 26586", "Peana detras del pali del Paseo Colon junto a Panalpina", "Junto a Pulperia El Llano", "Frente a Residecia Los Olivos", "Junto a casa con rotulo trabajos de soldadura frentea lote con higueron", "Lotebaldio frente a casa blanca de rejas negras", "Parada final frente a bodegas Wal Mart", "Parada terminal costado Oeste del Parque de Escazu", "Junto a entrada principal El Sol Naciente", "Contiguo al Centro Comercial Escazu Auto Mercado AM con bahia ", "Avenida 6 Calles 36 y 38", "Parada Final Parque Ministerio de Salud", "Costado Este del Liceo de Escazu frente a casa esquinera con cerca de bambu y porton cafe", "Junto a propiedad con cerca vegetal alero  de tejas y porton verde", "Frente a Comidas Al Este", "Junto a telefono publico frente a casa color terracota con porton cafe de lamina metalica", "Junto a propiedad cpn rejas verdes contiguo a arbol de itabo frente a lote baldio", "Junto a rotulo de Bar Tipico Los Itabos", "Poste CNFL 26296", "Junto a propiedad con rejas negras frente a Ferreteria Aguilar", "Junto a propiedad en alto con rampa de concreto y porton verde", "Poste CNFL 11031817J3", "Arcadia", "Junto a pulperia La Estrella del Sur","Parada final frente al costado Norte del Gimnasio de la Escuela de El Carmen", "Casa de madera color cafe con porton color crema 50 mts antes del Juzgado", "Junto a casas con tapia terracota porton negro Hulera Escazu", "15 mts antes poste CNFL 24803, Bar La Uvita", "Junto a Peluqueria Estilos", "25 mts despues Abastecedor Lorymar", "100 mts antes del Bar Reyes", "50 mts despues Fruteria Los Reyes", "30 mts despues de la Iglesia de Bello Horizonte frente a Fruteria Los Angeles", "Junto al poste frente Condominios Alvareda", "Frente a Condominios Blanca Cola", "Parada final en Pinares 75 mts Este Pulperia Mini Jovi", "Caseta frente a Farmacia San Miguel Parque de Escazu", "Frente a casa anaranjada de rejas negras antiguo hogar de ancianos", "Esquina con cerca de bambu, rejas negras y lambre , navaja, Piedra Azul", "Esquina frente a entrada principal de Condominios San Miguel", "Junto a porton verde y telefono publico", "Frente a propiedad con porton blanco, muro con hiedra y alambre navaja", "Frente al Arbol de Guachipelin", "Poste CNFL 6889", "Junto al Salon Comunal Barrio Santa Teresita, entrada Hogar de Ancianos", "Poste CNFL  25588", "Junto a propiedad con muro, telefono publico, torre ICE", "Junto a casa blanca", "Terminal, frente a caseta negra, lote baldio cerca de postes de concreto","Casa Esteban","Cementerio Sta Ana","Cruz Roja Sta Ana","Avalon","Super El Roble","Casa Blanca Sta Ana","Bomba Montes, Centro Comercial Paseo del Angel","Ceviche del Rey","Vuelta de Rest. Bacchus","Frente a Vicar","Entrada a Pozos","Centro Comercial El Roble","Frente a Furati","Frente a Taller Mecanico Zuniga","Frente a Plaza INVU","Entrada a La Chispa","Frente a los Mora","Policia de Proximidad Puente Pozos","Escuela Rep. de Francia","Frente a Carniceria Pozos","Pan Mora","Frente a entrada Barrio Marcial Aguiluz","Entrada Urb. La INTEX","Frente a Los Pajaritas","Entrada Calle Cubilla","Entrada a Bo Corazon de Jesus","Frente Bar Jenny","Frente Escuela Rep. de Honduras","Centro Comercial Milano","Mas x Menos","Diengo","Puerta de Hierro","Autos Chava","MATRA","Frente a Momentum","Frente a Forum 2","Mc Donalds","Frente a Colegio Tecnico Lindora","Entrada al tajo","Lindora3, 1-2","Frente a la torre","Frente a los telefonos publicos","PRINCIPAL PIEDADES 1","Frente Colegio de Santa Ana","Frente a Calle Tit","Niehaus","Escuela Rio Oro","Carniceria Los Tista","Patagonia Motor Psycho","Hacienda Paraiso","Los Venados","Entrada a La Carana","Condominio Las Pampas","Fte a Escuela Piedades","Fte a Iglesia Piedades","Frente a Hotel Canal Grande","Centro Comercial Amparo","Los Elizondo","Frente Pulperia La Rosita","Costado Super Rio Oro","Chakas","Los Barrantes","Los Chapas","Los Pozuelo","El Salto","Los Caretos","Entrada CONVERSA","Los Tortolas","Entrada Cebadilla","Fabrica de Hielo","Benito Venado","El muerto","Chichi Cantillo","La Granja","Plantel ETRANSA","Montana del Sol","Rigo Chapa","Los Umana","Taller Cabalito","Escuela La Mina","Los Angeles 1","Los Angeles 2","Los Angeles 3","Los Angeles 4","Los Angeles 5","Los Angeles 6","Ciudad Colon-Piedades1","Ciudad Colon-Piedades2","Ciudad Colon-Piedades3","Ciudad Colon-Piedades4","Ciudad Colon-Piedades5","Ciudad Colon-Piedades6","Ciudad Colon-Piedades7","Ciudad Colon-Piedades8","Ciudad Colon-Piedades9","Ciudad Colon-Piedades10","Ciudad Colon-Piedades11","Ciudad Colon-Piedades12","Ciudad Colon-Piedades13","Ciudad Colon-Piedades14","Ciudad Colon-Piedades15","Ciudad Colon-Piedades16","Ciudad Colon-Piedades17","Ciudad Colon-Piedades18","Ciudad Colon-Piedades19","Ciudad Colon-Piedades20","Ciudad Colon-Piedades21","Ciudad Colon-Piedades22","Ciudad Colon-Piedades23","Ciudad Colon-Piedades24","Palo Quemado 1","Palo Quemado 2","Forum 1","Megasuper Sta Ana Pista","Peaje","CIMA","Trilogia","Marriot Plaza Itkatzu","Raven","Costado Oeste Multiplaza","Construplaza","Villa Real","Bloquera Santa Ana","Puente Pozos","Radial Belen5","Frente a Hollywood","ARESEP","POPS Sabana","Dona Lela World Gym","Taj-Mahal","La Tamalera","JFK10","Hotel Alta","Condominios","El Alto","El Estribo, Entrada Bo Vasquez","Entrada a Bo San Rafael","Frente a Servicentro JSM","Montoya1","Los Copetones","Ricardo Cespedes","Entrada a Rancho Macho","Interseccion Paso Machete","Entrada Quebrador","Iglesia Evangelica Montoya","Los Anchna","Los RoblesLos Alvarez","Ladrillera","Los Gallos","Carmen Herrera","Entrada Bo Los Herrera","El poste","Gimnasio Municipal Salitral","Asilo de Ancianos Enrique y Ana","Muebleria Los Leones","Aldeas SOS","Salitral5","Entrada Bo Los Herrera","La Palma","Cases","Principal Salitral3","Deposito Maderas El Salitre","Fte Ceramica El Salitre","Estadio Salitral","Lisandro Sosa","Fte Bar Aritos","Los Sandi","Principal Salitral10","Bar La Fuente","Super Pinar","Matinilla2","Fte casa de los Zuniga","Entrada a Urb. Madreselva","Iglesia San Rafael Matinilla","Fte. a entrada Calle Perico","Matinilla7","Tapezco","Fte a casa Pele","Familia Meson","Matinilla11","Matinilla12","Casa Nili","Fte. casa de Eva","Fte. casa de Don Helio","La Cruzada","El Trapiche","Tanques AyA","Los Alvarez","Recibidor Matinilla","Matinilla21","Matinilla22","Fte. Pulperia La Amistad","Fte a la Escuela Matinilla","Familia Montoya","Montoya16","Montoya17","Pulperia El Cruce","CJ Carrizal 1","CJ Carrizal 2","CJ Carrizal 3","CJ Carrizal 4","CJ Carrizal 5","CJ Carrizal 6","CJ Carrizal 7","CJ Carrizal 8"};
	static String[] nombresparadas2_1={"Parada final frente a casa de madera, piedra grande","Propiedad con tapia con alero de tejas", "Frente a propiedad con cerca y porton  de malla y bambu", "Junto a porte, contiguo a lote baldio", "Poste CNFL 250809", "Poste CNFL 25093", "Frente a propiedad con  muro alero de tejas", "Poste CNFL 25073", "A la salida del puente, Parada final Corazon de Jesus", "Caseta junto a iglesia", "Junto a casa, porton negro metalico, alero de tejas", "Junto a casa de concreto de color cafe claro", "Pulperia El Brujo", "25 mts despues de Super y Carnes El Country", "Junto a porton negro y rotulo no virar a la derecha", "Frente al costado Este de La Violeta", "Contiguo a la Asociacion de Bienestar Social de Escazu",  "Junto a Clinica Veterinaria 24 Horas", "Junto a Super Herrera", "Esquina Suroeste de Condominio Condados del Country", "Frente a salida de Condominio Los Prados", "Multicentro Paco", "Bahia en Plaza Laureles", "Bahia El Country, Apartotel Villas del Rio", "Junto a Peana Bahia La Primavera , diagonal a CNFL", "Costado Norte Plaza de San Rafael, Bahia La Escuela", "Caseta junto a Mc Donalds San Rafael de Escazu", "Caseta de Tienda Ali Baba, frente a Vivero Exotica", "Casetas y bahia Centro Comercial Trejos Montealegre", "Caseta frente a Contraloria General de la Republica", "Avenida 6, Calles 36 y 38", "Parada final , Parque Ministerio de Salud", "Frente a Condominio La Reserva Parada final Guachipelin", "Junto a propiedad de 2 pisos, muro beige, rejas cafes", "Caseta, frente a entrada Residencial Pinar del Rio", "Hacienda El Cocoy", "Frente a Condominio Colinas del Oeste", "Caseta centro comercial frente a Club Cubano Jose Marti", "Saint Mary School", "100 mts antes Cruce Pista Ruta 27", "Clinica Dental San Gabriel", "Frente a Escuela de Guachipelin", "Caseta tipo parabus 21450, junto a tapia de Cerro Alto", "Caseta tipo parabus 21451, junto a lote baldio", "Contiguo a Monnry", "Caseta tipo parabus, justo al Polideportivo de Guachipelin", "Contiguo a Plaza Florencia", "Junto a fuente de Condominio Los Prados", "Condominio Condado  del Country", "Caseta con publicidad de Hitachi, diagonal al Super Herrera", "Frente a Clinica Veterinaria 24 Horas", "Junto a entrada principal del Bar Snoopy", "50 mts al Este de La Violta", "Frente a La Municipalidad, costado Norte del Parque de  Escazu", "Junto a entrada principal El Sol Naciente", "Caseta frente a Topografia y Catastro Gustavo Porras", "Contiguo al Centro Comercial Escazu, Auto Mercado AM,  con bahia", "Caseta junto a Mc Donalds San Rafael de Escazu", "Caseta frente a Vivero Exotica", "Entre Pizza Hut y Centro Comercial Los Anonos", "Caseta junto a Distribuidora Santa Barbara Bello Horizonte", "Junto al muro de gaviones contiguo a Euromotor", "Caseta diagonal al edificio SYL", "Bajo Anonos, Contiguo al telefono publico", "Caseta diagonal al edificio SYL", "Caseta frente a UCIMED", "Contiguo al MAG", "Caseta junto a esquina Noreste de ARESEP", "100 mts Oeste de AMPM de La Sabana", "Caseta junto a la Contraloria General de la Republica", "Caseta junto a Universal La Sabana", "Avenida 6, Calles 36 y 38", "Calle 26, Avenidas 4 y 6", "Calle 26, Avenidas 0 y 2", "Parada final, Calle 16 Avenidas 0 y 1", "Frente a bodegas de Wall Mart", "Caseta junto a casa blanca, rejas cafes", "Frente a casa con rotulo trabajos de soldadura", "Junto a Residencial Los Olivos", "Parada inicial, caseta frente a finca de los Ossencbach, cerca de poro", "Esquina con cerca de malla, costado Este Cantina La Guaria", "Costado sur de Iglesia de San Antonio,junto a gradas", "Esquina costado sur de la cancha de basquebol", "Caseta junto a telefono publico", "Frente a Panalera Tuti, frente a entrada Calle El Mana", "Frente a Condominios Rocas de La Montana, entrada a La Pajarera", "Entrada al Colegio", "Junto a Tienda Arcari, frente a Centro de Acabados Maderas Camacho", "Frente a Apartamentos porton negro, 75 mts Oeste de Farmacia Escazu", "Frente a Distribuidora San Miguel, junto a casa blanco con celeste", "Costado Oeste Escuela Rep De Venezuela de Escazu", "Parada terminal, costado Oeste del Parque de Escazu", "Parada final, frente al costado Norte del Gimnacio de la Escuela de El Carmen", "Esquina con muro de block expuesto, diagonal a Super La Florecilla", "Poste CNFL 26241", "Poste CNFL 26237", "Junto a poste, contiguo a propiedad con muro de contencion  de piedra y cerca de malla", "Junto a propiedad esquinera, poste CNFL 26278", "Tapia amarilla con rojo", "Junto al poste CNFL 25365", "Comidas Al Este", "Parada final en Pinares 75 mts Este Pulperia Mini Jovi", "Apartamentos Blanca Cola, entrada al Hotel Mirador", "Junto a tapia con hiedra de Condominios Alvareda", "Junto a telefono publico de la Escuela de Bello Horizonte", "Bar Los Reyes, frente poste CNFL 24693", "Junto a lote baldio con postes de concreto", "Junto a porton azul frente a Peluqueria Estilos", "Frente a poste CNFL 24803", "75 mts Oeste de la Hulera Escazu", "Terminal, caseta junto al telefono publico, Condominios Villas de San Antonio", "Caseta junto a telefono, entrada calle Lotes Badilla, Pinturas Aqualin", "Frente a poste CNFL 25485", "25 mts antes poste CNFL 25492", "Junto a poste muro de block expuesto cerca de bambu telefono publico", "Junto a Abastecedor Yireth, frente a Farmacia La Merced", "Junto a entrada Sur del Colegio Tecnico de Escazu", "Terminal, frente a caseta negra, lote baldio cerca de postes de concreto", "Poste CNFL 25755", "Junto a propiedad con porton blanco de  madera, rejas blancas", "Poste CNFL 25588", "25 mts despues del poste CNFL  25489", "Frente a poste CNFL 25530, cerca de bambu", "Costado Este del Liceo de Escazu", "Caseta Parque de Escazu, segunda caseta, frente Municipalidad de Escazu", "Parada Final, Calle 16 Avenidas 1 y 3","Bloquera Santa Ana","Contiguo Escuela Honduras","Bar Jenny","Costado Hules Tecnicos","Frente a Calle Cubilla","Entrada Calle Los Pajaritas","Frente a Urb INTEX","Contiguo Casa Felix Rodriguez, PAN MORA","Contiguo Bodegas Tio Pelon","Frente Barrio Marcial Aguiluz","Frente Iglesia Pozos","Frente a Policia de Proximidad Puente Pozos","Finca Los Pozuelo","Frente los Mora","Frente a La Chispa","Frente a la plaza pozos","Frente a Taller mecanico Los Zuniga","Frente a Furati","Frente a Centro Comercial El Roble","Entrada a Pozos","Cementerio Sta Ana","Cruz Roja Sta Ana","Avalon","Super El Roble","Casa Blanca Sta Ana","Bomba Montes, Centro Comercial Paseo del Angel","Ceviche del Rey","Vuelta de Rest. Bacchus","Frente a Vicar","Frente Super Lindora","Lindora 2","Interseccion al tajo","Frente al Colegio Tecnico","Salida a la Radial","Entrada Bosques de Lindora","Frente a Momentum","Frente a Auto Mercado","Frente BAC San Jose","Citi Bank","HSBC","Frente a Pizza Hut","Frente a Diengo","Puerta de Hierro","Frente a mas x Menos","Grupo Inka","Los Tortolas","Interseccion CONVERSA","Los Caretos","El Salto","Los Chapas","Los Barrantes","Chakas","Super Rio Oro","Hotel Canal Grande","Frente Pulperia La Rosita","Los Elizondo","Centro Comercial Amparo","Escuela Ezequiel Morales","Iglesia Piedades","Entrada La Carana","Condominio Las Pampas","Los Venados","Hacienda Paraiso","Frente a Patagonia, Motor Psycho","Carniceria Los Tista","Super Rio Oro","Niehaus","Calle Titi","Colegio de Santa Ana","Costado de Picantes","Barrio Espana 1","Taller Cabalito","Los Umana","Rigo Chapa","Entrada Parque Montana del Sol","Plantel ETRANSA","La Granja","Chichi Cantillo","El muerto","Benito Venado","Fabrica de Hielo","Salida a Piedades","Plantel Brasil","Frente a Swiss Travel","Chatarrera","Ciudad Colon-Piedades 4","Fte Escuela Brasil","Ciudad Colon-Piedades 6","Ciudad Colon-Piedades 7","Frente Rancho El Higueron","Deposito Maderas","Ciudad Colon-Piedades 10","Ciudad Colon-Piedades 11","Ciudad Colon-Piedades 12","Ciudad Colon-Piedades 14","Ciudad Colon-Piedades 15","Ciudad Colon-Piedades 16","Cruz Roja Ciudad Colon","Cruce UPAZ","Ciudad Colon-Piedades 19","Fte Iglesia Ciudad Colon","Servicentro Los Angeles","Ciudad Colon-Piedades 22","Ciudad Colon-Piedades 13","Ciudad Colon-Piedades 23","Ciudad Colon-Piedades 24","Ciudad Colon-Piedades 25","Ciudad Colon-Piedades 26","Ciudad Colon-Piedades 27","Ciudad Colon-Piedades 28","Ciudad Colon-Piedades 29","Ciudad Colon-Piedades 30","Ciudad Colon-Piedades 31","Ciudad Colon-Piedades 32","Ciudad Colon-Piedades 33","Ciudad Colon-Piedades 34","Los Angeles 1","Los Angeles 2","Los Angeles 3","Los Angeles 4","Los Angeles 6","Los Angeles 5","Los Angeles 7","Los Angeles 8","Los Angeles 9","Radial Belen 1","Palo Quemado 1","Palo Quemado 2","Forum 1, adentro","Megasuper Pista","Villa Real","Puente Pozos","Tunel Construplaza","Hotal Real Intercontinental","Multiplaza","PriceSmart","CIMA","Peaje","Walmart","JFK1","JFK2","JFK3","JFK4","JFK5","JFK6","JFK7","JFK8","JFK9","La Paco","Laureles","Villas del Rio","Urgelles","Entrada La Primavera","Matinilla2","Matinilla3","Matinilla4","Matinilla5","Matinilla6","Matinilla7","Matinilla8","Matinilla9","Matinilla10","Matinilla11","Matinilla12","Matinilla13","Matinilla14","Principal Salitral1","Principal Salitral2","Principal Salitral3","Principal Salitral4","Principal Salitral5","Principal Salitral6","Principal Salitral7","Principal Salitral8","Pedro Loco","Principal Salitral10","Principal Salitral11","Principal Salitral12","Salitral1","Salitral2","Salitral3","Matinilla15","Matinilla16","Matinilla17","Matinilla18","Matinilla19","Matinilla20","Matinilla21","Matinilla22","Matinilla23","Matinilla24","Montoya 1","Montoya 2","Montoya 3","Montoya 4","Montoya 5","Montoya 6","Montoya 7","Montoya 8","Montoya 9","Montoya 10","Montoya 11","Montoya 12","Montoya 13","Montoya 14","Montoya 15","Montoya 16","Montoya 17","CJ Carrizal 1","CJ Carrizal 2","CJ Carrizal 4","CJ Carrizal 5","CJ Carrizal 6","CJ Carrizal 7","CJ Carrizal 8","Pulperia El Brujo","CJ Carrizal10","CJ Carrizal 3"};
	static double[] latitudesramales={9.935436,9.904267,9.920020,9.909232,9.909669,9.902330,9.897018,9.902061,9.909728,9.891262,9.919610,9.932340,9.904413,9.943013,9.962130, 9.963427,9.967181,9.957927,9.943772,9.912587,9.905637,9.888648,9.915740,9.914292,9.931623,9.947913,9.936337, 9.933853,9.933516,9.915930, 9.944150};
	static double[] longitudesramales={-84.085560,-84.128477,-84.140757,-84.132678,-84.156706,-84.140563,-84.141603,-84.157446,-84.127493,-84.127772,-84.126903,-84.085515,-84.126901,-84.152617,-84.174100,-84.189605,-84.189654,-84.204395,-84.224353,-84.179278,-84.182075,-84.171853,-84.195991,-84.241733,-84.169572,-84.177897,-84.222271,-84.181072, -84.188185,-84.141000,-84.148900};
	static boolean sendLugarStop=true;
	static int counterstartservicebarras=0;
	static double velprevia=0;
	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}
	

	//@SuppressLint("NewApi")
	@Override
	  public void onCreate(){
		/*defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
		        new Thread.UncaughtExceptionHandler() {
		            @Override
		            public void uncaughtException(Thread thread, Throwable ex) {
		            	
		                // here I do logging of exception to a db
		            	logger.info(ex.getLocalizedMessage());
		            	 counterServiceDied(1); 	 
		                PendingIntent myActivity = PendingIntent.getService(getBaseContext(),
								192837, new Intent(getBaseContext(), LocationService.class),
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
		isSavingToLocal=false;
		isisSendingtoSQL=false;
		isSendingDiferido=false;
		// isSearchingRamal=false;
		 isTestingDispositivo=false;
		 isSetDataOn=false;
		 testlasttransmision=true;
		handler=new Handler();
		setLoggerFile();
		logger.info("Servicio Principal Reiniciado");
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notibuilder=new Notification.Builder(context);
		helperlugares=BaseLugaresLocal.getInstance(context);
		lugares=helperlugares.getReadableDatabase();
		cursor = lugares.rawQuery(" SELECT Latitud,Longitud,Lugar FROM Puntos", null);
		data=new DataForSql(this);
		if (context!=null){
			Intent i=new Intent();
			i.setAction("com.iwop.rastreadormovil.getdatabarras");
			context.sendBroadcast(i);
		}
		//BugSenseHandler.initAndStartSession(context, "548c7e40");
		
		//AudioManager am;
		//am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		//am.setMode(AudioManager.RINGER_MODE_SILENT);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SOL");
		wl.acquire();
		prefsparametros =getSharedPreferences("RastreoMovilParametros",Context.MODE_PRIVATE);
        datosrastreo=getSharedPreferences("DatosRastreo", Context.MODE_PRIVATE);
        editor=datosrastreo.edit();
        sendSeguro=datosrastreo.getBoolean("sendSeguro", sendSeguro);
        sendLugar=datosrastreo.getBoolean("sendLugar", sendLugar);
	    bus=prefsparametros.getInt("Bus", Context.MODE_PRIVATE);
	    //sentido=datosrastreo.getInt("Sentido", 0);
	    sentidoprevio=datosrastreo.getInt("SentidoOld", 0);
	    //contadorcarreras=datosrastreo.getFloat("ContadorCarreras", 0);
	    km=datosrastreo.getFloat("MetrosRecorridos", LocationGPS.metrosr);
	    ultimatransmision=datosrastreo.getLong("UltimaTransmision", ultimatransmision);
	    contador_cambiosramal=datosrastreo.getInt("Contador_CambiosRamal", 0);
	    setFirstRamal=datosrastreo.getBoolean("setFirstRamal", setFirstRamal);
		//ramal=datosrastreo.getInt("RamalInt", ramal);
	    //ultimoramal=datosrastreo.getInt("UltimoRamal", ramal);
	    stopcountbarras=datosrastreo.getBoolean("CountBarrasStop", false);
	    setFirstRamal2=datosrastreo.getBoolean("setFirstRamal2", false);
	    getdatabarras.setAction(ValoresEstaticos.action_getdata);
	    noti = notibuilder.setContentTitle("Rastreo unidad " + String.valueOf(bus)).setContentText("Cia de Inversiones La Tapachula").setSmallIcon(R.drawable.ic_launcher).build();

		imei=prefsparametros.getString("Imei", imei);
		DataForSql.bus=bus;
		DataForSql.imei=imei;
		float metros=getMetrosRecorridos();
		DataForSql.metros_recorridos=metros;
		isFromPrincipal=false;		
		isFirstRunning=true;
		resetdatabarras=datosrastreo.getBoolean("resetDataBarras", resetdatabarras);

		if (isFirstRunning){
		context=this;listenergps=new LocationGPS();
		listenernet=new LocationNet();
			getLocationProviders();

		
		startForeground(1,noti);
		
		}
		
		
		
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		 super.onStartCommand(intent, flags, startId);	
		 if (isFirstRunning==false){
			 isSavingToLocal=false;
			 isisSendingtoSQL=false;
			 isSendingDiferido=false;
			// isSearchingRamal=false;
			 isTestingDispositivo=false;
			 isSetDataOn=false;
			 testlasttransmision=true;
		/*defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
			 Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
					 new Thread.UncaughtExceptionHandler() {
						 @Override
						 public void uncaughtException(Thread thread, Throwable ex) {

							 // here I do logging of exception to a db
							 logger.info(ex.getLocalizedMessage());
							 counterServiceDied(1);
							 PendingIntent myActivity = PendingIntent.getService(getBaseContext(),
									 192837, new Intent(getBaseContext(), LocationService.class),
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
		handler=new Handler();
		 context=this;
		 PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		 PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SOL");
		 wl.acquire();
		 prefsparametros =getSharedPreferences("RastreoMovilParametros",Context.MODE_PRIVATE);
		 datosrastreo=getSharedPreferences("DatosRastreo", Context.MODE_PRIVATE);
		 editor=datosrastreo.edit();
		 sendLugar=datosrastreo.getBoolean("sendLugar", sendLugar);
		 sendSeguro=datosrastreo.getBoolean("sendSeguro", sendSeguro);
		 bus=prefsparametros.getInt("Bus", Context.MODE_PRIVATE);
		 //sentido=datosrastreo.getInt("Sentido", 0);
		 //contadorcarreras=datosrastreo.getFloat("ContadorCarreras", 0);
		 sentidoprevio=datosrastreo.getInt("SentidoOld", 0);
		 contador_cambiosramal=datosrastreo.getInt("Contador_CambiosRamal", 0);
		 km=datosrastreo.getFloat("MetrosRecorridos", LocationGPS.metrosr);
		 ultimatransmision=datosrastreo.getLong("UltimaTransmision", ultimatransmision);
		 //BugSenseHandler.initAndStartSession(context, "548c7e40");
		 setLoggerFile();
		 logger.info("Servicio Principal Reiniciado");
		 //ramal=datosrastreo.getInt("RamalInt", ramal);
		 //ultimoramal=datosrastreo.getInt("UltimoRamal", ramal);
		 getdatabarras.setAction(ValoresEstaticos.action_getdata);
		 setFirstRamal=datosrastreo.getBoolean("setFirstRamal", setFirstRamal);
			 NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		 notibuilder=new Notification.Builder(context);
		 noti = notibuilder.setContentTitle("Rastreo unidad " + String.valueOf(bus)).setContentText("Cia de Inversiones La Tapachula").setSmallIcon(R.drawable.ic_launcher).build();
		 stopcountbarras=datosrastreo.getBoolean("CountBarrasStop", false);
		 resetdatabarras=datosrastreo.getBoolean("resetDataBarras", resetdatabarras);
		imei=prefsparametros.getString("Imei", imei);
		setFirstRamal2=datosrastreo.getBoolean("setFirstRamal2", false);

		DataForSql.bus=bus;
		DataForSql.imei=imei;
		float metros=getMetrosRecorridos();
		DataForSql.metros_recorridos=metros;
		isFromPrincipal=false;
		if (listenergps==null)
		{
			listenergps=new LocationGPS();
		}
		if (listenernet==null)
		{
			listenernet=new LocationNet();
		}
		context=this;
		helperlugares=BaseLugaresLocal.getInstance(context);
		lugares=helperlugares.getReadableDatabase();
		cursor = lugares.rawQuery(" SELECT Latitud,Longitud,Lugar FROM Puntos", null);
		data=new DataForSql(this);
		if (context!=null){
			Intent i=new Intent();
			i.setAction("com.iwop.rastreadormovil.getdatabarras");
			context.sendBroadcast(i);
		}
		
		//AudioManager am;
		//am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		//am.setMode(AudioManager.RINGER_MODE_SILENT);
		//BugSenseHandler.initAndStartSession(context, "548c7e40");

		

				getLocationProviders();

	
		
		startForeground(1, noti);
		
		 }
		 else{
			 isFirstRunning=false;
		 }
		
        return START_STICKY;
	}
	//ESTE METODO VA A CAMBIAR LAS FRECUENCIAS DE ACTUALIZACION Y TRANSMISION DE DATOS CON EL FIN DE CONSERVAR AL MAXIMO LA BATERIA
	/*public static void setPowerSaveMode(boolean b){
		
		if (b){
			
			isConnect=true;
			datosrastreo.edit().putBoolean("isConnect", true).commit();
			selectLocationProvider(7);
			setAlarm(1200000);
			
			
		}
		else{
			
			isConnect=false;
			datosrastreo.edit().putBoolean("isConnect", false).commit();
			selectLocationProvider(8);
			setAlarm(900000);
			
		}
	
		
	}*/
	
	/*private static void executeCommandWithoutWait(Context context, final String option, final String command) {
	   
		   
			   boolean success = false;
			    String su = "su";
			    for (int i=0; i < 3; i++) {
			        // "su" command executed successfully.
			        if (success) {
			            // Stop executing alternative su commands below. 
			            break;
			        }
			        if (i == 1) {
			            su = "/system/xbin/su";
			        } else if (i == 2) {
			            su = "/system/bin/su";
			        }       
			        try {
			            // execute command
			        	Process s=Runtime.getRuntime().exec((new String[]{su, option, command}));
			        	s.waitFor();
			            Runtime.getRuntime().exec(new String[]{su, option, command});
			            
			        } catch (IOException e) {
			            Log.e("Rastreado Movil", "su command has failed due to: " + e.fillInStackTrace());
			        } catch (InterruptedException e) {
			        	Log.e("Rastreado Movil", "su command has failed due to: " + e.fillInStackTrace());
					}   
			    }
		   
	   
	}*/
	public static void resetNetwork2(){
		
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
					handler.postAtFrontOfQueue(new Runnable() {
						
						@Override
						public void run() {
							
							int enabled = 1;
				            // Set Airplane / Flight mode using su commands.
				           // String command = "settings put global airplane_mode_on" + " " + enabled;
				            //Command commando=new Command(0, "settings put global airplane_mode_on" + " " + enabled);
				            try {
				            	
				            	//if (RootTools.isRootAvailable()){
								//RootTools.getShell(true).add(commando);
								//command = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state" + " " + enabled;
								//commando=new Command(0, "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state" + " " + enabled);
								//RootTools.getShell(true, 3);
								//Shell.runRootCommand(commando);
				            	RunAsRoot(setAirplaneOn);
								logger.info("ResetNetwork. Set modo avion ON.");
								//enabled=0;
								handler.postDelayed(new Runnable() {
									
									@Override
									public void run() {
										//Command commando=new Command(0, "settings put global airplane_mode_on" + " " + 0);
										//command = "settings put global airplane_mode_on" + " " + enabled;
										try {
											//RootTools.getShell(true, 3);
											//Shell.runRootCommand(commando);
											//commando=new Command(0, "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state" + " " + 0);
											//RootTools.getShell(true, 3);
											//Shell.runRootCommand(commando);
											RunAsRoot(setAirplaneOff);
											logger.info("ResetNetwork. Set modo avion OFF.");
										} catch (IOException e) {
											logger.info("ResetNetwork. Error set modo avion OFF."+e.getLocalizedMessage());
											LocationService.isSendingDiferido=false;
											LocationService.isisSendingtoSQL=false;
											return;
										} 
										/*commando=new CommandCapture(0, "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state" + " " + 0);
										RootTools.getShell(true).add(commando);*/
										
									}
								}, 15000);
								
				            	//}
							} catch (final IOException e) {
								try {
									RunAsRoot(setAirplaneOff);
									logger.info("ResetNetwork. Error set modo avion ON." + e.getLocalizedMessage() + ". Poniendo modo avion OFF.");
								} catch (IOException e1) {
									logger.info("ResetNetwork. Error set modo avion ON."+e.getLocalizedMessage());
									LocationService.isSendingDiferido=false;
									LocationService.isisSendingtoSQL=false;
									return;
								}
							} 
				            //executeCommandWithoutWait(context, "-c", command);
				            
				            //command = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state" + " " + enabled;
				            //executeCommandWithoutWait(context, "-c", command);
							
						}
					});
					/*handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							 	int enabled=0;
							 	String command = "settings put global airplane_mode_on" + " " + enabled;
					            executeCommandWithoutWait(context, "-c", command);
					            command = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state" + " " + enabled;
					            executeCommandWithoutWait(context, "-c", command);
							
						}
					}, 4000);*/
			        // API 17 onwards.
			        //if (isRooted(context)) {            
			            
			        
				}
				else {
			        // API 16 and earlier.
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							Settings.System.putInt(context.getContentResolver(),
								      Settings.System.AIRPLANE_MODE_ON, 1);

								// Post an intent to reload
								Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
								intent.putExtra("state", 1);
								context.sendBroadcast(intent);
								logger.info("ResetNetwork. Set modo avion OFF. API <= a 16");
							
						}
					}, 1000);
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							Settings.System.putInt(context.getContentResolver(),
								      Settings.System.AIRPLANE_MODE_ON, 0);

								// Post an intent to reload
								Intent intent2 = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
								intent2.putExtra("state", 0);
								context.sendBroadcast(intent2);
							logger.info("ResetNetwork. Set modo avion ON. API <= a 16");
							
						}
					}, 4000);
						

						
			    }
			
		
		
	}
	 public static void RunAsRoot(String[] cmds) throws IOException{
		 Process p = Runtime.getRuntime().exec("su");
         DataOutputStream os = new DataOutputStream(p.getOutputStream());            
         for (String tmpCmd : cmds) {
                 os.writeBytes(tmpCmd+"\n");
         }           
         os.writeBytes("exit\n");  
         os.flush();
	 }

	public static void getLocationProviders(){
		/*Location l=new Location("Test");
		l.setLatitude(9.920360);
		l.setLongitude(-84.140300);
		l.setTime(Calendar.getInstance().getTimeInMillis());
		getLugar(l);*/
		if(ModuloSQL.testLogin()==false){
			Toast.makeText(LocationService.getContext(),"No hay comunicacion con base de datos", Toast.LENGTH_LONG).show();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					getLocationProviders();
				}
			},1000);
			return;
		}

		lmanager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

		isSendingDiferido=false;
		isisSendingtoSQL=false;

		List<String> list;
		list=lmanager.getAllProviders();
		StringBuilder lis=new StringBuilder();
		lis.append(list.toString());
		selectLocationProvider(7);

			return;
		//}
		//return;



		
	}

	public static void selectLocationProvider(int i){
		//SE PUSO ESTAS LINEAS PARA VER SI ARRANCA LUEGO HAY QUE QUITARLAS
		/*Calendar c=Calendar.getInstance();
		if (c.get(Calendar.HOUR)>4){
			i=7;
			isAhorro=false;
		}*/
		// FIN MODIFICACION.
		//Toast.makeText(LocationService.getContext(),"Select Location Providers",Toast.LENGTH_SHORT).show();
		if (LocationGPS.metrosr!=0){
		
		LocationService.setMetrosRecorridos(LocationGPS.metrosr);
		LocationService.setCoordenadasPrevias(LocationGPS.latitudprevia, LocationGPS.longitudprevia);}

		

		lugarseguro=datosrastreo.getBoolean("LugarSeguro", false);
		//isAhorro=datosrastreo.getBoolean("isAhorro", false);
		
		if (lmanager==null){} {lmanager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);}
		if (listenergps==null){listenergps=new LocationGPS();}
		if (listenernet==null){listenernet=new LocationNet();}
		
		if (i==0){
				//Toast.makeText(context, "Provider 0", 30).show();
				
				lmanager.removeUpdates(listenernet);
				lmanager.removeUpdates(listenergps);								
				lmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, listenergps);
				lmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listenernet);
			}	
		if (i==1){
				
				lmanager.removeUpdates(listenernet);
						
		}
		if (i==2){
				lmanager.removeUpdates(listenernet);
				LocationNet.isfirst=true;				
				lmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listenernet);
		}
		if (i==3){
			lmanager.removeUpdates(listenernet);
		}
		if (i==4){
			if (listenernet==null){
			listenernet=new LocationNet();
			}
			LocationNet.isfirst=true;
			lmanager.removeUpdates(listenernet);
			lmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listenernet);
				
		}
		if (i==5){
			lmanager.removeUpdates(listenergps);
			handler.postDelayed(new Runnable(){

				@Override
				public void run() {
					
					lmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, listenergps);
				}},8000);
			
		}
		if (i==6){
			lmanager.removeUpdates(listenernet);
			lmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, listenergps);
		}
		if (i==7){
			
			lmanager.removeUpdates(listenernet);
			lmanager.removeUpdates(listenergps);								
			lmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, listenergps);
			lmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listenernet);
		}
		if (i==8){
			lmanager.removeUpdates(listenernet);
			lmanager.removeUpdates(listenergps);								
			lmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, listenergps);
			lmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listenernet);
		}
		// ******  AHORRO DE ENERGIA EN LA NOCHE  *****
		if (i==10){
			//Toast.makeText(context, "select provider 10", Toast.LENGTH_SHORT).show();
			lmanager.removeUpdates(listenergps);
			lmanager.removeUpdates(listenernet);
			lmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listenernet);
			lmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, listenergps);
			return;
					
		}
		return;

		
		
	}
	public void onDestroy(){
		
		super.onDestroy();		
		
		//editor.putFloat("Metros_Recorridos_Previo", kmprevio);
		//editor.putLong("UltimaTransmision", ultimatransmision);
		//LocationService.setCoordenadasPrevias(latitudprevia, longitudprevia);
		//LocationService.setMetrosRecorridos(km);
		if(lmanager!=null){
			if(listenergps!=null){lmanager.removeUpdates(listenergps);}
			if(listenernet!=null){lmanager.removeUpdates(listenernet);}
		}
		if (prefsparametros.getBoolean("IsActive", false)){
		
		//ModuloSQL.closeConnection();
		
		if (conexion!=null){
			try {
				conexion.close();
				
			} catch (java.sql.SQLException e) {
				
				//DataForErrors.setFecha(Calendar.getInstance().getTimeInMillis());
				//DataForErrors.setMensaje(e.getLocalizedMessage()+" linea LocationService.OnDestroy SQL Exception");
				//DataForErrors.sendError();
			}
			
		}
		Intent intent = new Intent();
		intent.setAction("android.intent.action.resetrastreoservice");
		sendBroadcast(intent);}
		return;
	
	}
	public static void setLoggerFile(){


		try {

			// This block configure the logger with handler and formatter

			fh = new FileHandler(Environment.getExternalStorageDirectory().getPath()+"/LogCoordenadas.log");
			//Toast.makeText(context,Environment.getExternalStorageDirectory().getPath()+"/LogSOL"+df.format(Calendar.getInstance().getTime())+".log",Toast.LENGTH_LONG).show();
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

			// the following statement is used to log any messages
			//logger.info("Archivo log creado");

		} catch (SecurityException e) {
			return;
		} catch (IOException e) {
			return;
		}

		//logger.info("Hi How r u?");

	}
	public static float getMetrosRecorridos(){
		datosrastreo =context.getSharedPreferences("DatosRastreo",Context.MODE_PRIVATE);
		float metros=datosrastreo.getFloat("MetrosRecorridos", DataForSql.metros_recorridos);
		return metros;
	}
	public static void setMetrosRecorridos(float m){
		if (editor==null){
			editor=datosrastreo.edit();
		}
		datosrastreo =context.getSharedPreferences("DatosRastreo",Context.MODE_PRIVATE);
		editor.putFloat("MetrosRecorridos", m).commit();
		
		km=m;
		return;
	}
	public static Context getContext(){
		return context;
	}
	
	public static boolean counterServiceDied(int i){
		if (editor==null){
			editor=datosrastreo.edit();
		}
		datosrastreo =context.getSharedPreferences("DatosRastreo",Context.MODE_PRIVATE);
		int counter=datosrastreo.getInt("ContadorErrores", 0);
		counter=counter+i;		
		editor.putInt("ContadorErrores", counter).commit();
		return true;
	}
	
	public static void consultaSaldo(){
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage("+8888", null, "SALDO", null, null);	

		return;
		}
	public static void sendSMS(String s){
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage("+50686375602", null, s, null, null);	

		return;
		}
	

	public static long getLastTransmision(){
		if (datosrastreo==null || context==null||editor==null){
			context=getContext();
			datosrastreo=context.getSharedPreferences("DatosRastreo", Context.MODE_PRIVATE);
			if (editor==null){
				editor=datosrastreo.edit();
			}
		}
		return datosrastreo.getLong("UltimaTransmision", fecha.getTime());
	}
	/*public static void setSentido(int i){
		if (datosrastreo==null || context==null||editor==null){
			context=getContext();
			datosrastreo=context.getSharedPreferences("DatosRastreo",Context.MODE_PRIVATE);
			if (editor==null){
				editor=datosrastreo.edit();
			}
		}
		if (sentidoprevio==0){
			sentidoprevio=datosrastreo.getInt("SentidoOld", 0);
		}
		
		sentido=i;
		editor.putInt("Sentido", i).commit();
		editor.putInt("ConsecutivoSentido", datosrastreo.getInt("ConsecutivoSentido", 0)+1).commit();
		editor.commit();
		if (sentidoprevio!=sentido){
			sentidoprevio=sentido;
			//editor.putInt("SentidoOld", sentidoprevio).apply();
			editor.putInt("ContadorGeneralEntradasPrevio", DataForSql.generalentradaspe).apply();
			editor.putLong("ContadoresGeneralesPrevio", DataForSql.generalentradaspe+DataForSql.generalentradasps+DataForSql.generalsalidaspe+DataForSql.generalsalidasps).commit();
			if (ultimoramal==ramal||sramal.equals(datosrastreo.getString("UltimoRamalS", sramal))){
				//editor.putInt("ContadorGeneralEntradasPrevio", DataForSql.contadorentradaspe+DataForSql.contadorentradasps).commit();
				contadorcarreras=contadorcarreras+(float)0.5;
				editor.putFloat("ContadorCarreras", contadorcarreras);
				editor.commit();
			}
			else{
				if (ramal>0){
					//editor.putInt("ContadorGeneralEntradasPrevio", 0).commit();
					contadorcarreras=(float)0.5;
					editor.putFloat("ContadorCarreras", contadorcarreras);
					editor.commit();
				}
			}
			
		}
		
		
		
		
	}
	*/


	/*public static boolean isConnectedtoInternet(){
		if (context!=null){
			cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm!=null){
				net=cm.getActiveNetworkInfo();
				if (net!=null){
					if (net.isConnected()){
						return true;
					}
					else{
						return false;
					}
					}}}
		return false;
		//return ModuloSQL.checkConnection();
	}*/


	//ESTE METODO SE CREO A PARTIR DE V 31.4 PARA COMPROBAR APENAS LLEGUE A ALGUN PREDIO, SI LLEVA MUCHO TIEMPO SIN TRANSMITIR RESETEA EL TELEFONO
@TargetApi(Build.VERSION_CODES.KITKAT)
public static void getLugar(Location arg0){
	onLocationChanged(arg0);
	counter++;
	Location referencia=new Location("Referencia");
	Logger.getLogger("Coordenadas").log(Level.SEVERE,"GetLugar " + String.valueOf(counter));
	meters=20000;
	distancia=3000;
	//if (kmprevio2==0){
	//kmprevio2=km;
	if(cursor.moveToFirst())
	{

		referencia.setLatitude(cursor.getFloat(cursor.getColumnIndex("Latitud")));
		referencia.setLongitude(cursor.getDouble(cursor.getColumnIndex("Longitud")));
		//Logger.getLogger("Coordenadas").log(Level.INFO, cursor.getString(cursor.getColumnIndex("Lugar")));
		distancia=arg0.distanceTo(referencia);
		if (distancia<1000)
		{
			//Logger.getLogger("Coordenadas").log(Level.INFO, "Lugar Cercanos encontrado: " + cursor.getString(cursor.getColumnIndex("Lugar")));
			lugarseguro=true;
			lugar=cursor.getString(cursor.getColumnIndex("Lugar"));
			meters=distancia;

		}

		while (cursor.moveToNext())
		{
			//Logger.getLogger("Coordenadas").log(Level.SEVERE, cursor.getString(cursor.getColumnIndex("Lugar")));
			referencia.setLatitude(cursor.getFloat(cursor.getColumnIndex("Latitud")));
			referencia.setLongitude(cursor.getDouble(cursor.getColumnIndex("Longitud")));
			distancia=arg0.distanceTo(referencia);
			if (distancia<1000 & distancia<meters)
			{
				//Logger.getLogger("Coordenadas").log(Level.INFO, "Lugar Cercanos encontrado: " + cursor.getString(cursor.getColumnIndex("Lugar")));
				lugarseguro=true;
				lugar=cursor.getString(cursor.getColumnIndex("Lugar"));
				meters=distancia;
				if (distancia<50){
					break;
				}
			}

		}
	}

	editor.putBoolean("LugarSeguro", lugarseguro).commit();
	editor.putBoolean("sendSeguro", sendSeguro).commit();


	editor.putString("Lugar", lugar).commit();
	handler.postDelayed(new Runnable() {
		@Override
		public void run() {
			getLocationProviders();
		}
	},1000);

}
	public static void onLocationChanged(final android.location.Location arg0) {
		Logger.getLogger("Coordenadas").log(Level.SEVERE,"OnLocationChanged ");
		context=LocationService.getContext();

		if (isNetSelected){
			isNetSelected=false;
		}
		//Toast.makeText(context, String.valueOf(arg0.getTime()), 2).show();
		if (location_multiplaza_testpoint==null){
			location_multiplaza_testpoint=new Location("TestPointMultiplaza");
			location_multiplaza_testpoint.setLatitude(latitudesramales[latitudesramales.length-1]);
			location_multiplaza_testpoint.setLongitude(longitudesramales[longitudesramales.length-1]);
		}
		if (previa==null){
			previa=new Location("Previa");
			previa.setLatitude(arg0.getLatitude());
			previa.setLongitude(arg0.getLongitude());
		}
		if (locationParadaEncontrada==null){
			locationParadaEncontrada=new Location("Parada Encontrada");
			locationParadaEncontrada.setLatitude(0);
			locationParadaEncontrada.setLongitude(0);
		}
		locationactual=arg0;

		c=Calendar.getInstance();

		LocationService.handler.post(new Runnable() {
			@Override
			public void run() {
				//Toast.makeText(context,"Ubicacion actualizda",Toast.LENGTH_SHORT).show();
			}
		});
		if (fechaprevia==0 || fechaprevia2==0||fechaprevia3==0||fechaprevia4==0||fechaprevia5==0){
			fechaprevia = arg0.getTime();
			fechaprevia2=fechaprevia3=fechaprevia4=fechaprevia5=fechaprevia;

		}
		if (latitudprevia==0 || longitudprevia==0)
		{
			double []coordenadas=LocationService.getCoordenadasPrevias();
			if (coordenadas[0]+coordenadas[1]!=0){
				latitudprevia=coordenadas[0];
				longitudprevia=coordenadas[1];}
			else{
				latitudprevia=arg0.getLatitude();
				longitudprevia=arg0.getLongitude();
				LocationService.setCoordenadasPrevias(latitudprevia, longitudprevia);
			}

		}
		mLastLocationMills = SystemClock.elapsedRealtime();

		fecha=new Timestamp(arg0.getTime());
		LocationService.fecha=fecha;


		if (segstop<300 & (distance>=10&arg0.getAccuracy()<=50)){

			calculatekm=true;
		}
		if (segstop>300){

			calculatekm=false;
		}
		if (velprevia==0){
			velprevia=arg0.getSpeed()*3.6;
		}
		//METODO PARA SABER SI UN BUS FRENO O ACELERO MUY RAPIDO. PARAMETRO: diferencia de 25Km/h en 2.5s y si mantiene la precision menor a 50
		/*if (((arg0.getSpeed()*3.6)<(velprevia-40)||(arg0.getSpeed()*3.6)>(velprevia+40))&arg0.getTime()<=fechaprevia3+2500&arg0.getAccuracy()<50){
			LocationService.sendAlarmStop(arg0.getSpeed()*3.6,velprevia);
			velprevia=arg0.getSpeed()*3.6;
		}
		if (arg0.getSpeed()*3.6>max_speed&arg0.getAccuracy()<50){
			LocationService.sendAlarmSpeed(arg0.getSpeed()*3.6);
		}*/
		fecha=new Timestamp(arg0.getTime());
		LocationService.fecha=fecha;


		//lugar=LocationService.getLugar();
		//lugar="Desconocido";
		previa.setLatitude(latitudprevia);
		previa.setLongitude(longitudprevia);
		distance=arg0.distanceTo(previa);
		/*if (distance>3 || lugar==null){
			lugar=LocationService.getLugar();
		}*/
		if (distance > 0.5 & arg0.getAccuracy() <= 100 & calculatekm) {

			metrosr = distance + metrosr;
			LocationService.km = metrosr;
			latitudprevia = arg0.getLatitude();
			longitudprevia = arg0.getLongitude();
		}
		/*if (lugar!=null) {
			if (((lugar.contains("Predio") || lugar.contains("Terminal")))) {
				if (distance > 3.5 & arg0.getAccuracy() <= 30 & calculatekm) {
					//metrosr=LocationService.getMetrosRecorridos();
					metrosr = metrosr + distance;
					LocationService.km = metrosr;
					latitudprevia = arg0.getLatitude();
					longitudprevia = arg0.getLongitude();
				}
			} else {
				if (distance > 0.5 & arg0.getAccuracy() <= 100 & calculatekm) {

					metrosr = distance + metrosr;
					LocationService.km = metrosr;
					latitudprevia = arg0.getLatitude();
					longitudprevia = arg0.getLongitude();
				}
			}
		}*
		/*if (setTimeCarrera&segstop<20){
			setTimeCarrera=false;
			LocationService.editor.putBoolean("SetTimeCarrera", setTimeCarrera).commit();
			LocationService.setInicioCarrera();
		}*/

		if (isGettingParada==false ){
			fechaprevia4=arg0.getTime();
			isGettingParada=true;
			//scheduler.remove(threadgetparada);
			getParadaCercana();

		}
		/*if (isSetBarrasOff==false & (arg0.getTime()>=fechaprevia6+4000)){
			fechaprevia6=arg0.getTime();
			isSetBarrasOff=true;
			//scheduler.remove(threadgetparada);
			setSenalNoContar();

		}*/
		if (isGettingRamal==false){
			fechaprevia5=arg0.getTime();
			isGettingRamal=true;
			//scheduler.remove(threadgetramal);
			getRamalCercano();
		}


		/*if (valoresestaticos.getSimulate()){
			if (segstop>30&isInRamal&sendData&ramalencontrado!=0&ramalencontrado!=11&ramalencontrado!=12&ramalencontrado!=26&ramalencontrado!=27)
			{

				sendData=false;
				LocationService.editor.putBoolean("sendData", sendData);
				sentido=2;
				LocationService.setSentido(sentido);
				LocationService.setRamal(-1,true,sentido);
				setTimeCarrera=true;
				LocationService.editor.putBoolean("SetTimeCarrera", setTimeCarrera);

			}
			if (LocationService.sendDatosramal==false){
				LocationService.sendDatosramal=true;
			}
		}*/
		mLastLocation = arg0;
		velprevia=arg0.getSpeed()*3.6;
		fechaprevia3=arg0.getTime();
		LocationService.latitudprevia=latitudprevia;
		LocationService.longitudprevia=longitudprevia;
		LocationService.km=metrosr;
		/*if (segstop>=300&segstop<360&isInRamal==false&sendLugarStop==false){
			sendLugarStop=true;
			final int min=(int)segstop/60;
			if (LocationService.isSavingToLocal==false)
			{
			scheduler.execute(new Runnable() {

				@Override
				public void run() {
					LocationService.sendAlarmLugarStop(min);
				}
			});
			}

		}*/
		/*if (fecha.getTime()>=fechaprevia2+ValoresEstaticos.ESPERA_ENVIO_LOCAL)
		{
			//
			fechaprevia2=fecha.getTime();
			if (LocationService.isSavingToLocal==false)
			{
				//scheduler.remove(threadsendtolocal);
				LocationService.sendToLocal(arg0);
			}
		}*/

		/*if (fecha.getTime()>=fechaprevia2+ValoresEstaticos.ESPERA_ENVIO_LOCAL&LocationService.datosrastreo.getBoolean("isConnect", true)==false)
		{
			fechaprevia2=fecha.getTime();
			if (LocationService.isSavingToLocal==false)
			{
				//scheduler.remove(threadsendtolocal);
				scheduler.submit(threadsendtolocal);
			}


		}*/
		//ENVIA DATOS AL SERVIDOR CADA X SEGUNDOS;
		LocationService.sendToServer(locationactual);
		LocationService.sendToLocal(locationactual);
		LocationService.sendDiferido(locationactual.getTime());

		if (arg0.getSpeed()>2||(distance>=10&arg0.getAccuracy()<=30)){
			/*scheduler.execute(new Runnable() {

				@Override
				public void run() {
					if (setTimeCarrera){
						LocationService.editor.putBoolean("SetTimeCarrera", false);
					}
					setTimeCarrera=false;
					if (sendLugarStop&segstop>=300){
						int min=(int)segstop/60;
						LocationService.sendAlarmLugarStop(min);
						sendLugarStop=false;
					}

				}
			});	*/
			segstop=0;

		}
		else{

			segstop=segstop+2;



		}

		/*if (segstop>90&isInRamal&arg0.getSpeed()*3.6>1&arg0.getAccuracy()<40){
			setTimeCarrera=true;
			LocationService.setInicioCarrera();
		}*/




		//if(scheduler.getActiveCount()>activethread){
		//activethread=scheduler.getActiveCount();//}
		//scheduler.purge();
		//scheduler.shutdown();
		//scheduler=null;
		/*try {
			scheduler.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {

		}*/
		/*LocationService.handler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, String.valueOf(scheduler.getActiveCount()), 20).show();

			}
		});
		*/
	}
	public static void getRamalCercano(){

		//DETECTA RAMAL
		Logger.getLogger("Coordenadas").log(Level.SEVERE,"Get Ramal Cercano");
		float distance2=0;
		if (DatosEstaticosRastreo.getRamalEncontrado()!=30&DatosEstaticosRastreo.getRamalEncontrado()>=0)
		{
			locationramal.setLatitude(latitudesramales[DatosEstaticosRastreo.getRamalEncontrado()]);
			locationramal.setLongitude(longitudesramales[DatosEstaticosRastreo.getRamalEncontrado()]);
			distance2=locationactual.distanceTo(locationramal);


		}
		else
		{
			distance2=(int)500;
		}
		if (LocationService.IsInTerminal&distance2>300){
			LocationService.IsInTerminal=false;
			DatosEstaticosRastreo.setContadoresSalidas();
		}
		if (locationactual.distanceTo(location_multiplaza_testpoint)<=75&segstop>=120&DatosEstaticosRastreo.getRamalMayorPeso()!=13){
			LocationService.sendDatosramal=true;
			LocationService.editor.putBoolean("SendDataRamal", LocationService.sendDatosramal).commit();
			DatosEstaticosRastreo.setNombreRamalMayorPeso("Multiplaza");
			DatosEstaticosRastreo.setRamalMayorPeso(13);
			isGettingRamal=false;
			return;

		}
		if ((int)distance2>200||(DatosEstaticosRastreo.getRamalEncontrado()==1 & distance2>50)||(DatosEstaticosRastreo.getRamalEncontrado()==12 & distance2>100)||LocationService.setFirstRamal2||isInRamal==false)
		{

			int i=0;
			int rango=50;
			//SE PONE LENGHT-2 YA QUE LOS ULTIMOS PUNTOS DE RAMAL SON PARA TESTPOINT DE MULTIPLAZA.
			while(i<=latitudesramales.length-2)
			{
				//RANGO PARA RAMAL DE ESCAZU CENTRO. DEBIDO A QUE PASAN MUCHOS BUSES CERCA SE ESTABLECE EN 15 MTS
				if (i==2||i==1)
				{
					if (i==1){
						rango=50;
					}
					if (i==2){
						rango=50;
					}
				}

				else
				{
					//SI SE ESTA PROBANDO UN RAMAL DIFERENTE DE TERMINAL EL RANGO ES 75 SINO EL RANGO ES 160
					if (i!=0&i!=12&i!=27){
						rango=150;
					}

					if (i==27||i==0){
						rango=300;
					}
					if(i==27){
						rango=300;
					}
					if (i==12){
						rango=100;
					}
					//SE CAMBIA RANGO A RAMAL DE MULTIPLAZA DE 50 A 350 YA QUE PUEDE SER QUE BUSES NO LLEGUEN TAN CERCA DEL PUNTO DE RAMAL

					if (i==13){
						rango=350;
					}
					if(i==27||i==28){
						rango=100;
					}
					if (i==29){
						rango=50;
					}
					if (i==9){
						rango=500;
					}
				}
				locationramal.setLatitude(latitudesramales[i]);
				locationramal.setLongitude(longitudesramales[i]);
				distance2=locationactual.distanceTo(locationramal);
				if ((int)distance2<=rango)
				{

					if (i!=DatosEstaticosRastreo.getRamalEncontrado()||isInRamal==false||LocationService.setFirstRamal2)
					{
						if ((i==11||i==0))
						{

							DatosEstaticosRastreo.setRamalEncontrado(i);
							DatosEstaticosRastreo.setRamalEncontrado(i);
							//setSentido=false;
							DatosEstaticosRastreo.setSentido(1);
							isInRamal=true;
							sendData=true;
							LocationService.editor.putBoolean("sendData", sendData).commit();
							LocationService.editor.putBoolean("isInRamal", isInRamal).commit();
							LocationService.IsInTerminal=true;
							LocationService.setFirstRamal2=false;
							LocationService.editor.putBoolean("setFirstRamal2", false).commit();
							LocationService.setRamal(0,true,1);
							//}
							break;

						}
						else
						{
							if (i==12)
							{
								DatosEstaticosRastreo.setRamalEncontrado(i);
								//if(setSentido)
								//{
								DatosEstaticosRastreo.setSentido(2);
								//setSentido=false;
								isInRamal=true;
								sendData=true;

								LocationService.editor.putBoolean("sendData", sendData);
								LocationService.editor.putBoolean("isInRamal", isInRamal);
								//LocationService.editor.putBoolean("setSentido", setSentido);
								LocationService.editor.commit();
								LocationService.IsInTerminal=true;
								//LocationService.setFirstRamal2=false;
								LocationService.setRamal(0,true,2);
								//}
								break;
							}
							if (i==29)
							{
								DatosEstaticosRastreo.setRamalEncontrado(i);
								//if(setSentido)
								//{
								sendData=false;
								LocationService.editor.putBoolean("sendData", sendData);
								LocationService.editor.commit();
								DatosEstaticosRastreo.setSentido(2);

								LocationService.IsInTerminal=true;
								DatosEstaticosRastreo.setNombreRamalMayorPeso("Escazu Centro");
								LocationService.sendDatosramal=true;
								LocationService.editor.putBoolean("SendDataRamal", LocationService.sendDatosramal);
								DatosEstaticosRastreo.setRamalMayorPeso(2);

								LocationService.setRamal(-1,true,2);
								setTimeCarrera=true;



								//}
								break;
							}
							else
							{
								if (i==26)
								{
									if (DatosEstaticosRastreo.getRamalEncontrado()>=24){
										DatosEstaticosRastreo.setRamalEncontrado(i);
										//if(setSentido)
										//{
										DatosEstaticosRastreo.setSentido(2);
										//setSentido=false;
										isInRamal=true;
										sendData=true;
										LocationService.editor.putBoolean("sendData", sendData);
										LocationService.editor.putBoolean("isInRamal", isInRamal);
										//LocationService.editor.putBoolean("setSentido", setSentido);
										LocationService.editor.commit();
										LocationService.IsInTerminal=true;
										LocationService.setRamal(0,true,2);
										//}
										break;
									}
									break;
								}
								else
								{
									//COMPORTAMIENTO PARA RAMAL MUSMANNI. SI ES BARRIO SE COMPORTA COMO TERMINAL SJ, SINO NO HACE NADA.
									if (i==27||i==28){
										if (DatosEstaticosRastreo.getRamalEncontrado()<24&DatosEstaticosRastreo.getRamalEncontrado()!=26&i==27){
											DatosEstaticosRastreo.setRamalEncontrado(i);
											//if(setSentido)
											//{
											//setSentido=false;
											DatosEstaticosRastreo.setSentido(1);
											isInRamal=true;
											sendData=true;
											LocationService.editor.putBoolean("sendData", sendData);
											LocationService.editor.putBoolean("isInRamal", isInRamal);
											//LocationService.editor.putBoolean("setSentido", setSentido);
											LocationService.editor.commit();
											LocationService.IsInTerminal=true;
											LocationService.setRamal(0,true,1);
											//}
											break;
										}
										else{

											DatosEstaticosRastreo.setRamalEncontrado(i);
										}
										break;
									}
									else
									{

										DatosEstaticosRastreo.setRamalEncontrado(i);
										if (latituddetectaramal==0||longituddetectaramal==0)
										{
											latituddetectaramal=locationactual.getLatitude();
											longituddetectaramal=locationactual.getLongitude();

										}
										//detectoRamal.setLatitude(locationactual.getLatitude());
										//detectoRamal.setLongitude(locationactual.getLongitude());

										isInRamal=true;
										sendData=true;
										LocationService.editor.putBoolean("sendData", sendData).commit();
										LocationService.editor.putBoolean("isInRamal", isInRamal).commit();
										LocationService.setRamal(i,false,DatosEstaticosRastreo.getSentido());
										break;
									}
								}

							}
						}
					}
					else
					{
						break;
					}

				}
				else
				{
					if (isInRamal)
					{

						isInRamal=false;
						latituddetectaramal=0;
						longituddetectaramal=0;
						LocationService.editor.putBoolean("isInRamal", isInRamal);
						LocationService.editor.commit();
					}

				}
				i++;
			}

		}

		else
		{
			if (segstop>=60&(DatosEstaticosRastreo.getRamalEncontrado()==27||DatosEstaticosRastreo.getRamalEncontrado()==28)&DatosEstaticosRastreo.getRamalMayorPeso()>=24&isInRamal&sendData){
				sendData=false;
				LocationService.editor.putBoolean("sendData", sendData);
				LocationService.editor.commit();
				DatosEstaticosRastreo.setSentido(2);
				LocationService.IsInTerminal=true;
				LocationService.setRamal(-1,true,2);
				setTimeCarrera=true;
			}
			else
			{
				if (segstop>=15&DatosEstaticosRastreo.getRamalMayorPeso()==DatosEstaticosRastreo.getUltimoRamalEncontrado()&isInRamal&sendData&DatosEstaticosRastreo.getRamalEncontrado()!=0&DatosEstaticosRastreo.getRamalEncontrado()!=11&DatosEstaticosRastreo.getRamalEncontrado()!=12&DatosEstaticosRastreo.getRamalEncontrado()!=26&DatosEstaticosRastreo.getRamalEncontrado()!=27&LocationService.setFirstRamal2==false)
				{
					sendData=false;
					LocationService.editor.putBoolean("sendData", sendData);
					LocationService.editor.commit();
					DatosEstaticosRastreo.setSentido(2);
					LocationService.IsInTerminal=true;
					LocationService.setRamal(-1,true,2);
					setTimeCarrera=true;
				}
				else{
					if (segstop>=10&DatosEstaticosRastreo.getRamalMayorPeso()>DatosEstaticosRastreo.getUltimoRamalEncontrado()&DatosEstaticosRastreo.getRamalMayorPeso()!=DatosEstaticosRastreo.getUltimoRamalEncontrado()&(DatosEstaticosRastreo.getRamalEncontrado()!=2&DatosEstaticosRastreo.getRamalEncontrado()!=28)&isInRamal&sendData&DatosEstaticosRastreo.getRamalEncontrado()!=0&DatosEstaticosRastreo.getRamalEncontrado()!=11&DatosEstaticosRastreo.getRamalEncontrado()!=12&DatosEstaticosRastreo.getRamalEncontrado()!=26&DatosEstaticosRastreo.getRamalEncontrado()!=27&LocationService.setFirstRamal2==false)
					{
						sendData=false;
						LocationService.editor.putBoolean("sendData", sendData);
						LocationService.editor.commit();
						DatosEstaticosRastreo.setSentido(2);
						LocationService.IsInTerminal=true;
						LocationService.setRamal(-1,true,2);
						setTimeCarrera=true;
					}
					else{
						if (segstop>=30&DatosEstaticosRastreo.getRamalMayorPeso()<DatosEstaticosRastreo.getUltimoRamalEncontrado()&DatosEstaticosRastreo.getRamalMayorPeso()!=DatosEstaticosRastreo.getUltimoRamalEncontrado()&(DatosEstaticosRastreo.getRamalEncontrado()!=2&DatosEstaticosRastreo.getRamalEncontrado()!=28)&isInRamal&sendData&DatosEstaticosRastreo.getRamalEncontrado()!=0&DatosEstaticosRastreo.getRamalEncontrado()!=11&DatosEstaticosRastreo.getRamalEncontrado()!=12&DatosEstaticosRastreo.getRamalEncontrado()!=26&DatosEstaticosRastreo.getRamalEncontrado()!=27&LocationService.setFirstRamal2==false)
						{
							sendData=false;
							LocationService.editor.putBoolean("sendData", sendData);
							LocationService.editor.commit();
							DatosEstaticosRastreo.setSentido(2);
							LocationService.IsInTerminal=true;
							LocationService.setRamal(-1,true,2);
							setTimeCarrera=true;
						}
						else
							//LOGICA SI CAMBIA DE UN RAMAL DE MAYOR PESO A OTRO DE MENOR PESO EJEMPLO DE PAVICEN (18) A POZOS IMAS(15)
							if (segstop>=30&DatosEstaticosRastreo.getSentido()==2&DatosEstaticosRastreo.getRamalMayorPeso()>DatosEstaticosRastreo.getRamalEncontrado()&DatosEstaticosRastreo.getRamalMayorPeso()!=DatosEstaticosRastreo.getUltimoRamalEncontrado()&(DatosEstaticosRastreo.getRamalEncontrado()!=2&DatosEstaticosRastreo.getRamalEncontrado()!=28)&isInRamal&sendData&DatosEstaticosRastreo.getRamalEncontrado()!=0&DatosEstaticosRastreo.getRamalEncontrado()!=11&DatosEstaticosRastreo.getRamalEncontrado()!=12&DatosEstaticosRastreo.getRamalEncontrado()!=26&DatosEstaticosRastreo.getRamalEncontrado()!=27&LocationService.setFirstRamal2==false)
							{
								sendData=false;
								LocationService.editor.putBoolean("sendData", sendData);
								LocationService.editor.commit();
								DatosEstaticosRastreo.setSentido(2);
								LocationService.IsInTerminal=true;
								LocationService.setFirstRamal2=true;
								LocationService.setRamal(DatosEstaticosRastreo.getRamalEncontrado(),false,DatosEstaticosRastreo.getSentido());
								setTimeCarrera=true;
							}
					}
				}



				if (LocationService.sendDatosramal==false){
					LocationService.sendDatosramal=true;
				}
			}
		}
		isGettingRamal=false;


	}
	public static boolean setRamal(int i,boolean sendCambio,int sentido2){
		Logger.getLogger("Coordenadas").log(Level.SEVERE,"Sent Ramal");
		if (datosrastreo==null || context==null||editor==null){
			context=getContext();
			datosrastreo=context.getSharedPreferences("DatosRastreo", Context.MODE_PRIVATE);
			if (editor==null){
				editor=datosrastreo.edit();
			}
		}
		if (setFirstRamal2){
			if (datosrastreo.getInt("firstEntradasPe", DataForSql.generalentradaspe)<DataForSql.generalentradaspe&lugar.indexOf("Predio")==-1&datosrastreo.getInt("firstEntradasPe", DataForSql.generalentradaspe)>0){

				if (i==1){DatosEstaticosRastreo.setNombreRamalMayorPeso("San Antonio");}
				if (i==2||i==29){DatosEstaticosRastreo.setNombreRamalMayorPeso("Escazu Centro");}
				if (i==3){DatosEstaticosRastreo.setNombreRamalMayorPeso("Santa Teresa");}
				if (i==4){DatosEstaticosRastreo.setNombreRamalMayorPeso("Corazon de Jesus");}
				if (i==5){DatosEstaticosRastreo.setNombreRamalMayorPeso("Vista Oro");}
				if (i==6){DatosEstaticosRastreo.setNombreRamalMayorPeso("El Carmen");}
				if (i==7){DatosEstaticosRastreo.setNombreRamalMayorPeso("Bebedero");}
				if (i==8){DatosEstaticosRastreo.setNombreRamalMayorPeso("Lotes");}
				if (i==9){DatosEstaticosRastreo.setNombreRamalMayorPeso("Curio");}
				if (i==10){DatosEstaticosRastreo.setNombreRamalMayorPeso("Bello Horizonte");}
				if (i==13){DatosEstaticosRastreo.setNombreRamalMayorPeso("Multiplaza");}
				if (i==14){DatosEstaticosRastreo.setNombreRamalMayorPeso("Guachipelin");}
				if (i==15){DatosEstaticosRastreo.setNombreRamalMayorPeso("Pozos Imas");}
				if (i==16){DatosEstaticosRastreo.setNombreRamalMayorPeso("Pavicen");}
				if (i==17){DatosEstaticosRastreo.setNombreRamalMayorPeso("Lindora");}
				if (i==18){DatosEstaticosRastreo.setNombreRamalMayorPeso("Los Angeles");}
				if (i==19){DatosEstaticosRastreo.setNombreRamalMayorPeso("Salitral");}
				if (i==20){DatosEstaticosRastreo.setNombreRamalMayorPeso("Montoya");}
				if (i==21){DatosEstaticosRastreo.setNombreRamalMayorPeso("Matinilla");}
				if (i==22){DatosEstaticosRastreo.setNombreRamalMayorPeso("Barrio Espana");}
				if (i==23){DatosEstaticosRastreo.setNombreRamalMayorPeso("Ciudad Colon");}
				if (i==24){DatosEstaticosRastreo.setNombreRamalMayorPeso("Sta Ana Calle Vieja");}
				if (i==25){DatosEstaticosRastreo.setNombreRamalMayorPeso("Sta Ana Pista");}
				//SE CAMBIA RAMALES DE PISTA Y CALLEVIEJA AL MAYOR NUMERO YA QUE HAY CARRERAS DE BARRIOS HACIA SJ Y SE PUEDEN IR POR PISTA O POR CV
				//A PARTIR DE VERSION 29.7


				sendDatosramal=true;
				editor.putBoolean("SendDataRamal", sendDatosramal).commit();
				DatosEstaticosRastreo.setRamalMayorPeso(i);
				DatosEstaticosRastreo.setUltimoRamalEncontrado(i, DatosEstaticosRastreo.getNombreRamalMayorPeso());
				setFirstRamal2=false;
				editor.putBoolean("setFirstRamal2", false).commit();
				return true;
			}
		}
		if (i==2 & DatosEstaticosRastreo.getRamalMayorPeso()==13){
			DatosEstaticosRastreo.setNombreRamalMayorPeso("Multiplaza Escazu");
			sendDatosramal=true;
			editor.putBoolean("SendDataRamal", sendDatosramal).commit();
			DatosEstaticosRastreo.setRamalMayorPeso(100);

		}
		if (i>=DatosEstaticosRastreo.getRamalMayorPeso())
		{


			//SI PASA POR RAMAL ESCAZU CENTRO (2) Y ANTES VENIA DE SAN ANTONIO (1) SIGUE COMO RAMAL SAN ANTONIO (1)
			if (DatosEstaticosRastreo.getRamalMayorPeso()==1&i==2){
				DatosEstaticosRastreo.setRamalMayorPeso(1);
				i=1;
			}
			if (DatosEstaticosRastreo.getRamalMayorPeso()==2&DatosEstaticosRastreo.getUltimoRamalEncontrado()==2&DatosEstaticosRastreo.getSentido()==1&i==3){
				return true;
			}
			//TERMINAL-SAN ANTONIO-CENTRO-CORAZON-VISTA ORO-STA TERESA-CARMEN-BEBEDERO-LOTES-CURIO-BELLO
			if (i==1){DatosEstaticosRastreo.setNombreRamalMayorPeso("San Antonio");}
			if (i==2||i==29){DatosEstaticosRastreo.setNombreRamalMayorPeso("Escazu Centro");}
			if (i==3){DatosEstaticosRastreo.setNombreRamalMayorPeso("Santa Teresa");}
			if (i==4){DatosEstaticosRastreo.setNombreRamalMayorPeso("Corazon de Jesus");}
			if (i==5){DatosEstaticosRastreo.setNombreRamalMayorPeso("Vista Oro");}
			if (i==6){DatosEstaticosRastreo.setNombreRamalMayorPeso("El Carmen");}
			if (i==7){DatosEstaticosRastreo.setNombreRamalMayorPeso("Bebedero");}
			if (i==8){DatosEstaticosRastreo.setNombreRamalMayorPeso("Lotes");}
			if (i==9){DatosEstaticosRastreo.setNombreRamalMayorPeso("Curio");}
			if (i==10){DatosEstaticosRastreo.setNombreRamalMayorPeso("Bello Horizonte");}
			if (i==13){DatosEstaticosRastreo.setNombreRamalMayorPeso("Multiplaza");}
			if (i==14){DatosEstaticosRastreo.setNombreRamalMayorPeso("Guachipelin");}
			if (i==15){DatosEstaticosRastreo.setNombreRamalMayorPeso("Pozos Imas");}
			if (i==16){DatosEstaticosRastreo.setNombreRamalMayorPeso("Pavicen");}
			if (i==17){DatosEstaticosRastreo.setNombreRamalMayorPeso("Lindora");}
			if (i==18){DatosEstaticosRastreo.setNombreRamalMayorPeso("Los Angeles");}
			if (i==19){DatosEstaticosRastreo.setNombreRamalMayorPeso("Salitral");}
			if (i==20){DatosEstaticosRastreo.setNombreRamalMayorPeso("Montoya");}
			if (i==21){DatosEstaticosRastreo.setNombreRamalMayorPeso("Matinilla");}
			if (i==22){DatosEstaticosRastreo.setNombreRamalMayorPeso("Barrio Espana");}
			if (i==23){DatosEstaticosRastreo.setNombreRamalMayorPeso("Ciudad Colon");}
			if (i==24){DatosEstaticosRastreo.setNombreRamalMayorPeso("Sta Ana Calle Vieja");}
			if (i==25){DatosEstaticosRastreo.setNombreRamalMayorPeso("Sta Ana Pista");}
			sendDatosramal=true;
			editor.putBoolean("SendDataRamal", sendDatosramal).commit();
			DatosEstaticosRastreo.setRamalMayorPeso(i);
			editor.commit();
			return true;
		}
		if (updateRamal){
			if (ModuloSQL.Ejecutar(updateramal, false)){
				updateRamal=false;
			}
		}
		if ((i==0||i==-1)&sendDatosramal){
			sendDatosramal=false;
			if (DatosEstaticosRastreo.getUltimoRamalEncontrado()==-1&DatosEstaticosRastreo.getRamalMayorPeso()>0)
			{
				DatosEstaticosRastreo.setUltimoRamalEncontrado(DatosEstaticosRastreo.getRamalMayorPeso(), DatosEstaticosRastreo.getNombreRamalMayorPeso());

			}
			if (setFirstRamal){
				DatosEstaticosRastreo.setUltimoRamalEncontrado(DatosEstaticosRastreo.getRamalMayorPeso(), DatosEstaticosRastreo.getNombreRamalMayorPeso());

				Calendar calendar=Calendar.getInstance();
				calendar.set(Calendar.HOUR, 04);
				calendar.set(Calendar.MINUTE, 00);
				calendar.set(Calendar.SECOND, 00);
				setFirstRamal=false;
				String update="update tablamonitoreo set Ramal='"+DatosEstaticosRastreo.getNombreRamalMayorPeso()+"' where Fecha>='"+new Timestamp(calendar.getTimeInMillis())+"' and Bus='"+bus+"'";

				ModuloSQL.Ejecutar(update, false);

			}
			Calendar c=Calendar.getInstance();

			if (DatosEstaticosRastreo.getUltimoRamalEncontrado()!=DatosEstaticosRastreo.getRamalMayorPeso())
			{
				if (DatosEstaticosRastreo.getRamalMayorPeso()==100){
					DatosEstaticosRastreo.setRamalMayorPeso(13);

				}
				contador_cambiosramal++;
				editor.putInt("Contador_CambiosRamal", contador_cambiosramal).commit();
				DatosEstaticosRastreo.setUltimoRamalEncontrado(DatosEstaticosRastreo.getRamalMayorPeso(), DatosEstaticosRastreo.getNombreRamalMayorPeso());
				updateRamal=true;

			}

			if (setFirstRamal==false){
				tiempocarrera= c.getTimeInMillis()-datosrastreo.getLong("LastTerminal", c.getTimeInMillis());
				editor.putLong("LastTerminal", c.getTimeInMillis()).commit();


			}
			else{
				setFirstRamal=false;
				datosrastreo.edit().putBoolean("setFirstRamal", setFirstRamal).commit();
			}
			if (i==0){
				DatosEstaticosRastreo.setRamalMayorPeso(i);
				//sramal="Terminal";
				//editor.putString("Ramal", sramal);

			}

			return true;

		}
		else {
			if (i==0){
				DatosEstaticosRastreo.setRamalMayorPeso(i);

				return true;
			}
		}
		return true;
	}
	public static void getParadaCercana(){

		//DETECTA PARADA
		Logger.getLogger("Coordenadas").log(Level.SEVERE,"Get Parada Cercana");

		if (locationactual.distanceTo(locationParadaEncontrada)>=25){
			float distance2=50;
			int i=0;
			int rango=25;

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
						locationParadaEncontrada.setLatitude(latitudesparadas1_2[i]);
						locationParadaEncontrada.setLongitude(longitudesparadas1_2[i]);
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

			while(i<=latitudesparadas2_1.length-1)
			{
				locationparada.setLatitude(latitudesparadas2_1[i]);
				locationparada.setLongitude(latitudesparadas2_1[i]);
				distance2=locationactual.distanceTo(locationparada);
				//Toast.makeText(context, String.valueOf(distance2), Toast.LENGTH_SHORT).show();
				if ((int)distance2<=rango)
				{
					//Toast.makeText(context, "GET PARADA CERCANA", Toast.LENGTH_SHORT).show();
					if (i!=paradaencontrada)
					{
						locationParadaEncontrada.setLatitude(latitudesparadas2_1[i]);
						locationParadaEncontrada.setLongitude(latitudesparadas2_1[i]);
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


			//}
			//Toast.makeText(context, "FinParada", 30);
			isGettingParada=false;
		}
		else{
			isGettingParada=false;
		}
	}
	public static void sendDiferido(final long time){
		//logger.info("SendDiferido. Comenzo envio diferido");
		Logger.getLogger("Coordenadas").log(Level.SEVERE, "Send Diferido");
		sentencia="";

		baselocal_diferido =BaseSQLLocal.getInstance(context,Environment.getExternalStorageDirectory().getPath()+"/Rastreo "+String.valueOf(bus)+".sqlite");
		db_diferido = baselocal_diferido.getReadableDatabase();
		if (db_diferido.isOpen()==false){

			logger.info("SendDiferido. Error base de datos no abierta");
			isSendingDiferido=false;
			baselocal_diferido.close();
			Calendar c=Calendar.getInstance();
			Timestamp f=new Timestamp(c.getTimeInMillis());
			String s="insert into MonitoreoMovil_Alarmas values ('"+f+"','ERR','"+bus+"','Cursor Null','"+0+"')";
			ModuloSQL.Ejecutar(s, false);
			return;
		}
		Cursor cur;
		//Calendar cal = Calendar.getInstance();
		//int offset = cal.getTimeZone().getOffset(time+20000);
		//Log.i("SendDiferido", String.valueOf(offset));
		fechadif=new Timestamp(time);
		//fechadif=new Timestamp (time+20000-21600000);
		int counter=0;
		cur=db_diferido.query(false, "Rastreo", new String[]{"Fecha","Latitud","Longitud","Precision","Velocidad","Metros_Recorridos","Imei","Bus","VelocidadMax","Lugar","TotalVIPS","GeneralVIPS","Bateria","EntradasPe","SalidasPe","EntradasPs","SalidasPs","GeneralEntradasPe","GeneralSalidasPe","GeneralEntradasPs","GeneralSalidasPs","BloqPe","GeneralBloqPe","BloqPs","GeneralBloqPs","IdParada","NombreParada","Proveedor","Sentido","Ramal","VersionFirmware","TipoRed","ContadorCarreras","ActiveThread","LastID","MarcasEnBus","ContadorCambiosRamal","EnRamal","SendDataRamal","RamalEncontrado","SegStop"}, "Fecha >= '"+fechadif+"'", null, null, null, "Fecha ASC", "0,100",null);
		//Log.i("SendDiferido", fechadif.toString());
		if (cur==null){
			logger.info("SendDiferido. Cursor null");
			isSendingDiferido=false;
			db_diferido.close();
			baselocal_diferido.close();
			Calendar c=Calendar.getInstance();
			Timestamp f=new Timestamp(c.getTimeInMillis());
			String s="insert into MonitoreoMovil_Alarmas values ('"+f+"','ERR','"+bus+"','Cursor Null','"+0+"')";
			ModuloSQL.Ejecutar(s, false);
			return;
		}
		if (cur.moveToFirst()){

			fechadif=Timestamp.valueOf(cur.getString(cur.getColumnIndex("Fecha")));
			sentencia=sentencia+" ('"+fechadif+"','"+cur.getFloat(cur.getColumnIndex("Latitud"))+"','"+cur.getFloat(cur.getColumnIndex("Longitud"))+"','"+cur.getString(cur.getColumnIndex("NombreParada"))+"','"+cur.getFloat(cur.getColumnIndex("Precision"))+"','Diferido','"+cur.getString(cur.getColumnIndex("Lugar"))+"','"+cur.getFloat(cur.getColumnIndex("Metros_Recorridos"))+"')";
			//Log.i("SendDiferido1", fechadif.toString());
			while(cur.moveToNext()&counter<=100){

				//Log.i("SendDiferido2", fechadif.toString());
				fechadif=Timestamp.valueOf(cur.getString(cur.getColumnIndex("Fecha")));
				km=cur.getFloat(cur.getColumnIndex("Metros_Recorridos"));
				sentencia=sentencia+",('"+fechadif+"','"+cur.getFloat(cur.getColumnIndex("Latitud"))+"','"+cur.getFloat(cur.getColumnIndex("Longitud"))+"','"+cur.getString(cur.getColumnIndex("NombreParada"))+"','"+cur.getFloat(cur.getColumnIndex("Precision"))+"','Diferido','"+cur.getString(cur.getColumnIndex("Lugar"))+"','"+cur.getFloat(cur.getColumnIndex("Metros_Recorridos"))+"')";
				counter++;
			}


			sentencia="insert into coordenadas (Fecha, Latitud, Longitud, Nombre_Parada_SOL, Exactitud, Proveedor, Lugar_Ref_Sol, km) values "+sentencia;
			//Log.i("SendDiferido3", sentencia);
			Logger.getLogger("Coordenadas").log(Level.SEVERE, sentencia);


		}
		else{

			//Log.i("SendDiferido", db.getPath());
			logger.info("SendDiferido. Error, no se encontró informacion en base de datos local");
			sentencia=null;
			Calendar c=Calendar.getInstance();
			Timestamp f=new Timestamp(c.getTimeInMillis());
			String s="insert into MonitoreoMovil_Alarmas values ('"+f+"','ERR','"+bus+"','Cursor Null','"+0+"')";
			ModuloSQL.Ejecutar(s, false);
			//isSendingDiferido=false;
		}


		if (sentencia!=null){
			//logger.info("SendDiferido. Comenzo Envio Diferido");
			sentencia= Normalizer.normalize(sentencia, Normalizer.Form.NFD);
			sentencia = sentencia.replaceAll("[^\\p{ASCII}]", "");
			//if (
			ModuloSQL.Ejecutar(sentencia,true);
			LocationService.setUltimaTransmision(fechadif.getTime(), km);
			sentencia="";
			//isSendingDiferido=false;


		}
		else{
			logger.info("SendDiferido. Error, sentencia es null");
			sentencia="";
			Calendar c=Calendar.getInstance();
			Timestamp f=new Timestamp(c.getTimeInMillis());
			String s="insert into MonitoreoMovil_Alarmas values ('"+f+"','ERR','"+bus+"','Sentencia Null','"+0+"')";
			ModuloSQL.Ejecutar(s, false);
			//isSendingDiferido=false;
		}
		if (cur!=null){
			cur.close();
		}
		if (db_diferido!=null){
			db_diferido.close();
		}
		if (baselocal_diferido!=null){
			baselocal_diferido.close();
		}
		//logger.info("SendDiferido. Termino enviando diferido");
		isSendingDiferido=false;
	}
	public static void sendToServer(final Location arg0){
		Logger.getLogger("Coordenadas").log(Level.SEVERE,"Send to Server");
		//Toast.makeText(LocationService.getContext(), "Send to Server", Toast.LENGTH_SHORT).show();
		//logger.info("SendToSQLServer. Comenzo Enviado a SQL");
		isisSendingtoSQL=true;
		if (editor==null||datosrastreo==null){
			datosrastreo=context.getSharedPreferences("DatosRastreo",Context.MODE_PRIVATE);
			editor=datosrastreo.edit();
		}
		editor.commit();
		//getLugar(arg0);
		//if (isConnectedtoInternet()){

			//Toast.makeText(context, "Net Connected", Toast.LENGTH_SHORT);
			/*if (testlasttransmision){
				
				ResultSet rs=ModuloSQL.Listar("select Fecha, Km_recorridos from TablaMonitoreo where Bus='"+bus+"' order by Fecha Desc Limit 1");
				if (rs!=null){
					try {
					if (rs.first()){
						LocationService.setUltimaTransmision(rs.getTimestamp("Fecha").getTime(), rs.getFloat("Km_recorridos"));
						rs.close();
						rs=null;
					}
					testlasttransmision=false;
					isisSendingtoSQL=false;
				} catch (java.sql.SQLException e) {
					//Toast.makeText(LocationService.getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
					//prefsparametros.edit().putString("Error SQL", e.getLocalizedMessage()).commit();
					testlasttransmision=true;
					isisSendingtoSQL=false;
					return;
				}
			}
				
			}*/
			//else{
				//contadoralarm=0;
				//datosrastreo.edit().putInt("ContadorAlarm", contadoralarm);
			
				if (ultimatransmision==0){
					ultimatransmision=datosrastreo.getLong("UltimaTransmision", ultimatransmision);
				}
				if (ultimatransmision==0){
					ultimatransmision=Calendar.getInstance().getTimeInMillis();
				}
				if (kmprevio==0||km==0){
					km=datosrastreo.getFloat("MetrosRecorridos", km);
					kmprevio=datosrastreo.getFloat("Metros_Recorridos_Previo", km);
					if (kmprevio==0){
						kmprevio=km;
						editor.putFloat("Metros_Recorridos_Previo", kmprevio).commit();
					
					}
					difkm=km-kmprevio;
				}
				else{
					if (kmprevio<1)
					{
						kmprevio=datosrastreo.getFloat("Metros_Recorridos_Previo", km);
					}
					difkm=km-kmprevio;
				}

					//SI NO ESTA LA TAREA SAVEINTOSQL EJECUTANDOSE INICIA LA TAREA PARA GUARDAR LOS DATOS.
						data.setLongitud(arg0.getLongitude());
						data.setLatitud(arg0.getLatitude());

						if (km>0){
							data.setKm_recorridos(km);
						}
						else{
							data.setKm_recorridos(getMetrosRecorridos());
						}
						
						data.setPrecision(arg0.getAccuracy());
						data.setProveedor(arg0.getProvider());
						data.setLugar(lugar);
						//data.setKm_recorridos(LocationGPS.metrosr);
						data.setParadaCercana(datosrastreo.getString("ParadaEncontradaS", "Desconocida"));

						//LocationService.logger.info("Termino calculo marcas actuales");
						//Toast.makeText(LocationService.getContext(), "save sql 2", Toast.LENGTH_LONG).show();
						lugarprevio=lugar;
						if (timesendsql==0)
							{
								timesendsql = Calendar.getInstance().getTimeInMillis();
							}
							contadorlugares=0;
							if (data.saveData()){
								LocationService.setUltimaTransmision(arg0.getTime(), km);
								//logger.info("SendToSQLServer. Se terminó con éxito envio online.");
								isisSendingtoSQL=false;
							}
							else{
								logger.info("SendToSQLServer. Falló envio online.");
								isisSendingtoSQL=false;
							}
								


							





		/*}

		else{
			logger.info("SendToSQLServer. Error Enviando a SQL. No hay internet");
			isSendingDiferido=false;
			isisSendingtoSQL=false;
			//Toast.makeText(LocationService.getContext(), "Sin Internet", Toast.LENGTH_LONG).show();
		}*/
		//GUARDO LOS DATOS DENTRO DE CLASE DATAFORSQL PARA GUARDARLOS EN EL SERVIDOR
		isisSendingtoSQL=false;
	}
	public static void sendToLocal(Location arg0){
		Logger.getLogger("Coordenadas").log(Level.SEVERE,"Send To Local");
		if (isSavingToLocal==false){
			isSavingToLocal=true;
			Calendar c=Calendar.getInstance();
			if(c.get(Calendar.HOUR_OF_DAY)==00 & resetdatabarras==false){
				if (handler==null){
					handler=new Handler();
				}
				if (datosrastreo==null || context==null || editor==null){
					context=getContext();
					datosrastreo=context.getSharedPreferences("DatosRastreo",Context.MODE_PRIVATE);
					editor=datosrastreo.edit();
				}

				//isAhorro=true;
				String sentencia="insert into MonitoreoMovil_Alarmas values ('"+fecha+"','INF','"+bus+"','Resumen diario de minutos fuera de ambos predios','"+datosrastreo.getInt("TiempofueraPredio", 0)+"')";
				String sentencia2="insert into MonitoreoMovil_Alarmas values ('"+fecha+"','INF','"+bus+"','Resumen diario de minutos fuera de lugares permitidos','"+datosrastreo.getInt("MinFueraPermitido", 0)+"')";
				ModuloSQL.Ejecutar(sentencia, false);
				ModuloSQL.Ejecutar(sentencia2, false);
				editor.putInt("MinFueraPermitido", 0).commit();
				editor.putInt("TiempofueraPredio", 0).commit();
				editor.putInt("ConsecutivoSentido", 0).commit();
				editor.putFloat("ContadorCarreras", (float) 0).commit();
				resetdatabarras=true;
				editor.putBoolean("resetDataBarras", resetdatabarras).commit();
				//LocationService.lmanager.removeGpsStatusListener(LocationGPS.listenerGPS);
				//LocationGPS.addlistener=false;
			/*handler.post(new Runnable(){

				@Override
				public void run() {
					selectLocationProvider(10);
					if (prefsparametros.getBoolean("IsBatteryConnected", ListenerBooteo.isConnect)){
						setAlarm(3000000);}
						else{
							setAlarm(900000);
						}
				}});*/
				LocationService.consultaSaldo();
				//editor.putBoolean("isAhorro", true).commit();
			}
			if(c.get(Calendar.HOUR_OF_DAY)>=04 & resetdatabarras&DataForSql.generalentradaspe>0){
				if (handler==null){
					handler=new Handler();
				}
				if (datosrastreo==null || context==null || editor==null){
					context=getContext();
					datosrastreo=context.getSharedPreferences("DatosRastreo",Context.MODE_PRIVATE);
					editor=datosrastreo.edit();
				}
				setFirstRamal=true;
				setFirstRamal2=true;
				editor.putBoolean("setFirstRamal2", setFirstRamal2).commit();
				editor.putInt("firstEntradasPe", DataForSql.generalentradaspe).commit();
				datosrastreo.edit().putFloat("ContadorCarreras", (float) 0.5).commit();
				DatosEstaticosRastreo.setUltimoRamalEncontrado(-1, DatosEstaticosRastreo.getNombreUltimoRamal());
				DatosEstaticosRastreo.setRamalMayorPeso(-1);
				contador_cambiosramal=0;
				editor.putInt("Contador_CambiosRamal", contador_cambiosramal).commit();


				DatosEstaticosRastreo.resetContadorCarreras();
				DatosEstaticosRastreo.setContadoresGenerales();

				Intent i=new Intent();
				i.setAction("com.iwop.rastreadormovil.resetdatabarras");
				context.sendBroadcast(i);

			}
			if (arg0.getAccuracy()<=500)
			{
				lugarseguro=false;
			}

			if (helperlugares==null || lugares == null || cursor_lugares ==null )
			{
				helperlugares=BaseLugaresLocal.getInstance(getContext());
				lugares=helperlugares.getReadableDatabase();
				cursor_lugares = lugares.rawQuery(" SELECT Latitud,Longitud,Lugar FROM Puntos", null);


			}


			if (lugares!=null){
				Location referencia=new Location("Referencia");
				cursor_lugares = lugares.rawQuery(" SELECT Latitud,Longitud,Lugar FROM Puntos", null);
				if(cursor_lugares.moveToFirst()&arg0.getAccuracy()<=500)
				{
					referencia.setLatitude(cursor_lugares.getFloat(cursor_lugares.getColumnIndex("Latitud")));
					referencia.setLongitude(cursor_lugares.getDouble(cursor_lugares.getColumnIndex("Longitud")));
					distancia=arg0.distanceTo(referencia);
					if (distancia<1000)
					{
						lugarseguro=true;
						lugar=cursor_lugares.getString(cursor_lugares.getColumnIndex("Lugar"));
						meters=distancia;

					}

					while (cursor_lugares.moveToNext())
					{
						referencia.setLatitude(cursor_lugares.getFloat(cursor_lugares.getColumnIndex("Latitud")));
						referencia.setLongitude(cursor_lugares.getDouble(cursor_lugares.getColumnIndex("Longitud")));
						distancia=arg0.distanceTo(referencia);
						if (distancia<1000 & distancia<meters)
						{
							lugarseguro=true;
							lugar=cursor_lugares.getString(cursor_lugares.getColumnIndex("Lugar"));
							meters=distancia;
							if (distancia<50){
								break;
							}
						}

					}
					editor.putString("Lugar", lugar).commit();
				}
			}

			context.sendBroadcast(getdatabarras);
			data.setLongitud(arg0.getLongitude());
			data.setLatitud(arg0.getLatitude());

			data.setKm_recorridos(getMetrosRecorridos());
			data.setPrecision(arg0.getAccuracy());

			meters=20000;
			distancia=3000;
			editor.putBoolean("LugarSeguro", lugarseguro).commit();
			editor.putBoolean("sendSeguro", sendSeguro).commit();
			if (lugarseguro==false){
				if (sendLugar){
					sendSeguro=true;
					editor.putBoolean("sendSeguro", sendSeguro).commit();
					editor.putBoolean("sendLugar", sendLugar).commit();
					editor.putLong("fechaLugarNoSeguro", c.getTimeInMillis()).commit();
					String sentencia="insert into MonitoreoMovil_Alarmas values ('"+fecha+"','LNP','"+bus+"','Unidad fuera del area permitida','"+0+"')";
					sendAlertaLugar(sentencia);

				}
				sendLugar=false;
				editor.putBoolean("sendLugar", sendLugar).commit();
			}
			if (lugarseguro & sendSeguro&arg0.getAccuracy()<=100){
				long l=datosrastreo.getLong("fechaLugarNoSeguro", c.getTimeInMillis());
				l=(c.getTimeInMillis()-l)/60000;
				int min=datosrastreo.getInt("MinFueraPermitido", 0);
				min=min+(int)l;
				editor.putInt("MinFueraPermitido", min).commit();
				sendSeguro=false;
				editor.putBoolean("sendSeguro", sendSeguro).commit();
				sendLugar=true;
				editor.putBoolean("sendLugar", sendLugar).commit();
				String sentencia="insert into MonitoreoMovil_Alarmas values ('"+fecha+"','LNP','"+bus+"','Unidad dentro del area permitida','"+min+"')";
				sendAlertaLugar(sentencia);
			}



			editor.putString("Lugar", lugar).commit();
			if (lugar.contains("Desconocido")){
				lugar=datosrastreo.getString("Lugar", "Desconocido");
			}


			//="";


			//SharedPreferences datosbarras=context.getSharedPreferences("DatosBarras",Context.MODE_MULTI_PROCESS);
			if (IsInTerminal){
				contadorentradas=DataForSql.generalentradaspe-(int)DatosEstaticosRastreo.getGeneralEntradas();
				if (contadorentradas<0){
					contadorentradas=DataForSql.contadorentradaspe;
				}

				editor.putInt("ContadorSalidasPrevio", DataForSql.generalsalidaspe+DataForSql.generalsalidasps).commit();
			}
			else{
				contadorentradas=DataForSql.generalentradaspe-(int)DatosEstaticosRastreo.getGeneralEntradas();
				contadorsalidas=(DataForSql.generalsalidaspe+DataForSql.generalsalidasps)-(int)DatosEstaticosRastreo.getGeneralSalidas();
				if (contadorentradas>=contadorsalidas){

				}
				else{


				}

			}
			//usdbh.onUpgrade(db, 2, 3);
			String sentencia="INSERT INTO Rastreo (Fecha, Latitud, Longitud, Precision, Velocidad, Metros_Recorridos, Imei, Bus, VelocidadMax, Lugar, TotalVIPS, GeneralVIPS, Bateria, EntradasPe, SalidasPe, EntradasPs, SalidasPs, GeneralEntradasPe, GeneralSalidasPe, GeneralEntradasPs, GeneralSalidasPs, BloqPe, GeneralBloqPe, BloqPs, GeneralBloqPs, IdParada, NombreParada, Proveedor, Sentido, Ramal, VersionFirmware, TipoRed, ContadorCarreras, ActiveThread, LastID, MarcasEnBus, ContadorCambiosRamal, EnRamal, SendDataRamal, RamalEncontrado, SegStop) VALUES ('"+fecha+"',"+arg0.getLatitude()+","+arg0.getLongitude()+","+arg0.getAccuracy()+","+arg0.getSpeed()*3.6+","+LocationGPS.metrosr+","+"'"+LocationService.imei+"'"+","+bus+","+DataForSql.velocidadexcedida+",'"+lugar+"','"+0+"','"+0+"','"+DataForSql.bateria+"','"+DataForSql.contadorentradaspe+"','"+DataForSql.contadorsalidaspe+"','"+DataForSql.contadorentradasps+"','"+DataForSql.contadorsalidasps+"','"+DataForSql.generalentradaspe+"','"+DataForSql.generalsalidaspe+"','"+DataForSql.generalentradasps+"','"+DataForSql.generalsalidasps+"','"+DataForSql.contadorbloqueospe+"','"+DataForSql.generalbloqueospe+"','"+DataForSql.contadorbloqueosps+"','"+DataForSql.generalbloqueosps+"','"+datosrastreo.getInt("ParadaEncontradaInt", 0)+"','"+datosrastreo.getString("ParadaEncontradaS", "")+"','"+arg0.getProvider()+"','"+DatosEstaticosRastreo.getSentido()+"','"+datosrastreo.getString("UltimoRamalS", "")+"','"+ValoresEstaticos.versionFirmare+"','"+getTipoRed()+"','"+datosrastreo.getFloat("ContadorCarreras", (float)0)+"','"+LocationGPS.activethread+"','0','"+DataForSql.marcasactual+"','"+contador_cambiosramal+"','"+LocationGPS.isInRamal+"','"+LocationGPS.sendData+"','"+DatosEstaticosRastreo.getRamalEncontrado()+"','"+LocationGPS.segstop+"')";

			try
			{

				baselocal_guarda =BaseSQLLocal.getInstance(context,Environment.getExternalStorageDirectory().getPath()+"/Rastreo "+String.valueOf(bus)+".sqlite");
				db_guarda=baselocal_guarda.getWritableDatabase();

				if (db_guarda.isOpen()){
					////Log.i("UbicacionDB", db.getPath());
					//Log.i("VersionDB", String.valueOf(db.getVersion()));
					//Log.i("SendToLocal", sentencia);
					db_guarda.execSQL(sentencia);
					db_guarda.close();
					baselocal_guarda.close();
					isSavingToLocal=false;

				}

				else
				{
					baselocal_guarda =BaseSQLLocal.getInstance(context,Environment.getExternalStorageDirectory().getPath()+"/Rastreo "+String.valueOf(bus)+".sqlite");
					db_guarda=baselocal_guarda.getWritableDatabase();
					db_guarda.execSQL(sentencia);
					db_guarda.close();
					baselocal_guarda.close();
					isSavingToLocal=false;
				}
				isSavingToLocal=false;
				if (cursor_lugares!=null)
				{
					cursor_lugares.close();
				}



			}
			catch (SQLException ex)
			{

				isSavingToLocal=false;
				return;
			}
			catch (NullPointerException ex)
			{

				isSavingToLocal=false;
				return;
			}

		}

	}

	
	public static void setUltimaTransmision(final long l, float kmprevio){
		
		if (datosrastreo==null || context==null){
			context=getContext();
			datosrastreo=context.getSharedPreferences("DatosRastreo",Context.MODE_PRIVATE);
			
		}
		if (editor==null){
			editor=datosrastreo.edit();
		}
		if (km==0){
			km=datosrastreo.getFloat("MetrosRecorridos", km);
		}
		
		editor.putFloat("Metros_Recorridos_Previo", kmprevio).commit();
		editor.putLong("UltimaTransmision", l).commit();
		ultimatransmision=l;
		contadoralarm=0;
		editor.putInt("ContadorAlarm", contadoralarm).commit();
		LocationService.setCoordenadasPrevias(latitudprevia, longitudprevia);
		LocationService.setMetrosRecorridos(km);
		LocationService.kmprevio=km;
		editor.commit();
		
		//SI PASAN 10 MINUTOS SIN TRANSMITIR INCREMENTA ALARMA EN 1 Y ME RESETEA EL SERVICIO DE LOCALIZACION.

		
			//}
		//}
	}

	public static String getLugar(){
		return lugar;
	}
	public static void sendAlertaLugar(String s){
		if (context==null){
			
			context=getContext();
			
		}
		cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		net=cm.getActiveNetworkInfo();
		if (net!=null){
			if (net.isConnected()){
			ModuloSQL.Ejecutar(s,false);}
		}		
		
	}
	public static int getSentido(){
		if (datosrastreo==null || context==null){
			context=getContext();
			datosrastreo=context.getSharedPreferences("DatosRastreo",Context.MODE_PRIVATE);
						
		}
		return datosrastreo.getInt("Sentido", 0);
	}
	public static boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}

	public static void sendAlarmSpeed(double f){
		Calendar c=Calendar.getInstance();
		
		int speed=(int)f;
		Timestamp fecha=new Timestamp(c.getTimeInMillis());
		String sentencia="insert into MonitoreoMovil_Alarmas values ('"+fecha+"','VEL','"+bus+"','Unidad excedio velocidad maxima permitida','"+speed+"')";
		
		ModuloSQL.Ejecutar(sentencia,false);
	}
	
	public static void sendAlarmLugarStop(int min){
		Calendar c=Calendar.getInstance();
		String sentencia="";
		Timestamp fecha=new Timestamp(c.getTimeInMillis());
		if (min<=5){
			sentencia="insert into MonitoreoMovil_Alarmas values ('"+fecha+"','STP','"+bus+"','Unidad cumplio 5 min detenida en "+lugar+" se informara cuando se vuelva a mover','"+min+"')";
		}
		else{
			sentencia="insert into MonitoreoMovil_Alarmas values ('"+fecha+"','STP','"+bus+"','Unidad se movio luego de estar detenida en "+lugar+"','"+min+"')";
		}
		ModuloSQL.Ejecutar(sentencia,false);
	}
	/*public static void searchRamal(Location l, int i){
		
		minstop=i;
		LocationService.getRamalCercano(l);
		
	}*/
	public static double[] getCoordenadasPrevias(){
		if (datosrastreo==null || context==null || editor==null){
			context=getContext();
			datosrastreo=context.getSharedPreferences("DatosRastreo",Context.MODE_PRIVATE);
			editor=datosrastreo.edit();			
		}
		double latitud=Double.parseDouble(datosrastreo.getString("LatitudPrevia", "0.0"));
		double longitud=Double.parseDouble(datosrastreo.getString("LongitudPrevia", "0.0"));
		double [] coordenadas={latitud, longitud};
		return coordenadas;
	}
	public static void setCoordenadasPrevias(double latitud, double longitud){
		if (datosrastreo==null || context==null || editor==null){
			context=getContext();
			datosrastreo=context.getSharedPreferences("DatosRastreo",Context.MODE_PRIVATE);
			editor=datosrastreo.edit();			
		}
		latitudprevia=latitud;
		longitudprevia=longitud;
		editor.putString("LatitudPrevia", String.valueOf(latitud));
		editor.putString("LongitudPrevia", String.valueOf(longitud));
		editor.commit();
	}
	/*public static boolean getRamalCercano(Location arg1){
		isSearchingRamal=true;
		float distance2=0;
		if (ramalencontrado!=30)
		{
			locationramal.setLatitude(latitudesramales[ramalencontrado]);
			locationramal.setLongitude(longitudesramales[ramalencontrado]);
			distance2=arg1.distanceTo(locationramal);
			
		}
		else 
		{
			distance2=(int)500;
		}
		if ((int)distance2>200)
		{
			
			int i=0;
			int rango=50;
			while(i<=latitudesramales.length-1)
			{
				//RANGO PARA RAMAL DE ESCAZU CENTRO. DEBIDO A QUE PASAN MUCHOS BUSES CERCA SE ESTABLECE EN 15 MTS
				if (i==2)
				{
					rango=15; 
				}
				else
				{
					//SI SE ESTA PROBANDO UN RAMAL DIFERENTE DE TERMINAL EL RANGO ES 75 SINO EL RANGO ES 160
					if (i!=0&i!=12&i!=27){
						rango=75;
					}
					
					if (i==12||i==27||i==0){
						rango=160;
					}
				}
			locationramal.setLatitude(latitudesramales[i]);
			locationramal.setLongitude(longitudesramales[i]);
			distance2=arg1.distanceTo(locationramal);
			if ((int)distance2<=rango)
			{
				
				if (i!=ramalencontrado)
				{
					if ((i==11||i==0))
					{
						ramalencontrado=i;
						//if(setSentido)
						//{
							setSentido=false;
							sentido=1;
							isInRamal=true;
							sendData=true;
							editor.putBoolean("sendData", sendData);
							editor.putBoolean("isInRamal", isInRamal);
							editor.putBoolean("setSentido", setSentido);
							editor.commit();
							setSentido(sentido);
							setRamal(0,true,sentido);
						//}
						break;
					
					}
					else
					{
						if (i==12)
						{
							ramalencontrado=i;
							//if(setSentido)
							//{
								setSentido=false;
								isInRamal=true;
								sendData=true;
								editor.putBoolean("sendData", sendData);
								editor.putBoolean("isInRamal", isInRamal);
								editor.putBoolean("setSentido", setSentido);
								editor.commit();
								setSentido(2);
								
								setRamal(0,true,2);	
							//}
							break;
						}
						else
						{
							if (i==26)
							{
								if (ramalencontrado==16||ramalencontrado==15){
									ramalencontrado=i;
									//if(setSentido)
									//{
										setSentido=false;
										isInRamal=true;
										sendData=true;
										editor.putBoolean("sendData", sendData);
										editor.putBoolean("isInRamal", isInRamal);
										editor.putBoolean("setSentido", setSentido);
										editor.commit();
										setSentido(2);
										setRamal(0,true,2);	
									//}
									break;
								}
								break;
							}
							else
							{
								//COMPORTAMIENTO PARA RAMAL MUSMANNI. SI ES BARRIO SE COMPORTA COMO TERMINAL SJ, SINO NO HACE NADA.
								if (i==27){
									if (ramalencontrado>16&ramalencontrado!=26){
										ramalencontrado=i;
										//if(setSentido)
										//{
											setSentido=false;
											sentido=1;
											isInRamal=true;
											sendData=true;
											editor.putBoolean("sendData", sendData);
											editor.putBoolean("isInRamal", isInRamal);
											editor.putBoolean("setSentido", setSentido);
											editor.commit();
											setSentido(sentido);
											setRamal(0,true,sentido);
										//}
										break;
									}
									break;
								}
								else
								{
						
									ramalencontrado=i;
									if (latituddetectaramal==0||longituddetectaramal==0)
									{
										latituddetectaramal=arg1.getLatitude();
										longituddetectaramal=arg1.getLongitude();
									
									}
									detectoRamal.setLatitude(arg1.getLatitude());
									detectoRamal.setLongitude(arg1.getLongitude());
									setSentido=true;
									isInRamal=true;
									sendData=true;
									editor.putBoolean("sendData", sendData).commit();
									editor.putBoolean("isInRamal", isInRamal).commit();
									editor.putBoolean("setSentido", setSentido).commit();
									setRamal(i,false,sentido);
									break;
								}
							}
				
						}
					}
				}
				else
				{
					break;
				}
				
			}
			else
			{
				if (isInRamal)
				{
					
					isInRamal=false;
					latituddetectaramal=0;
					longituddetectaramal=0;
					editor.putBoolean("isInRamal", isInRamal);
					editor.commit();
				}
				
			}
			i++;
		}
			
		}
		
		else
		{
			if (minstop>30&isInRamal&sendData&ramalencontrado!=0&ramalencontrado!=11&ramalencontrado!=12&ramalencontrado!=26&ramalencontrado!=27)
			{
				sendData=false;
				editor.putBoolean("sendData", sendData);
				editor.commit();
				sentido=2;
				LocationService.setSentido(sentido);
				LocationService.setRamal(-1,true,sentido);
				setTimeCarrera=true;
			}
			
			if (LocationService.sendDatosramal==false){
				LocationService.sendDatosramal=true;
			}
		}
		//isSearchingRamal=false;
		isSearchingRamal=false;
		return true;
		
	}*/

	
	public static String getTipoRed() {
	    TelephonyManager mTelephonyManager = (TelephonyManager)
	            context.getSystemService(Context.TELEPHONY_SERVICE);
	    int networkType = mTelephonyManager.getNetworkType();
	    switch (networkType) {
	        case TelephonyManager.NETWORK_TYPE_GPRS:
	        case TelephonyManager.NETWORK_TYPE_EDGE:
	        case TelephonyManager.NETWORK_TYPE_CDMA:
	        case TelephonyManager.NETWORK_TYPE_1xRTT:
	        case TelephonyManager.NETWORK_TYPE_IDEN:
	            return "2G";
	        case TelephonyManager.NETWORK_TYPE_UMTS:
	        case TelephonyManager.NETWORK_TYPE_EVDO_0:
	        case TelephonyManager.NETWORK_TYPE_EVDO_A:
	        case TelephonyManager.NETWORK_TYPE_HSDPA:
	        case TelephonyManager.NETWORK_TYPE_HSUPA:
	        case TelephonyManager.NETWORK_TYPE_HSPA:
	        case TelephonyManager.NETWORK_TYPE_EVDO_B:
	        case TelephonyManager.NETWORK_TYPE_EHRPD:
	        case TelephonyManager.NETWORK_TYPE_HSPAP:
	            return "3G";
	        case TelephonyManager.NETWORK_TYPE_LTE:
	            return "4G";
	        default:
	            return "Desconocido";
	    }
	}
	public static void sendAlarmStop(double v1,double v2){
		Calendar c=Calendar.getInstance();
		int reiteraciones=datosrastreo.getInt("Frenazos", 0);
		reiteraciones++;
		editor.putInt("Frenazos", reiteraciones).commit();
		int vel1=(int)v1;
		int vel2=(int)v2;
		Timestamp fecha=new Timestamp(c.getTimeInMillis());
		String sentencia="insert into MonitoreoMovil_Alarmas values ('"+fecha+"','VEL','"+bus+"','Unidad freno/acelero bruscamente, paso de "+vel1+" a "+vel2+" km/h','"+reiteraciones+"')";
		ModuloSQL.Ejecutar(sentencia,false);
	}
	
	
	
	/*public static class BusquedaRamal extends AsyncTask<android.location.Location,Integer,Boolean>{

		@Override
		protected Boolean doInBackground(Location... arg0) {
			isSearchingRamal=true;
			float distance2=0;
			Location arg1=arg0[arg0.length-1];
			if (ramalencontrado!=30)
			{
				locationramal.setLatitude(latitudesramales[ramalencontrado]);
				locationramal.setLongitude(longitudesramales[ramalencontrado]);
				distance2=arg1.distanceTo(locationramal);
				
			}
			else 
			{
				distance2=(int)500;
			}
			if ((int)distance2>200)
			{
				
				int i=0;
				int rango=50;
				while(i<=latitudesramales.length-1)
				{
					//RANGO PARA RAMAL DE ESCAZU CENTRO. DEBIDO A QUE PASAN MUCHOS BUSES CERCA SE ESTABLECE EN 15 MTS
					if (i==2)
					{
						rango=15; 
					}
					else
					{
						//SI SE ESTA PROBANDO UN RAMAL DIFERENTE DE TERMINAL EL RANGO ES 75 SINO EL RANGO ES 160
						if (i!=0&i!=12&i!=27){
							rango=75;
						}
						
						if (i==12||i==27||i==0){
							rango=160;
						}
					}
				locationramal.setLatitude(latitudesramales[i]);
				locationramal.setLongitude(longitudesramales[i]);
				distance2=arg1.distanceTo(locationramal);
				if ((int)distance2<=rango)
				{
					
					if (i!=ramalencontrado)
					{
						if ((i==11||i==0))
						{
							ramalencontrado=i;
							if(setSentido)
							{
								setSentido=false;
								sentido=1;
								isInRamal=true;
								sendData=true;
								editor.putBoolean("sendData", sendData);
								editor.putBoolean("isInRamal", isInRamal);
								editor.putBoolean("setSentido", setSentido);
								editor.commit();
								setSentido(sentido);
								setRamal(0,true,sentido);
							}
							break;
						
						}
						else
						{
							if (i==12)
							{
								ramalencontrado=i;
								if(setSentido)
								{
									setSentido=false;
									isInRamal=true;
									sendData=true;
									editor.putBoolean("sendData", sendData);
									editor.putBoolean("isInRamal", isInRamal);
									editor.putBoolean("setSentido", setSentido);
									editor.commit();
									setSentido(2);
									
									setRamal(0,true,2);	
								}
								break;
							}
							else
							{
								if (i==26)
								{
									if (ramalencontrado==16||ramalencontrado==15){
										ramalencontrado=i;
										if(setSentido)
										{
											setSentido=false;
											isInRamal=true;
											sendData=true;
											editor.putBoolean("sendData", sendData);
											editor.putBoolean("isInRamal", isInRamal);
											editor.putBoolean("setSentido", setSentido);
											editor.commit();
											setSentido(2);
											setRamal(0,true,2);	
										}
										break;
									}
									break;
								}
								else
								{
									//COMPORTAMIENTO PARA RAMAL MUSMANNI. SI ES BARRIO SE COMPORTA COMO TERMINAL SJ, SINO NO HACE NADA.
									if (i==27){
										if (ramalencontrado>16&ramalencontrado!=26){
											ramalencontrado=i;
											if(setSentido)
											{
												setSentido=false;
												sentido=1;
												isInRamal=true;
												sendData=true;
												editor.putBoolean("sendData", sendData);
												editor.putBoolean("isInRamal", isInRamal);
												editor.putBoolean("setSentido", setSentido);
												editor.commit();
												setSentido(sentido);
												setRamal(0,true,sentido);
											}
											break;
										}
										break;
									}
									else
									{
							
										ramalencontrado=i;
										if (latituddetectaramal==0||longituddetectaramal==0)
										{
											latituddetectaramal=arg1.getLatitude();
											longituddetectaramal=arg1.getLongitude();
										
										}
										detectoRamal.setLatitude(arg1.getLatitude());
										detectoRamal.setLongitude(arg1.getLongitude());
										setSentido=true;
										isInRamal=true;
										sendData=true;
										editor.putBoolean("sendData", sendData).commit();
										editor.putBoolean("isInRamal", isInRamal).commit();
										editor.putBoolean("setSentido", setSentido).commit();
										setRamal(i,false,sentido);
										break;
									}
								}
					
							}
						}
					}
					else
					{
						break;
					}
					
				}
				else
				{
					if (isInRamal)
					{
						
						isInRamal=false;
						latituddetectaramal=0;
						longituddetectaramal=0;
						editor.putBoolean("isInRamal", isInRamal);
						editor.commit();
					}
					
				}
				i++;
			}
				
			}
			
			else
			{
				if (minstop>30&isInRamal&sendData&ramalencontrado!=0&ramalencontrado!=11&ramalencontrado!=12&ramalencontrado!=26&ramalencontrado!=27)
				{
					sendData=false;
					editor.putBoolean("sendData", sendData);
					editor.commit();
					sentido=2;
					LocationService.setSentido(sentido);
					LocationService.setRamal(-1,true,sentido);
					setTimeCarrera=true;
				}
				
				if (LocationService.sendDatosramal==false){
					LocationService.sendDatosramal=true;
				}
			}
			//isSearchingRamal=false;
			
			return true;
			}
			
		
		protected void onPostExecute(Boolean rs) {
			//Toast.makeText(context, "TErmino ramal", 30).show();
			isSearchingRamal=false;
		}


	}*/
	public class TestDispositivo extends AsyncTask<String,Integer,Boolean>{
		
		@Override
		protected Boolean doInBackground(String... arg0) {
			isTestingDispositivo=true;
			try {
				if(conexion==null){
					
					Class.forName(libreria).newInstance();
					conexion=DriverManager.getConnection(url,user,pass);
					PreparedStatement s=conexion.prepareStatement("SELECT CONNECTION_ID()as id");
					ResultSet rs=s.executeQuery();
					if (rs.first()){
						idconexion=rs.getLong("id");
					}
					rs.close();
					s.close();
					rs=null;
					s=null;
				}
				if (conexion!=null){
				if (conexion.isClosed()==false){
					if (conexion.isValid(15)==false){
						
						conexion=DriverManager.getConnection(url,user,pass);
						changeId=true;
						
						}
					}
				if (changeId){
					changeId=false;
					PreparedStatement s=conexion.prepareStatement("KILL "+idconexion);
					try {
					s.execute();}
					catch(java.sql.SQLException e){}
					s=conexion.prepareStatement("SELECT CONNECTION_ID()as id");
					
					ResultSet rs=s.executeQuery();
					if (rs.first()){
						idconexion=rs.getLong("id");
					}
					rs.close();
					s.close();
					rs=null;
					s=null;
					
				}
				Statement s2 = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		        ResultSet rs=  s2.executeQuery(arg0[0]);
	            if (rs.first()){
	            	if (prefsparametros.getInt("Bus", 0)!=rs.getInt("Unidad")){
	            		prefsparametros.edit().putInt("Bus", rs.getInt("Unidad")).commit();
	            		prefsparametros.edit().putBoolean("IsActive", true).commit();
	            		prefsparametros.edit().putInt("IdDispositivo", rs.getInt("Consecutivo")).commit();
	            		bus=rs.getInt("Unidad");
	            		
	            		
	            		noti = notibuilder.setContentTitle("Rastreo unidad "+String.valueOf(bus)).setContentText("Cia de Inversiones La Tapachula").setSmallIcon(R.drawable.ic_launcher).build();
	            		NotificationManager m=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	            		m.notify(1, noti);
	            		rs.close();
	            		s2.close();
	            		rs=null;
	            		s2=null;
	            		conexion.close();
	            	}
	            	else{
	            		prefsparametros.edit().putBoolean("IsActive", true).commit();
	            		prefsparametros.edit().putInt("IdDispositivo", rs.getInt("Consecutivo")).commit();
	            	}
	            	if (rs!=null){rs.close();}
	            	if (s2!=null){s2.close();}
            		rs=null;
            		s2=null;
            		conexion.close();
	            	return true;
	            }
	            else{
	            	if (rs!=null){rs.close();}
	            	if (s2!=null){s2.close();}
            		
            		rs=null;
            		s2=null;
	            	conexion.close();
	            	return false;
	            }
				
				}
				else{
					return false;
				}
			} catch (InstantiationException e) {
				//DataForErrors.setFecha(Calendar.getInstance().getTimeInMillis());
				//DataForErrors.setMensaje(e.getLocalizedMessage()+" linea TestDispositivo.Backgroud InstantiationException");
				//DataForErrors.sendError();
				prefsparametros.edit().putBoolean("IsActive", false).commit();
				if (conexion!=null){
					try {
						conexion.close();
					} catch (java.sql.SQLException e1) {
						//DataForErrors.setFecha(Calendar.getInstance().getTimeInMillis());
						//DataForErrors.setMensaje(e.getLocalizedMessage()+" linea TestDispositivo.Backgroud SQLException 1");
						//DataForErrors.sendError();
						
					}
				}
				return false;
			} catch (IllegalAccessException e) {
				//DataForErrors.setFecha(Calendar.getInstance().getTimeInMillis());
				//DataForErrors.setMensaje(e.getLocalizedMessage()+" linea TestDispositivo.Backgroud IllegalAccesException");
				//DataForErrors.sendError();
				prefsparametros.edit().putBoolean("IsActive", false).commit();
				if (conexion!=null){
					try {
						conexion.close();
					} catch (java.sql.SQLException e1) {
						//DataForErrors.setFecha(Calendar.getInstance().getTimeInMillis());
						//DataForErrors.setMensaje(e.getLocalizedMessage()+" linea TestDispositivo.Backgroud SQLException 2");
						///DataForErrors.sendError();
						
					}
				}
				return false;
			} catch (ClassNotFoundException e) {
				//DataForErrors.setFecha(Calendar.getInstance().getTimeInMillis());
				//DataForErrors.setMensaje(e.getLocalizedMessage()+" linea TestDispositivo.Backgroud ClassNotFoundException");
				//DataForErrors.sendError();
				prefsparametros.edit().putBoolean("IsActive", false).commit();
				if (conexion!=null){
					try {
						conexion.close();
					} catch (java.sql.SQLException e1) {
					
					}
				}
				return false;
			} catch (java.sql.SQLException e) {
				if (prefsparametros.edit().putBoolean("IsActive", false).commit()!=true){
				prefsparametros.edit().putBoolean("IsActive", false).commit();}
				if (conexion!=null){
					try {
						conexion.close();
					} catch (java.sql.SQLException e1) {
						
					}
				}
				return false;
				
			}
		}
		@Override
		protected void onPostExecute(Boolean rs) {
			if (rs==false)
			{
				new Handler().postDelayed(new Runnable() 
				{
					@Override
					public void run() {
						getLocationProviders();
					}
				}, 30000);
						
			}
			isTestingDispositivo=false;
		}
		@Override
	    protected void onCancelled() {
	        isTestingDispositivo = false;
	    }
	}
	
	}