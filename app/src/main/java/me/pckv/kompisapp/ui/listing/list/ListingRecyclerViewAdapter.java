package me.pckv.kompisapp.ui.listing.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.Repository;
import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.data.model.Location;
import me.pckv.kompisapp.ui.listing.view.ListingActivity;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Listing} and makes a call to the
 * specified.
 */
public class ListingRecyclerViewAdapter extends RecyclerView.Adapter<ListingRecyclerViewAdapter.ViewHolder> implements Filterable {

    private static final int MAXIMUM_DISTANCE = 100;

    private final List<Listing> mValues;
    private final List<Listing> mValuesFull;
    private Location location;

    private Context mContext;
    private Repository repository;

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Listing> filteredList = new ArrayList<>(mValuesFull);

            // If a constraint is given, remove any item that does not match the filter
            if (constraint != null && constraint.length() > 0) {
                String filterPattern = constraint.toString().trim();
                filteredList.removeIf(listing -> !listing.matchesQuery(filterPattern));
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mValues.clear();
            mValues.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public ListingRecyclerViewAdapter(Context context, List<Listing> listings) {
        repository = Repository.getInstance();
        mContext = context;

        hideAndSort(listings);

        mValues = listings;
        mValuesFull = new ArrayList<>(mValues);
    }

    public void updateListings(List<Listing> listings) {
        hideAndSort(listings);

        mValues.clear();
        mValuesFull.clear();
        mValues.addAll(listings);
        mValuesFull.addAll(listings);
        notifyDataSetChanged();
    }

    public void updateLocation(Location location) {
        this.location = location;
        updateListings(new ArrayList<>(mValuesFull));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Listing listing = mValues.get(position);

        holder.mListing = listing;
        holder.mTitleView.setText(listing.getTitle());
        holder.mOwnerNameView.setText(String.format(mContext.getString(R.string.owner_label), listing.getOwner().getDisplayName()));

        if (location != null) {
            holder.mDistanceView.setText(String.format(mContext.getString(R.string.distance_label), location.distanceTo(listing.getLocation())));
        }

        holder.mView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ListingActivity.class);
            intent.putExtra("listingJson", JSON.toJSONString(listing));
            ((Activity) mContext).startActivityForResult(intent, ListingsActivity.REFRESH_LISTINGS_REQUEST);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void hideAndSort(List<Listing> listings) {
        // Hide inactive listings
        listings.removeIf(listing -> (!listing.isActive() && !repository.isOwner(listing)));

        // Sort by distance and remove listings too far away
        if (location != null) {
            Collections.sort(listings, (o1, o2) ->
                    Double.compare(location.distanceTo(o1.getLocation()), location.distanceTo(o2.getLocation())));
            listings.removeIf(listing -> location.distanceTo(listing.getLocation()) > MAXIMUM_DISTANCE);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mOwnerNameView;
        public final TextView mDistanceView;
        public Listing mListing;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = view.findViewById(R.id.title);
            mOwnerNameView = view.findViewById(R.id.owner_name);
            mDistanceView = view.findViewById(R.id.distance);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
