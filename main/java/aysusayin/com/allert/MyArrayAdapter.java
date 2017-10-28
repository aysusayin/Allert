package aysusayin.com.allert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aysu on 7.09.2017.
 */

public class MyArrayAdapter extends BaseAdapter {
    private Context contex;
    private ArrayList<Product> arrayList;

    public MyArrayAdapter(Context contex, ArrayList<Product> arrayList) {
        this.contex = contex;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row, null);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.textView, viewHolder.textViewTitle);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Product model = arrayList.get(position);
        viewHolder.textViewTitle.setText("" + model.getName().toString());
        return convertView;
    }

    private class ViewHolder {
        private TextView textViewTitle;

        public ViewHolder(View convertView) {
            textViewTitle = (TextView) convertView.findViewById(R.id.textView);
        }

    }

}
