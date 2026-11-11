package za.co.clai.website.content;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Every price format used in content/treatments.yml (Nov 2026) parses to the right structured-data price. */
class TreatmentPriceTest {

    @Test
    void exactPrice() {
        assertThat(TreatmentPrice.parse("<p>Text.<br><br><b>Price: R 1150</b><br><br><b>Time: 60 minutes</b></p>"))
                .isEqualTo(new TreatmentPrice(1150, null, null, null));
    }

    @Test
    void thousandsWithSpaceOrComma() {
        assertThat(TreatmentPrice.parse("<b>Price: R 15 990</b><br><br><b>Time: 90 minutes</b>").exact()).isEqualTo(15990);
        assertThat(TreatmentPrice.parse("<b>Price: R1,150</b>").exact()).isEqualTo(1150);
    }

    @Test
    void range() {
        assertThat(TreatmentPrice.parse("<b>Price: R 2675 - R 3100</b>")).isEqualTo(new TreatmentPrice(null, 2675, 3100, null));
    }

    @Test
    void fromPrice() {
        assertThat(TreatmentPrice.parse("<b>From: R 4890</b><br><br><b>Time: 120 minutes</b>")).isEqualTo(new TreatmentPrice(null, 4890, null, null));
        assertThat(TreatmentPrice.parse("<b>From R 2500</b>")).isEqualTo(new TreatmentPrice(null, 2500, null, null));
    }

    @Test
    void perVial() {
        assertThat(TreatmentPrice.parse("Our <b>Sculptra treatments</b> start at <b>R7,900 per vial</b>, with a discount"))
                .isEqualTo(new TreatmentPrice(null, 7900, null, "vial"));
        assertThat(TreatmentPrice.parse("are priced from <b>R8,500 per vial</b>")).isEqualTo(new TreatmentPrice(null, 8500, null, "vial"));
    }

    @Test
    void severalPricesBecomeARange() {
        assertThat(TreatmentPrice.parse("<b>Price</b><br>Treatment 1: R 6500<br>Treatment 2-3: R 3500<br><b>Time</b>"))
                .isEqualTo(new TreatmentPrice(null, 3500, 6500, null));
        assertThat(TreatmentPrice.parse("<b>Pro-Peel R 950</b><br><b>MD-Peel R 1100</b>")).isEqualTo(new TreatmentPrice(null, 950, 1100, null));
    }

    @Test
    void noPrice() {
        assertThat(TreatmentPrice.parse("<p>Comedonal acne includes blackheads and whiteheads.</p>")).isNull();
    }

    @Test
    void summaryIsTheFirstParagraphAsText() {
        assertThat(Treatments.summary("<p class=\"mb-0\">\n  First &amp; best.\n  <br><br>\n  Second.</p>")).isEqualTo("First & best.");
        assertThat(Treatments.summary("<p>" + "word ".repeat(100) + "</p>")).hasSizeLessThanOrEqualTo(300).endsWith("…");
    }
}
