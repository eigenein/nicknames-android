package ninja.eigenein.nicknames.core;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Model {

    private final HashMap<String, ArrayList<CharLevel>> p = new HashMap<>();
    private final HashSet<String> breakable = new HashSet<>();

    public static Model fromJSON(final JSONObject modelObject) throws JSONException {
        final Model model = new Model();
        // Read breakable sequences.
        final JSONArray breakable = modelObject.getJSONArray("breakable");
        for (int i = 0; i < breakable.length(); i++) {
            model.breakable.add(breakable.getString(i));
        }
        // Read probabilities.
        final JSONObject p = modelObject.getJSONObject("p");
        final Iterator<String> keyIterator = p.keys();
        while (keyIterator.hasNext()) {
            final String key = keyIterator.next();
            // Initialize list of continuations.
            final ArrayList<CharLevel> modelChars;
            if (!model.p.containsKey(key)) {
                modelChars = new ArrayList<>();
                model.p.put(key, modelChars);
            } else {
                modelChars = model.p.get(key);
            }
            // Fill in the list.
            final JSONArray chars = p.getJSONArray(key);
            for (int i = 0; i < chars.length(); i++) {
                final JSONArray aChar = chars.getJSONArray(i);
                modelChars.add(new CharLevel(aChar.getString(0).charAt(0), aChar.getDouble(1)));
            }
        }

        return model;
    }
}
