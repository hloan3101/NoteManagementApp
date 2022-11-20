package com.example.notemanagerapp.repository;



import com.example.notemanagerapp.api.API;
import com.example.notemanagerapp.constants.Constants;
import com.example.notemanagerapp.livedata.RefreshLiveData;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.model.Dashboard;
import com.example.notemanagerapp.service.DashboardService;
import com.example.notemanagerapp.sharedpreferences.DataLocalManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardRepository {

    private DashboardService dashboardService;

    public DashboardRepository() {
        this.dashboardService = API.getAccount().create(DashboardService.class);
    }

    public RefreshLiveData<List<Dashboard>> getDashboard() {
        final RefreshLiveData<List<Dashboard>> liveData = new RefreshLiveData<>(callback -> {
            dashboardService.getDashboard(Constants.TAB_DASHBOARD, DataLocalManager.getEmail())
                    .enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            List<Dashboard> dashboards = new ArrayList<>();
                            for (List<String> dashboard : response.body().getData()) {
                                dashboards.add(new Dashboard(dashboard.get(0), dashboard.get(1)));
                            }

                            callback.onDataLoaded(dashboards);
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                        }
                    });
        });

        return liveData;
    }
}
