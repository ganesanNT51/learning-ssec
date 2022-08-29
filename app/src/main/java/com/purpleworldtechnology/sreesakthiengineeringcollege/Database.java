package com.purpleworldtechnology.sreesakthiengineeringcollege;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Base64;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    private Context ctx;
    Integer parent_id;

    private static final String DATABASE_NAME = "ssec_2022_v1.db";


    private static final String DB_PATH_SUFFIX = "/databases/";
    private static final int DATABASE_VERSION = 1;
    String encodedImage;

//    byte[] decodedString = Base64.decode(encodedImage, Base64.URL_SAFE);
//    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


    Helper helper = new Helper();

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //http://www.geeks.gallery/read-and-copy-database-from-assets-folder/
    public void CopyDatabaseFromAsset() throws IOException {
        InputStream myInput = ctx.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = getDatabasePath();

        // if the path doesn't exist first, create it
        File f = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    public void checkDataBase() {
        File dbFile = ctx.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                CopyDatabaseFromAsset();
                System.out.println("Copying sucess from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        } else {
            System.out.println("DB already exists");
        }

    }


    public Banners checkMaxBannerId() {
        Banners a = null;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        try {
            String[] parameters = null;
            String sql = "";

            sql = "SELECT * from banners order by banner_id desc limit 1 ;";
//           parameters = new String[]{String.valueOf(id)};
            Cursor cursor = db.rawQuery(sql, new String[] { });
            if (cursor.moveToFirst())
            {
                do {
                    a = new Banners();
                    a.banner_id = cursor.getInt(cursor.getColumnIndex("banner_id"));
                    a.banner_name = cursor.getString(cursor.getColumnIndex("banner_name"));
                    a.banner_data = cursor.getString(cursor.getColumnIndex("banner_data"));

                    a.sub_header_text = cursor.getString(cursor.getColumnIndex("sub_header_text"));
                    a.sub_header_visiblity = cursor.getInt(cursor.getColumnIndex("sub_header_visiblity"));
                    a.is_visible = cursor.getInt(cursor.getColumnIndex("is_visible"));

                    a.created_at = cursor.getString(cursor.getColumnIndex("created_at"));
                    a.updated_at = cursor.getString(cursor.getColumnIndex("updated_at"));
                    a.deleted_at = cursor.getString(cursor.getColumnIndex("deleted_at"));


                } while (cursor.moveToNext());
            }
            cursor.close();

            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception ex) {
            Log.i("LOG", ex.getMessage());
        }
        return a;
    }


   // Get banner data start

    public ArrayList<Banners> getBanner() {
        ArrayList<Banners> list = new ArrayList<Banners>();

        try {
            String sql = "SELECT  *  FROM banners ";

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            Cursor cursor = db.rawQuery(sql, new String[] { });
            if (cursor.moveToFirst()) {
                do {
                    Banners a      = new Banners();
                    a.banner_id = cursor.getInt(cursor.getColumnIndex("banner_id"));
                    a.banner_name = cursor.getString(cursor.getColumnIndex("banner_name"));
                    a.banner_data = cursor.getString(cursor.getColumnIndex("banner_data"));

                    a.sub_header_text = cursor.getString(cursor.getColumnIndex("sub_header_text"));
                    a.sub_header_visiblity = cursor.getInt(cursor.getColumnIndex("sub_header_visiblity"));
                    a.is_visible = cursor.getInt(cursor.getColumnIndex("is_visible"));

                    a.created_at = cursor.getString(cursor.getColumnIndex("created_at"));
                    a.updated_at = cursor.getString(cursor.getColumnIndex("updated_at"));
                    a.deleted_at = cursor.getString(cursor.getColumnIndex("deleted_at"));
                    list.add(a);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
        }
        catch (Exception ex) {
            Log.e("LOG",ex.getMessage());
        }
        return list;
    }

   // Get Banner data end


    //pages table insert from web service
    public void insertBanner(String result) {

        this.createBannersTable();

        // String sql = "DELETE FROM pages";

        String insertSql = "INSERT INTO banners ([banner_id],[banner_name],[banner_data],[sub_header_text],[sub_header_visiblity],[is_visible],[created_at],[updated_at],[deleted_at]) VALUES (?,?,?,?,?,?,?,?,?)";
//        String insertSql = "INSERT INTO menu_table ([id],[title],[description],[new_update],[color],[parent_id]) VALUES (?,?,?,?,?,?)";

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.beginTransactionNonExclusive();
//        db.execSQL(sql); //truncate table query
        try {
            //Parse abstracts received from Server and update into DB
            SQLiteStatement insertStmt = db.compileStatement(insertSql);
            JSONArray json = new JSONArray(result);
            int len = json.length();
            for (int i = 0; i < len; i++) {
                JSONObject e = json.getJSONObject(i);

                insertStmt.bindString(1, String.valueOf(e.getInt("banner_id")));
                insertStmt.bindString(2, e.getString("banner_name"));
                insertStmt.bindString(3, e.getString("banner_data"));

                insertStmt.bindString(4, e.getString("sub_header_text"));
                insertStmt.bindString(5, e.getString("sub_header_visiblity"));
                insertStmt.bindString(6, e.getString("is_visible"));

                insertStmt.bindString(7, String.valueOf(e.getInt("created_at")));
                insertStmt.bindString(8, e.getString("updated_at"));
                insertStmt.bindString(9, String.valueOf(e.getInt("deleted_at")));
                insertStmt.execute();
                insertStmt.clearBindings();
            }

            db.setTransactionSuccessful();
            db.endTransaction();

            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception ex) {
            Log.i("LOG", ex.getMessage());
        }
    }


    // createBannersTable  create
    public void createBannersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS banners (banner_id INTEGER PRIMARY KEY AUTOINCREMENT, banner_name TEXT DEFAULT NULL,banner_data TEXT DEFAULT NULL," +
                "sub_header_text TEXT DEFAULT NULL,sub_header_visiblity INTEGER DEFAULT 0," +
                "is_visible INTEGER DEFAULT 0,created_at TEXT DEFAULT NULL,updated_at TEXT DEFAULT  NULL,deleted_at TEXT DEFAULT NULL)";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        try {

            db.execSQL(sql);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception ex) {
            Log.i("LOG", ex.getMessage());
        }
    }





    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }








    // *************** map page id with page and asset table created on Feb 27 ****
//    public PageAssetMap assetId(Integer page_id) {
//        PageAssetMap a = null;
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//        try {
//
//            String[] parameters = null;
//            String sql = "";
//
//            sql = "SELECT * from page_asset_map where page_id = ?";
//            parameters = new String[]{String.valueOf(page_id)};
//            Cursor cursor = db.rawQuery(sql, parameters);
//            if (cursor.moveToFirst())
//            {
//                do {
//                    a = new PageAssetMap();
//                    a.asset_id = cursor.getInt(cursor.getColumnIndex("asset_id"));
//                    a.page_id = cursor.getInt(cursor.getColumnIndex("page_id"));
//
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//
//            DatabaseManager.getInstance().closeDatabase();
//        } catch (Exception ex) {
//            Log.i("LOG", ex.getMessage());
//        }
//        return  a;
////        return new PageAssetMap[]{a};
//    }
    //*****************************************************************************





}
