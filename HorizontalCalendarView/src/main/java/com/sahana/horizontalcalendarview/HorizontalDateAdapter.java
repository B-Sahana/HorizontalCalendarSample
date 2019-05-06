package com.sahana.horizontalcalendarview;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sahana.horizontalcalendarview.model.DateModel;

import java.util.List;

/**
 * Created by SahanaB on 09/09/18.
 */
public class HorizontalDateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements CustomHorizontalCalendar.LayoutClickListener {

    private List<DateModel> mValues;
    protected int mRowIndex = -1;
    private CustomHorizontalCalendar mCustomHorizontalCalendar;
    private OnHorizontalDateSelectListener mOnHorizontalDateSelectListener;

    protected void setOnHorizontalDateSelectListener(OnHorizontalDateSelectListener onHorizontalDateSelectListener) {
        mOnHorizontalDateSelectListener = onHorizontalDateSelectListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_date, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DateViewHolder) {
            try {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                Activity activity = (Activity) holder.itemView.getContext();
                activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int width = displaymetrics.widthPixels;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.width = (int) (width - 69 * Resources.getSystem().getDisplayMetrics().density) / 5;
                holder.itemView.setLayoutParams(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((DateViewHolder) holder).setData(mValues.get(position), position);
            mCustomHorizontalCalendar.setLayoutClickListener(this);
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    protected void setData(List<DateModel> values, CustomHorizontalCalendar customHorizontalCalendar) {
        mValues = values;
        mCustomHorizontalCalendar = customHorizontalCalendar;
        notifyDataSetChanged();
    }

    @Override
    public void onLayoutClick(int position) {
        mRowIndex = position;
        notifyDataSetChanged();
        notifyItemChanged(mRowIndex);
    }

    private class DateViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private TextView dayOfTheWeekTextView;
        private RelativeLayout dateRelativeLayout;


        public DateViewHolder(View itemView) {
            super(itemView);
            dayOfTheWeekTextView = itemView.findViewById(R.id.dayOfTheWeekTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            dateRelativeLayout = itemView.findViewById(R.id.dateRelativeLayout);

        }

        private void setData(final DateModel values, int position) {
            if (dateRelativeLayout == null)
                dateRelativeLayout = itemView.findViewById(R.id.dateRelativeLayout);
            dateTextView.setText(values.day);
            dayOfTheWeekTextView.setText(values.dayOfWeek);
            if (mRowIndex == position) {
                if (dateRelativeLayout != null)
                    dateRelativeLayout.setBackgroundResource(mCustomHorizontalCalendar.mSelectedBgResourceId);
                dateTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), mCustomHorizontalCalendar.mSelectedTextColorResourceId));
                dayOfTheWeekTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), mCustomHorizontalCalendar.mSelectedTextColorResourceId));
                if (mOnHorizontalDateSelectListener != null)
                    mOnHorizontalDateSelectListener.onDateClick(values);
            } else {
                if (dateRelativeLayout != null)
                    dateRelativeLayout.setBackgroundResource(mCustomHorizontalCalendar.mBgResourceId);
                dateTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), mCustomHorizontalCalendar.mTextColorResourceId));
                dayOfTheWeekTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), mCustomHorizontalCalendar.mTextColorResourceId));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLayoutClick(getAdapterPosition());
                    //   if (mOnHorizontalDateSelectListener != null)
                    // mOnHorizontalDateSelectListener.onDateClick(values);
                }
            });
        }
    }
}

