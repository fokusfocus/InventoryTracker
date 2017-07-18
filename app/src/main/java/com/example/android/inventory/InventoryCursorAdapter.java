package com.example.android.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.inventory.data.InventoryContract;
import com.example.android.inventory.data.InventoryContract.InvEntry;


public class InventoryCursorAdapter extends CursorAdapter {

    /** EditText field to enter the inventory quantity */
    //private EditText mQuantityText;

    private Uri mCurrentInvUri;


    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        //final int productId = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry._ID));

        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        final Button saleButton = (Button) view.findViewById(R.id.sale);

        // Find the columns of inventory attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(InvEntry.COLUMN_INV_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(InvEntry.COLUMN_INV_QTY);
        int priceColumnIndex = cursor.getColumnIndex(InvEntry.COLUMN_INV_PRICE);
        int idColumnIndex = cursor.getColumnIndex(InvEntry._ID);

        // Read the inventory attributes from the Cursor for the current inventory
        String invName = cursor.getString(nameColumnIndex);
        final String invQty = cursor.getString(quantityColumnIndex);
        String invPrice = cursor.getString(priceColumnIndex);
        final int invId = cursor.getInt(idColumnIndex);

        // Update the TextViews with the attributes for the current inventory
        nameTextView.setText(invName);
        quantityTextView.setText(invQty);
        priceTextView.setText(invPrice);

                //do stuff to decrease quantity by 1
                saleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Uri currentProductUri = ContentUris.withAppendedId(InvEntry.CONTENT_URI, invId);

                        //grab current quantity and decrease by 1
                        String quantityString = invQty.trim();
                        int quantity = Integer.parseInt(quantityString);

                        //prevent quantity to go to negative numbers
                        if (quantity > 0) {
                            quantity = quantity - 1;
                        } else quantity = 0;

                        // Create a ContentValues object where column names are the keys,
                        ContentValues values = new ContentValues();
                        values.put(InventoryContract.InvEntry.COLUMN_INV_QTY, quantity);
                        context.getContentResolver().update(currentProductUri, values, null, null);

                    }
                });

    }


}
