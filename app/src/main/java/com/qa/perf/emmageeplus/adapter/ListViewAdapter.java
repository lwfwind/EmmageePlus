package com.qa.perf.emmageeplus.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.qa.perf.emmageeplus.R;
import com.qa.perf.emmageeplus.bean.Process;
import com.qa.perf.emmageeplus.utils.ContextHelper;

import java.util.List;

/**
 * customizing adapter.
 *
 * @author andrewleo
 */
public class ListViewAdapter extends BaseAdapter {
    /**
     * The Checked process.
     */
    public Process checkedProcess;
    /**
     * The Process list.
     */
    List<Process> processList;
    /**
     * The Last checked position.
     */
    int lastCheckedPosition = -1;
    /**
     * The Context helper.
     */
    ContextHelper contextHelper = new ContextHelper();
    /**
     * The Activity.
     */
    Activity activity;
    /**
     * The Checked change listener.
     */
    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                final int checkedPosition = buttonView.getId();
                if (lastCheckedPosition != -1) {
                    RadioButton tempButton = (RadioButton) activity.findViewById(lastCheckedPosition);
                    if ((tempButton != null) && (lastCheckedPosition != checkedPosition)) {
                        tempButton.setChecked(false);
                    }
                }
                checkedProcess = processList.get(checkedPosition);
                lastCheckedPosition = checkedPosition;
            }
        }
    };

    /**
     * Instantiates a new List view adapter.
     *
     * @param activity the activity
     */
    public ListViewAdapter(Activity activity) {
        this.activity = activity;
        processList = contextHelper.getAllPackages(activity.getBaseContext());
    }

    @Override
    public int getCount() {
        return processList.size();
    }

    @Override
    public Object getItem(int position) {
        return processList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Process process = (Process) processList.get(position);
        if (convertView == null)
            convertView = this.activity.getLayoutInflater().inflate(R.layout.list_item, parent, false);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.imgViAppIcon = (ImageView) convertView.findViewById(R.id.image);
            holder.txtAppName = (TextView) convertView.findViewById(R.id.text);
            holder.rdoBtnApp = (RadioButton) convertView.findViewById(R.id.rb);
            holder.rdoBtnApp.setFocusable(false);
            holder.rdoBtnApp.setOnCheckedChangeListener(checkedChangeListener);
        }
        holder.imgViAppIcon.setImageDrawable(process.getIcon());
        holder.txtAppName.setText(process.getAppName());
        holder.rdoBtnApp.setId(position);
        holder.rdoBtnApp.setChecked(checkedProcess != null && getItem(position) == checkedProcess);
        return convertView;
    }

    /**
     * save status of all installed processes
     *
     * @author andrewleo
     */
    class ViewHolder {
        /**
         * The Txt app name.
         */
        TextView txtAppName;
        /**
         * The Img vi app icon.
         */
        ImageView imgViAppIcon;
        /**
         * The Rdo btn app.
         */
        RadioButton rdoBtnApp;
    }
}
