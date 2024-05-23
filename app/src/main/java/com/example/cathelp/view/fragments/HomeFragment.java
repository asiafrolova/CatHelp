package com.example.cathelp.view.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.cathelp.R;
import com.example.cathelp.repositories.HomeRepo;
import com.example.cathelp.adapters.HomeAdapter;
import com.example.cathelp.viewModals.HomeViewModel;
import com.example.cathelp.databinding.FragmentHomeBinding;
import com.example.cathelp.model.Event;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class HomeFragment extends Fragment implements HomeAdapter.HomeInterface {
    FragmentHomeBinding binding;
    private HomeAdapter homeAdapter;
    private HomeViewModel homeViewModel;
    private NavController navController;
    private SearchView searchView;
    private String filter = "no";
    private HomeRepo homeRepo = new HomeRepo();

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();


    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeAdapter = new HomeAdapter(this);
        binding.homeRecycleView.setAdapter(homeAdapter);
        binding.scrollViewHome.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(oldScrollY<scrollY){
                    binding.toTopFab.setVisibility(View.VISIBLE);
                }else{

                    binding.toTopFab.setVisibility(View.GONE);
                }
            }
        });
        binding.toTopFab.setOnClickListener(v -> {
            binding.scrollViewHome.setScrollY(0);
            binding.toTopFab.setVisibility(View.GONE);
        });


        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(binding.toolbarSearch);
        setHasOptionsMenu(true);
        binding.filterDog.setOnClickListener(v -> {
            if(filter.equals("dog")){
                filter = "no";
                setPressedFilter();
            }else {
                filter = "dog";
                setPressedFilter();
            }
            Log.d("TAG","filer is"+filter);
            homeRepo.filterEvents(filter);
            dataObserve();
        });
        binding.filterCat.setOnClickListener(v -> {
            if(filter.equals("cat")){
                filter = "no";
                setPressedFilter();
            }else {
                filter = "cat";
                setPressedFilter();
            }
            Log.d("TAG","filer is"+filter);
            homeRepo.filterEvents(filter);
            dataObserve();
        });
        binding.filterFish.setOnClickListener(v -> {
            if(filter.equals("fish")){
                filter = "no";
                setPressedFilter();
            }else {
                filter = "fish";
                setPressedFilter();
            }
            homeRepo.filterEvents(filter);
            dataObserve();
        });

        binding.filterParrot.setOnClickListener(v -> {
            if(filter.equals("parrot")){
                filter = "no";
                setPressedFilter();
            }else {
                filter = "parrot";
                setPressedFilter();
            }
            homeRepo.filterEvents(filter);
            dataObserve();
        });
        binding.filterSalamand.setOnClickListener(v -> {
            if(filter.equals("exotic")){
                filter = "no";
                setPressedFilter();
            }else {
                filter = "exotic";
                setPressedFilter();
            }
            homeRepo.filterEvents(filter);
            dataObserve();
        });




        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.getEvents().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                homeAdapter.submitList(events);
                homeAdapter.notifyDataSetChanged();
            }
        });
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.options_menu, menu);
        //searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));

        searchView.setIconified(true);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                homeRepo.searchEvents(query);
                //searchDatabase(query);
                //homeAdapter.notifyDataSetChanged();
                dataObserve();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                //homeRepo.searchEvents(query);
                //searchDatabase(query);
                //dataObserve();
                if(query.isEmpty() || query.equals(" ")){
                    homeRepo.searchEvents(query);
                    dataObserve();
                }
                return true;
            }

        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                dataObserve();
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);

    }
    private void setPressedFilter(){
        switch (filter){
            case "dog":
                binding.filterDog.setBackgroundResource(R.drawable.view_card_grey);
                binding.filterCat.setBackgroundResource(R.drawable.view_card);
                binding.filterParrot.setBackgroundResource(R.drawable.view_card);
                binding.filterFish.setBackgroundResource(R.drawable.view_card);
                binding.filterSalamand.setBackgroundResource(R.drawable.view_card);
                break;
            case "cat":
                binding.filterDog.setBackgroundResource(R.drawable.view_card);
                binding.filterCat.setBackgroundResource(R.drawable.view_card_grey);
                binding.filterParrot.setBackgroundResource(R.drawable.view_card);
                binding.filterFish.setBackgroundResource(R.drawable.view_card);
                binding.filterSalamand.setBackgroundResource(R.drawable.view_card);
                break;
            case "parrot":
                binding.filterDog.setBackgroundResource(R.drawable.view_card);
                binding.filterCat.setBackgroundResource(R.drawable.view_card);
                binding.filterParrot.setBackgroundResource(R.drawable.view_card_grey);
                binding.filterFish.setBackgroundResource(R.drawable.view_card);
                binding.filterSalamand.setBackgroundResource(R.drawable.view_card);
                break;
            case "fish":
                binding.filterDog.setBackgroundResource(R.drawable.view_card);
                binding.filterCat.setBackgroundResource(R.drawable.view_card);
                binding.filterParrot.setBackgroundResource(R.drawable.view_card);
                binding.filterFish.setBackgroundResource(R.drawable.view_card_grey);
                binding.filterSalamand.setBackgroundResource(R.drawable.view_card);
                break;
            case "exotic":
                binding.filterDog.setBackgroundResource(R.drawable.view_card);
                binding.filterCat.setBackgroundResource(R.drawable.view_card);
                binding.filterParrot.setBackgroundResource(R.drawable.view_card);
                binding.filterFish.setBackgroundResource(R.drawable.view_card);
                binding.filterSalamand.setBackgroundResource(R.drawable.view_card_grey);
                break;
            case "no":
                binding.filterDog.setBackgroundResource(R.drawable.view_card);
                binding.filterCat.setBackgroundResource(R.drawable.view_card);
                binding.filterParrot.setBackgroundResource(R.drawable.view_card);
                binding.filterFish.setBackgroundResource(R.drawable.view_card);
                binding.filterSalamand.setBackgroundResource(R.drawable.view_card);
                break;
        }
    }


    public void dataObserve(){
        binding.homeRecycleView.setAdapter(homeAdapter);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        homeViewModel.getEvents().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                homeAdapter.submitList(events);
                homeAdapter.notifyDataSetChanged();




            }
        });


    }

    @Override
    public void addItem(Event event) {
        boolean isAdded = homeViewModel.addEventToMarks(event);
        homeAdapter.notifyDataSetChanged();


    }

    @Override
    public void onItemClick(Event event) {
        homeViewModel.setEvent(event);
        navController.navigate(R.id.action_homeFragment_to_detailFragment);
    }




    @Override
    public void onStart() {
        super.onStart();
        homeAdapter = new HomeAdapter(this);

        /*binding.homeRecycleView.addItemDecoration(new DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL));
        binding.homeRecycleView.addItemDecoration(new DividerItemDecoration(requireContext(),
                DividerItemDecoration.HORIZONTAL));*/

        dataObserve();
    }
}