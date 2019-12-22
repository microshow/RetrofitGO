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
import io.microshow.retrofitgo.sample.databinding.FragmentTest2Binding;

/**
 * Created by Super on 2018/12/24.
 */
public class TestFragment2 extends Fragment {

    FragmentTest2Binding binding;

    private String title;

    public static Fragment newInstance (String title) {
        TestFragment2 fragment = new TestFragment2();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test2,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.titleText.setText(title);
    }
}
