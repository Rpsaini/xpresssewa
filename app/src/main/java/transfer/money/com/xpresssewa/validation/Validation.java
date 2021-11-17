package transfer.money.com.xpresssewa.validation;

import android.content.Context;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Validation
{
    private Context ct;

    public Validation(Context ct)

    {
        this.ct=ct;
    }

    public boolean checkNullString(EditText txt)
    {
        String txtis=txt.getText()+"";
        System.out.println("hererere null check  "+txtis);
        if(txtis.length()<=0)
        {

            txt.requestFocus();
            Toast.makeText(ct,txt.getTag()+"", Toast.LENGTH_LONG).show();
            return false;
        }
       return true;
    }

    public boolean checkStringLimit(EditText txt)
    {
        String txtis=txt.getText()+"";
        if(txtis.length()<5)
        {
            txt.requestFocus();
            Toast.makeText(ct,txt.getTag()+" or Add More", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }






    public boolean checkNullStringAuto(AutoCompleteTextView txt)
    {
        String txtis=txt.getText()+"";

        if(txtis==null||txtis==""||("").equals(txtis))
        {
            txt.requestFocus();
            Toast.makeText(ct,txt.getTag()+"", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public boolean checkNullStringButton(Button txt)
    {
        String txtis=txt.getText()+"";

        if(txtis==null||txtis==""||("").equals(txtis))
        {
            txt.requestFocus();
            Toast.makeText(ct,txt.getTag()+"", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }








    public boolean CheckSpinner(Spinner txt)
    {

    if(txt.getSelectedItemPosition()<=0)
        {
            txt.requestFocus();
            Toast.makeText(ct,txt.getTag()+"", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    public String replaceString(String txt)
    {

        if(txt.contains("'")||txt.contains("`"))
        {
             txt=txt.replace("'","");
             txt=txt.replace("`","");
            return txt;
        }
      return txt;
    }






    public boolean checkMobile(EditText txt, int maxlength)
    {
     if(checkNullString(txt))
     {
         String val=txt.getText()+"";
         val=val.trim();
         System.out.println("lenth less than=="+(val.length()<maxlength));
         System.out.println("lenth greater than=="+(val.length()));

         if(val.length()<maxlength||val.length()>10)
         {
             txt.requestFocus();
             Toast.makeText(ct,txt.getTag()+"", Toast.LENGTH_LONG).show();
             return false;
         }

     }
        return true;
    }

    public boolean checkpasswordlength(EditText txt, int maxlength)
    {
        if(checkNullString(txt))
        {
            String val=txt.getText()+"";
            val=val.trim();
            if(val.length()<maxlength)
            {
                txt.requestFocus();
                Toast.makeText(ct," Password must contain 8 characters.", Toast.LENGTH_LONG).show();
                return false;
            }

        }
        return true;
    }




    public boolean checkEmail(String txt)
    {
          String val=txt+"";
                   String EMAIL_PATTERN =
                    "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

         Pattern pattern = Pattern.compile(EMAIL_PATTERN);
         Matcher matcher = pattern.matcher(val);
            if(matcher.matches())
            {
             return true;
            }
            else
            {
                //txt.requestFocus();
                //Toast.makeText(ct,txt.getTag()+"",Toast.LENGTH_LONG).show();
                return false;
            }
    }

    public boolean checkDataBaserange(String txt, int dbcollengh, String txtname)
    {
        if(txt.length()>dbcollengh)
        {
            Toast.makeText(ct, "Enter Short "+txtname, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }


public  boolean dateCompare(Button Stdate, Button eData)
   {
try {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    Date start = sdf.parse(Stdate.getText() + "");
    Date end = sdf.parse(eData.getText()+"");
    if(start.before(end))
    {
    return true;
    }
    else
    {
        eData.requestFocus();
        Toast.makeText(ct,"Start date Can not be greater Than End Date", Toast.LENGTH_LONG).show();
        return false;
    }
}
catch (Exception e)
{
    e.printStackTrace();
}

    return  false;
   }


    public boolean validateDateFormat(String dateToValdate)
    {

        System.out.println("dte format====="+dateToValdate);

        if(dateToValdate.length()>0) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date strDate = sdf.parse(dateToValdate);
                if (System.currentTimeMillis() > strDate.getTime()) {

                    return  true;
                }
                else
                {
                    Toast.makeText(ct, "Enter valid date of birth", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(ct, "Enter valid date of birth", Toast.LENGTH_LONG).show();
            return false;

        }

        return false;
    }

    public boolean passwordPolicy(String password)
    {
        String pattern = "(?=.*[0-9])(?=.*[a-z]).{8,}";
        //String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        return password.matches(pattern);

    }


    private boolean isPasswordVisible=false;
    public void passwordHideShow(EditText showpasswordTxt, ImageView hideShowImg)
    {
        if(isPasswordVisible)
        {

            showpasswordTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            //hideShowImg.setImageResource(R.mipmap.eyehide);
            isPasswordVisible=false;
        }
        else
        {
            showpasswordTxt.setInputType(InputType.TYPE_CLASS_TEXT);
            showpasswordTxt.setTransformationMethod(null);
            isPasswordVisible=true;
           // hideShowImg.setImageResource(R.mipmap.eyeview);
        }

    }





}
