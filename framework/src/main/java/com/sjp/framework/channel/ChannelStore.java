package com.sjp.framework.channel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ChannelStore {

    private final HashMap<String, ChannelLiveData> mMap = new HashMap<>();

    final void put(String key, ChannelLiveData viewModel) {
        ChannelLiveData oldViewModel = mMap.put(key, viewModel);
        if (oldViewModel != null) {
            oldViewModel.onDestroy();
        }
    }

    final boolean delete(String key) {
        if (mMap.containsKey(key) == true) {
            ChannelLiveData oldViewModel = mMap.get(key);
            if (oldViewModel != null) {
                oldViewModel.onDestroy();
            }
            mMap.remove(key);
            return true;
        }
        return false;
    }

    final ChannelLiveData get(String key) {
        return mMap.get(key);
    }

    Set<String> keys() {
        return new HashSet<>(mMap.keySet());
    }

    public final void clear() {
        for (ChannelLiveData vm : mMap.values()) {
            vm.onDestroy();
        }
        mMap.clear();
    }
}