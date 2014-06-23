package andy.study.dailyrecord.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import andy.study.dailyrecord.util.ConfigLoader;

public class SqliteManager extends SQLiteOpenHelper {

	private static String DB_NAME = ConfigLoader.DATABASE_NAME;
	private static int VERSION = ConfigLoader.DATABASE_VERSION;
	
	public SqliteManager(Context context) {
		super(context, DB_NAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + ConfigLoader.RECORD_TABLE + " ( " +
				" id integer primary key AUTOINCREMENT," +
				" type_id integer not null," +
				" type_name nvarchar(21)," +
				" record_value float not null," +
				" note nvarchar(200)," +				
				" recorddate char(10))");		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}

	
}
