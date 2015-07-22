package ninja.eigenein.nicknames.activities

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import ninja.eigenein.nicknames.R

public abstract class BaseActivity : AppCompatActivity() {

    protected fun setupToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val actionBar = getSupportActionBar()
        actionBar.setDisplayHomeAsUpEnabled(true)
    }
}
