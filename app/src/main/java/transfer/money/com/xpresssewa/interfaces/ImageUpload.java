package transfer.money.com.xpresssewa.interfaces;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageUpload {

    @Multipart
    @POST("UploadImage.ashx")
    Observable<String> uploadImage(
                                           @Part("ProofType") RequestBody ProofType,
                                           @Part("MemberId") RequestBody MemberId,
                                           @Part("Method") RequestBody Method,
                                           @Part MultipartBody.Part image,
                                           @Part MultipartBody.Part image1,
                                           @Part MultipartBody.Part image2
                                          );

}
