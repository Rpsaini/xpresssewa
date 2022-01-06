package transfer.money.com.xpresssewa.View;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.util.SimpleDialog;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class BuisnessProfileStepII extends AppCompatActivity {

    private TextInputLayout et_business_name,et_registration_number,et_website,et_business_address,et_city,et_postcode;
    private Spinner spinner_roleincompany,spinner_buisnessdo_category,spinner_buisnessdo_subcategory;
    private TextView company_continue;
    private Showtoast showtoast;
    private LinearLayout ll_main_layout;
    ArrayList<String> roleArray=new ArrayList<>();
    final ArrayList<String> subCategoryDataAr=new ArrayList<>();
    private JSONArray subCategoryJsonArray=new JSONArray();
    private String CompanyTypeId="",BusinessCountryId="";
    private JSONObject dataObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buisness_profile_step_ii);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        UtilClass.getUserData(this);
        et_business_name=findViewById(R.id.et_business_name);
        et_registration_number=findViewById(R.id.et_registration_number);
        et_website=findViewById(R.id.et_website);
        et_business_address=findViewById(R.id.et_business_address);
        et_city=findViewById(R.id.et_city);
        et_postcode=findViewById(R.id.et_postcode);
        ll_main_layout=findViewById(R.id.ll_main_layout);
        spinner_roleincompany=findViewById(R.id.spinner_roleincompany);
        company_continue=findViewById(R.id.company_continue);
        spinner_buisnessdo_category=findViewById(R.id.spinner_buisnessdo_category);
        spinner_buisnessdo_subcategory=findViewById(R.id.spinner_buisnessdo_subcategory);
        showtoast=new Showtoast();

        saveInformation();

        findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    private void saveInformation()
    {


        try {
            roleArray.add("Select Role");
            String data = getIntent().getStringExtra("userdata");
             dataObj = new JSONObject(data);

            et_business_address.getEditText().setText(dataObj.getString("BusinessAddress"));
            et_business_name.getEditText().setText(dataObj.getString("BusinessName"));
            et_registration_number.getEditText().setText(dataObj.getString("RegistrationNumber"));
            et_postcode.getEditText().setText(dataObj.getString("BusinessPostalCode"));
            et_city.getEditText().setText(dataObj.getString("BusinessCity"));
            et_website.getEditText().setText(dataObj.getString("WebsiteName"));

            CompanyTypeId= dataObj.getString("CompanyTypeId");
            BusinessCountryId= dataObj.getString("CountryId");//its BusinessCountryId





           final JSONArray CompanyRoleList =dataObj.getJSONArray("CompanyRoleList");
            for(int x=0;x<CompanyRoleList.length();x++)
            {
                JSONObject obj=CompanyRoleList.getJSONObject(x);
                roleArray.add(obj.getString("Title"));
            }

            ArrayAdapter<String> spinnerStateAdapter = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, roleArray);
            spinner_roleincompany.setAdapter(spinnerStateAdapter);

            papulateOcupationSpinner(dataObj.getString("CompanyRoleId"),CompanyRoleList,spinner_roleincompany);
            spinner_roleincompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    try
                    {
                        if(position>0) {
                            System.out.println("Company role list===" + CompanyRoleList + "===" + position);

                            JSONObject obj = CompanyRoleList.getJSONObject(position-1);
                            CreateBuisnessProfile.profileData.put("CompanyRoleId", obj.getString("Id"));
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    CreateBuisnessProfile.profileData.put("State", spinner_roleincompany.getSelectedItem() + "");
                }
            });

            //Select Ctegory====

            ArrayList<String> categoryArray=new ArrayList<>();
            categoryArray.add("Select Category");

           final  JSONArray CategoriesList=dataObj.getJSONArray("CategoriesList");
           subCategoryJsonArray=dataObj.getJSONArray("SubCategoriesList");


           System.out.println("Lsit of subcategory==="+subCategoryJsonArray);

           for(int x=0;x<CategoriesList.length();x++)
            {
                JSONObject obj=CategoriesList.getJSONObject(x);
                categoryArray.add(obj.getString("Title"));
            }

            subCategoryDataAr.add("Select Sub Category");
            for(int x=0;x<subCategoryJsonArray.length();x++)
            {
                JSONObject obj=subCategoryJsonArray.getJSONObject(x);
                subCategoryDataAr.add(obj.getString("Title"));
            }
            setSubCategoryData();


            ArrayAdapter<String> spinnerCategoryAdapter = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, categoryArray);
            spinner_buisnessdo_category.setAdapter(spinnerCategoryAdapter);


            papulateOcupationSpinner(dataObj.getString("BusinessCategoryId"),CategoriesList,spinner_buisnessdo_category);
            papulateOcupationSpinner(dataObj.getString("BusinessSubCategoryId"), subCategoryJsonArray, spinner_buisnessdo_subcategory);
            spinner_buisnessdo_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try
                    {
                        if(position>0)
                        {
                            JSONObject obj = CategoriesList.getJSONObject(position-1);
                            CreateBuisnessProfile.profileData.put("BusinessCategoryId", obj.getString("Id"));
                            getSubCategory(obj.getString("Id"));
                        }

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            company_continue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (et_business_name.getEditText().getText().toString().length() == 0) {
                        showtoast.showToast(BuisnessProfileStepII.this, "Error", "Enter Buisness name", ll_main_layout);
                        return;
                    }
                    if (spinner_roleincompany.getSelectedItemPosition() == 0) {
                        showtoast.showToast(BuisnessProfileStepII.this, "Error", "Select your role", ll_main_layout);
                       return;
                    }


                    if (et_registration_number.getEditText().getText().toString().length() == 0) {
                        showtoast.showToast(BuisnessProfileStepII.this, "Error", "Enter Registration number ", ll_main_layout);
                        return;
                    }

//                    if (et_website.getEditText().getText().toString().length() == 0) {
//                        showtoast.showToast(BuisnessProfileStepII.this, "Error", "Enter Website ", ll_main_layout);
//                        return;
//                    }
                    if(spinner_buisnessdo_category.getSelectedItemPosition()==0)
                    {
                        showtoast.showToast(BuisnessProfileStepII.this, "Error", "Select Category", ll_main_layout);
                        return;
                    }

                    if(spinner_buisnessdo_subcategory.getSelectedItemPosition()==0)
                    {
                        showtoast.showToast(BuisnessProfileStepII.this, "Error", "Select SubCategory", ll_main_layout);
                        return;
                    }
                    if (et_business_address.getEditText().getText().toString().length() == 0) {
                        showtoast.showToast(BuisnessProfileStepII.this, "Error", "Enter buisness name ", ll_main_layout);
                        return;
                    }
                    if (et_city.getEditText().getText().toString().length() == 0) {
                        showtoast.showToast(BuisnessProfileStepII.this, "Error", "Enter City name ", ll_main_layout);
                        return;
                    }



                    if (et_postcode.getEditText().getText().toString().length() == 0) {
                        showtoast.showToast(BuisnessProfileStepII.this, "Error", "Enter Postal code ", ll_main_layout);
                        return;
                    } else
                        {
                        CreateBuisnessProfile.profileData.put("BusinessName", et_business_name.getEditText().getText()+"");
                        CreateBuisnessProfile.profileData.put("CompanyTypeId", CompanyTypeId);

                        CreateBuisnessProfile.profileData.put("RegistrationNumber", et_registration_number.getEditText().getText()+"");
                        CreateBuisnessProfile.profileData.put("WebsiteName",et_website.getEditText().getText()+"" );
                        CreateBuisnessProfile.profileData.put("BusinessCountryId",BusinessCountryId );
                        CreateBuisnessProfile.profileData.put("BusinessAddress",et_business_address.getEditText().getText()+"");
                        CreateBuisnessProfile.profileData.put("BusinessCity", et_city.getEditText().getText()+"");
                        CreateBuisnessProfile.profileData.put("BusinessPostalCode", et_postcode.getEditText().getText()+"");
//                        CreateBuisnessProfile.profileData.remove("Type");
                        CreateBuisnessProfile.profileData.put("Type", "2");

                        CreateBuisnessProfile.profileData.remove("Hash");
                        System.out.println("request data==="+CreateBuisnessProfile.profileData);
                        new ServerHandler().sendToServer(BuisnessProfileStepII.this, "UpdateUser", CreateBuisnessProfile.profileData, 0,1, new CallBack() {
                            @Override
                            public void getRespone(String dta, ArrayList<Object> respons) {

                                try
                                {
                                    JSONObject obj=new JSONObject(dta);

                                    SimpleDialog simpleDialog = new SimpleDialog();
                                    final Dialog confirmDialog = simpleDialog.simpleDailog(BuisnessProfileStepII.this, R.layout.confirmation_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
                                    ImageView selected_Curreny = confirmDialog.findViewById(R.id.selected_Curreny);
                                    TextView txt_currency_name = confirmDialog.findViewById(R.id.txt_currency_name);
                                    TextView txt_no = confirmDialog.findViewById(R.id.txt_no);
                                    txt_no.setVisibility(View.GONE);
                                    TextView txt_yes = confirmDialog.findViewById(R.id.txt_yes);
                                    TextView txt_msg = confirmDialog.findViewById(R.id.txt_msg);
                                    selected_Curreny.setImageResource(R.drawable.checked);
                                    if(obj.getBoolean("status")) {
                                        txt_currency_name.setText("Profile Updated");
                                        txt_msg.setText("Your business profile has been successfully updated.");
                                        txt_yes.setText("OK");

                                        txt_yes.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                confirmDialog.dismiss();
                                                Intent resultIntent = new Intent();
                                                resultIntent.putExtra("result", "");
                                                setResult(RESULT_OK, resultIntent);
                                                finish();
                                            }
                                        });

                                    }
                                    else
                                    {
                                        showtoast.showToast(BuisnessProfileStepII.this,"Error","Details not Saved",ll_main_layout);
                                    }



                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        });



                    }
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void papulateOcupationSpinner(String id, JSONArray array,Spinner spinner)
    {
        try
        {

            for(int x=0;x<array.length();x++)
            {
                JSONObject occupationObj =array.getJSONObject(x);
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




    private void getSubCategory(String subCategoryID)
    {

        Map<String,String> m=new LinkedHashMap<>();
        m.put("CategoryId",subCategoryID);
        System.out.println("Get sub cat====="+m);
        new ServerHandler().sendToServer(this, "GetSubCategory", m, 0,1,new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons)
            {
                try
                {
                    subCategoryDataAr.clear();
                    JSONObject jsonObject=new JSONObject(dta);
                    if(jsonObject.getString("status").equalsIgnoreCase("true"))
                    {
                        subCategoryJsonArray=jsonObject.getJSONArray("SubCategoriesList");
                        subCategoryDataAr.add("Select Sub Category");
                        for(int x=0;x<subCategoryJsonArray.length();x++)
                        {
                            JSONObject dataObj=subCategoryJsonArray.getJSONObject(x);
                            subCategoryDataAr.add(dataObj.getString("Title"));
                        }


                       setSubCategoryData();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setSubCategoryData() {

        try {
            ArrayAdapter<String> subcategoryArrayAdapter = new ArrayAdapter<String>
                    (BuisnessProfileStepII.this, R.layout.spinner_item, subCategoryDataAr);

            spinner_buisnessdo_subcategory.setAdapter(subcategoryArrayAdapter);
            spinner_buisnessdo_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        for (int x = 0; x < subCategoryJsonArray.length(); x++) {
                            try {
                                if (subCategoryJsonArray.getJSONObject(x).getString("Title").equalsIgnoreCase(subCategoryDataAr.get(position))) {
                                    CreateBuisnessProfile.profileData.put("BusinessSubCategoryId", subCategoryJsonArray.getJSONObject(x).getString("Id"));
                                    break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            System.out.println("Selcted Subcategory==="+dataObj.getString("BusinessSubCategoryId")+"=="+subCategoryJsonArray);
            papulateOcupationSpinner(dataObj.getString("BusinessSubCategoryId"), subCategoryJsonArray, spinner_buisnessdo_subcategory);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
