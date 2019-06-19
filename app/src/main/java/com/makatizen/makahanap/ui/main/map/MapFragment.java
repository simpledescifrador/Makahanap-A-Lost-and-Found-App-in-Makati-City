package com.makatizen.makahanap.ui.main.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseFragment;

public class MapFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        getActivityFragmentComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        return view;
    }

    @Override
    protected void init() {

    }
}
