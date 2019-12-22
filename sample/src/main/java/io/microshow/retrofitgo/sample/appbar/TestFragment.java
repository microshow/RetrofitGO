package io.microshow.retrofitgo.sample.appbar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.microshow.retrofitgo.sample.R;
import io.microshow.retrofitgo.sample.databinding.FragmentTestBinding;

/**
 * Created by Super on 2018/12/24.
 */
public class TestFragment extends Fragment {

    FragmentTestBinding binding;

    private String title;

    public static Fragment newInstance (String title) {
        TestFragment fragment = new TestFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RVAdapter.init(binding.recyclerView);
    }
}
