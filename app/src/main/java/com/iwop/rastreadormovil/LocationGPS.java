package com.iwop.rastreadormovil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
//import android.widget.Toast;
//import android.widget.Toast;

public class LocationGPS implements LocationListener{
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
	static GpsStatus.Listener listenerGPS=null;
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
	static ValoresEstaticos valoresestaticos=new ValoresEstaticos();
	//static int ramal=0;
	@Override
	public void onLocationChanged(final android.location.Location arg0) {
		context=LocationService.getContext();

		if (isNetSelected){
			isNetSelected=false;
		}
		//Toast.makeText(context, String.valueOf(arg0.getTime()), 2).show();

		if (previa==null){
			previa=new Location("Previa");
			previa.setLatitude(arg0.getLatitude());
			previa.setLongitude(arg0.getLongitude());
			locationParadaEncontrada=arg0;
		}
		locationactual=arg0;

		c=Calendar.getInstance();	
		if (addlistener==false)
		{
			listenerGPS=new MyGPSListener();
			try {
			
			LocationService.lmanager.addGpsStatusListener(listenerGPS);
			addlistener=true;
			
			}
			catch (SecurityException ex){
				return;
			}
		}
		LocationService.handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context,"Ubicacion actualizda",Toast.LENGTH_SHORT).show();
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
		
		if (isGettingParada==false & (arg0.getTime()>=fechaprevia4+4000)){
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
		/*if (isGettingRamal==false){
			fechaprevia5=arg0.getTime();
			isGettingRamal=true;
			//scheduler.remove(threadgetramal);
			getRamalCercano();
		}*/
		
		
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
		if (fecha.getTime()>=fechaprevia+ValoresEstaticos.ESPERA_ENVIO_SQL)
		{
			LocationService.handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(LocationService.context,"SendToServer",Toast.LENGTH_LONG).show();
				}
			});
			//LocationService.logger.info("LocationGPS. SendToServer");
			fechaprevia=fecha.getTime();
			//scheduler.execute(threadsenttosql);
			LocationService.sendToServer(locationactual);
				
			
		}
		else{
			if (LocationService.isisSendingtoSQL){
			//LocationService.logger.info("LocationGPS. No se pudo enviar online debido a que isisSendingToSQL=true");
			}
			if (fecha.getTime()<fechaprevia){
				//LocationService.logger.info("LocationGPS. FechaPrevia mayor a fecha actual");
			}
		}
		
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
		

		
		
		
		


	@Override
	public void onProviderDisabled(String arg0) {
		
		LocationService.GPS_ENABLE=false;
		//LocationService.selectLocationProvider(2);
		//LocationService.setMetrosRecorridos(metrosr);
		//LocationService.setCoordenadasPrevias(latitudprevia, longitudprevia);
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void onProviderEnabled(String arg0) {
		
		LocationService.GPS_ENABLE=true;
		//LocationService.selectLocationProvider(1);
		//LocationService.selectLocationProvider(4);
		
		// TODO Auto-generated method stub
		
	}
	public void getParadaCercana(){
		
				//DETECTA PARADA

		
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



	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		//GpsStatus gps=new GpsStatus();
		
		
		// TODO Auto-generated method stub
		
	}
	
	  private class MyGPSListener implements GpsStatus.Listener {
			
			
			
		    public void onGpsStatusChanged(int event) {
		    	
		    	
		        switch (event) {
		        	case GpsStatus.GPS_EVENT_STARTED:
		        		//Toast.makeText(context, "Provider 2 EVENT STARTED", 30).show();
		        		//context=LocationService.context;
		        		if (metrosr==0)
		        		{
		        			metrosr=LocationService.getMetrosRecorridos();
		        		}
		        		sendData=LocationService.datosrastreo.getBoolean("sendData", true);
		    			isInRamal=LocationService.datosrastreo.getBoolean("isInRamal", true);
		    			setTimeCarrera=LocationService.datosrastreo.getBoolean("SetTimeCarrera", true);
		    			sendContadoresParadas=LocationService.datosrastreo.getBoolean("SendContadoresParadas", false);
		        		//LocationNet.isfirst=true;
		        		//LocationService.selectLocationProvider(2);
		        		break;
		        	case GpsStatus.GPS_EVENT_FIRST_FIX:
		        		//mLastLocationMills=fecha.getTime();
		        		//Toast.makeText(context, "Provider 3 FIX", 30).show();  		
		        		if (metrosr==0)
		        		{
		        			metrosr=LocationService.getMetrosRecorridos();
		        		}
		        		LocationService.selectLocationProvider(3);		        		
		        		isGPSfix=true;
		        		sendData=LocationService.datosrastreo.getBoolean("sendData", true);
		    			isInRamal=LocationService.datosrastreo.getBoolean("isInRamal", true);
		    			//ramalencontrado=LocationService.datosrastreo.getInt("", 30);
		    			//setSentido=LocationService.datosrastreo.getBoolean("setSentido", true);
		    			setTimeCarrera=LocationService.datosrastreo.getBoolean("SetTimeCarrera", true);
		    			sendContadoresParadas=LocationService.datosrastreo.getBoolean("SendContadoresParadas", false);
		    			//sentido=LocationService.datosrastreo.getInt("Sentido", 0);
		        		break;
		        	case GpsStatus.GPS_EVENT_STOPPED:
		        		//Toast.makeText(context, "GPS Deshabilitado", 30).show();
		        		//LocationService.setMetrosRecorridos(metrosr);
		        		//LocationService.selectLocationProvider(2);
		        		//LocationService.setCoordenadasPrevias(latitudprevia, longitudprevia);
		        		//LocationService.setMetrosRecorridos(metrosr);
		        		//LocationService.lmanager.removeGpsStatusListener(listenerGPS);
		        		
		        		//addlistener=false;
		        		
		        		/*if (LocationService.isAhorro==false){
		        			Intent intent = new Intent();
		    				intent.setAction("com.rastreomovil.startservice");
		    				context.sendBroadcast(intent);
		    				
		    				context.sendBroadcast(intent);
		    				//contador.cancel();
		        		}*/
		        		
		        		break;
		        	case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
		        			cantidadsatelites=0;
		        			for (GpsSatellite sat:LocationService.lmanager.getGpsStatus(null).getSatellites()){
		        				if (sat.usedInFix()){
		        					cantidadsatelites++;
		        				}
		        			}
		        			if(mLastLocationMills>0){
		                    if(((SystemClock.elapsedRealtime() - mLastLocationMills) < 60000)||((SystemClock.elapsedRealtime() - mLastLocationMills) < 480000))
		                    {
		                    	
		                    	
		                        if (!isGPSfix){ 
		                            	Log.i("GPS","Fix Acquired");
		                            	isGPSfix = true;
		                            	LocationService.selectLocationProvider(3);
		                            	
		                            	isNetSelected=false;
		                        }
		                        
		                        else
		                        {
		                        	if (isGPSfix) 
		                        	{
		                        		isNetSelected=false;
		                        		
		                        	}
		                        	 
		                        
		                    }
		                        isNetSelected=false; 
		                        }
		                    else
                        	{
		                    	if (isNetSelected==false){
		                    		
                        		//Toast.makeText(context, "Location 2", 30).show();
		                    		LocationService.selectLocationProvider(4);
		                    		//LocationService.setMetrosRecorridos(metrosr);
		                    		//LocationService.setCoordenadasPrevias(latitudprevia, longitudprevia);
                        		isGPSfix = false;
                        		isNetSelected=true;}
                        	}
	                        	
		        			}
		                
		                break;
		            
		            
		        }
		    }
		}

	
	}


