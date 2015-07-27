package ninja.eigenein.nicknames;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;

public class Application extends android.app.Application {

    private static final String LOG_TAG = Application.class.getSimpleName();

    private static JSONObject models;

    @Override
    public void onCreate() {
        readModels();
    }

    public JSONObject getModels() {
        return models;
    }

    private void readModels() {
        Log.i(LOG_TAG, "Reading models.");
        final InputStream stream = getApplicationContext().getResources().openRawResource(R.raw.models);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
        try {
            models = new JSONObject(reader.readLine());
        } catch (final Exception e) {
            throw new RuntimeException("Error reading models.", e);
        }
        final Iterator<String> keys = models.keys();
        while (keys.hasNext()) {
            Log.i(LOG_TAG, "Read model: " + keys.next());
        }
    }
}
