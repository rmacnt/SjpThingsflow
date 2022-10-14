package com.sjp.framework.channel;

import androidx.annotation.NonNull;

public class ChannelProvider {

    private final static String DEFAULT_KEY = "com.taling.android.app.channel";

    private ChannelStore store;

    public ChannelProvider(ChannelStore store) {
        this.store = store;
    }

    public void clear() {
        store.clear();
    }

    @NonNull
    public <T> ChannelLiveData<T> get(Class<T> value) {
        return getInternal(DEFAULT_KEY + ":" + value.getCanonicalName());
    }

    @NonNull
    public <T> ChannelLiveData<T> get(String key) {
        return getInternal(DEFAULT_KEY + ":" + key);
    }


    public boolean delete(String key) {
        String k = DEFAULT_KEY + ":" + key;
        return store.delete(k);
    }

    //======================================================================
    // Private Methods
    //======================================================================

    private <T> ChannelLiveData<T> getInternal(String key) {
        ChannelLiveData<T> old = store.get(key);
        if (old != null) {
            return old;
        } else {
            old = new ChannelLiveData<>();
            store.put(key, old);
        }
        return old;
    }
}
