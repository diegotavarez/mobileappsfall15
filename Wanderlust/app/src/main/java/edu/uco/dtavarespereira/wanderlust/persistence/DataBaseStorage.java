package edu.uco.dtavarespereira.wanderlust.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.uco.dtavarespereira.wanderlust.entity.Place;

/**
 * Created by diegotavarez on 12/1/15.
 */
public class DataBaseStorage implements StorageSystem {
    public static final String PLACES_TABLE_NAME = "FAVORITE_PLACES_TO_VISIT";
    public static final String PLACE_ID = "_id";
    public static final String PLACE_GOOGLE_ID = "GOOGLE_ID";
    public static final String PLACE_NAME = "NAME";
    public static final String PLACE_ADDRESS = "ADDRESS";
    public static final String PLACE_NUMBER = "PHONE_NUMBER";
    public static final String PLACE_WEBSITE = "WEBSITE";
    public static final String PLACE_RATING = "RATING";
    public static final String PLACE_LOCATION = "LOCATION";
    public static final String PLACE_CATEGORY = "CATEGORY";

    /** The Constant EXPENSE_CREATE_TABLE. */
    private static final String PLACES_CREATE_TABLE = "CREATE TABLE "
            + PLACES_TABLE_NAME + "  (" + PLACE_ID
            + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + PLACE_GOOGLE_ID +
            " TEXT NOT NULL," + PLACE_NAME
            + " TEXT NOT NULL," + PLACE_ADDRESS + " TEXT NOT NULL,"
            + PLACE_NUMBER + " TEXT NOT NULL," + PLACE_WEBSITE
            + " TEXT NOT NULL," + PLACE_RATING
            + " TEXT NOT NULL," + PLACE_LOCATION + " TEXT NOT NULL,"
            + PLACE_CATEGORY + " TEXT NOT NULL" + ");";

    /** Create database. */
    @SuppressWarnings("unused")
    private static final String TAG = "DataBaseStorage";

    /** The m db helper. */
    private DatabaseHelper mDbHelper;

    /** The m db. */
    private SQLiteDatabase mDb;

    /** The Constant DB_NAME. */
    private static final String DB_NAME = "DBP";

    /** The Constant DATABASE_VERSION. */
    private static final int DATABASE_VERSION = 1;

    /** The m ctx. */
    private final Context mCtx;

    /**
     * The Class DatabaseHelper.
     */
    public static class DatabaseHelper extends SQLiteOpenHelper {

        /*
         * (non-Javadoc)
         * @see
         * android.database.sqlite.SQLiteOpenHelper#onOpen(android.database.
         * sqlite.SQLiteDatabase)
         */
        @Override
        public final void onOpen(final SQLiteDatabase db) {
            super.onOpen(db);
        }

        /**
         * Instantiates a new database helper.
         *
         * @param context the context
         */
        DatabaseHelper(final Context context) {
            super(context, DB_NAME, null, DATABASE_VERSION);
        }

        /*
         * (non-Javadoc)
         * @see
         * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database
         * .sqlite.SQLiteDatabase)
         */
        @Override
        public final void onCreate(final SQLiteDatabase db) {
            db.execSQL(PLACES_CREATE_TABLE);
        }

        /*
        * (non-Javadoc)
        * @see
        * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database
        * .sqlite.SQLiteDatabase, int, int)
        */
        @Override
        public final void onUpgrade(final SQLiteDatabase db,
                                    final int oldVersion, final int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + PLACES_TABLE_NAME);
            onCreate(db);
        }
    }

    public DataBaseStorage(final Context ctx) throws  Exception{
        mCtx = ctx;
        try
        {
            open();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    /**
     * Creates the.
     */
    public final void create() {
        mDb.execSQL(PLACES_CREATE_TABLE);
    }

    /**
     * Recreate.
     */
    public final void recreate() {
        mDb.execSQL("DROP TABLE IF EXISTS " + PLACES_TABLE_NAME);
    }

    /**
     * Open.
     *
     * @return the data base storage
     * @throws SQLException the SQL exception
     */
    public final DataBaseStorage open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Close.
     */
    public final void close() {
        mDbHelper.close();
        mDb.close();
    }

    /**
     * Gets the MDB helper.
     *
     * @return the MDB helper
     */
    public final DatabaseHelper getMDBHelper() {
        return mDbHelper;
    }

    /**
     * Gets the mdb.
     *
     * @return the mdb
     */
    public final SQLiteDatabase getMdb() {
        return mDb;
    }

    @Override
    public void addPlace(final Place place) {
        final ContentValues values = expenseContentValues(place);
        mDb.insert(PLACES_TABLE_NAME, null, values);
    }

    /**
     * Delete expense by id.
     *
     * @param id the id
     */
    @Override
    public final void deletePlace(final Integer id) {
        mDb.delete(PLACES_TABLE_NAME, PLACE_ID + "=" + id, null);

    }

    /**
     * Updates existing expense with new values.
     *
     * @param place - existing expense with edited values
     */
    @Override
    public final void edit(final Place place) {
        final ContentValues values = expenseContentValues(place);
        mDb.update(PLACES_TABLE_NAME, values,
                PLACE_ID + "=" + place.getGoogleID(), null);
    }

    /**
     * Save the attributes of expenses in their respective columns.
     *
     * @param place to be saved
     * @return content values object
     */
    private ContentValues expenseContentValues(final Place place) {
        final ContentValues values = new ContentValues();

        values.put(PLACE_GOOGLE_ID, place.getGoogleID());
        values.put(PLACE_NAME, place.getName());
        values.put(PLACE_ADDRESS, place.getAddress());
        values.put(PLACE_NUMBER, place.getPhoneNumber());
        values.put(PLACE_WEBSITE, place.getWebsite());
        values.put(PLACE_RATING, place.getRating());
        values.put(PLACE_LOCATION, "");
        values.put(PLACE_CATEGORY, place.getCategory());

        return values;
    }

    @Override
    public final Place getPlace(final int key) {
        Cursor mCursor = null;
        mCursor = mDb.query(true, PLACES_TABLE_NAME, new String[] {
                        PLACE_ID, PLACE_GOOGLE_ID, PLACE_NAME, PLACE_ADDRESS, PLACE_NUMBER,
                        PLACE_WEBSITE, PLACE_RATING, PLACE_LOCATION,
                        PLACE_CATEGORY
                },
                PLACE_ID + "=?", new String[] {
                        String.valueOf(key)
                }, null,
                null, null, null);
        Place place = null;
        if (mCursor != null) {
            mCursor.moveToFirst();
            final Integer id = mCursor.getInt(mCursor.getColumnIndex(PLACE_ID));

            final String googleId =
                    mCursor.getString(mCursor.getColumnIndex(PLACE_GOOGLE_ID));
            final String name =
                    mCursor.getString(mCursor.getColumnIndex(PLACE_NAME));
            final String address = mCursor.getString(mCursor
                    .getColumnIndex(PLACE_ADDRESS));
            final String number = mCursor
                    .getString(mCursor.getColumnIndex(PLACE_NUMBER));
            final String website = mCursor.getString(mCursor
                    .getColumnIndex(PLACE_WEBSITE));
            final String rating = mCursor.getString(mCursor
                    .getColumnIndex(PLACE_RATING));
            final String location = mCursor.getString(mCursor
                    .getColumnIndex(PLACE_LOCATION));
            final String category = mCursor.getString(mCursor
                    .getColumnIndex(PLACE_CATEGORY));

            place = new Place(id, googleId, name, address, number, website, rating, null, category);
            place.setDBID(id);
            mCursor.close();
        }
        return place;

    }

    @Override
    public final List<Place> getPlaces() {
        final List<Place> places = new ArrayList<Place>();

        try {
            final String query = "SELECT * FROM " + PLACES_TABLE_NAME;
            final Cursor mCursor = mDb.rawQuery(query, null);

            if (mCursor != null) {
                mCursor.moveToFirst();
                while (!mCursor.isAfterLast()) {
                    places.add(getPlace(mCursor.getInt(mCursor
                            .getColumnIndex(PLACE_ID))));
                    mCursor.moveToNext();
                }
            }
            if (mCursor != null) {
                mCursor.close();
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(places);
        return places;

    }



}
