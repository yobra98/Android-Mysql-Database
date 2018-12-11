package yobra.msupa.brianyobra.database;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AllProductsActivity extends ListActivity {

    private String finalUrl="http://192.168.141.1/danjose/statics.php";
    private handlexml obj;
    private EditText title,link,description;

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;

    // url to get all products list
    private static String url_all_products = "http://192.168.141.1/danjose/statics.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products8";
    private static final String TAG_PID = "food id";
    private static final String TAG_NAME = "food name";
    private static final String TAG_DESC = "food description";
    private static final String TAG_PRICE = "food price";
    private static final String TAG_CAT = "food category";
    String data = "http://192.168.57.1/danjose/statics.php";


    // products JSONArray
    JSONArray food_details = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_products);

        // Get listview
        ListView lv = getListView();

        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();

        // initialize conncection and see if ur connected to  net
        new network1(this,data).execute(url_all_products);


        // Loading products in Background Thread
        new LoadAllProducts().execute();


        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String food_id = ((TextView) view.findViewById(R.id.pid)).getText().toString();

                Log.d("string", food_id);


                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        EditProductActivity.class);
                // sending pid to next activity
                in.putExtra(TAG_PID, food_id);


                // starting new activity and expecting some response back
                startActivityForResult(in, 100);


            }


        });

    }

    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     */

    class LoadAllProducts extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllProductsActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "POST", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    food_details = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < food_details.length(); i++) {
                        JSONObject c = food_details.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);
                        String desc = c.getString(TAG_DESC);
                        String price = c.getString(TAG_PRICE);
                        String cat = c.getString(TAG_CAT);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_DESC, desc);
                        map.put(TAG_PRICE, price);
                        map.put(TAG_CAT, cat);


                        // adding HashList to ArrayList
                        productsList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewProductActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }

            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            AllProductsActivity.this, productsList,
                            R.layout.list_item, new String[]{TAG_PID,
                            TAG_NAME, TAG_DESC, TAG_PRICE, TAG_CAT},
                            new int[]{R.id.pid, R.id.name, R.id.desc, R.id.price, R.id.cat});
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }


    }


    public class network1 extends AsyncTask {

        private String data;
        private Context context;


        public network1(Context context, String data){
            this.context = context;
            this.data = data;
        }

        protected void onPreExecute() {

            checkInternetConnection();
        }

        //check Internet connection.
        private void checkInternetConnection() {
            ConnectivityManager check = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            if (check != null) {
                NetworkInfo[] info = check.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            Toast.makeText(context, "Internet is connected",
                                    Toast.LENGTH_SHORT).show();
                        }
            } else {
                Toast.makeText(context, "not conencted to internet",
                        Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected String doInBackground(Object[] arg0) {

return null;
        }


        protected void onPostExecute(String result) {

        }
    }



}