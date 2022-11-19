package com.example.notemanagerapp.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notemanagerapp.livedata.RefreshLiveData;
import com.example.notemanagerapp.model.Dashboard;
import com.example.notemanagerapp.repository.DashboardRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private DashboardRepository dashboardRepository;
    private RefreshLiveData<List<Dashboard>> dashboardList;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        dashboardRepository = new DashboardRepository();
        dashboardList = dashboardRepository.getDashboard();
    }

    public LiveData<List<Dashboard>> getDashboardList() {
        return dashboardList;
    }

    public void refreshData() {
        dashboardList.refresh();
    }
}