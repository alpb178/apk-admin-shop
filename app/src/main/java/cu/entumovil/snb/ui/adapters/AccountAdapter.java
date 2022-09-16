package cu.entumovil.snb.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cu.entumovil.snb.R;

/**
 * Created by laura on 11-Jul-17.
 */

public class AccountAdapter extends ArrayAdapter<AccountModel> implements View.OnClickListener{

    private ArrayList<AccountModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtAccount;
    }

    public AccountAdapter(ArrayList<AccountModel> data, Context context) {
        super(context, R.layout.select_mail_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

     /*   int position=(Integer) v.getTag();
        Object object= getItem(position);
        AccountModel dataModel=(AccountModel)object;
        ((PremiumActivity)this.mContext).selectMail(dataModel.getMail());*/

    }

    private int lastPosition = -1;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        AccountModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.select_mail_item, parent, false);
            viewHolder.txtAccount = (TextView) convertView.findViewById(R.id.email);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtAccount.setText(dataModel.getMail());
        viewHolder.txtAccount.setCompoundDrawablesWithIntrinsicBounds(dataModel.getIcon(),null,null,null);
        // Return the completed view to render on screen
        return convertView;
    }
}
