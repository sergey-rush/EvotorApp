package ru.evotorapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by sergey-rush on 22.12.2017.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Slip> slipList;

    public ExpandableListAdapter(Context context, List<Slip> slipList) {
        this.context = context;
        this.slipList = slipList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return slipList.get(groupPosition).Products.get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Product product = (Product) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);

        tvName.setTag(product.SlipId);
        tvName.setText(product.Name);
        tvPrice.setText(Integer.toString(product.Price));

        RelativeLayout rlItem = (RelativeLayout) convertView.findViewById(R.id.rlItem);
        rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tvName = (TextView) view.findViewById(R.id.tvName);
                int slipId = Integer.parseInt(tvName.getTag().toString());
                showSlip(slipId);
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return slipList.get(groupPosition).Products.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return slipList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        return slipList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final Slip slip = (Slip) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String header = String.format("Заявка № %s от %s", Integer.toString(slip.Id), dateFormat.format(slip.Created));
        tvTitle.setText(header);

//      TextView tvCreated = (TextView) convertView.findViewById(R.id.tvCreated);
//      tvCreated.setText(dateFormat.format(slip.Created));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class AppMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private int slipId;
        public AppMenuItemClickListener(int slipId) {
            this.slipId = slipId;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.app_menu_show:
                    showSlip(slipId);
                    return true;
                case R.id.app_menu_delete:
                    onDeleteDialog(slipId);
                    return true;
                default:
            }
            return false;
        }
    }

    private void showSlip(int slipId) {
        SlipFragment fragment = SlipFragment.newInstance(slipId);
        ResponseActivity responseActivity = (ResponseActivity) context;
        responseActivity.showFragment(fragment);
    }

    private void onDeleteDialog(final int slipId) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Удаление заявки")
                .setMessage("Вы действительно желаете удалить эту заявку?")
                .setIcon(R.drawable.ic_question)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DataAccess dataAccess = DataAccess.getInstance(context);
                        dataAccess.removeSlip(slipId);
                        Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }
}


