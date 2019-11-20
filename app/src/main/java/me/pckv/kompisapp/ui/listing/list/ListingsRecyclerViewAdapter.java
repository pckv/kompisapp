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
public class ListingsRecyclerViewAdapter extends RecyclerView.Adapter<ListingsRecyclerViewAdapter.ViewHolder> implements Filterable {

    private final List<Listing> listings;
    private final List<Listing> listingsFull;
    private Location location;

    private Context context;
    private Repository repository;

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Listing> filteredList = new ArrayList<>(listingsFull);

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
            listings.clear();
            listings.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public ListingsRecyclerViewAdapter(Context context) {
        repository = Repository.getInstance();
        this.context = context;
        listings = new ArrayList<>();
        listingsFull = new ArrayList<>();
    }

    public void setListings(List<Listing> listings) {
        this.listings.clear();
        this.listings.addAll(listings);
        listingsFull.clear();
        listingsFull.addAll(listings);
        notifyDataSetChanged();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Listing listing = listings.get(position);

        holder.mListing = listing;
        holder.mTitleView.setText(listing.getTitle());
        holder.mOwnerNameView.setText(String.format(context.getString(R.string.owner_label), listing.getOwner().getDisplayName()));
        holder.mDistanceView.setText(String.format(context.getString(R.string.distance_label), listing.getDistance()));

        holder.mView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListingActivity.class);
            intent.putExtra("listingJson", JSON.toJSONString(listing));
            intent.putExtra("locationJson", JSON.toJSONString(location));
            ((Activity) context).startActivityForResult(intent, ListingsActivity.REFRESH_LISTINGS_REQUEST);
        });
    }

    @Override
    public int getItemCount() {
        return listings.size();
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
