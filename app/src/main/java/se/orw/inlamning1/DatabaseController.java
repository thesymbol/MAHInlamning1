package se.orw.inlamning1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Marcus Orwén on 2014-09-18.
 */
public class DatabaseController extends SQLiteOpenHelper{
    private static final String DB_NAME = "EconomyHelper";
    private static final int DB_VERSION = 2;
    private SQLiteDatabase database;

    /**
     * Constructor
     *
     * @param context Context
     */
    public DatabaseController(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Open write able database
     */
    public void open() {
        database = getWritableDatabase();
    }

    /**
     * Close connection to database
     */
    public void close() {
        database.close();
    }

    /**
     * Add income entry to database
     *
     * @param title The title of the entry.
     * @param date The date of the entry.
     * @param amount The amount of money/price.
     * @param category The category of the entry.
     */
    public void addIncomeEntry(String title, String date, int amount, String category, int userID) {
        ContentValues values = new ContentValues();
        values.put("datevalue", date);
        values.put("title", title);
        values.put("category", category);
        values.put("amount", amount);
        values.put("type", "Income");
        values.put("userID", userID);
        database.insert("income", null, values);
    }

    /**
     * removes an income entry from the database based on the id
     *
     * @param id The income ID
     */
    public void removeIncomeEntry(int id) {
        database.execSQL("DELETE FROM income WHERE id=" + id);
    }

    /**
     * Add outcome entry to database
     *
     * @param title The title of the entry.
     * @param date The date of the entry.
     * @param amount The amount of money/price.
     * @param category The category of the entry.
     */
    public void addOutcomeEntry(String title, String date, int amount, String category, int userID) {
        ContentValues values = new ContentValues();
        values.put("datevalue", date);
        values.put("title", title);
        values.put("category", category);
        values.put("amount", amount);
        values.put("type", "Outcome");
        values.put("userID", userID);
        database.insert("outcome", null, values);
    }

    /**
     * removes an outcome entry from the database based on the id
     *
     * @param id The outcome ID
     */
    public void removeOutcomeEntry(int id) {
        database.execSQL("DELETE FROM outcome WHERE id=" + id);
    }

    /**
     * Get all income and outcome sorted by date descending.
     *
     * @param userID - The userID of the user's income/outcome entries
     * @return Cursor
     */
    public Cursor getAllIncomeOutcome(int userID) {
        return database.rawQuery("SELECT * FROM outcome WHERE userID=" + userID + " UNION SELECT * FROM income WHERE userID=" + userID + " ORDER BY datevalue ASC", null);
    }

    /**
     * Get all income sorted by date descending.
     *
     * @return Cursor
     */
    public Cursor getAllIncome(int userID) {
        return database.rawQuery("SELECT * FROM income WHERE userID=" + userID + " ORDER BY datevalue DESC", null);
    }

    /**
     * Get all outcome sorted by date descending.
     *
     * @return Cursor
     */
    public Cursor getAllOutcome(int userID) {
        return database.rawQuery("SELECT * FROM outcome WHERE userID=" + userID + " ORDER BY datevalue DESC", null);
    }

    /**
     * Get user id by first and last names.
     *
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @return The user ID -1 if no user exists
     */
    public int getUserID(String firstName, String lastName) {
        Cursor cursor = database.rawQuery("SELECT id FROM users WHERE firstName='" + firstName + "' AND lastName='" + lastName + "'", null);
        int userID = -1;
        if(cursor.moveToFirst()) {
            userID = cursor.getInt(0);
        }
        cursor.close();
        return userID;
    }

    /**
     * Adds a user to the database
     *
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     */
    public void addUser(String firstName, String lastName) {
        ContentValues values = new ContentValues();
        values.put("firstName", firstName);
        values.put("lastName", lastName);
        database.insert("users", null, values);
    }

    /**
     * Called once app is started and cannot find the database tables.
     *
     * @param sqLiteDatabase The SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY, firstName VARCHAR(255), lastName VARCHAR(255), CONSTRAINT uniqueUsers UNIQUE(firstName, lastName));");
        sqLiteDatabase.execSQL("CREATE TABLE income (id INTEGER PRIMARY KEY, datevalue DATE, title VARCHAR(255), category VARCHAR(255), amount INTEGER(11), type VARCHAR(255), userID INTEGER, FOREIGN KEY (userID) REFERENCES users(id));");
        sqLiteDatabase.execSQL("CREATE TABLE outcome (id INTEGER PRIMARY KEY, datevalue DATE, title VARCHAR(255), category VARCHAR(255), amount INTEGER(11), type VARCHAR(255), userID INTEGER, FOREIGN KEY (userID) REFERENCES users(id));");
    }

    /**
     *
     *
     * @param sqLiteDatabase
     * @param i
     * @param i2
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    }
}
