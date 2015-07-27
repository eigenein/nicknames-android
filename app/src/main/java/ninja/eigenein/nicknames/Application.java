package ninja.eigenein.nicknames;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;

import ninja.eigenein.nicknames.core.Model;

public class Application extends android.app.Application {

    private static final String LOG_TAG = Application.class.getSimpleName();

    private static HashMap<String, Model> models = new HashMap<>();

    @Override
    public void onCreate() {
        try {
            readModels();
        } catch (final Exception e) {
            throw new RuntimeException("Error reading models.", e);
        }
    }

    public static Model getModel(final String key) {
        return models.get(key);
    }

    private void readModels() throws JSONException, IOException {
        Log.i(LOG_TAG, "Reading models.");
        final InputStream stream = getApplicationContext().getResources().openRawResource(R.raw.models);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
        final JSONObject modelsObject = new JSONObject(reader.readLine());
        final Iterator<String> keys = modelsObject.keys();
        while (keys.hasNext()) {
            final String key = keys.next();
            Log.i(LOG_TAG, "Initializing model: " + key);
            models.put(key, Model.fromJSON(modelsObject.getJSONObject(key)));
            Log.i(LOG_TAG, "Initialized model: " + key);
        }
    }
}
