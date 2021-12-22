package transfer.money.com.xpresssewa.ticket;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import transfer.money.com.xpresssewa.Adapter.ShowAllticketsImagesAdapter;
import transfer.money.com.xpresssewa.Adapter.TicketReplyListingAdapter;
import transfer.money.com.xpresssewa.BaseActivity;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.ApiProduction;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.interfaces.RxAPICallHelper;
import transfer.money.com.xpresssewa.interfaces.RxAPICallback;
import transfer.money.com.xpresssewa.interfaces.UploadticketImage;
import transfer.money.com.xpresssewa.util.AlertDialogs;
import transfer.money.com.xpresssewa.util.DialogCallBack;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class TicketDetailActivity extends AppCompatActivity {
    private ArrayList<JSONObject> dataAr = new ArrayList<>();
    private RelativeLayout ll_ticketreply;
    private String ticketId = "";
    private String strImagePath="",TicketImageLink="";
    String mainTicketImages="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        actions();
        init();
        replyMsg();

    }

    private void init() {
        try {
            String ticketData = getIntent().getStringExtra("ticketData");
            JSONObject jobj = new JSONObject(ticketData);
            System.out.println("ticket data===" + jobj);

            TextView txt_department = findViewById(R.id.txt_department);
            TextView txt_status = findViewById(R.id.txt_status);
            TextView txt_subject = findViewById(R.id.txt_subject);
            TextView txt_priority = findViewById(R.id.txt_priority);
//            TextView txt_lastdate = findViewById(R.id.txt_lastdate);
            TextView txt_datecreated = findViewById(R.id.txt_datecreated);
            TextView txt_description = findViewById(R.id.txt_description);
            TextView txt_id = findViewById(R.id.txt_id);
            ImageView attachImage = findViewById(R.id.sendImage);

            ll_ticketreply = findViewById(R.id.ll_ticketreply);
            ticketId = jobj.getString("TicketId");

            txt_id.setText(ticketId);
            txt_department.setText(jobj.getString("CategoryName"));

            txt_subject.setText(jobj.getString("TicketTitle"));

            txt_datecreated.setText(jobj.getString("CreatedDate"));
            txt_description.setText(jobj.getString("MainMessage"));

            String status=jobj.getString("Status");
            String priority=jobj.getString("Priority");
            txt_status.setText(status);
            txt_priority.setText(priority);
            getTicketMessages(ticketId);


            if(priority.equalsIgnoreCase("High"))
            {
                txt_priority.setTextColor(getResources().getColor(R.color.status_red));
            }
            else  if(priority.equalsIgnoreCase("Medium"))
            {
                txt_priority.setTextColor(getResources().getColor(R.color.blue_bt_color));
            }
            else  if(priority.equalsIgnoreCase("low"))
            {
                txt_priority.setTextColor(getResources().getColor(R.color.status_green));
            }

            if(status.equalsIgnoreCase("open"))
            {
                txt_status.setTextColor(getResources().getColor(R.color.status_green));
            }
            else if(status.equalsIgnoreCase("close"))
            {
                txt_status.setTextColor(getResources().getColor(R.color.status_red));
            }


            attachImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findViewById(R.id.sendImage).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialogs().alertDialog(TicketDetailActivity.this, "Choose", "Choose Image either from camera or from gallary?", "Camera", "Gallary", new DialogCallBack() {
                                @Override
                                public void getDialogEvent(String buttonPressed) {
                                    if (buttonPressed.equalsIgnoreCase("Camera")) {
                                        selectImage(0);
                                    } else {
                                        selectImage(1);
                                    }
                                }
                            });
                        }
                    });

                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void actions()
    {

//        TextView ticketDetail = findViewById(R.id.headertexttitle);
//        ticketDetail.setText("Ticket Details");

        TextView headertexttitle=findViewById(R.id.headertexttitle);
        headertexttitle.setText("Ticket ID : "+ticketId);
        headertexttitle.setVisibility(View.VISIBLE);

        System.out.println("Hereeeeee=====>");


        findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void getTicketMessages(String ticketId) {
        UtilClass.getUserData(TicketDetailActivity.this);
        Map<String, String> m = new LinkedHashMap<>();
        m.put("TicketId", ticketId);


        new ServerHandler().sendToServer(this, "TicketMessage", m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {

                try {
                    dataAr.clear();

                    JSONObject obj = new JSONObject(dta);
                    System.out.println("TicketMessages back==" + obj);
                    TicketImageLink=obj.getString("TicketImageLink");

                    if (obj.getBoolean("status")) {
                        JSONArray jsonObjectsAr = obj.getJSONArray("TicketReplyList");
                        for (int x = 0; x < jsonObjectsAr.length(); x++)
                        {
                            if(x==0)
                            {
                                showImages(jsonObjectsAr.getJSONObject(0).getString("AttachFile"),(RecyclerView)findViewById(R.id.recycler_view_for_all),"main_ticket");

                            }
                            else {
                                dataAr.add(jsonObjectsAr.getJSONObject(x));
                            }
                        }
                        showTicket(dataAr,"all_tickets");
                    } else {
                        new Showtoast().showToast(TicketDetailActivity.this, "Response", obj.getString("Message"), findViewById(R.id.ll_linearlayoutadditional));
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });
    }


    private void showTicket(ArrayList<JSONObject> dataAr,String type)
    {

        RecyclerView view_all_tickets_recycler = findViewById(R.id.reply_recycler_view);
        TicketReplyListingAdapter transferPurposeAdater = new TicketReplyListingAdapter(dataAr, TicketDetailActivity.this,TicketImageLink,type);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view_all_tickets_recycler.setLayoutManager(horizontalLayoutManagaer);
        view_all_tickets_recycler.setItemAnimator(new DefaultItemAnimator());
        view_all_tickets_recycler.setAdapter(transferPurposeAdater);
        view_all_tickets_recycler.scrollToPosition(dataAr.size() - 1);
    }

    private void replyMsg() {
        final EditText ed_reply = findViewById(R.id.ed_reply);
        RelativeLayout rr_postmessage = findViewById(R.id.rr_postmessage);
        rr_postmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_reply.getText().toString().length() == 0) {
                    new Showtoast().showToast(TicketDetailActivity.this, "Required", "Enter Ticket reply ", ll_ticketreply);
                    hideKeyboard(TicketDetailActivity.this);
                } else {
                    submitTicket(ed_reply.getText().toString(),"",ed_reply);
                }
            }
        });

    }

    private void submitTicket(String msg,String imagename,EditText ed_reply)
    {
        UtilClass.getUserData(TicketDetailActivity.this);


        Map<String, String> m = new LinkedHashMap<>();
        m.put("Description", msg);
        m.put("Image", imagename);
        m.put("TicketId", ticketId);
        m.put("Name", UtilClass.username);
        m.put("MemberId", UtilClass.member_id);


        new ServerHandler().sendToServer(TicketDetailActivity.this, "TicketReply", m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {

                try {

                    dataAr.clear();
                    JSONObject obj = new JSONObject(dta);
                    System.out.println("is saved ==" + obj);
                    if (obj.getBoolean("status")) {
                        getTicketMessages(ticketId);
                    } else {
                        new Showtoast().showToast(TicketDetailActivity.this, "Response", obj.getString("Message"), findViewById(R.id.ll_linearlayoutadditional));
                    }


                    if(ed_reply!=null)
                    {
                        ed_reply.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void selectImage(int actionCode) {
        if (checkAndRequestPermissions() == 0) {
            if (actionCode == 0) {
                dispatchTakePictureIntent();
            } else if (actionCode == 1) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, actionCode);
            }
        }
    }


    private Bitmap bmap;

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        bmap = null;

        Uri selectedImage = null;
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:

                {
                    try {

                        bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);

                        Uri uri = getImageUri(this, bmap);
                        strImagePath = getRealPathFromURI(uri);
                        uploadImageToServer(strImagePath, "file");



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                case 1:
                    if (resultCode == RESULT_OK) {
                        if (imageReturnedIntent != null) {
                            try {
                                selectedImage = imageReturnedIntent.getData();
                                strImagePath = getRealPathFromURI(selectedImage);
                                uploadImageToServer(strImagePath, "file");



                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
            }

            // new ConvertImage().execute();
        }
    }

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

    private void uploadImageToServer(String imagePath, String tag) {
        UtilClass.getUserData(TicketDetailActivity.this);
        BaseActivity.baseurl="https://demo.webcomsystems.net.au/";
        System.out.println("member id===" + UtilClass.member_id);
        File file = new File(imagePath);
        if (file != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData(tag, file.getName(), requestBody);

            RequestBody requestBodyProofType = RequestBody.create(MediaType.parse("text/plain"), "TicketsImages");
            RequestBody requestBodyMemberId = RequestBody.create(MediaType.parse("text/plain"), UtilClass.member_id);
            RequestBody requestBodyMethod = RequestBody.create(MediaType.parse("text/plain"), "UploadImage");

            UploadticketImage contestService = ApiProduction.getInstance(this).provideService(UploadticketImage.class);
            Observable<String> responseObservable = contestService.uploadImage(requestBodyProofType, requestBodyMemberId, requestBodyMethod, body);



            final ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.show();

            final Disposable disposable = RxAPICallHelper.call(responseObservable, new RxAPICallback<String>() {
                @Override
                public void onSuccess(String uploadFileResponse) {

                    String convertedToString = String.valueOf(uploadFileResponse);
                    System.out.println("Insie success=dd===" + convertedToString);
                    String imageName = uploadFileResponse.substring(uploadFileResponse.lastIndexOf("!") + 1, uploadFileResponse.length());
                    submitTicket("",imageName,null);
                    mProgressDialog.dismiss();


                }
                @Override
                public void onFailed(Throwable throwable) {
                    System.out.println("Inside failed=====>" + throwable.getMessage());
                    mProgressDialog.dismiss();

                }
            });


        }

        BaseActivity.baseurl="https://demoapi.webcomsystems.net.au/";
    }

    private void showImages(String AttachFile, RecyclerView recycler_view,String type)
    {
        ShowAllticketsImagesAdapter defaultTransferAdapter = new ShowAllticketsImagesAdapter(AttachFile.split(","), TicketDetailActivity.this,TicketImageLink,type);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(TicketDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view.setLayoutManager(horizontalLayoutManagaer);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(defaultTransferAdapter);
    }




}

