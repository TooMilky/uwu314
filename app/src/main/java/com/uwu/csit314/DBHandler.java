package com.uwu.csit314;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.google.firebase.auth.FirebaseAuth;
import com.uwu.csit314.Model.Category;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private FirebaseAuth mAuth;

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "uwuDB314";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "users";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our course name column
    private static final String NAME_COL = "username";

    // below variable id for our course duration column.
    private static final String DURATION_COL = "password";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + DURATION_COL + " TEXT)";

        String categoryQuery = "CREATE TABLE " + "Category" + " ("
                + "categoryId" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "image" + " TEXT,"
                + "name" + " TEXT)";

        String menuQuery = "CREATE TABLE Foods (menuId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "description TEXT," +
                "image TEXT," +
                "discount TEXT," +
                "name TEXT," +
                "price TEXT)";

        String orderDetailQuery = "CREATE TABLE OrderDetail (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ProductId TEXT," +
                "ProductName TEXT," +
                "Quantity TEXT," +
                "Price TEXT," +
                "Discount TEXT," +
                "Image TEXT)";

        //
        String rolesQuery = "CREATE TABLE " + "roles" + " ("
                + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "role" + " TEXT)";

        String userRolesQuery = "create table users_roles(\n" +
                "    user_role_id integer not null primary key,\n" +
                "    user_id integer not null,\n" +
                "    role_id integer not null,\n" +
                "    FOREIGN KEY(user_id) REFERENCES users(id),\n" +
                "    FOREIGN KEY(role_id) REFERENCES roles(id),\n" +
                "    UNIQUE (user_id, role_id)\n" +
                ");  ";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
        db.execSQL(menuQuery);
        db.execSQL(categoryQuery);
        db.execSQL(orderDetailQuery);
        db.execSQL(rolesQuery);
        db.execSQL(userRolesQuery);
        initDB(db);
    }

    // get all categories
    public List<Category> getCategories(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlselect ={"categoryId","image", "name"};
        String sqlTable ="Category";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlselect,null,null,null,null,null);
        final List<Category> result = new ArrayList<>();
        if (c.moveToFirst()){
            do{
                result.add(new Category(
                        c.getInt(c.getColumnIndexOrThrow("categoryId")),
                        c.getString(c.getColumnIndexOrThrow("image")),
                        c.getString(c.getColumnIndexOrThrow("name"))
                ));
            }while (c.moveToNext());
        }
        return result;
    };

    // insert example data
    public void initDB (SQLiteDatabase db){
        String query = String.format("INSERT INTO Foods(description,image,discount,name,price) VALUES('%s', '%s','%s','%s','%s');",
                "Example menu",
                "",
                "10",
                "Example",
                "20");
        String query2 = String.format("INSERT INTO Category(image,name) VALUES('%s','%s');",
                "",
                "Example category");
        db.execSQL(query2);
    }

    // example to check if column exist
    public boolean columnExists(String value) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT EXISTS (SELECT * FROM users WHERE username='"+value+"' LIMIT 1)";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        // cursor.getInt(0) is 1 if column with value exists
        if (cursor.getInt(0) == 1) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }


    // this method is use to add new course to our sqlite database.
    public void loginUser(String courseName, String courseDuration) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME_COL, courseName);
        values.put(DURATION_COL, courseDuration);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Boolean insertData(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }
}