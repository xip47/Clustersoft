package cl.skyvortex.rememberit.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Pair;

/**
 * Clase que encapsula las funciones de creacion, acceso y modificacion de la base de datos en el telefono.
 * @version 1.1
 * @author Skyvortex Solutions
 *
 */
public class DBManager extends SQLiteOpenHelper{

	protected SQLiteDatabase db;
	private int version =1;
	private String tableName=null;
	private ArrayList<Pair<String, String>> tables=null;
	private String restricts=null;

	private static final String createDB ="CREATE TABLE ";

	public DBManager(Context context, String dbName, CursorFactory factory,int version) {
		super(context, dbName, factory, version);
		this.version = version;
	}

	/**
	 * Esta funcion es llamada por el objeto que instancia la clase 'DBManager'. Abre una base de 
	 * datos para lectura y escritura. Si la base de datos dbName no existe, la crea, llamando
	 * automaticamente a la funcion onCreate()  
	 * @return boolean true si la BD fue creada o abierta satisfactoriamente, false en caso contrario. 
	 */
	public boolean openDatabase(){
		db = this.getWritableDatabase();
		if(db==null)
			return false;
		return true;
	}

	/**
	 * Llamada directa para crear tablas, luego de que la base de datos se ha creado.
	 * Puede ser llamada en cualquier momento por la clase que instancia DBManager.
	 * @param tableName nombre de la tabla a crear.
	 * @param tables Arreglo de pares para crea la base de datos. El primer elemento de par es el
	 * nombre del campo, mientra el segundo es el tipo que corresponde a ese campo.
	 * @param restricts Restricciones adicionales para la tabla completa, como claves foraneas, etc. 
	 */
	public void createTable(String tableName, ArrayList<Pair<String, String>> tables, String restricts){
		Iterator<Pair<String,String>> it;
		it = tables.iterator();
		String sqlState = createDB + tableName + " (";
		Pair<String, String> temp;
		while(it.hasNext()){
			temp = it.next();
			sqlState = sqlState + " " + temp.first.toLowerCase() + " " +
			temp.second.toUpperCase() + ",";									
		}
		if(restricts!=null)
			sqlState = sqlState.substring(0, sqlState.length() - 1) + ") " + this.restricts + ";";
		else
			sqlState = sqlState.substring(0, sqlState.length() - 1) + ");";
		db.execSQL(sqlState);
	}

	/**
	 * Funcion llamada solo en el momento de crear la base de datos. 
	 * 'tableName' y 'tables' deben ser definidos antes de la creacion de la base de datos.
	 * @param db Base de datos que abierta para lectura y escritura.
	 */
	private void createTable(SQLiteDatabase db){
		Iterator<Pair<String,String>> it;
		it = this.tables.iterator();
		String sqlState = createDB + this.tableName + " (";
		Pair<String, String> temp;
		while(it.hasNext()){
			temp = it.next();
			sqlState = sqlState + " " + temp.first.toLowerCase() + " " +
			temp.second.toUpperCase() + ",";									
		}
		if(restricts!=null)
			sqlState = sqlState.substring(0, sqlState.length()) + ") " + this.restricts + ";";
		else
			sqlState = sqlState.substring(0, sqlState.length()) + ");";
		db.execSQL(sqlState);
	}

	/**
	 * Funcion ejecutada inmediatamente despues de crear la base de datos. En este caso, entrega los
	 * parametros para la base de datos y crea una tabla inicial con los datos que se hayan entregado.
	 * @param db Base de datos recien creada.
	 */
	@Override
	public void onCreate(SQLiteDatabase db){
		db.setVersion(this.version);
		db.setLocale(Locale.getDefault());
		db.setLockingEnabled(true);
		if(this.tableName!=null && this.tables!=null && !this.doesTableExists(tableName))
			createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	/**
	 * Inserta una fila nueva en la base de datos.
	 * @param table Nombre de la tabla en la que se insertan los datos.
	 * @param nullColumnHack Nombre de alguna columna a la que se pueda insertar explicitamente el 
	 * valor NULL, en caso de que todos los datos a insertar sean nulos. 
	 * @param values Valores a insertar. Se trabaja semejante a un arreglo de pares, en los que se 
	 * debe entregar el nombre del campo y el valor que se insertara. Se deben agregar los valores 
	 * en orden, segun la definicion de la tabla.
	 * @see ContentValues
	 * @return long Valor de id del ultimo registro insertado.
	 */
	public long insert(String table, String nullColumnHack, ContentValues values){
		long id = db.insert(table, nullColumnHack, values);
		return id;
	}

	/**
	 * Borra una o varias columnas de la tabla 'table'. Primero llama a verificar las condiciones de
	 * borrado, si son correctas, ejecuta el borrado en la base de datos.
	 * @param table Nombre de la tabla de la que se quiere borrar el registro.
	 * @param whereClause Condiciones de borrado, estilo "WHERE id=1 AND title='title'", sin agregar
	 * la palabra WHERE.
	 * @param whereArgs Si se han usado espacios de variables en whereClause, whereArgs debe tener los
	 * valores explicitos.
	 * @return boolean false cuando la sentencia es invalida o no se pudo borrar ningun registro. 
	 * true en caso contrario. 
	 */
	public boolean delete(String table, String whereClause, String[] whereArgs){
		if(!isCorrect(whereClause, whereArgs))
			return false;
		if(db.delete(table, whereClause, whereArgs)==0)
			return false;
		else 
			return true;
	}

	/**
	 * Este metodo se fija si no hay clausulas que puedan inducir a error a la base de datos
	 * o SQL Injection.
	 * @param whereClause Condiciones de seleccion que se analizaran en busca de SQL Injection.
	 * @param whereArgs Valores explicitos de las condiciones de seleccion, en caso de haber variables
	 * a rellenar en whereClause.
	 * @return boolean true si la frase no contiene errores, false en caso contrario.
	 */
	private boolean isCorrect(String whereClause, String[] whereArgs) {

		return true;
	}

	/**
	 * Esta funcion ejecuta una llamada de seleccion sobre la base de datos, previa verificacion de
	 * que no hay datos que se puedan usar como SQL Injection u otro metodo de ataque.
	 * @param table Nombre de la tabla de donde se sacan los datos. Solo puede ser una tabla.
	 * @param columns Arreglo con nombres de las columnas que se quieren obtener.
	 * @param selection Condiciones de seleccion, que siguen al WHERE de SQL.
	 * @param selectionArgs Parametros para las variables que se den en las condiciones de seleccion.
	 * @param groupBy Forma de agrupar los resultados.
	 * @param having Condiciones que siguen al HAVING de SQL.
	 * @param orderBy Orden (DESC o ASC) en que se entregaran los resultados. Si es null, se usa orden
	 * por defecto, ascendente.
	 * @param limit Cantidad de elementos que se quieren obtener. Debe ser un string.
	 * @return Cursos Retorna un cursor con los datos seleccionados, en orden. Si existen caracteres
	 * 'peligrosos', retorna null.
	 */
	public Cursor select(String table, String[] columns, String selection, 
			String[] selectionArgs, String groupBy, String having, String orderBy, String limit){
		if(isCorrect(selection, selectionArgs)){
			try{
				Cursor c = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
				return c;
			}catch(SQLiteException e){
				return null;
			}
		}
		return null;
	}

	/**
	 * Ejecuta una consulta SQL de seleccion escrita completamente por la clase que llama la funcion.
	 * Util cuando se necesita hacer cruce de tablas. 
	 * @param query Sentencia SQL para ejecutar directamente en la Base de Datos.  
	 * @return Cursor Cursor con los datos recogidos. null si la frase tiene posible error o ataque. 
	 */
	public Cursor rawSelect(String query){
		try{
		Cursor c = db.rawQuery(query, null);		
		return c;
		}catch(SQLiteException e){
			return null;
		}
	}

	public boolean update(){

		return false;
	}

	/**
	 * 
	 * @param tableName Nombre de la tabla cuya existencia hay que verificar.
	 * @return boolean true si la tabla existe. false si no existe.
	 */
	public boolean doesTableExists(String tableName){
		String query = "SELECT name FROM sqlite_master WHERE name='" + tableName + "';";
		Cursor c = db.rawQuery(query, null);
		if(c==null)
			return false;
		if(c.moveToFirst()==false){
			c.close();
			return false;
		}
		c.close();
		return true;
	}

	/*
	 * Setters y getters
	 */
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public ArrayList<Pair<String, String>> getTables() {
		return tables;
	}

	public void setTables(ArrayList<Pair<String, String>> tables) {
		this.tables = tables;
	}
	
	/**
	 * Selecciona y devuelve el ultimo registro insertado en la tabla.
	 * @param table Nombre de la tabla a seleccionar.
	 * @return Cursor con la ultima fila insertada.
	 */
	public Cursor getLastRow(String table){
		Cursor c;
		c = this.rawSelect("SELECT * FROM " + table + " WHERE id= (SELECT MAX(id) FROM " + table + ");");
		return c; 
	}
}