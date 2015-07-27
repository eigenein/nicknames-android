package ninja.eigenein.nicknames.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import ninja.eigenein.nicknames.Application;
import ninja.eigenein.nicknames.R;
import ninja.eigenein.nicknames.core.Model;


public class MainActivity extends BaseActivity {

    private static final int MIN_NICKNAME_LENGTH = 3;

    private String sectionKey = "people_male";
    private Model model;

    private SeekBar lengthSeekBar;
    private TextView nicknameTextView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setupToolbar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        setupTabLayout();

        model = Application.getModel("people_male_latin");

        lengthSeekBar = (SeekBar)findViewById(R.id.seek_length);
        nicknameTextView = (TextView)findViewById(R.id.text_view_nickname);
        findViewById(R.id.layout_clickable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                generate();
            }
        });

        generate();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((DrawerLayout)findViewById(R.id.drawer)).openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupTabLayout() {
        final TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.navigation_latin));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.navigation_cyrillic));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                final String characterSet = tab.getPosition() == 0 ? "_latin" : "_cyrillic"; // TODO
                model = Application.getModel(sectionKey + characterSet);
                generate();
            }

            @Override
            public void onTabUnselected(final TabLayout.Tab tab) {
                // Do nothing.
            }

            @Override
            public void onTabReselected(final TabLayout.Tab tab) {
                // Do nothing.
            }
        });
    }

    private void generate() {
        nicknameTextView.setText(model.generate(lengthSeekBar.getProgress() + MIN_NICKNAME_LENGTH));
    }
}
