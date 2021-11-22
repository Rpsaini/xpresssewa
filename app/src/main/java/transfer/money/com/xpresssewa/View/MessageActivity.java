package transfer.money.com.xpresssewa.View;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.os.Environment;
import android.provider.MediaStore;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.interfaces.ApiProduction;
import transfer.money.com.xpresssewa.interfaces.ImageUpload;
import transfer.money.com.xpresssewa.interfaces.RxAPICallHelper;
import transfer.money.com.xpresssewa.interfaces.RxAPICallback;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.util.SimpleDialog;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class MessageActivity extends AppCompatActivity {
    private ImageView iv_screenshot, iv_screenshot_back,iv_screenshot_proof;
    private RelativeLayout chooseimagelayoutouter;
    private TextView gallery, camera;

    //private String imageFrontImageCamera = "", imageFrontImageGallery = "", imageBackImageCamera = "", imageBackImageHgallery = "",imageProofpath="";
    private int imgSelectionType = 0;
    private TextView tv_send;
    int imageCount = 0;

    SimpleDialog simpleDialog = new SimpleDialog();
    Dialog dialogConfirm;

    private String  imagePathfront="" ,imagePathBack="" ,imagePathAddress="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        UtilClass.getUserData(this);
        init();
       }

    protected void init() {
        final String Title = getIntent().getStringExtra("Title");
        TextView front_id_message = findViewById(R.id.front_id_message);
        TextView backidentitymessage = findViewById(R.id.backidentitymessage);
        TextView addressidentitymessage = findViewById(R.id.addressidentitymessage);
        front_id_message.setText("Upload the front side of your " + Title);
        backidentitymessage.setText("Upload the back side of your " + Title);
        backidentitymessage.setText("Upload Address proof Image");

        iv_screenshot = findViewById(R.id.iv_screenshot_front);
        iv_screenshot_back = findViewById(R.id.iv_screenshot_back);
        iv_screenshot_proof = findViewById(R.id.iv_screenshot_proof);
        tv_send = findViewById(R.id.tv_send);
        chooseimagelayoutouter = findViewById(R.id.chooseimagelayoutouter);
        gallery = findViewById(R.id.gallery);
        camera = findViewById(R.id.camera);

        iv_screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSelectionType = 0;
                slideUpDown();
            }
        });

        iv_screenshot_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSelectionType = 1;
                slideUpDown();
            }
        });

        iv_screenshot_proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSelectionType = 2;
                slideUpDown();
            }
        });
        findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imagePathfront.length() == 0) {
                    new Showtoast().showToast(MessageActivity.this, "Required", "Upload front of " + Title, chooseimagelayoutouter);
                } else if (imagePathBack.length() == 0) {
                    new Showtoast().showToast(MessageActivity.this, "Required", "Upload back of " + Title, chooseimagelayoutouter);
                } else if (imagePathAddress.length() == 0) {
                    new Showtoast().showToast(MessageActivity.this, "Required", "Upload address proof image.", chooseimagelayoutouter);
                } else {
                    uploadImageToServer();
                }
            }
        });
        chooseimagelayoutouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUpDown();
            }
        });

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


    }

    public void slideUpDown() {

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
    private void selectImage(int actionCode)
    {
        if (checkAndRequestPermissions() == 0)
        {
            if(actionCode == 0) {
                dispatchTakePictureIntent();
            } else if (actionCode == 1) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                         //you will get 15 cards
                        //15 jo bhi minimum
                        bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);

                        Uri uri = getImageUri(this, bmap);
                        String s = getRealPathFromURI(uri);
                        System.out.println("Selected camera image====" + s);
                        if(imgSelectionType == 0) {
                            imagePathfront = s;
                            iv_screenshot.setImageBitmap(bmap);
                        }
                        else if(imgSelectionType==1)
                        {
                            iv_screenshot_back.setImageBitmap(bmap);
                            imagePathBack = s;
                        }
                        else
                        {

                            iv_screenshot_proof.setImageBitmap(bmap);
                            imagePathAddress = s;
                        }


                        slideUpDown();

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
                                //  uploadImageToServer(s,"file");
                                InputStream image_stream = getContentResolver().openInputStream(selectedImage);
                                bmap = BitmapFactory.decodeStream(image_stream);
                                if(imgSelectionType == 0) {
                                    imagePathfront = s;
                                    iv_screenshot.setImageBitmap(bmap);
                                }
                                else if(imgSelectionType==1)
                                {
                                    iv_screenshot_back.setImageBitmap(bmap);
                                    imagePathBack = s;
                                }
                                else
                                {

                                    iv_screenshot_proof.setImageBitmap(bmap);
                                    imagePathAddress = s;
                                }

                                slideUpDown();

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

        int permissionCAMERA = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int readExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
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

    private void uploadImageToServer() {

        System.out.println("member id===" + UtilClass.member_id);
        File file = new File(imagePathfront);
        File file2 = new File(imagePathBack);
        File file3 = new File(imagePathAddress);
        if (file != null)
        {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);//front
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

            RequestBody requestBody2 = RequestBody.create(MediaType.parse("image/*"), file2);//back image
            MultipartBody.Part body2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestBody2);



            RequestBody requestBody3 = RequestBody.create(MediaType.parse("image/*"), file3);//address proff
            MultipartBody.Part body3 = MultipartBody.Part.createFormData("file3", file3.getName(), requestBody3);

            RequestBody requestBodyProofType = RequestBody.create(MediaType.parse("text/plain"), "1");
            RequestBody requestBodyMemberId = RequestBody.create(MediaType.parse("text/plain"), UtilClass.member_id);
            RequestBody requestBodyMethod = RequestBody.create(MediaType.parse("text/plain"), "ProofImage");

            ImageUpload contestService = ApiProduction.getInstance(this).provideService(ImageUpload.class);
            Observable<String> responseObservable = contestService.uploadImage(requestBodyProofType, requestBodyMemberId, requestBodyMethod, body,body2,body3);


            final ProgressDialog mProgressDialog = new ProgressDialog(this);
             mProgressDialog.show();

            Disposable disposable = RxAPICallHelper.call(responseObservable, new RxAPICallback<String>() {
                @Override
                public void onSuccess(String uploadFileResponse) {

                    System.out.println("Inside on sucess=====>" + uploadFileResponse);
                    mProgressDialog.dismiss();
                    try {

                        imageCount++;
                        mProgressDialog.dismiss();

                        //if(imageCount == 2)
                        {
                            DefaultConstatnts.isKyDockUploaded = true;
                            new SaveImpPrefrences().savePrefrencesData(MessageActivity.this,"1",DefaultConstatnts.IsKycApproved);

                            if (dialogConfirm != null && dialogConfirm.isShowing()) {
                                dialogConfirm.dismiss();
                            }
                            simpleDialog = new SimpleDialog();
                            dialogConfirm = simpleDialog.simpleDailog(MessageActivity.this, R.layout.successdialog, new ColorDrawable(android.graphics.Color.TRANSPARENT), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
                            dialogConfirm.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {

                                    Intent intent = new Intent();
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                }
                            });
                            dialogConfirm.findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(Throwable throwable) {

                }
            });



        }



    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }




}
