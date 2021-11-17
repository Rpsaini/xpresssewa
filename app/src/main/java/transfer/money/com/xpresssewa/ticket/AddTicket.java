package transfer.money.com.xpresssewa.ticket;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.io.InputStream;
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
import transfer.money.com.xpresssewa.Adapter.AddImageAdapter;
import transfer.money.com.xpresssewa.Adapter.TicketCategoryAdapter;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.ApiProduction;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.interfaces.RxAPICallHelper;
import transfer.money.com.xpresssewa.interfaces.RxAPICallback;
import transfer.money.com.xpresssewa.interfaces.UploadticketImage;
import transfer.money.com.xpresssewa.util.AnimationForView;
import transfer.money.com.xpresssewa.util.IsAnimationEndedCallback;
import transfer.money.com.xpresssewa.util.SimpleDialog;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;


public class AddTicket extends AppCompatActivity {
    AnimationForView animationForViews = new AnimationForView();
    private RelativeLayout chooseimagelayoutouter;
    private TextView gallery, camera;
    ArrayList<String> imageArray = new ArrayList<>();
    private ImageView imageView;
    private int screenHeight, screenWidth;
    public String selected_reason_Id = "";
    public Dialog reasonDialog;
    public View downView;
    private LinearLayout ll_linearlayoutadditional;
    private ArrayList<JSONObject> dataAr = new ArrayList<>();
    private String allImagesName = "";
    private int postion = 0;
    ArrayList<String> imagesNamesArray = new ArrayList();


    SimpleDialog simpleDialog = new SimpleDialog();
    private AddImageAdapter defaultTransferAdapter;
    Dialog dialogConfirm;
    LinearLayout ll_selectTicketCategory;
    TextView txt_showselected_reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);




        getScreenHeight();
        getCategory();
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        imageArray.add("");
        saveData();
        ll_linearlayoutadditional = findViewById(R.id.ll_linearlayoutadditional);
        txt_showselected_reason = findViewById(R.id.txt_showselected_reason);


        chooseimagelayoutouter = findViewById(R.id.chooseimagelayoutouter);
        gallery = findViewById(R.id.gallery);
        camera = findViewById(R.id.camera);
        ll_selectTicketCategory = findViewById(R.id.ll_selectTicketCategory);


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(1);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(0);
            }
        });


        ll_selectTicketCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog();
            }
        });
        for (int x = 0; x < 5; x++) {
            imagesNamesArray.add("");
        }
        init();
    }

    private void init() {

        findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recycler_view_default_payment = findViewById(R.id.add_dynamic_image_recycler);
        defaultTransferAdapter = new AddImageAdapter(imageArray, this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_default_payment.setLayoutManager(horizontalLayoutManagaer);
        recycler_view_default_payment.setItemAnimator(new DefaultItemAnimator());
        recycler_view_default_payment.setAdapter(defaultTransferAdapter);

    }


    public void slideUpDown(ImageView imageView, int postion) {

        if (imageView != null) {
            this.imageView = imageView;
            this.postion = postion;
        }
        if (!isPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
            chooseimagelayoutouter.startAnimation(bottomUp);
            chooseimagelayoutouter.setVisibility(View.VISIBLE);

        } else {

            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);
            chooseimagelayoutouter.startAnimation(bottomDown);
            chooseimagelayoutouter.setVisibility(View.GONE);
        }
    }

    private boolean isPanelShown() {
        return chooseimagelayoutouter.getVisibility() == View.VISIBLE;
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
        System.out.println("inside============" + requestCode + "===" + resultCode);
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
                        String s = getRealPathFromURI(uri);
                        uploadImageToServer(s, "file");

                        imageView.setImageBitmap(bmap);


                        slideUpDown(null, 0);

                        if (imageArray.size() < 5) {
                            imageArray.add("");
                            defaultTransferAdapter.notifyDataSetChanged();
                        }

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
                                String s = getRealPathFromURI(selectedImage);
                                uploadImageToServer(s, "file");
                                InputStream image_stream = getContentResolver().openInputStream(selectedImage);
                                bmap = BitmapFactory.decodeStream(image_stream);
                                imageView.setImageBitmap(bmap);


                                if (imageArray.size() < 5) {
                                    imageArray.add("");
                                    defaultTransferAdapter.notifyDataSetChanged();
                                }
                                slideUpDown(null, 0);

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


    //show category dialog


    private void showCategoryDialog() {
        final SimpleDialog simpleDialog = new SimpleDialog();
        reasonDialog = simpleDialog.simpleDailog(this, R.layout.transfer_purpose_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
        downView = reasonDialog.findViewById(R.id.ll_relativelayout);
        TextView transfer_title = reasonDialog.findViewById(R.id.transfer_title);
        reasonDialog.findViewById(R.id.ll_relativelayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downSourceDestinationView(downView, reasonDialog);
            }
        });

        reasonDialog.findViewById(R.id.img_hideview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downSourceDestinationView(downView, reasonDialog);
            }
        });
        animateUp(screenHeight, downView);


        transfer_title.setText("Select Ticket Category");


        RecyclerView recyclerViewForTransferReason = reasonDialog.findViewById(R.id.recycler_view_for_reason);
        TicketCategoryAdapter transferPurposeAdater = new TicketCategoryAdapter(dataAr, this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewForTransferReason.setLayoutManager(horizontalLayoutManagaer);
        recyclerViewForTransferReason.setItemAnimator(new DefaultItemAnimator());
        recyclerViewForTransferReason.setAdapter(transferPurposeAdater);

    }


    public void animateUp(int height, View sourcedestinationcontainer) {
        animationForViews.handleAnimation(AddTicket.this, sourcedestinationcontainer, 500, height, 00, IsAnimationEndedCallback.translationY, new IsAnimationEndedCallback() {
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
        animationForViews.handleAnimation(this, sourcedestinationcontainer, 500, 00, screenHeight, IsAnimationEndedCallback.translationY, new IsAnimationEndedCallback() {
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


    private void getCategory() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("CategoryId", "0");

        new ServerHandler().sendToServer(this, "GetTicketCategory", m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {

                try {
                    dataAr.clear();

                    JSONObject obj = new JSONObject(dta);
                    System.out.println("TicketData reason==" + obj);
                    if (obj.getBoolean("status")) {
                        JSONArray jsonObjectsAr = obj.getJSONArray("SubCategoriesList");

                        for (int x = 0; x < jsonObjectsAr.length(); x++) {
                            dataAr.add(jsonObjectsAr.getJSONObject(x));
                        }

                    } else {
                        new Showtoast().showToast(AddTicket.this, "Response", obj.getString("Message"), findViewById(R.id.ll_linearlayoutadditional));
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });
    }


    private void saveData() {
        final EditText ed_subject = findViewById(R.id.ed_subject);
        final EditText ed_description = findViewById(R.id.ed_description);

        findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_reason_Id.length() == 0) {
                    new Showtoast().showToast(AddTicket.this, "Required", "Select ticket category", ll_linearlayoutadditional);
                } else if (ed_subject.getText().toString().length() == 0) {
                    new Showtoast().showToast(AddTicket.this, "Required", "Enter Ticket subject", ll_linearlayoutadditional);
                } else if (ed_description.getText().toString().length() == 0) {
                    new Showtoast().showToast(AddTicket.this, "Required", "Enter Ticket message", ll_linearlayoutadditional);
                } else {
                    for (int x = 0; x < imagesNamesArray.size(); x++) {
                        if (imagesNamesArray.get(x).length() > 0) {
                            allImagesName = allImagesName + "," + imagesNamesArray.get(x);
                        }
                    }


                    System.out.println("All images----" + imagesNamesArray.size());

                    UtilClass.getUserData(AddTicket.this);
                    Map<String, String> m = new LinkedHashMap<>();
                    m.put("Description", ed_description.getText() + "");
                    m.put("Title", ed_subject.getText() + "");
                    m.put("Image", allImagesName.replaceFirst(",", ""));
                    m.put("CategoryName", txt_showselected_reason.getText().toString());
                    m.put("CategoryId", selected_reason_Id);
                    m.put("Email", UtilClass.email);
                    m.put("ContactNumber", UtilClass.contactnumber);
                    m.put("Name", UtilClass.username);
                    m.put("MemberId", UtilClass.member_id);

                    System.out.println("before to send===" + m);
                    new ServerHandler().sendToServer(AddTicket.this, "AddTicket", m, 0, 1, new CallBack() {
                        @Override
                        public void getRespone(String dta, ArrayList<Object> respons) {

                            try {
                                System.out.println("is saved ==" + dta);
                                dataAr.clear();

                                JSONObject obj = new JSONObject(dta);

                                if (obj.getBoolean("status")) {
                                    System.out.println("");
                                    new Showtoast().showToast(AddTicket.this, getResources().getString(R.string.app_name), "Your ticket has been submitted successfully.", findViewById(R.id.ll_linearlayoutadditional));

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    },3000);


                                } else {
                                    new Showtoast().showToast(AddTicket.this, "Response", obj.getString("Message"), findViewById(R.id.ll_linearlayoutadditional));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                        }
                    });


                }


            }
        });
    }


    private void uploadImageToServer(String imagePath, String tag) {
        UtilClass.getUserData(AddTicket.this);
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
                    String imageName = uploadFileResponse.substring(uploadFileResponse.lastIndexOf("!") + 1, uploadFileResponse.length());
                    System.out.println("Inside on sucess=====>" + uploadFileResponse + "===" + imageName);
                    imagesNamesArray.set(postion, imageName);
                    mProgressDialog.dismiss();
                }

                @Override
                public void onFailed(Throwable throwable) {
                    System.out.println("Inside failed=====>" + throwable.getMessage());
                    mProgressDialog.dismiss();

                }
            });


        }
    }





}


