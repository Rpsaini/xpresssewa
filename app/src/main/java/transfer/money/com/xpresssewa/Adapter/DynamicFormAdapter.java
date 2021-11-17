package transfer.money.com.xpresssewa.Adapter;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.RecipientDynamicActivity;

public class DynamicFormAdapter extends RecyclerView.Adapter<DynamicFormAdapter.MyViewHolder> {
    private RecipientDynamicActivity ira1;
    private JSONArray moviesList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextInputLayout dynamic_id;

        public MyViewHolder(View view) {
            super(view);
            dynamic_id = view.findViewById(R.id.dynamic_id);
            ira1.dynamicFieldAr.add(dynamic_id);


        }
    }

    public DynamicFormAdapter(JSONArray moviesList, RecipientDynamicActivity ira) {
        this.moviesList = moviesList;
        ira1 = ira;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dynamic_form_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

            final JSONObject dataObj = moviesList.getJSONObject(position);
            String type = dataObj.getString("Type");

            EditText editText = holder.dynamic_id.getEditText();
            holder.dynamic_id.setHint(dataObj.getString("PlaceHolder"));

            if (type.equalsIgnoreCase("1"))//Text
            {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (type.equalsIgnoreCase("2"))//multitextbox
            {

                editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            } else if (type.equalsIgnoreCase("3"))//numeric
            {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (type.equalsIgnoreCase("4"))//Letters_Only
            {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.length();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}