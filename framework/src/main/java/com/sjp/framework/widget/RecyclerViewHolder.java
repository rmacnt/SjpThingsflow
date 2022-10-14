package com.sjp.framework.widget;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerViewHolder<Item> extends RecyclerView.ViewHolder {

    //======================================================================
    // Variables
    //======================================================================

    private final View mItemView;

    private Item mItem;

    private boolean mItemChange;

    private boolean mAutoImageRelease = true;

    Bundle mArguments;

    private boolean mFirstLoad = true;

    private boolean mPayloadHolderChange = false;

    private boolean mBindArgumentsCall = false;

    //======================================================================
    // Constructor
    //======================================================================

    public RecyclerViewHolder(@NonNull ViewGroup viewGroup, @LayoutRes int itemVieId) {
        this(LayoutInflater.from(viewGroup.getContext()).inflate(itemVieId, viewGroup, false));
    }

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        mItemView = itemView;
        onCreateView(mItemView);
    }

    //======================================================================
    // Abstract Methods
    //======================================================================

    public abstract void onRefresh(Item item);

    //======================================================================
    // Methods
    //======================================================================

    public void onBindingLifeCycleScope(@Nullable LifecycleOwner owner) {

    }

    public boolean isPayloadHolderChange() {
        return mPayloadHolderChange;
    }

    public void setPayloadHolderChange(boolean mPayloadHolderChange) {
        this.mPayloadHolderChange = mPayloadHolderChange;
    }

    public void onBindArguments(@NonNull Bundle arguments) {
        mBindArgumentsCall = true;
    }

    void onPreBindViewHolder() {
        onBindArguments(getArgumentsInternal());
    }

    public void onBindViewHolder(Item item) {
        if (mFirstLoad) {
            mFirstLoad = false;
        }
        if (mBindArgumentsCall == false) {
            onBindArguments(getArgumentsInternal());
        }
        if (mItem == null || mItem.equals(item) == false) {
            mItem = item;
            mItemChange = true;
        } else {
            mItemChange = false;
        }

        onBind();
        onRefresh(item);
    }

    final public void onViewRecycled() {
        release();
        mItem = null;
        mFirstLoad = true;
        mPayloadHolderChange = false;
        mBindArgumentsCall = false;
    }

    final void onCreateViewHolder(int viewType) {

    }

    //======================================================================
    // Protected Methods
    //======================================================================

    protected void onCreateView(@NonNull View view) {

    }

    protected void onBind() {
        // Override
    }


    //======================================================================
    // Public Methods
    //======================================================================

    public static View buildItemView(RecyclerView recyclerView, int itemViewId) {
        return LayoutInflater.from(recyclerView.getContext()).inflate(itemViewId, recyclerView, false);
    }

    public final Item getItem() {
        return mItem;
    }

    public boolean isItemChange() {
        return mItemChange;
    }

    public boolean isAutoImageRelease() {
        return mAutoImageRelease;
    }

    public void setAutoImageRelease(boolean autoImageRelease) {
        mAutoImageRelease = autoImageRelease;
    }

    public final Resources getResources() {
        return getContext().getResources();
    }

    public final Context getContext() {
        return mItemView.getContext();
    }

    public final View getItemView() {
        return mItemView;
    }

    public final View findViewById(int id) {
        return mItemView.findViewById(id);
    }

    @NonNull
    @Deprecated
    public final Bundle getArguments() {
        return getArgumentsInternal();
    }

    /**
     * 리소스 해제
     */
    public void release() {
        release(mItemView);
        mBindArgumentsCall = false;
    }

    //======================================================================
    // Private Methods
    //======================================================================

    @NonNull
    public Bundle getArgumentsInternal() {
        if (mArguments == null) {
            mArguments = new Bundle();
        }
        return mArguments;
    }

    private void release(View rootView) {
        if (rootView == null) {
            return;
        }

        if (rootView instanceof ViewGroup) {
            ViewGroup groupView = (ViewGroup) rootView;

            int childCount = groupView.getChildCount();
            for (int index = 0; index < childCount; index++) {
                release(groupView.getChildAt(index));
            }
        }
    }
}
