package com.websarva.wings.android.kaimono2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//floatbutton
public class ExpandableAdapter extends RecyclerView.Adapter<ExpandableAdapter.ExpandableViewHolder> {
    private List<String> datalist;
    private Context context;
    private boolean[] expandedState;
    DatabaseHelper _dbhelper;
    private String ymd;


    public ExpandableAdapter(List<String> datalist,String ymd, Context context){
        //引数のデータをフィールドに格納
        this.datalist = datalist;
        this.context = context;
        this.expandedState = new boolean[datalist.size()];
        this.ymd = ymd;
        this._dbhelper = new DatabaseHelper(context);
    }

    @Override
    public ExpandableViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        //レイアウトインフレ―ターを取得
        LayoutInflater inflater = LayoutInflater.from(context);
        //画面部品
        View itemView = inflater.inflate(R.layout.kaulist,parent,false);
        return new ExpandableViewHolder(itemView);
    }

    //表示データの割り当て
    @Override
    public void onBindViewHolder(final ExpandableViewHolder holder,int position){
        String title = datalist.get(position);
        holder.titletextview.setText(title);
        //開閉に関する操作
        boolean isExpanded = expandedState[position];
        holder.excontent.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.exIcon.setImageResource(isExpanded ? R.drawable.ic_launcher_foreground : R.drawable.ic_launcher_foreground);

        List<Map<String,String>> Lists = new ArrayList<>();

        SQLiteDatabase db = _dbhelper.getReadableDatabase();
        String InListItem = "SELECT buyingname, buyingprice FROM kaimono WHERE shopname = ? AND buyingymd = ?";

        Cursor cursor = db.rawQuery(InListItem, new String[]{title, ymd});

        if (cursor.moveToFirst()){
            do{
                String A = cursor.getString(cursor.getColumnIndexOrThrow("buyingname"));
                String B = cursor.getString(cursor.getColumnIndexOrThrow("buyingprice"));

                Map<String, String> item = new HashMap<>();
                item.put("name", A);
                item.put("price", B);
                Lists.add(item);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        if (holder.listview != null) {
            String[] from = {"name","price"};
            int[] to = {android.R.id.text1,android.R.id.text2};

            SimpleAdapter adapter = new SimpleAdapter(context, Lists, android.R.layout.simple_list_item_2, from, to);
            holder.listview.setAdapter(adapter);
        }
        //開閉の状態
        holder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterposition = holder.getAdapterPosition();
                if (adapterposition != RecyclerView.NO_POSITION){
                    boolean isExpanded = expandedState[adapterposition];

                    expandedState[adapterposition] = ! isExpanded;

                    notifyItemChanged(adapterposition);
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        //件数をリターン
        return datalist.size();
    }

    public class ExpandableViewHolder extends RecyclerView.ViewHolder {

        public TextView titletextview;
        public ImageView exIcon;
        public LinearLayout excontent;
        public LinearLayout header;
        public ListView listview;

        public ExpandableViewHolder(View itemView){
            super(itemView);

            titletextview = itemView.findViewById(R.id.titles);
            exIcon = itemView.findViewById(R.id.expand_icon);
            excontent =  itemView.findViewById(R.id.expandable_content);
            header = itemView.findViewById(R.id.header);
            listview = itemView.findViewById(R.id.list_of_buying);
        }
    }
}
