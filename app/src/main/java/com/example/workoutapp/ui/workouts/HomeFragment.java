package com.example.workoutapp.ui.workouts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.workoutapp.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;
    private NavController navController = null;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TabItem easyTab, mediumTab, hardTab;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.tabLayout);
        easyTab = view.findViewById(R.id.easyTab);
        mediumTab = view.findViewById(R.id.mediumTab);
        hardTab = view.findViewById(R.id.hardTab);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(this));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { //Changes tab focus and fragment when clicking tabs
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { //Overridden in case we would like to use this for something.

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { //Overridden in case we would like to use this for something.

            }
        });

//        viewPager.setPageTransformer(new ViewPager2.PageTransformer() { //Adds a Depth transformation between fragments
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                final float MIN_SCALE = 0.75f;
//                int pageWidth = page.getWidth();
//
//                if ( position < -1 ) { // [ -Infinity,-1 )
//                    // This page is way off-screen to the left.
//                    page.setAlpha( 0 );
//
//                } else if ( position <= 0 ) { // [-1,0]
//                    // Use the default slide transition when moving to the left page
//                    page.setAlpha( 1 );
//                    page.setTranslationX( 0 );
//                    page.setScaleX( 1 );
//                    page.setScaleY( 1 );
//
//                } else if ( position <= 1 ) { // (0,1]
//                    // Fade the page out.
//                    page.setAlpha( 1 - position );
//
//                    // Counteract the default slide transition
//                    page.setTranslationX( pageWidth * -position );
//
//                    // Scale the page down ( between MIN_SCALE and 1 )
//                    float scaleFactor = MIN_SCALE
//                            + ( 1 - MIN_SCALE ) * ( 1 - Math.abs( position ) );
//                    page.setScaleX( scaleFactor );
//                    page.setScaleY( scaleFactor );
//
//                } else { // ( 1, +Infinity ]
//                    // This page is way off-screen to the right.
//                    page.setAlpha( 0 );
//                }
//            }
//        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() { //Changes tab focus while sliding
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        navController = Navigation.findNavController(view);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.no_image_found, getContext().getTheme()));
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
    }
}