package za.co.clai.website.content;

import java.util.List;

/**
 * One accordion card from content/treatments.yml.
 *
 * @param id       key in treatments.yml, referenced by pages (e.g. "glass-skin-facial")
 * @param title    button label (plain text; it is HTML-escaped when rendered)
 * @param body     card body HTML, normally one {@code <p class="mb-0">...</p>}
 * @param children ids of cards shown in a nested accordion under the body (e.g. Sculptra Face / Body), or empty
 * @param price    price parsed from the body text (for structured data), or null when the card states none
 * @param summary  the body's first paragraph as plain text, at most ~300 characters (structured data description)
 */
public record Treatment(String id, String title, String body, List<String> children, TreatmentPrice price, String summary) {

    /** Part of the nested accordion's ids: "sculptra" -> "Sculptra", "glow-and-behold" -> "GlowAndBehold". */
    public String key() {
        StringBuilder b = new StringBuilder();
        for (String part : id.split("[^A-Za-z0-9]+")) {
            if (!part.isEmpty()) {
                b.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
            }
        }
        return b.toString();
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
