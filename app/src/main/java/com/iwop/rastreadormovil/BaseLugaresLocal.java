package com.iwop.rastreadormovil;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class BaseLugaresLocal extends SQLiteOpenHelper {
 
    //Sentencia SQL para crear la tabla de Usuarios
    String createTable= "CREATE TABLE Puntos (Latitud DOUBLE, Longitud DOUBLE, Lugar TEXT, Filtro TEXT)";
    private static BaseLugaresLocal mInstance = null;
    private static final String DATABASE_NAME = Environment.getExternalStorageDirectory().getPath()+"/PuntosReferencia.sqlite";
    //private static final String DATABASE_TABLE = "table_name";
    private static final int DATABASE_VERSION = 1;
    private BaseLugaresLocal(Context contexto) {
        super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static BaseLugaresLocal getInstance(Context ctx) {

        // Use the application context, which will ensure that you 
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
          mInstance = new BaseLugaresLocal(ctx.getApplicationContext());
        }
        return mInstance;
      }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creaci de la tabla
        db.execSQL(createTable);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aqtilizamos directamente la opci�n de
        //      eliminar la tabla anterior y crearla de nuevo vac�a con el nuevo formato.
        //      Sin embargo lo normal serue haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que est.
 
        //Se elimina la veerior de la tabla
        //db.execSQL("DROP TABLE IF EXISTS Usuarios");
 
        //Se crea la nueva versla tabla
        //db.execSQL(cre);
    }
}
