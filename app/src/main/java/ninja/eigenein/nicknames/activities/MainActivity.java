package ninja.eigenein.nicknames.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashMap;

import ninja.eigenein.nicknames.Application;
import ninja.eigenein.nicknames.R;
import ninja.eigenein.nicknames.core.Model;


public class MainActivity extends BaseActivity {

    private static final String STATE_NICKNAME = "nickname";
    private static final String STATE_LENGTH = "length";

    private static final int MIN_NICKNAME_LENGTH = 3;
    private static final HashMap<Integer, String> MENU_ITEM_SECTION_KEY = new HashMap<>();

    private String sectionKey = "people_male";
    private String characterSet = "latin";

    private SeekBar lengthSeekBar;
    private TextView nicknameTextView;

    static {
        MENU_ITEM_SECTION_KEY.put(R.id.navigation_male, "people_male");
        MENU_ITEM_SECTION_KEY.put(R.id.navigation_female, "people_female");
        MENU_ITEM_SECTION_KEY.put(R.id.navigation_elves, "elves");
        MENU_ITEM_SECTION_KEY.put(R.id.navigation_dwarves, "dwarves");
        MENU_ITEM_SECTION_KEY.put(R.id.navigation_orcs, "orcs");
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setupToolbar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        setupTabLayout();

        lengthSeekBar = (SeekBar)findViewById(R.id.seek_length);
        nicknameTextView = (TextView)findViewById(R.id.text_view_nickname);
        findViewById(R.id.layout_clickable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                generate();
            }
        });

        final DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        ((NavigationView)findViewById(R.id.navigation_view)).setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {
                sectionKey = MENU_ITEM_SECTION_KEY.get(menuItem.getItemId());
                drawerLayout.closeDrawers();
                generate();
                return true;
            }
        });

        if ((savedInstanceState != null) && savedInstanceState.containsKey(STATE_LENGTH)) {
            lengthSeekBar.setProgress(savedInstanceState.getInt(STATE_LENGTH));
        }
        if ((savedInstanceState != null) && savedInstanceState.containsKey(STATE_NICKNAME)) {
            nicknameTextView.setText(savedInstanceState.getCharSequence(STATE_NICKNAME));
        } else {
            generate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                ((DrawerLayout)findViewById(R.id.drawer)).openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle savedInstanceState) {
        savedInstanceState.putInt(STATE_LENGTH, lengthSeekBar.getProgress());
        savedInstanceState.putCharSequence(STATE_NICKNAME, nicknameTextView.getText());
    }

    private void setupTabLayout() {
        final TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.navigation_latin).setTag("latin"));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.navigation_cyrillic).setTag("cyrillic"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                characterSet = (String)tab.getTag();
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
        final Model model = Application.getModel(sectionKey + "_" + characterSet);
        nicknameTextView.setText(model.generate(lengthSeekBar.getProgress() + MIN_NICKNAME_LENGTH));
    }
}
