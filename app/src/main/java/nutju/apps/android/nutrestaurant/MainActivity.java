package nutju.apps.android.nutrestaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    // Explicit
    private UserTABLE objUserTABLE;
    private FoodTABLE objFoodTABLE;
    private EditText edtUser, edtPassword;
    private String strUser, strPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind Widget
        bindWidget();

        // Create & Connect SQLite
        createAndConnect();

        // Test add new Value
        //testAddNewValue();

        // Delete all data
        deleteAllData();

        // Synchronize JSON to SQLite
        syncJSONtoSQLite();

    }   // onCreate

    public void clickLogin(View view) {
        strUser = edtUser.getText().toString().trim();
        strPassword = edtPassword.getText().toString().trim();

        if (strUser.equals("") || strPassword.equals("")) {
            // empty user or password
            myAlertDialog("มีช่องว่าง", "กรุณากรอกทุกช่องนะครับ");

        } else {
            // user and password not empty
            checkUser();

        }
    }   // Click Log in

    private void checkUser() {
        try {
            String[] strMyResult = objUserTABLE.searchUser(strUser);

            if (strPassword.equals(strMyResult[2])) {
                welcome(strMyResult[3]);

            } else {
                myAlertDialog("เกิดข้อผิดพลาด", "รหัสผ่านไม่ถูกต้อง, กรุณาลองใหม่อีกครั้ง");
            }

        } catch (Exception e) {
            myAlertDialog("เกิดข้อผิดพลาด", "ไม่พบ User: " + strUser + " ในระบบ");
        }
    }   // check user

    private void welcome(String strName) {
        
    }   // welcome

    private void myAlertDialog(String strTitle, String strMessage) {
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.danger);
        objBuilder.setTitle(strTitle);
        objBuilder.setMessage(strMessage);
        objBuilder.setCancelable(false);
        objBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        objBuilder.show();
    }

    private void bindWidget() {
        edtUser = (EditText) findViewById(R.id.edtUser);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
    }

    private void syncJSONtoSQLite() {

        // 0. Change Policy
        StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(myPolicy);

        int intTimes = 0;
        while (intTimes <= 1) {

            // Constant
            InputStream objInputStream = null;
            String strJSON = null;
            String strUrlUser = "http://swiftcodingthai.com/24Sep/php_get_data_nutju.php";
            String strUrlFood = "http://swiftcodingthai.com/24Sep/php_get_data_food_master.php";
            HttpPost objHttpPost;

            // 1. Create InputStream
            try {
                HttpClient objHttpClient = new DefaultHttpClient();
                if (intTimes != 1) {
                    objHttpPost = new HttpPost(strUrlUser);
                } else {
                    objHttpPost = new HttpPost(strUrlFood);
                }
                HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
                HttpEntity objHttpEntity = objHttpResponse.getEntity();
                objInputStream = objHttpEntity.getContent();

            } catch (Exception e) {
                Log.d("Restaurant", "InputStream : " + e.toString());
            }

            // 2. Create strJSON
            try {
                BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));
                StringBuilder objStringBuilder = new StringBuilder();
                String strLine = null;

                while ((strLine = objBufferedReader.readLine()) != null ) {
                    objStringBuilder.append(strLine);
                }   // while
                objInputStream.close();
                strJSON = objStringBuilder.toString();

            } catch (Exception e) {
                Log.d("Restaurant", "strJSON : " + e.toString());
            }

            // 3. Update SQLite
            try {
                final JSONArray objJsonArray = new JSONArray(strJSON);

                for (int i = 0; i < objJsonArray.length(); i++) {
                    JSONObject object = objJsonArray.getJSONObject(i);
                    if (intTimes != 1) {
                        // For userTABLE
                        String strUser = object.getString("user");
                        String strPassword = object.getString("password");
                        String strName = object.getString("name");
                        objUserTABLE.addNewUser(strUser, strPassword, strName);

                    } else {
                        // For foodTABLE
                        String strFood = object.getString("Food");
                        String strSource = object.getString("Source");
                        String strPrice = object.getString("Price");
                        objFoodTABLE.addNewFood(strFood, strSource, strPrice);

                    }

                }   // for

            } catch (Exception e) {
                Log.d("Restaurant", "SQLite : " + e.toString());
            }

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
