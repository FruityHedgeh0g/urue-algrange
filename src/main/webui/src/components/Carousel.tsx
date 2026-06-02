import React, { useEffect, useMemo, useRef, useState } from "react";
import "./Carousel.css";

export interface CarouselSlide {
  src: string;
  alt: string;
  title?: string;
  caption?: string;
  href?: string;
}

export interface CarouselProps {
  slides: CarouselSlide[];
  autoPlay?: boolean;
  interval?: number; // ms
  pauseOnHover?: boolean;
  showArrows?: boolean;
}

const clampIndex = (idx: number, len: number) => {
  if (len <= 0) return 0;
  const r = idx % len;
  return r < 0 ? r + len : r;
};

export const Carousel: React.FC<CarouselProps> = ({
  slides,
  autoPlay = true,
  interval = 5000,
  pauseOnHover = true,
  showArrows = true,
}) => {
  const [index, setIndex] = useState(0);
  const [paused, setPaused] = useState(false);
  const len = slides.length;
  const trackRef = useRef<HTMLDivElement | null>(null);
  const focusWithin = useRef(false);

  const goTo = (i: number) => setIndex((prev) => clampIndex(i, len));
  const next = () => setIndex((prev) => clampIndex(prev + 1, len));
  const prev = () => setIndex((prev) => clampIndex(prev - 1, len));

  // Auto play effect
  useEffect(() => {
    if (!autoPlay || len <= 1) return;
    if (paused || focusWithin.current) return;
    const id = window.setInterval(() => {
      setIndex((i) => clampIndex(i + 1, len));
    }, Math.max(1800, interval));
    return () => window.clearInterval(id);
  }, [autoPlay, interval, paused, len]);

  const onMouseEnter = () => {
    if (pauseOnHover) setPaused(true);
  };
  const onMouseLeave = () => {
    if (pauseOnHover) setPaused(false);
  };

  // Manage focus pause (for accessibility)
  const onFocusIn = () => {
    focusWithin.current = true;
  };
  const onFocusOut = () => {
    focusWithin.current = false;
  };

  // Keyboard navigation
  const onKeyDown: React.KeyboardEventHandler<HTMLDivElement> = (e) => {
    if (e.key === "ArrowRight") {
      e.preventDefault();
      next();
    } else if (e.key === "ArrowLeft") {
      e.preventDefault();
      prev();
    }
  };

  // Basic touch/swipe support
  const touchStartX = useRef<number | null>(null);
  const onTouchStart: React.TouchEventHandler<HTMLDivElement> = (e) => {
    touchStartX.current = e.changedTouches[0].clientX;
  };
  const onTouchEnd: React.TouchEventHandler<HTMLDivElement> = (e) => {
    const start = touchStartX.current;
    if (start == null) return;
    const dx = e.changedTouches[0].clientX - start;
    const threshold = 40; // px
    if (dx > threshold) {
      prev();
    } else if (dx < -threshold) {
      next();
    }
    touchStartX.current = null;
  };

  const transform = useMemo(() => `translateX(${-index * 100}%)`, [index]);

  return (
    <section
      className="carousel"
      aria-roledescription="carousel"
      aria-label="Carrousel"
      onMouseEnter={onMouseEnter}
      onMouseLeave={onMouseLeave}
      onKeyDown={onKeyDown}
      onFocus={onFocusIn}
      onBlur={onFocusOut}
    >
      <div
        className="carousel-viewport"
        role="group"
        aria-roledescription="slides"
        onTouchStart={onTouchStart}
        onTouchEnd={onTouchEnd}
        tabIndex={0}
      >
        {showArrows && len > 1 && (
          <button
            type="button"
            className="carousel-arrow left"
            aria-label="Précédent"
            onClick={prev}
          >
            ‹
          </button>
        )}

        <div className="carousel-track" ref={trackRef} style={{ transform }}>
          {slides.map((s, i) => {
            const img = (
              <img src={s.src} alt={s.alt} loading={i === index ? "eager" : "lazy"} />
            );
            return (
              <div className="carousel-slide" role="group" aria-roledescription="slide" aria-label={`Slide ${i + 1} sur ${len}`} key={i}>
                {s.href ? (
                  <a href={s.href} aria-label={s.title || s.alt}>
                    {img}
                  </a>
                ) : (
                  img
                )}
                {(s.title || s.caption) && (
                  <div className="carousel-caption" aria-live="polite">
                    {s.title && <h3 style={{ margin: 0 }}>{s.title}</h3>}
                    {s.caption && <p style={{ margin: 0 }}>{s.caption}</p>}
                  </div>
                )}
              </div>
            );
          })}
        </div>

        {showArrows && len > 1 && (
          <button
            type="button"
            className="carousel-arrow right"
            aria-label="Suivant"
            onClick={next}
          >
            ›
          </button>
        )}

        {len > 1 && (
          <div className="carousel-dots" aria-label="Navigation des slides">
            {slides.map((_, i) => (
              <button
                key={i}
                type="button"
                className={`carousel-dot${i === index ? " active" : ""}`}
                aria-label={`Slide ${i + 1}`}
                aria-current={i === index ? true : undefined}
                onClick={() => goTo(i)}
              />
            ))}
          </div>
        )}
      </div>
    </section>
  );
};

export default Carousel;
