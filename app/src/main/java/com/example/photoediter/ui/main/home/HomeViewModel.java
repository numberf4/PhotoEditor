package com.example.photoediter.ui.main.home;

import com.example.photoediter.repository.PhotoRepository;
import com.example.photoediter.ui.base.BaseViewModel;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends BaseViewModel {
    PhotoRepository photoRepository;
    @Inject
    public HomeViewModel(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }


}
