package cu.entumovil.informationmodule.UI.Activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import cu.entumovil.informationmodule.R;
import cu.entumovil.informationmodule.UI.Fragments.AboutFragment;
import cu.entumovil.informationmodule.UI.Fragments.DescriptionFragment;


public class AboutActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitleColor(getResources().getColor(R.color.accent));
        setupFragment(AboutFragment.newInstance());
    }

    @Override
    public void onBackPressed() {
        if (currentFragment instanceof DescriptionFragment) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            setTitle("Acerca de");
            setupFragment(AboutFragment.newInstance());
        } else {
            finish();
            super.onBackPressed();
        }
    }

    public void setupFragment(Fragment fragment) {
        currentFragment = fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void showDescription(View view) {
        setupFragment(DescriptionFragment.newInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (currentFragment instanceof DescriptionFragment) {
                getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
                setTitle("Acerca de");
                setupFragment(AboutFragment.newInstance());
            } else {
                finish();
                super.onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
