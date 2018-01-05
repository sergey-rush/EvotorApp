package ru.evotorapp;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * Created by sergey-rush on 22.12.2017.
 */
public class SlipListFragment extends Fragment {

    public SlipListFragment() {}

    private Context context;
    private ExpandableListAdapter expandableListAdapter;
    private ExpandableListView lvSlips;
    private List<Slip> slipList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_slip_list, container, false);

        ResponseActivity responseActivity = (ResponseActivity) getActivity();
        responseActivity.setHeader(R.string.slip_list_fragment_title);

        context = view.getContext();

        DataAccess dataAccess = DataAccess.getInstance(context);

        slipList = dataAccess.getSlips(100);

        lvSlips = (ExpandableListView) view.findViewById(R.id.lvSlips);
        expandableListAdapter = new ExpandableListAdapter(context, slipList);
        lvSlips.setAdapter(expandableListAdapter);

        // Listview Group click listener
        lvSlips.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //Toast.makeText(context,"Group Clicked " + groupPosition, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        lvSlips.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(context, groupPosition + " Expanded", Toast.LENGTH_SHORT).show();
            }
        });

        lvSlips.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(context, groupPosition + " Collapsed", Toast.LENGTH_SHORT).show();

            }
        });

        lvSlips.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Toast.makeText(context, groupPosition + " : " + childPosition + " Clicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return view;
    }
}