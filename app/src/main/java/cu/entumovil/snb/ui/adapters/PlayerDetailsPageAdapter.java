package cu.entumovil.snb.ui.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PlayerDetailsPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList= new ArrayList<>();

    public PlayerDetailsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).toString();
    }

    public void addTab(Fragment tabFragment){
        fragmentList.add(tabFragment);
        notifyDataSetChanged();
    }

}
