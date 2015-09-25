package nutju.apps.android.nutrestaurant;

import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    // Explicit
    private UserTABLE objUserTABLE;
    private FoodTABLE objFoodTABLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create & Connect SQLite
        createAndConnect();

        // Test add new Value
        //testAddNewValue();

        // Delete all data
        deleteAllData();

        // Synchronize JSON to SQLite
        syncJSONtoSQLite();

    }   // onCreate

    private void syncJSONtoSQLite() {

        // 0. Change Policy
        StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(myPolicy);

        int intTimes = 0;
        while (intTimes <= 1) {

            // Constant
            InputStream objInputStream = null;
            String strJSON = null;
            

            intTimes += 1;
        }   // while

    }   // sync JSON to SQLite

    private void deleteAllData() {
        SQLiteDatabase objSqLiteDatabase = openOrCreateDatabase("Restaurant.db", MODE_PRIVATE, null);
        objSqLiteDatabase.delete("userTABLE", null, null);
        objSqLiteDatabase.delete("foodTABLE", null, null);
    }

    private void testAddNewValue() {
        objUserTABLE.addNewUser("testUser", "testPass", "โดเรม่อน");
        objFoodTABLE.addNewFood("ข้าวราดผัดกระเพราไก่", "testSource", "40");
    }

    private void createAndConnect() {
        objUserTABLE = new UserTABLE(this);
        objFoodTABLE = new FoodTABLE(this);
    }   // create & connect

}   // Main Class
