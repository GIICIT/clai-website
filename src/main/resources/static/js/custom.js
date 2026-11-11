/*

Template: LuxurySpa - Spa & Beauty HTML Template
Author: potenzaglobalsolutions
Design and Developed by: potenzaglobalsolutions.com

NOTE: This file contains all scripts for the actual Template.

*/

/*================================================
[  Table of contents  ]
================================================

:: Preloader
:: Menu
:: Sticky
:: Tooltip
:: Popover
:: Counter
:: Owl carousel
:: Slick slider
:: Datetime picker
:: Select2
:: Range Slider
:: Countdown
:: Swiper Animation
:: Back to top
:: Magnific Popup
:: Search bar

======================================
[ End table content ]
======================================*/
//POTENZA var

(function ($) {
  "use strict";
  var POTENZA = {};

/*************************
  Predefined Variables
*************************/
  var $window = $(window),
    $document = $(document),
    $body = $('body'),
    $countdownTimer = $('.countdown'),
    $counter = $('.counter');
  //Check if function exists
  $.fn.exists = function () {
    return this.length > 0;
  };

/*************************
        Preloader
  *************************/
  POTENZA.preloader = function () {
    $("#load").fadeOut();
    $('#pre-loader').delay(0).fadeOut('slow');
  };


/*************************
      Menu
  *************************/
  POTENZA.dropdownmenu = function () {
    if ($('.navbar').exists()) {
      $('.dropdown-menu a.dropdown-toggle').on('click', function (e) {
        if (!$(this).next().hasClass('show')) {
          $(this).parents('.dropdown-menu').first().find('.show').removeClass("show");
        }
        var $subMenu = $(this).next(".dropdown-menu");
        $subMenu.toggleClass('show');
        $(this).parents('li.nav-item.dropdown.show').on('hidden.bs.dropdown', function (e) {
          $('.dropdown-submenu .show').removeClass("show");
        });
        return false;
      });
    }
  };

/*************************
         Sticky
*************************/

POTENZA.isSticky = function () {
  $(window).scroll(function(){
    if ($(this).scrollTop() > 450) {
       $('.header-sticky').addClass('is-sticky');
    } else {
       $('.header-sticky').removeClass('is-sticky');
    }
  });
};


  /*************************
       Tooltip
  *************************/
  POTENZA.Tooltip = function() {
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
      var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
    })
  }

  /*************************
        Popover
  *************************/
  POTENZA.Popover = function() {
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
      var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl)
    })
  }

/*************************
       Counter
*************************/
  POTENZA.counters = function () {
    var counter = jQuery(".counter");
    if (counter.length > 0) {
      $counter.each(function () {
        var $elem = $(this);
        $elem.appear(function () {
          $elem.find('.timer').countTo();
        });
      });
    }
  };

  /*************************
       Owl carousel
*************************/
  POTENZA.carousel = function () {
    var owlslider = jQuery("div.owl-carousel");
    if (owlslider.length > 0) {
      owlslider.each(function () {
        var $this = $(this),
          $items = ($this.data('items')) ? $this.data('items') : 1,
          $loop = ($this.attr('data-loop')) ? $this.data('loop') : true,
          $navdots = ($this.data('nav-dots')) ? $this.data('nav-dots') : false,
          $navarrow = ($this.data('nav-arrow')) ? $this.data('nav-arrow') : false,
          $autoplay = ($this.attr('data-autoplay')) ? $this.data('autoplay') : true,
          $autospeed = ($this.attr('data-autospeed')) ? $this.data('autospeed') : 5000,
          $smartspeed = ($this.attr('data-smartspeed')) ? $this.data('smartspeed') : 1000,
          $autohgt = ($this.data('autoheight')) ? $this.data('autoheight') : false,
          $space = ($this.attr('data-space')) ? $this.data('space') : 30,
          $animateOut = ($this.attr('data-animateOut')) ? $this.data('animateOut') : false;
        $(this).owlCarousel({
          loop: $loop,
          items: $items,
          responsive: {
            0: {
              items: $this.data('xx-items') ? $this.data('xx-items') : 1
            },
            480: {
              items: $this.data('xs-items') ? $this.data('xs-items') : 1
            },
            768: {
              items: $this.data('sm-items') ? $this.data('sm-items') : 2
            },
            980: {
              items: $this.data('md-items') ? $this.data('md-items') : 3
            },
            1200: {
              items: $items
            }
          },
          dots: $navdots,
          autoplayTimeout: $autospeed,
          smartSpeed: $smartspeed,
          autoHeight: $autohgt,
          margin: $space,
          nav: $navarrow,
          navText: ["<i class='fas fa-chevron-left'></i>", "<i class='fas fa-chevron-right'></i>"],
          autoplay: $autoplay,
          autoplayHoverPause: true
        });
      });
    }
  }

  /*************************
        Slick slider
*************************/
  POTENZA.slickslider = function () {
    if ($('.slider-for').exists()) {
      $('.slider-for').slick({
        slidesToShow: 1,
        slidesToScroll: 1,
        arrows: true,
        asNavFor: '.slider-nav'
      });
      $('.slider-nav').slick({
        slidesToShow: 3,
        slidesToScroll: 1,
        asNavFor: '.slider-for',
        dots: false,
        centerMode: true,
        focusOnSelect: true
      });
    }
  };

  /*************************
      datetime picker
  *************************/
  POTENZA.datetimepickers = function () {
    if ($('.datetimepickers').exists()) {
      $('#datetimepicker-01, #datetimepicker-02').datetimepicker({
        format: 'L'
      });
      $('#datetimepicker-03, #datetimepicker-04').datetimepicker({
        format: 'LT'
      });
    }
  };

  /*************************
      Select2
  *************************/
  POTENZA.select2 = function () {
    if ($('.basic-select').exists()) {
      var select = jQuery(".basic-select");
      if (select.length > 0) {
        $('.basic-select').select2({dropdownCssClass : 'bigdrop'});
      }
    }
  };

/*************************
      Range Slider
*************************/
  POTENZA.rangesliders = function () {
    if ($('.property-price-slider').exists()) {
      var rangeslider = jQuery(".rangeslider-wrapper");
      $("#property-price-slider").ionRangeSlider({
        type: "double",
        min: 0,
        max: 10000,
        from: 1000,
        to: 8000,
        prefix: "$",
        hide_min_max: true,
        hide_from_to: false
      });
    }
  };

  /*************************
           Countdown
  *************************/
  POTENZA.countdownTimer = function () {
    if ($countdownTimer.exists()) {
      $countdownTimer.downCount({
        date: '12/31/2024 12:00:00', // Month/Date/Year HH:MM:SS
        offset: -4
      });
    }
  }

/*************************
   Swiper Animation
*************************/
POTENZA.swiperAnimation = function () {
  var siperslider = jQuery(".swiper-container");
  if (siperslider.length > 0) {
    var swiperAnimation = new SwiperAnimation();
        var swiper = new Swiper(".swiper-container", {
          init : true,
          direction: "horizontal",
          effect: "slide",
          loop: true,

          keyboard: {
            enabled: true,
            onlyInViewport: true
          },
            pagination: {
              el: '.swiper-pagination',
              dynamicBullets: true,
              clickable: true,
            },
            // Navigation arrows
            navigation: {
              nextEl: '.swiper-button-next',
              prevEl: '.swiper-button-prev',
            },
          on: {
            init: function() {
              swiperAnimation.init(this).animate();
            },
            slideChange: function() {

              swiperAnimation.init(this).animate();
            }
          }
        });
    }
  }

/*************************
     Back to top
*************************/
  POTENZA.goToTop = function () {
    var $goToTop = $('#back-to-top');
    $goToTop.hide();
    $window.scroll(function () {
      if ($window.scrollTop() > 100) $goToTop.fadeIn();
      else $goToTop.fadeOut();
    });
    $goToTop.on("click", function () {
      $('body,html').animate({
        scrollTop: 0
      }, 1000);
      return false;
    });
  }

/*************************
     Search bar
*************************/
POTENZA.searchbar = function () {
  if ($("#search").exists()) {
    $('a[href="#search"]').on('click', function(event) {
        event.preventDefault();
        $('#search').addClass('open');
        $('#search > form > input[type="search"]').focus();
    });
    $('#search, #search button.close').on('click keyup', function(event) {
        if (event.target == this || event.target.className == 'close' || event.keyCode == 27) {
            $(this).removeClass('open');
        }
    });
    //Do not include! This prevents the form from submitting for DEMO purposes only!
    // $('form').submit(function(event) {
    //     event.preventDefault();
    //     return false;
    // })
   }
}

  /*************************
      Magnific Popup
  *************************/
  POTENZA.mediaPopups = function () {
    if ($(".popup-single").exists() || $(".popup-gallery").exists() || $('.modal-onload').exists() || $(".popup-youtube, .popup-vimeo, .popup-gmaps").exists()) {
      if ($(".popup-single").exists()) {
        $('.popup-single').magnificPopup({
          type: 'image'
        });
      }
      if ($(".popup-gallery").exists()) {
        $('.popup-gallery').magnificPopup({
          delegate: 'a.portfolio-img',
          type: 'image',
          tLoading: 'Loading image #%curr%...',
          mainClass: 'mfp-img-mobile',
          gallery: {
            enabled: true,
            navigateByImgClick: true,
            preload: [0, 1] // Will preload 0 - before current, and 1 after the current image
          }
        });
      }
      if ($(".popup-youtube, .popup-vimeo, .popup-gmaps").exists()) {
        $('.popup-youtube, .popup-vimeo, .popup-gmaps').magnificPopup({
          disableOn: 700,
          type: 'iframe',
          mainClass: 'mfp-fade',
          removalDelay: 160,
          preloader: false,
          fixedContentPos: false
        });
      }
      var $modal = $('.modal-onload');
      if ($modal.length > 0) {
        $('.popup-modal').magnificPopup({
          type: 'inline'
        });
        $(document).on('click', '.popup-modal-dismiss', function (e) {
          e.preventDefault();
          $.magnificPopup.close();
        });
        var elementTarget = $modal.attr('data-target');
        setTimeout(function () {
          $.magnificPopup.open({
            items: {
              src: elementTarget
            },
            type: "inline",
            mainClass: "mfp-no-margins mfp-fade",
            closeBtnInside: !0,
            fixedContentPos: !0,
            removalDelay: 500
          }, 0)
        }, 1500);
      }
    }
  }

  /*************************
      Animated circle text
  *************************/
  POTENZA.animatedcircletext = function () {
    const text = document.querySelector(".text");
    if (!text) return; // only the home page has the circle text; without this every other page threw a TypeError
    text.innerHTML = text.innerText
      .split("")
      .map(
        (char, i) => `<span style="transform:rotate(${i * 11.3}deg)">${char}</span>`
      )
      .join("");
  }

    /*************************
     Qty
    *************************/
    POTENZA.qty = function () {
  $(document).ready(function() {
      $('.minus').click(function () {
        var $input = $(this).parent().find('input');
        var count = parseInt($input.val()) - 1;
        count = count < 1 ? 1 : count;
        $input.val(count);
        $input.change();
        return false;
      });
      $('.plus').click(function () {
        var $input = $(this).parent().find('input');
        $input.val(parseInt($input.val()) + 1);
        $input.change();
        return false;
      });
    });
}

  /****************************************************
       POTENZA Window load and functions
  ****************************************************/
  //Window load functions
  $window.on("load", function () {
    POTENZA.preloader()
  });
  //Document ready functions
  $document.ready(function () {
    POTENZA.counters(),
    POTENZA.slickslider(),
    POTENZA.datetimepickers(),
    POTENZA.select2(),
    POTENZA.dropdownmenu(),
    POTENZA.isSticky(),
    POTENZA.goToTop(),
    POTENZA.countdownTimer(),
    POTENZA.rangesliders(),
    POTENZA.Tooltip(),
    POTENZA.Popover(),
    POTENZA.swiperAnimation(),
    POTENZA.searchbar(),
    POTENZA.mediaPopups(),
    POTENZA.carousel(),
    POTENZA.animatedcircletext(),
    POTENZA.qty();
  });
  })(jQuery);


  (() => {
  const mqDesktop = window.matchMedia('(min-width: 992px)');
  const isDesktop = () => mqDesktop.matches;

  /* ---------------- helpers ---------------- */
  function eachOpenPanel(scope, cb) {
  (scope || document).querySelectorAll('.treatments .js-category-panel.open').forEach(cb);
}

  function closeAllIn(scope) {
  eachOpenPanel(scope, (p) => {
  p.classList.remove('open');
  p.querySelector('.js-category-toggle')?.setAttribute('aria-expanded', 'false');
  const c = p.closest('.owl-carousel');
  if (c) unlockCarousel(c);
});
}

  function getOwl(carouselEl) {
  if (!(window.jQuery && jQuery.fn && jQuery.fn.owlCarousel)) return null;
  const $c = jQuery(carouselEl);
  const owl = $c.data('owl.carousel');
  return owl ? { $c, owl } : null;
}

  function rememberOriginals($c, owl) {
  if ($c.data('traylockStored')) return;
  $c.data('traylockStored', true);
  $c.data('origMouseDrag', !!(owl.settings?.mouseDrag ?? owl.options?.mouseDrag ?? true));
  $c.data('origTouchDrag', !!(owl.settings?.touchDrag ?? owl.options?.touchDrag ?? true));
  $c.data('origAutoplay',  !!(owl.settings?.autoplay  ?? owl.options?.autoplay  ?? false));
}

  /* capture-phase blockers so Owl can’t start dragging while locked */
  function ensureBlockers(carouselEl) {
  if (carouselEl.__owlLockBlockers) return;
  const stopIfLocked = (ev) => { if (carouselEl.dataset.locked === '1') ev.stopImmediatePropagation(); };
  const targets = [carouselEl, carouselEl.querySelector('.owl-stage-outer'), carouselEl.querySelector('.owl-stage')].filter(Boolean);
  const types = ['pointerdown','pointermove','mousedown','mousemove','touchstart','touchmove','dragstart'];
  targets.forEach(node => types.forEach(t => node.addEventListener(t, stopIfLocked, true)));
  carouselEl.__owlLockBlockers = true;
}

  function bindAutoplayKill($c, owl, carouselEl) {
  const kill = () => {
  if (carouselEl.dataset.locked !== '1') return;
  try { $c.trigger('stop.owl.autoplay'); } catch {}
  const ap = owl._plugins && owl._plugins.autoplay;
  if (ap) { try { clearTimeout(ap._timeout); } catch {} ap._playing = false; }
};
  $c.off('.traylock');
  $c.on('play.owl.autoplay.traylock translated.owl.carousel.traylock changed.owl.carousel.traylock refreshed.owl.carousel.traylock resized.owl.carousel.traylock dragged.owl.carousel.traylock', kill);
  kill();
}

  function unbindAutoplayKill($c) { $c.off('.traylock'); }

  function lockCarousel(carouselEl) {
  if (!carouselEl || carouselEl.dataset.locked === '1') return;
  carouselEl.dataset.locked = '1';
  ensureBlockers(carouselEl);

  const api = getOwl(carouselEl);
  if (api) {
  const { $c, owl } = api;
  rememberOriginals($c, owl);
  owl.settings.mouseDrag = false;
  owl.settings.touchDrag = false;
  owl.options.mouseDrag  = false;
  owl.options.touchDrag  = false;
  try { $c.trigger('stop.owl.autoplay'); } catch {}
  try { $c.trigger('refresh.owl.carousel'); } catch {}
  bindAutoplayKill($c, owl, carouselEl);
}
}

  function unlockCarousel(carouselEl) {
  if (!carouselEl) return;
  if (carouselEl.querySelector('.js-category-panel.open')) return; // still open somewhere
  carouselEl.dataset.locked = '0';

  const api = getOwl(carouselEl);
  if (api) {
  const { $c, owl } = api;
  unbindAutoplayKill($c);
  const origMouse = $c.data('origMouseDrag');
  const origTouch = $c.data('origTouchDrag');
  const origAuto  = $c.data('origAutoplay');
  owl.settings.mouseDrag = !!origMouse;
  owl.settings.touchDrag = !!origTouch;
  owl.options.mouseDrag  = !!origMouse;
  owl.options.touchDrag  = !!origTouch;
  try { $c.trigger('refresh.owl.carousel'); } catch {}
  if (origAuto) { try { $c.trigger('play.owl.autoplay'); } catch {} }
}
}

  function lockOrUnlockFor(panelEl) {
  const carousel = panelEl.closest('.owl-carousel');
  if (!carousel) return;
  if (panelEl.classList.contains('open')) lockCarousel(carousel);
  else unlockCarousel(carousel);
}

  /* ---------------- Desktop: lock while hovered open ---------------- */
  document.addEventListener('mouseenter', (e) => {
  if (!isDesktop()) return;
  const panel = e.target.closest('.treatments .js-category-panel');
  if (!panel) return;
  lockCarousel(panel.closest('.owl-carousel'));
}, true);

  document.addEventListener('mouseleave', (e) => {
  if (!isDesktop()) return;
  const panel = e.target.closest('.treatments .js-category-panel');
  if (!panel) return;
  const carousel = panel.closest('.owl-carousel');
  if (!carousel) return;
  setTimeout(() => {
  const stillHovering = carousel.querySelector('.js-category-panel:hover') || carousel.matches(':hover');
  if (!stillHovering) unlockCarousel(carousel);
}, 40);
}, true);

  /* ---------------- Mobile ONLY ---------------- */
  try { mqDesktop.addEventListener?.('change', e => { if (e.matches) closeAllIn(); }); } catch {}

  // Only TITLE toggles now (image no longer opens)
  function mobileToggle(e) {
  const title = e.target.closest('.treatments .js-category-toggle');
  if (!title) return;              // ignore image taps completely
  e.preventDefault();
  e.stopPropagation();

  const box   = title.closest('.category-box');
  const panel = box && box.querySelector('.js-category-panel');
  if (!panel) return;

  // close siblings in same carousel
  const carousel = panel.closest('.owl-carousel');
  if (carousel) {
  carousel.querySelectorAll('.js-category-panel.open').forEach(p => {
  if (p !== panel) {
  p.classList.remove('open');
  p.querySelector('.js-category-toggle')?.setAttribute('aria-expanded', 'false');
}
});
}

  const willOpen = !panel.classList.contains('open');
  panel.classList.toggle('open', willOpen);
  title.setAttribute('aria-expanded', willOpen ? 'true' : 'false');
  lockOrUnlockFor(panel);
}

  // prevent touch->click double toggle
  let lastTouch = 0;
  document.addEventListener('touchend', (e) => {
  if (isDesktop()) return;
  lastTouch = Date.now();
  mobileToggle(e);
}, { passive: false });

  document.addEventListener('click', (e) => {
  if (isDesktop()) return;
  if (Date.now() - lastTouch < 350) return;
  mobileToggle(e);
}, { passive: false });

  // Outside tap closes & unlocks (💡 image tap now counts as outside)
  document.addEventListener('click', (e) => {
  if (isDesktop()) return;
  if (e.target.closest('.treatments .js-category-panel')) return;
  closeAllIn();
  document.querySelectorAll('.owl-carousel').forEach(unlockCarousel);
});

  // any scroll closes & unlocks (mobile)
  window.addEventListener('scroll', () => {
  if (isDesktop()) return;
  closeAllIn();
  document.querySelectorAll('.owl-carousel').forEach(unlockCarousel);
}, { passive: true });

  // swipe/drag anywhere closes & unlocks (mobile)
  let start = null;
  document.addEventListener('touchstart', (e) => {
  if (isDesktop()) return;
  const t = e.touches[0];
  start = { x: t.clientX, y: t.clientY };
}, { passive: true });

  document.addEventListener('touchmove', (e) => {
  if (isDesktop() || !start) return;
  const t = e.touches[0];
  if (Math.abs(t.clientX - start.x) > 18 || Math.abs(t.clientY - start.y) > 18) {
  const under = e.target.closest('.owl-carousel');
  if (under) { closeAllIn(under); unlockCarousel(under); }
  else { closeAllIn(); document.querySelectorAll('.owl-carousel').forEach(unlockCarousel); }
  start = null;
}
}, { passive: true });

  // safety net: if Owl starts dragging on mobile, close & unlock
  if (window.jQuery && jQuery.fn && jQuery.fn.owlCarousel) {
  jQuery('.owl-carousel').on('drag.owl.carousel', function () {
  if (isDesktop()) return;
  closeAllIn(this);
  unlockCarousel(this);
});
}

  // mobile a11y (keyboard on title)
  document.addEventListener('keydown', (e) => {
  if (isDesktop()) return;
  if (e.key !== 'Enter' && e.key !== ' ') return;
  const header = e.target.closest('.treatments .js-category-toggle');
  if (!header) return;
  e.preventDefault();
  header.click();
});
})();

/* ===== Drop-in: Freeze Treatments carousels on desktop, enable on mobile ===== */
(function () {
  var mq = window.matchMedia('(min-width: 992px)');

  function applyTreatmentsCarouselMode() {
    jQuery('.treatments .owl-carousel').each(function () {
      var el  = this;
      var $c  = jQuery(el);
      var owl = $c.data('owl.carousel');

      // If Owl not initialized yet (rare), try again shortly.
      if (!owl) { setTimeout(applyTreatmentsCarouselMode, 50); return; }

      var isDesktop = mq.matches;

      // Drag settings: OFF on desktop, ON on mobile
      owl.settings.mouseDrag = !isDesktop;
      owl.settings.touchDrag = !isDesktop;
      owl.options.mouseDrag  = !isDesktop;
      owl.options.touchDrag  = !isDesktop;

      // Autoplay: OFF on desktop, mobile uses data-autoplay (default true)
      var mobileAuto = ($c.attr('data-autoplay') ? $c.data('autoplay') : true);
      owl.settings.autoplay = isDesktop ? false : !!mobileAuto;
      owl.options.autoplay  = isDesktop ? false : !!mobileAuto;

      // If you have any lock guards based on dataset.locked, make it explicit
      el.dataset.locked = isDesktop ? '1' : '0';

      try { $c.trigger(isDesktop ? 'stop.owl.autoplay' : (mobileAuto ? 'play.owl.autoplay' : 'stop.owl.autoplay')); } catch (e) {}
      try { $c.trigger('refresh.owl.carousel'); } catch (e) {}
    });
  }

  // Run once now (after DOM ready)…
  jQuery(applyTreatmentsCarouselMode);
  // …and again after full load (in case inits happen late)
  jQuery(window).on('load', applyTreatmentsCarouselMode);

  // Keep in sync with breakpoint changes
  if (mq.addEventListener) mq.addEventListener('change', applyTreatmentsCarouselMode);
  else if (mq.addListener) mq.addListener(applyTreatmentsCarouselMode); // Safari

  // Safety: whenever a mobile tap closes a tray, re-enable drag
  document.addEventListener('click', function (e) {
    if (mq.matches) return; // desktop
    // If you tap outside any open panel, make sure carousels are unlocked + draggable
    if (!e.target.closest('.treatments .js-category-panel.open')) {
      applyTreatmentsCarouselMode();
    }
  }, true);
})();
