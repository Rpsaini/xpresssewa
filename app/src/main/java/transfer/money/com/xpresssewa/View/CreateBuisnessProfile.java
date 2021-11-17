package transfer.money.com.xpresssewa.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import io.reactivex.annotations.Nullable;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class CreateBuisnessProfile extends AppCompatActivity {

    private String CompanyTypeId="", CountryId = "";
    private LinearLayout ll_outer_layout;
    private Spinner buisnessSpinner, spinner_country;
    public static LinkedHashMap<String,String> profileData=new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_buisness_profile);

        ll_outer_layout = findViewById(R.id.ll_outer_layout);
        buisnessSpinner = findViewById(R.id.spinner_buisnesstype);
        spinner_country = findViewById(R.id.spinner_country);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);

        findViewById(R.id.continuebtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (spinner_country.getSelectedItemPosition() == 0) {
                        new Showtoast().showToast(CreateBuisnessProfile.this, "Error", "Select Country", ll_outer_layout);
                    } else if (buisnessSpinner.getSelectedItemPosition() == 0) {
                        new Showtoast().showToast(CreateBuisnessProfile.this, "Error", "Select Buisness Type", ll_outer_layout);
                    } else {

                        JSONObject userdata = new JSONObject(getIntent().getStringExtra("userdata"));

                        userdata.remove("CountryId");
                        userdata.remove("CompanyTypeId");
                        userdata.put("CountryId", CountryId);
                        userdata.put("CompanyTypeId",CompanyTypeId);



                        if (getIntent().getStringExtra("type").equalsIgnoreCase("1"))
                        {
                            Intent i = new Intent(CreateBuisnessProfile.this, CreatePersonalProfile.class);
                            i.putExtra("userdata", userdata+"");
                            startActivityForResult(i,102);
                            finish();
                        }
                        else
                            {
                            Intent i = new Intent(CreateBuisnessProfile.this, BuisnessProfileStepII.class);
                            i.putExtra("userdata", userdata+"");
                            startActivityForResult(i,102);
                            finish();
                           }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        init();

        findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        try {



            if(getIntent().hasExtra("userdata"))
              {
                String data = getIntent().getStringExtra("userdata");


                  System.out.println("Intremit data==="+data);
                JSONObject obj = new JSONObject(data);
                final JSONArray countryAr  = obj.getJSONArray("CountryList");

                ArrayList<String> countryjavaAr = new ArrayList<>();
                countryjavaAr.add("Select Country");

                for (int x = 0; x < countryAr.length(); x++) {
                    countryjavaAr.add(countryAr.getJSONObject(x).getString("CountryName"));

                }

                ArrayAdapter<String> spinnerCountryAdapter = new ArrayAdapter<String>
                        (this, R.layout.spinner_item, countryjavaAr);
                spinner_country.setAdapter(spinnerCountryAdapter);



                if(getIntent().getStringExtra("type").equalsIgnoreCase("1"))
                {
                    papulateSpinner(obj.getString("CountryId"),countryAr,spinner_country);
                }
                else
                {
                    papulateSpinner(obj.getString("BusinessCountryId"),countryAr,spinner_country);
                }

                System.out.println("Buisness country==="+obj.getString("BusinessCountryId"));
                spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try {

                            if(position>0) {
                                JSONObject countObj = countryAr.getJSONObject(position-1);
                                CountryId = countObj.getString("Id");
                                System.out.println("Select BusinessCountryId=="+CountryId);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });




                //=====================================================

                final JSONArray buisnessjAr = obj.getJSONArray("CompanyTypeList");
                final ArrayList<String> buisnessAr = new ArrayList<>();
                buisnessAr.add("Select Buisness");
                for (int x = 0; x < buisnessjAr.length(); x++) {
                    buisnessAr.add(buisnessjAr.getJSONObject(x).getString("Title"));
                }


                ArrayAdapter<String> companyTypeListAdapter = new ArrayAdapter<String>
                        (this, R.layout.spinner_item, buisnessAr);
                buisnessSpinner.setAdapter(companyTypeListAdapter);
                System.out.println("Buisness array---"+buisnessjAr+"=="+obj.getString("CompanyTypeId"));
                papulateSpinner(obj.getString("CompanyTypeId"),buisnessjAr,buisnessSpinner);

                buisnessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            if(position>0) {
                                JSONObject bObj = buisnessjAr.getJSONObject(position-1);
                                CompanyTypeId = bObj.getString("Id");
                                System.out.println("Company type id===" + CompanyTypeId);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void papulateSpinner(String id, JSONArray array,Spinner spinner)
    {
        try
        {
            System.out.println("Company role id==="+id+"==="+array);
            for(int x=0;x<array.length();x++)
            {
                JSONObject occupationObj =array.getJSONObject(x);
                System.out.println("id-----"+id+"==="+occupationObj.getString("Id")+"=="+x);
                if(id.equalsIgnoreCase(occupationObj.getString("Id")))
                {
                    spinner.setSelection(x+1);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==102)
        {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result", "");
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
