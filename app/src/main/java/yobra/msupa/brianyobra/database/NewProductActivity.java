package yobra.msupa.brianyobra.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewProductActivity extends Activity {
    private NotificationManager mNotificationManager;
    private int notificationID = 100;
    private int numMessages = 0;

    // Progress Dialog
    private ProgressDialog pDialog;



    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputPrice;
    EditText inputDesc;
    EditText txtcat;
    EditText txtphoto;

    // url to create new product
    private static String url_create_product = "http://192.168.141.1/danjose/nuprod.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputPrice = (EditText) findViewById(R.id.inputPrice);
        inputDesc = (EditText) findViewById(R.id.inputDesc);
        txtcat = (EditText) findViewById(R.id.cat);

        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);

        // button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /* catch error or inputing null in edit textbox  and saving successfully */

                // try{
                //if (inputName = null) {

                alert();
                // }

                //} catch (JSONException e) {
                //e.printStackTrace();
                //}


            }
        });
    }

    protected void displayNotification() {
        Log.i("Start", "notification");
        NotificationCompat.Builder
                mBuilder =
                new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("New Message");
        mBuilder.setContentText("You've received new message.");
        mBuilder.setTicker("New Message Alert!");
        mBuilder.setSmallIcon(R.drawable.android_business);
/* Increase notification number every time a new notification
arrives */
        mBuilder.setNumber(++numMessages);
/* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(this, AllProductsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationView.class);
/* Adds the Intent that starts the Activity to the top of the stack
*/
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

    }



    protected void updateNotification() {
        Log.i("Update", "notification");
/* Invoking the default notification service */
        NotificationCompat.Builder
                mBuilder =
                new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Updated Message");
        mBuilder.setContentText("You've got updated message.");
        mBuilder.setTicker("Updated Message Alert!");
        mBuilder.setSmallIcon(R.drawable.android_business);
/* Increase
arrives
        notification
                number
        every
                time
        a
        new
                notification*/
        mBuilder.setNumber(++numMessages);
/* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(this, AllProductsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationView.class);
/* Adds the Intent that starts the Activity to the top of the stack
*/
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
/* Update the existing notification using same notification ID */
        mNotificationManager.notify(notificationID, mBuilder.build());
    }


    public void alert(){
       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
       alertDialogBuilder.setMessage(R.string.decision);
       alertDialogBuilder.setPositiveButton(R.string.positive_button,
               new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface arg0, int arg1) {
                       String name = inputName.getText().toString();
                       String price = inputPrice.getText().toString();
                       String description = inputDesc.getText().toString();
                       String category = txtcat.getText().toString();

                       new CreateNewProduct().execute(name, price, description, category);
                       displayNotification();
                       updateNotification();
                       Intent positveActivity = new Intent(getApplicationContext(),yobra.msupa.brianyobra.database.AllProductsActivity.class);
                       startActivity(positveActivity);
                   }
               });

        alertDialogBuilder.setNegativeButton(R.string.negative_button,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent negativeActivity = new Intent(getApplicationContext(), yobra.msupa.brianyobra.database.NewProductActivity.class);
                        startActivity(negativeActivity);
                    }

                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    /**
     * Background Async Task to Create new product
     * */


    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(NewProductActivity.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String food_name = args[0],
                    food_price = args[1],
                    food_description = args[2],
                    food_category = args[3];



            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("food_name", food_name));
            params.add(new BasicNameValuePair("food_price", food_price));
            params.add(new BasicNameValuePair("food_description", food_description));
            params.add(new BasicNameValuePair("food_category", food_category));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

//positive();
                } else {
                    // failed to create product
//negative();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}