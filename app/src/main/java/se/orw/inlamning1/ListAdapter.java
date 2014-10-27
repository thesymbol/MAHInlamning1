package se.orw.inlamning1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Marcus Orwén on 2014-09-18.
 *
 * A custom adapter class for the income/outcome list.
 */
public class ListAdapter extends ArrayAdapter<AdapterData> {
    private LayoutInflater inflater;
    private ArrayList<AdapterData> adapterDatas;

    /**
     * Constructor that initializes the adapter's variables
     *
     * @param context The context to be used
     * @param objects The ArrayList of objects
     */
    public ListAdapter(Context context, ArrayList<AdapterData> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.adapterDatas = objects;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Get the custom view for the list.
     *
     * @param position The position from the list.
     * @param convertView -
     * @param parent -
     * @return View
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null) {
            view = inflater.inflate(R.layout.layout_adapter_data, parent, false);
        } else {
            view = convertView;
        }
        TextView tvText = (TextView)view.findViewById(R.id.tvText);
        tvText.setText(adapterDatas.get(position).getText());

        ImageView ivImage = (ImageView)view.findViewById(R.id.ivImage);
        ivImage.setBackground(view.getResources().getDrawable(adapterDatas.get(position).getImageID()));
        return view;
    }
}
