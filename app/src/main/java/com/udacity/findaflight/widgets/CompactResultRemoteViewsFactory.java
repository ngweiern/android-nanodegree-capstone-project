package com.udacity.findaflight.widgets;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.lifecycle.LiveData;

import com.udacity.findaflight.R;
import com.udacity.findaflight.data.CompactResult;
import com.udacity.findaflight.database.AppDatabase;

import java.util.List;

public class CompactResultRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {


    private List<CompactResult> mCompactResults;
    private Context mContext = null;
    private AppDatabase appDatabase;

    public CompactResultRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    // Reference: https://www.reddit.com/r/androiddev/comments/998z2c/help_getting_widget_to_populate_with_livedata_and/
    // Reference: https://github.com/DeFBeD/LifeNotes/blob/master/app/src/main/java/com/example/jburgos/life_notes/widget/WidgetService.java
    // Reference: (not directly referenced, but relevant) https://stackoverflow.com/questions/51973927/android-how-to-access-room-database-from-widget/52792651
    @Override
    public void onCreate() {
        appDatabase = AppDatabase.getInstance(mContext);
    }

    @Override
    public void onDataSetChanged() {
//        final LiveData<List<CompactResult>> compactResults = appDatabase.compactResultDao().loadAllCompactResults();
        mCompactResults = appDatabase.compactResultDao().loadAllCompactResults();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return mCompactResults == null ? 0 : mCompactResults.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCompactResults == null) return null;

        RemoteViews rv = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_compact_result_on_list);
        CompactResult compactResult = mCompactResults.get(position);

        List<String> airlines = compactResult.getAirlines();
        String airlinesString = "";
        for (String airline : airlines) {
            airlinesString += airline + ", ";
        }
        rv.setTextViewText(R.id.airlines, airlinesString.substring(0, airlinesString.length() - 2));
        rv.setTextViewText(R.id.travel_period, compactResult.getTravelPeriod());
        rv.setTextViewText(R.id.price, "$" + Integer.toString(compactResult.getPrice()));
        rv.setTextViewText(R.id.outbound_start_time, compactResult.getOutboundStartTime());
        rv.setTextViewText(R.id.outbound_start_airport, compactResult.getOutboundStartAirport());
        rv.setTextViewText(R.id.outbound_end_time, compactResult.getOutboundEndTime());
        rv.setTextViewText(R.id.outbound_end_airport, compactResult.getOutboundEndAirport());

        if (!compactResult.getInboundEndTime().isEmpty()) {
            rv.setViewVisibility(R.id.fourth_row_relative_layout, View.VISIBLE);
            rv.setTextViewText(R.id.inbound_end_time, compactResult.getInboundEndTime());
            rv.setTextViewText(R.id.inbound_end_airport, compactResult.getInboundEndAirport());
            rv.setTextViewText(R.id.inbound_start_time, compactResult.getInboundStartTime());
            rv.setTextViewText(R.id.inbound_start_airport, compactResult.getInboundStartAirport());
        }


        Bundle extras = new Bundle();
        extras.putParcelable(CompactResultsWidgetProvider.COMPACT_RESULT, (Parcelable) compactResult);

        // Next, set a fill-intent, which will be used to fill in the pending intent template
        // that is set on the collection view in StackWidgetProvider.
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        // Make it possible to distinguish the individual on-click
        // action of a given item
        rv.setOnClickFillInIntent(R.id.cross_button, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}