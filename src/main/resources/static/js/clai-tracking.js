/*
 * CLAI conversion tracking (Google Analytics 4 events).
 * Loaded on every page from fragments/layout :: page-end. Sends one event per enquiry action:
 *
 *   book_appointment_click  "Book appointment" buttons (header and page heroes)
 *   whatsapp_click          the floating WhatsApp button or any wa.me link
 *   phone_click             tel: links (footer, contact page)
 *   email_click             mailto: links (footer, contact page)
 *   contact_form_submit     the contact form on /contact-us (sent when it is submitted)
 *   price_list_open         opening the price list (sent from fragments/pdf-modal)
 *
 * Each event carries link_location (header, hero, footer, contact_page, floating_button, page)
 * so reports can show where people clicked. To count them as conversions, mark the events as
 * key events in GA4: Admin > Data display > Events.
 */
(function () {
  "use strict";

  function track(name, params) {
    if (typeof window.gtag !== "function") return;
    window.gtag("event", name, Object.assign({ page_path: location.pathname }, params || {}));
  }
  window.claiTrack = track;

  function locationOf(el) {
    if (el.closest(".whatsapp-float")) return "floating_button";
    if (el.closest("header, .header")) return "header";
    if (el.closest("footer, .footer, .footer-bottom")) return "footer";
    if (el.closest(".message-info, .contact-us-form")) return "contact_page";
    if (el.closest(".products-banner, .features-section")) return "hero";
    return "page";
  }

  document.addEventListener("click", function (ev) {
    var a = ev.target.closest && ev.target.closest("a[href]");
    if (!a) return;
    var href = a.getAttribute("href") || "";
    var where = { link_location: locationOf(a) };
    if (href.indexOf("https://wa.me/") === 0) track("whatsapp_click", where);
    else if (href.indexOf("tel:") === 0) track("phone_click", where);
    else if (href.indexOf("mailto:") === 0) track("email_click", where);
    else if (a.classList.contains("appointment")) track("book_appointment_click", where);
  }, { capture: true });

  document.addEventListener("submit", function (ev) {
    if (ev.target && ev.target.id === "contactForm") track("contact_form_submit", { link_location: "contact_page" });
  }, { capture: true });
})();
