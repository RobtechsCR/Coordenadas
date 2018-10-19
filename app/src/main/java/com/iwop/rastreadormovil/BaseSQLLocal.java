package com.iwop.rastreadormovil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class BaseSQLLocal extends SQLiteOpenHelper {
 
    //Sentencia SQL para crear la tabla de Usuarios
	
    String createTable= "CREATE TABLE Rastreo (Fecha TIMESTAMP, Latitud FLOAT, Longitud FLOAT, Precision FLOAT, Velocidad FLOAT, Metros_Recorridos FLOAT, Imei TEXT, Bus INTEGER, VelocidadMax FLOAT, Lugar TEXT, TotalVIPS INTEGER, GeneralVIPS INTEGER, Bateria TEXT, EntradasPe INTEGER, SalidasPe INTEGER, EntradasPs INTEGER, SalidasPs INTEGER, GeneralEntradasPe INTEGER, GeneralSalidasPe INTEGER, GeneralEntradasPs INTEGER, GeneralSalidasPs INTEGER, BloqPe INTEGER, GeneralBloqPe INTEGER, BloqPs INTEGER, GeneralBloqPs INTEGER, IdParada INTEGER, NombreParada TEXT, Proveedor TEXT, Sentido INT, Ramal TEXT, VersionFirmware FLOAT, TipoRed TEXT, ContadorCarreras FLOAT, ActiveThread INTEGER, LastID DOUBLE, MarcasEnBus INTEGER, ContadorCambiosRamal INTEGER, EnRamal TEXT, SendDataRamal TEXT, RamalEncontrado INTEGER, SegStop INTEGER)";
    private static String DATABASE_NAME = "database_name";
    //private static String DATABASE_TABLE = "Rastreo";
    private static final int DATABASE_VERSION = 4;
    private static BaseSQLLocal mInstance = null;
    
    private BaseSQLLocal(Context contexto) {
        super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static BaseSQLLocal getInstance(Context ctx,String s) {

        // Use the application context, which will ensure that you 
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
    	DATABASE_NAME=s;
        if (mInstance == null) {
          mInstance = new BaseSQLLocal(ctx);
        }
        
        return mInstance;
      }
    
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de crea la tabla
        db.execSQL(createTable);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aqutilizamos directamente la de
        //      eliminar la tabla anterior y crearla de nuevo vac con el nuevo formato.
        //      Sin embargo lo normal s haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este m
 
        //Se elimina la verserior de la tabla
    	File src=new File(DATABASE_NAME);
    	File bk=new File (DATABASE_NAME+"_backup_"+Calendar.getInstance().toString());
    	try {
			copyBackUp(src,bk);
		} catch (IOException e) {
			
		}
        db.execSQL("DROP TABLE IF EXISTS Rastreo");
 
        //Se crea la nueva versla tabla
        db.execSQL(createTable);
    }
    private void copyBackUp(File src, File dst) throws IOException{
    	
    	    InputStream in = new FileInputStream(src);
    	    OutputStream out = new FileOutputStream(dst);

    	    // Transfer bytes from in to out
    	    byte[] buf = new byte[1024];
    	    int len;
    	    while ((len = in.read(buf)) > 0) {
    	        out.write(buf, 0, len);
    	    }
    	    in.close();
    	    out.close();
    	
    }
}
