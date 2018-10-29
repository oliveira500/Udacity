package br.org.sidia.baking.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import br.org.sidia.baking.R;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListWidgetItem(getApplicationContext());
    }

    public class ListWidgetItem implements RemoteViewsFactory {

        private Context mContext;
        private Cursor mCursor;

        public ListWidgetItem(Context context){
            this.mContext = context;
            this.mCursor = null;
        }

        @Override
        public void onCreate() {
            this.mCursor = mContext.getContentResolver().query(WidgetContract.IngredientEntry.CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onDataSetChanged() {
            final long token = Binder.clearCallingIdentity();
            try {
                if(this.mCursor != null) {
                    this.mCursor.close();
                }
                this.mCursor = mContext.getContentResolver().query(WidgetContract.IngredientEntry.CONTENT_URI, null, null, null, null);
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }

        @Override
        public void onDestroy() {
            if (this.mCursor != null){
                this.mCursor.close();
                this.mCursor = null;
            }
        }

        @Override
        public int getCount() {
            if (mCursor != null)
                return mCursor.getCount();
            else
                return 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.row_listview_widget);

            if (mCursor != null && getCount() > 0) {
                int indexIngredient = mCursor.getColumnIndex(WidgetContract.IngredientEntry.INGREDIENT_NAME);
                mCursor.moveToPosition(position);

                String ingredient = mCursor.getString(indexIngredient);
                remoteViews.setTextViewText(R.id.txt_ingredient_widget, ingredient);
            }else{
                remoteViews.setTextViewText(R.id.txt_ingredient_widget, "No data");
            }

            return remoteViews;
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
            return false;
        }
    }

}
