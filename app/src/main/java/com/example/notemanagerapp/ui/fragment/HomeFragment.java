package com.example.notemanagerapp.ui.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notemanagerapp.R;
import com.example.notemanagerapp.databinding.FragmentHomeBinding;
import com.example.notemanagerapp.model.Dashboard;
import com.example.notemanagerapp.ui.viewmodel.HomeViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private List<Dashboard> dashboardList = new ArrayList<>();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.fragmentHomePieChart.setDrawHoleEnabled(true);
        binding.fragmentHomePieChart.setUsePercentValues(true);
        binding.fragmentHomePieChart.setEntryLabelTextSize(12);
        binding.fragmentHomePieChart.setEntryLabelColor(Color.BLACK);
        binding.fragmentHomePieChart.setCenterText("The progress of notes");
        binding.fragmentHomePieChart.setCenterTextSize(24);
        binding.fragmentHomePieChart.getDescription().setEnabled(false);

        Legend l = binding.fragmentHomePieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

        initView();

        return binding.getRoot();
    }

    public void initView() {
        viewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        viewModel.getDashboardList().observe(getViewLifecycleOwner(), dashboards -> {

            ArrayList<PieEntry> entries = new ArrayList<>();
            dashboardList = dashboards;
            for (int i = 0; i < dashboardList.size(); i++) {
                String status = dashboardList.get(i).getNameStatus();
                int numberOfStatus = Integer.parseInt(dashboardList.get(i).getNumberOfStatus());
                entries.add(new PieEntry(numberOfStatus, status));
            }

            ArrayList<Integer> colors = new ArrayList<>();
            for (int color : ColorTemplate.MATERIAL_COLORS) {
                colors.add(color);
            }

            for (int color : ColorTemplate.VORDIPLOM_COLORS) {
                colors.add(color);
            }

            PieDataSet dataSet = new PieDataSet(entries, "Note");
            dataSet.setColors(colors);

            PieData data = new PieData(dataSet);
            data.setDrawValues(true);
            data.setValueFormatter(new PercentFormatter(binding.fragmentHomePieChart));
            data.setValueTextSize(12f);
            data.setValueTextColor(Color.BLACK);

            binding.fragmentHomePieChart.setData(data);
            binding.fragmentHomePieChart.invalidate();

            binding.fragmentHomePieChart.animateY(1400, Easing.EaseInOutQuad);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
