package com.example.nerdlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link nerdLAuncherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class nerdLAuncherFragment extends Fragment {
    private RecyclerView recyclerView;
    private static final String TAG = "NerdLauncherFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public nerdLAuncherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment nerdLAuncherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static nerdLAuncherFragment newInstance() {
        nerdLAuncherFragment fragment = new nerdLAuncherFragment();
      //  Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        setupAdapter();



        return view;
    }

    private void setupAdapter() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activties = pm.queryIntentActivities(intent,0);
        Collections.sort(activties, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(pm).toString(),b.loadLabel(pm).toString());
            }
        });

        Log.i(TAG,"Found "+activties.size()+" activities");

        //set adapter to recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ActivityAdapter(activties));
    }

    private class ActivtyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ResolveInfo resolveInfo;
        private TextView textView;

        public ActivtyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
            itemView.setOnClickListener(this);
        }

        public void bindActivity (ResolveInfo resolveInfo){
            this.resolveInfo = resolveInfo;
            PackageManager pm = getActivity().getPackageManager();
            String app_name = resolveInfo.loadLabel(pm).toString();
            textView.setText(app_name);
        }

        @Override
        public void onClick(View view) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            Intent i = new Intent(Intent.ACTION_MAIN);
             i.setClassName(activityInfo.applicationInfo.packageName,activityInfo.name);
             i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             startActivity(i);

        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivtyViewHolder> {
        List<ResolveInfo> mActivties;

        public ActivityAdapter(List<ResolveInfo> mActivties) {
            this.mActivties = mActivties;
        }


        @NonNull
        @Override
        public ActivtyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_list_item_1,parent,false);

            return new ActivtyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ActivtyViewHolder holder, int position) {
            ResolveInfo resolveInfo = mActivties.get(position);
            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return mActivties.size();
        }
    }
}