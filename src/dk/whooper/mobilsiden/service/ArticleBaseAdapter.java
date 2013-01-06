package dk.whooper.mobilsiden.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dk.whooper.mobilsiden.R;
import dk.whooper.mobilsiden.business.Item;
import org.jsoup.Jsoup;

import java.util.List;


public class ArticleBaseAdapter extends BaseAdapter {

    private List<Item> itemArray;
    private Context mContext;
    private LayoutInflater inflator;
    int checkbox;

    /**
     * @param context
     * @param itemArray
     */
    public ArticleBaseAdapter(Context context, List<Item> itemArray) {
        this.mContext = context;
        this.itemArray = itemArray;
        this.inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return itemArray.size();
    }

    @Override
    public String getItem(int position) {
        return itemArray.get(position).getTitle();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final MainListHolder mHolder;
        View v = convertView;
        if (convertView == null) {
            mHolder = new MainListHolder();
            v = inflator.inflate(dk.whooper.mobilsiden.R.layout.article_row_view, null);
            mHolder.txt1 = (TextView) v.findViewById(dk.whooper.mobilsiden.R.id.title1);
            mHolder.txt2 = (TextView) v.findViewById(dk.whooper.mobilsiden.R.id.description);
            mHolder.txt3 = (TextView) v.findViewById(R.id.date);
            v.setTag(mHolder);
        } else {
            mHolder = (MainListHolder) v.getTag();
        }
        mHolder.txt1.setText(itemArray.get(position).getTitle());
        mHolder.txt2.setText(Jsoup.parse(itemArray.get(position).getDescription()).text());

        String[] dateSplitted = itemArray.get(position).getPubDate().split(" ");
        String date = dateSplitted[0] + " " + dateSplitted[1] + " " + dateSplitted[2] + " " + dateSplitted[3];
        mHolder.txt3.setText(date);

        /*mHolder.txt.setTextSize(12);
        mHolder.txt.setTextColor(Color.YELLOW);
        mHolder.txt.setPadding(5, 5, 5, 5);*/
        //mHolder.image.setImageResource(R.drawable.icon);


        return v;
    }

    class MainListHolder {
        private TextView txt1;
        private TextView txt2;
        private TextView txt3;
    }


}
