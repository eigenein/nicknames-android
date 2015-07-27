package ninja.eigenein.nicknames.core;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Model {

    private static final int N = 3;
    private static final Random RANDOM = new Random();

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
            final ArrayList<CharLevel> modelContinuations;
            if (!model.p.containsKey(key)) {
                modelContinuations = new ArrayList<>();
                model.p.put(key, modelContinuations);
            } else {
                modelContinuations = model.p.get(key);
            }
            // Fill in the list.
            final JSONArray continuations = p.getJSONArray(key);
            for (int i = 0; i < continuations.length(); i++) {
                final JSONArray continuation = continuations.getJSONArray(i);
                modelContinuations.add(new CharLevel(
                        continuation.getString(0).charAt(0),
                        continuation.getDouble(1)));
            }
        }

        return model;
    }

    public String generate(final int length) {
        String word = "";

        for (int i = 0; i < N - 1; i++) {
            word += "$";
        }

        while (true) {
            final int endingStart = word.length() - N + 1; // subtract the prefix $$
            final String ending = word.substring(endingStart, word.length());
            if ((endingStart >= length) && breakable.contains(ending)) {
                break;
            }

            final List<CharLevel> continuations = p.get(ending);
            if (continuations == null) {
                break;
            }

            final double level = RANDOM.nextDouble();
            for (final CharLevel continuation : continuations) {
                if (level < continuation.getLevel()) {
                    word += continuation.getChar();
                    break;
                }
            }
        }

        return word.replace("$", "");
    }
}
