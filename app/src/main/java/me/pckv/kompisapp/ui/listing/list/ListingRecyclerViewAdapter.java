package me.pckv.kompisapp.ui.listing.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.model.Listing;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Listing} and makes a call to the
 * specified.
 * TODO: Replace the implementation with code for your data type.
 */
public class ListingRecyclerViewAdapter extends RecyclerView.Adapter<ListingRecyclerViewAdapter.ViewHolder> {

    private final List<Listing> mValues;

    public ListingRecyclerViewAdapter(List<Listing> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Listing listing = mValues.get(position);

        holder.mListing = listing;
        holder.mTitleView.setText(listing.getTitle());
        holder.mOwnerNameView.setText(listing.getOwner().getDisplayName());
        holder.mDistanceView.setText("5km");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: view details
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
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
