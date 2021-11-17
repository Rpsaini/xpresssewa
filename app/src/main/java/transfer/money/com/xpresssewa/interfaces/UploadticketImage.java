package transfer.money.com.xpresssewa.interfaces;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface UploadticketImage {


    @POST("UploadImage.ashx")
    @Multipart
    Observable<String> uploadImage(
            @Part("foldername") RequestBody ProofType,
            @Part("MemberId") RequestBody MemberId,
            @Part("Method") RequestBody Method,
            @Part MultipartBody.Part image
    );

//    @POST("UploadImage.ashx")
//    @Multipart
//    Observable<String> uploadImageTicketReply(
//            @Part("foldername") RequestBody Image,
//            @Part("MemberId") RequestBody MemberId,
//            @Part("Method") RequestBody Method,
//            @Part MultipartBody.Part image
//    );



}