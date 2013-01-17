package dk.whooper.mobilsiden.service;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dk.whooper.mobilsiden.R;
import dk.whooper.mobilsiden.business.Article;

import java.util.List;


public class ArticleBaseAdapter extends BaseAdapter {

    private List<Article> articleList;
    private Context mContext;
    private LayoutInflater inflator;
    int checkbox;

    /**
     * @param context
     * @param itemArray
     */
    public ArticleBaseAdapter(Context context, List<Article> itemArray) {
        this.mContext = context;
        this.articleList = itemArray;
        this.inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return articleList.size();
    }

    @Override
    public Article getItem(int position) {
        return articleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setItemArray(List<Article> itemArray) {
        this.articleList = itemArray;
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

        if (articleList.get(position).isUnread()) {
            mHolder.txt1.setTypeface(null, Typeface.BOLD);
        } else {
            mHolder.txt1.setTypeface(null, Typeface.NORMAL);
        }
        mHolder.txt1.setText(articleList.get(position).getHeader());
        mHolder.txt2.setText("");

        String[] dateSplitted = articleList.get(position).getPublished().split("T");
        String date = dateSplitted[0];
        mHolder.txt3.setText(date);

        return v;
    }

    class MainListHolder {
        private TextView txt1;
        private TextView txt2;
        private TextView txt3;
    }


}
