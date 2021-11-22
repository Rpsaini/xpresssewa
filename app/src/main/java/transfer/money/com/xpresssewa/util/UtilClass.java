package transfer.money.com.xpresssewa.util;

import android.content.Context;

import org.json.JSONObject;

import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;

public class UtilClass
{
    public static String defaultToSymble="";
    public static String DefaultFromSymble="";
    public static String DefaultCountryname="";
    public static String getDefaultSourceImage="";
    public static String getDefaultDestImage="";

    public static String defaultSourceSDCountryId="75";
    public static String defaultSourceCountryId="2";


    public static String defaultDestinationSDCountryId="79";
    public static String defaultDestinationCountryId="4";
    public static  String member_id="";
    public static  String isFingerAuth="";
    public static  String isLockScreen="";
    public static  String CountryCode="";
    public static String notificationBroadCast = "com.notification.broadcast";
    public static  String username,email,contactnumber;

    public static String transferReason="transferReason";
    public static String transferReference="transferReference";



    public static JSONObject getUserData(Context ct)
    {
        try {

            String login_detail = new SaveImpPrefrences().reterivePrefrence(ct, "login_detail").toString();
            if (!login_detail.equalsIgnoreCase("0"))
            {



                JSONObject dataObj=new JSONObject(login_detail);


                DefaultFromSymble= dataObj.getString("SourceSymbol");
                defaultToSymble=dataObj.getString("DestinationSymbol");
                getDefaultDestImage=dataObj.getString("FlagImageDestination");
                getDefaultSourceImage=dataObj.getString("FlagImageSource");
                defaultSourceSDCountryId=dataObj.getString("SDCountryId");
                defaultSourceCountryId=dataObj.getString("CountryId");
                DefaultCountryname=dataObj.getString("CountryName");
                CountryCode=dataObj.getString("CountryCode");

                member_id=dataObj.getString("MemberId");
                contactnumber=dataObj.getString("ContactNumber");
                username=dataObj.getString("UserName");
                email=dataObj.getString("Email");

                dataObj.getString("UserId");
                dataObj.getString("UserImage");
                dataObj.getString("Version");
                dataObj.getString("RefCode");
                dataObj.getString("MemberId");






// {"Message":"success",
//                "UserName":"John Smith","UserId":"tusha10000","DestinationSymbol":"AUD",
//                "SourceSymbol":"INR",
//                "FlagImageDestination":"https:\/\/moneytransfer.mysports365.com\/assets\/images\/flags\/PNGFlags\/au.png",
//                "FlagImageSource":"https:\/\/moneytransfer.mysports365.com\/assets\/images\/flags\/PNGFlags\/in.png",
//                "SDCountryId":80,"CountryId":3,"CountryCode":"IN","CountryName":"India","UserImage":"",
//                "Email":"john@gmail.com","ContactNumber":"998-815-1580","Version":"1","IsPhoneVerified":false,
//                "RefCode":"tusha10000","CreatedDate":"2019-04-16T11:41:47","ReponseCode":1,"MemberId":10000,"status":true}
//




                return  new JSONObject(login_detail);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        return null;
    }


}
