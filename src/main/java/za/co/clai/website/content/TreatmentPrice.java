package za.co.clai.website.content;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A treatment's price as written in its card text (content/treatments.yml), for structured data.
 * Exactly one of: {@code exact} set; or {@code min} (with optional {@code max}) for "From R 2500",
 * "R 2675 - R 3100", "from R8,500 per vial" or cards listing several prices (e.g. Sunekos, Pro/MD peel).
 *
 * @param exact a single fixed price in rand, or null
 * @param min   lowest price in rand, or null
 * @param max   highest price in rand, or null (open-ended "from")
 * @param unit  "vial" for per-vial prices, otherwise null
 */
public record TreatmentPrice(Integer exact, Integer min, Integer max, String unit) {

    /** An amount: "R 1150", "R1150", "R 15 990", "R7,900". */
    private static final String AMOUNT = "R\\s?(\\d{1,3}(?:[ ,]\\d{3})+|\\d+)";
    private static final Pattern RANGE = Pattern.compile("Price:\\s*" + AMOUNT + "\\s*-\\s*" + AMOUNT);
    private static final Pattern EXACT = Pattern.compile("Price:\\s*" + AMOUNT + "(?!\\s*-)");
    private static final Pattern FROM = Pattern.compile("(?i)\\bfrom:?\\s*" + AMOUNT);
    private static final Pattern PER_VIAL = Pattern.compile("(?i)(?:start at|from)\\s*" + AMOUNT + "\\s*per vial");
    private static final Pattern ANY = Pattern.compile(AMOUNT);

    /** Parses a card body (HTML); null when it states no price. */
    public static TreatmentPrice parse(String bodyHtml) {
        String text = bodyHtml.replaceAll("<[^>]+>", " ").replaceAll("&nbsp;", " ").replaceAll("\\s+", " ");
        Matcher m;
        if ((m = PER_VIAL.matcher(text)).find()) {
            return new TreatmentPrice(null, amount(m.group(1)), null, "vial");
        }
        if ((m = RANGE.matcher(text)).find()) {
            return new TreatmentPrice(null, amount(m.group(1)), amount(m.group(2)), null);
        }
        if ((m = EXACT.matcher(text)).find()) {
            return new TreatmentPrice(amount(m.group(1)), null, null, null);
        }
        if ((m = FROM.matcher(text)).find()) {
            return new TreatmentPrice(null, amount(m.group(1)), null, null);
        }
        List<Integer> all = new ArrayList<>();
        m = ANY.matcher(text);
        while (m.find()) {
            all.add(amount(m.group(1)));
        }
        if (all.isEmpty()) {
            return null;
        }
        int lo = all.stream().min(Integer::compare).orElseThrow();
        int hi = all.stream().max(Integer::compare).orElseThrow();
        return lo == hi ? new TreatmentPrice(lo, null, null, null) : new TreatmentPrice(null, lo, hi, null);
    }

    private static int amount(String s) {
        return Integer.parseInt(s.replaceAll("[ ,]", ""));
    }
}
