package transfer.money.com.xpresssewa.View;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import transfer.money.com.xpresssewa.BaseActivity;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.ApiProduction;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.interfaces.RxAPICallHelper;
import transfer.money.com.xpresssewa.interfaces.RxAPICallback;
import transfer.money.com.xpresssewa.interfaces.UploadticketImage;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.ticket.AddTicket;
import transfer.money.com.xpresssewa.util.AnimationForView;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.util.IsAnimationEndedCallback;
import transfer.money.com.xpresssewa.util.UtilClass;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.navigationView)
    BottomNavigationView navigationView;

    @BindView(R.id.rel_navigationview)
    RelativeLayout rel_navigationview;


    @BindView(R.id.fl_top)
    FrameLayout fl_top;


    FragmentTransaction ft_main;
    public JSONArray sourcecCurrncy, destinationCurrency;
    SaveImpPrefrences saveImpPrefrences = new SaveImpPrefrences();

    AnimationForView animationForView;
    public int screenHeight,screenWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animationForView=new AnimationForView();
        getScreenHeight();
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        ButterKnife.bind(this);
        UtilClass.getUserData(this);
        DefaultConstatnts.isKyDockUploaded=false;

        getNotification();

        sendBroadcast(new Intent(UtilClass.notificationBroadCast));
        try {

            String pin=saveImpPrefrences.reterivePrefrence(MainActivity.this, "Pin") +"";
            String MemberId=saveImpPrefrences.reterivePrefrence(MainActivity.this, DefaultConstatnts.MemberId) +"";
            String login=saveImpPrefrences.reterivePrefrence(MainActivity.this, DefaultConstatnts.login_detail) +"";


            JSONObject jsonObject=new JSONObject(login);
            UtilClass.getDefaultDestImage=jsonObject.getString("FlagImageDestination");
            UtilClass.getDefaultSourceImage=jsonObject.getString("FlagImageSource");
            UtilClass.defaultToSymble=jsonObject.getString("DestinationSymbol");
            UtilClass.DefaultFromSymble=jsonObject.getString("SourceSymbol");

            UtilClass.DefaultCountryname=jsonObject.getString("CountryName");
            UtilClass.defaultSourceCountryId=jsonObject.getString("CountryId");
            UtilClass.defaultSourceCountryId=jsonObject.getString("SDCountryId");


            if(pin.equalsIgnoreCase("0"))
            {
                Intent intent = new Intent(MainActivity.this, SetPinActivity.class);
                intent.putExtra(DefaultConstatnts.pinKey, DefaultConstatnts.pinSetup);
                startActivity(intent);
                finish();

            }
            else if(getIntent().getStringExtra(DefaultConstatnts.IsShowPin).equalsIgnoreCase("yes"))
            {
                Intent intent = new Intent(MainActivity.this, SetPinActivity.class);
                intent.putExtra(DefaultConstatnts.pinKey, DefaultConstatnts.pinVerify);
                startActivity(intent);
                finish();
            }

        }
        catch (Exception e) {

        }


        String isKycApproved=new SaveImpPrefrences().reterivePrefrence(this, DefaultConstatnts.IsKycApproved).toString();
        if(isKycApproved.equalsIgnoreCase("3"))//approved
        {
             // done
        }
        else if(isKycApproved.equalsIgnoreCase("2"))
        {
            //Myprofile
        }
        else if(isKycApproved.equalsIgnoreCase("1"))
        {
            //inprocess  at invoice
        }



        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getNotificationData();


    }

    protected void init() {
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // if(menuItem)
                bottomMenu(menuItem);
                return false;
                //My Contests
            }
        });



        if(getIntent().hasExtra("external_call"))
        {

            MenuItem menu=navigationView.getMenu().getItem(2);
            menu.setChecked(true);
            SendFragment sendFragment = new SendFragment();
            Bundle args = new Bundle();

            sendFragment.setArguments(args);
            replaceMainFragment(sendFragment,"send");
        }
        else
        {
            MenuItem menu=navigationView.getMenu().getItem(0);
            menu.setChecked(true);

            ActivityFragment activityFragment = new ActivityFragment();
            replaceMainFragment(activityFragment,"activity");
        }

    }

    private void replaceMainFragment(Fragment upcoming, String tag) {

        ft_main = getSupportFragmentManager().beginTransaction();
        ft_main.replace(R.id.fl_top, upcoming);
        //ft_main.addToBackStack(tag);
        ft_main.commit();
    }

    private void bottomMenu(MenuItem menuItem)
    {

        menuItem.setChecked(true);
        switch (menuItem.getItemId())
        {

            case R.id.menu_activity:
                ActivityFragment activityFragment = new ActivityFragment();
                replaceMainFragment(activityFragment,"activity");
                break;
            case R.id.menu_account:
                AccountFragment accountFragment = new AccountFragment();
                replaceMainFragment(accountFragment,"account");
                break;
            case R.id.menu_send:
                SendFragment sendFragment = new SendFragment();
                Bundle args = new Bundle();
                sendFragment.setArguments(args);
                replaceMainFragment(sendFragment,"send");
                break;

            case R.id.menu_recipients:
                RecipientFragment recipientFragment = new RecipientFragment();
                Bundle bd = new Bundle();
                bd.putString("symbol", "");
                recipientFragment.setArguments(bd);
                replaceMainFragment(recipientFragment,"recipient");

                break;
            case R.id.menu_invite:
                InviteFragment inviteFragment = new InviteFragment();
                replaceMainFragment(inviteFragment,"invite");

                break;


        }
    }

    public void sendFromSelectedList(JSONObject dataObj) {

        MenuItem menu=navigationView.getMenu().getItem(2);
        menu.setChecked(true);
        SendFragment sendFragment = new SendFragment();
        Bundle args = new Bundle();
        args.putString("data", dataObj + "");
        sendFragment.setArguments(args);
        replaceMainFragment(sendFragment,"send");

    }

    public void callToRecipientWithSymbol(String DestinationSymbol, String SourceSymbol, String
            FlagImageDestination, String callFrom, String SDCountryId, String CountryId, JSONObject obj) {

        RecipientFragment sendFragment = new RecipientFragment();
        MenuItem menu=navigationView.getMenu().getItem(3);
        menu.setChecked(true);

        Bundle args = new Bundle();
        args.putString("symbol", DestinationSymbol);
        args.putString("DestinationSymbol", DestinationSymbol + "");
        args.putString("SourceSymbol", SourceSymbol + "");
        args.putString("FlagImageDestination", FlagImageDestination + "");
        args.putString("callFrom", callFrom + "");
        args.putString("SDCountryId", SDCountryId + "");
        args.putString("CountryId", CountryId + "");
        args.putString("data", obj + "");
        sendFragment.setArguments(args);
        replaceMainFragment(sendFragment,"recipient");





//        Intent args=new Intent(MainActivity.this,RecipientActivity.class);
//        args.putExtra("symbol", DestinationSymbol);
//        args.putExtra("DestinationSymbol", DestinationSymbol + "");
//        args.putExtra("SourceSymbol", SourceSymbol + "");
//        args.putExtra("FlagImageDestination", FlagImageDestination + "");
//        args.putExtra("callFrom", callFrom + "");
//        args.putExtra("SDCountryId", SDCountryId + "");
//        args.putExtra("CountryId", CountryId + "");
//        args.putExtra("data", obj + "");
//        startActivity(args);

    }






  public void callMyProfileFragment(String editPersonal)
   {

       MenuItem menu=navigationView.getMenu().getItem(1);
       menu.setChecked(true);
    AccountFragment accountFragment = new AccountFragment();

    Bundle bundle=new Bundle();
    bundle.putString("Callfrom","edit_personal");
    accountFragment.setArguments(bundle);

    replaceMainFragment(accountFragment,"editPersonal");

   }


   public void animateUp(int height, View sourcedestinationcontainer) {
        animationForView.handleAnimation(MainActivity.this, sourcedestinationcontainer, 500, height, 00, IsAnimationEndedCallback.translationY, new IsAnimationEndedCallback() {
            @Override
            public void getAnimationStatus(String status) {

                switch (status) {
                    case IsAnimationEndedCallback.animationCancel: {
                        break;
                    }
                    case IsAnimationEndedCallback.animationEnd: {


                        break;
                    }

                    case IsAnimationEndedCallback.animationRepeat: {
                        break;
                    }

                    case IsAnimationEndedCallback.animationStart: {
                        break;
                    }

                }
            }
        });
    }


    public void downSourceDestinationView(View sourcedestinationcontainer, final Dialog dialog) {
        animationForView.handleAnimation(this, sourcedestinationcontainer, 500, 00, screenHeight, IsAnimationEndedCallback.translationY, new IsAnimationEndedCallback() {
            @Override
            public void getAnimationStatus(String status) {

                switch (status) {
                    case IsAnimationEndedCallback.animationCancel: {
                        break;
                    }
                    case IsAnimationEndedCallback.animationEnd: {

                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        break;
                    }

                    case IsAnimationEndedCallback.animationRepeat: {
                        break;
                    }

                    case IsAnimationEndedCallback.animationStart: {
                        break;
                    }

                }
            }
        });
    }

    private void getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }


//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//        int count = getSupportFragmentManager().getBackStackEntryCount();
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getSupportFragmentManager().popBackStack();
//        }
//    }


    int exitCount=1;
    @Override
    public void onBackPressed() {
        if(exitCount>=2)
        {
            finishAffinity();
            super.onBackPressed();
        }
        else
        {
            Toast.makeText(MainActivity.this,"Tap again to exit "+getResources().getString(R.string.app_name)+" app",Toast.LENGTH_LONG).show();
            exitCount++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exitCount=1;
                }
            },3000);

        }

    }



    private void getNotificationData()
    {
        try {
            UtilClass.getUserData(MainActivity.this);
            Map<String, String> m = new LinkedHashMap<>();
            m.put("MemberId", UtilClass.member_id);
            new ServerHandler().sendToServer(MainActivity.this, "GetNotifications", m, 1, 1, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        System.out.println("notification data==" + dta);
                        JSONObject obj = new JSONObject(dta);

                        if (obj.getString("status").equalsIgnoreCase("true"))
                        {
                            new SaveImpPrefrences().savePrefrencesData(MainActivity.this,obj.getJSONArray("SubCategoriesList")+"",DefaultConstatnts.getNotificationData);
                        }
                        else
                        {
                            //showtoast.showToast(getActivity(), "Response", obj.getString("Message"), view.findViewById(R.id.ll_recipientlayoutmain));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getNotification() {
        BroadcastReceiver getChatBroadCast;
        getChatBroadCast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getNotificationData();
                    }
                });

            }
        };
        registerReceiver(getChatBroadCast, new IntentFilter(UtilClass.notificationBroadCast));


    }




    //browse image========

    TextView txt_uploadimage;
    private TextView gallery, camera;
    ImageView imageView;
    public void slideUpDown(ImageView imageView,TextView txt_uploadimage) {


        if (imageView != null) {
            this.imageView = imageView;
            this.txt_uploadimage=txt_uploadimage;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage("Choose image form");
        //Yes Button
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                selectImage(0);
            }
        });

        //No Button
        builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                selectImage(1);
            }
        });
        AlertDialog alertDialog = builder.create();
        //error
        alertDialog.show();

    }




    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void selectImage(int actionCode) {
        if (checkAndRequestPermissions() == 0) {
            if (actionCode == 0) {
                dispatchTakePictureIntent();
            } else if (actionCode == 1)
            {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, actionCode);
            }
        }
    }


    private Bitmap bmap;
    private String imagePath="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("Result code==="+requestCode);
        if (requestCode == 101)//Call from recipient fragment
        {
            RecipientFragment recipientFragment = new RecipientFragment();
            Bundle bd = new Bundle();
            bd.putString("symbol", "");
            recipientFragment.setArguments(bd);
            replaceMainFragment(recipientFragment,"recipient");
        }
        else if(requestCode==1001)//call from review activity to change amount  and symbol etc etc
        {
            if(data!=null)
            {
                if (data.getStringExtra("isSendScreenDataLoaded").equalsIgnoreCase("yes"))
                {
                    try {
                        JSONObject dataFromReview=new JSONObject(data.getStringExtra("data"));
                        dataFromReview.put("getAmountFromReviewActivity","yes");
                        sendFromSelectedList(dataFromReview);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if(data.getStringExtra("isSendScreenDataLoaded").equalsIgnoreCase("isTransaction"))
                {
                    MenuItem menu=navigationView.getMenu().getItem(0);
                    menu.setChecked(true);
                    ActivityFragment sendFragment = new ActivityFragment();
                    Bundle args = new Bundle();
                    sendFragment.setArguments(args);
                    replaceMainFragment(sendFragment,"activity");
                }

            }
        }

       else {
            bmap = null;
            Uri selectedImage = null;
            if (resultCode != RESULT_CANCELED) {
                switch (requestCode) {
                    case 0:
//                 if(imageReturnedIntent != null)
                    {
                        try {

                            bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);

                            Uri uri = getImageUri(this, bmap);
                            imagePath = getRealPathFromURI(uri);

                            imageView.setImageBitmap(bmap);




                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    case 1:
                        if (resultCode == RESULT_OK) {
                            if (data != null) {
                                try {
                                    selectedImage = data.getData();
                                     imagePath = getRealPathFromURI(selectedImage);

                                    InputStream image_stream = getContentResolver().openInputStream(selectedImage);
                                    bmap = BitmapFactory.decodeStream(image_stream);
                                    imageView.setImageBitmap(bmap);




                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                }
            }
        }



    }

//    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        System.out.println("inside============" + requestCode + "===" + resultCode);
//
//
//            // new ConvertImage().execute();
//        }
//    }

    static final int REQUEST_TAKE_PHOTO = 0;
    Uri photoURI;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println("inside exception===" + ex.getMessage());

            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "transfer.money.com.xpresssewa.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private int checkAndRequestPermissions() {

        int permissionCAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int readExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (readExternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writeExternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
            return 1;
        }

        return 0;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void uploadImageToServer(String transactionId,Dialog uploadReceipt) {
        BaseActivity.baseurl="https://demo.webcomsystems.net.au/";
        System.out.println("member id===" + UtilClass.member_id);
        File file = new File(imagePath);
        if (file != null) {

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

            RequestBody requestBodyProofType = RequestBody.create(MediaType.parse("text/plain"), "UploadReceipt");
            RequestBody requestBodyMemberId = RequestBody.create(MediaType.parse("text/plain"), UtilClass.member_id);
            RequestBody transactionid = RequestBody.create(MediaType.parse("text/plain"), transactionId);
            RequestBody requestBodyMethod = RequestBody.create(MediaType.parse("text/plain"), "UploadImage");

            UploadticketImage contestService = ApiProduction.getInstance(this).provideService(UploadticketImage.class);
            Observable<String> responseObservable = contestService.uploadReceipt(requestBodyProofType, requestBodyMemberId,transactionid, requestBodyMethod, body);


            final ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.show();

            final Disposable disposable = RxAPICallHelper.call(responseObservable, new RxAPICallback<String>() {
                @Override
                public void onSuccess(String uploadFileResponse) {
                    String imageName = uploadFileResponse.substring(uploadFileResponse.lastIndexOf("!") + 1, uploadFileResponse.length());
                    System.out.println("Inside on sucess=====>" + uploadFileResponse + "===" + imageName);
                    uploadReceipt.dismiss();
                    mProgressDialog.dismiss();


                }

                @Override
                public void onFailed(Throwable throwable)
                {
                    mProgressDialog.dismiss();

                }
            });



        }
        BaseActivity.baseurl="https://demoapi.webcomsystems.net.au/";
    }




}
