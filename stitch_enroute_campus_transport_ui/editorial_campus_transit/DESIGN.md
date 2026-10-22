---
name: Editorial Campus Transit
colors:
  surface: '#fbf9f3'
  surface-dim: '#dcdad4'
  surface-bright: '#fbf9f3'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f5f3ed'
  surface-container: '#f0eee8'
  surface-container-high: '#eae8e2'
  surface-container-highest: '#e4e2dd'
  on-surface: '#1b1c18'
  on-surface-variant: '#43474c'
  inverse-surface: '#30312d'
  inverse-on-surface: '#f3f1eb'
  outline: '#73777c'
  outline-variant: '#c3c7cc'
  surface-tint: '#4d6171'
  primary: '#021625'
  on-primary: '#ffffff'
  primary-container: '#172b3a'
  on-primary-container: '#7e93a5'
  inverse-primary: '#b4c9dc'
  secondary: '#536254'
  on-secondary: '#ffffff'
  secondary-container: '#d6e7d5'
  on-secondary-container: '#59685a'
  tertiary: '#011912'
  on-tertiary: '#ffffff'
  tertiary-container: '#162e26'
  on-tertiary-container: '#7c968b'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#d0e5f9'
  primary-fixed-dim: '#b4c9dc'
  on-primary-fixed: '#081d2c'
  on-primary-fixed-variant: '#354959'
  secondary-fixed: '#d6e7d5'
  secondary-fixed-dim: '#bbcbba'
  on-secondary-fixed: '#111f14'
  on-secondary-fixed-variant: '#3c4a3d'
  tertiary-fixed: '#cde9dc'
  tertiary-fixed-dim: '#b1cdc0'
  on-tertiary-fixed: '#072018'
  on-tertiary-fixed-variant: '#334c42'
  background: '#fbf9f3'
  on-background: '#1b1c18'
  surface-variant: '#e4e2dd'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 36px
    fontWeight: '700'
    lineHeight: 44px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.02em
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 11px
    fontWeight: '600'
    lineHeight: 14px
    letterSpacing: 0.04em
  headline-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  margin: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style
This design system defines a calm, collegiate, and subtly luxurious mobility companion tailored specifically for university campus transport. Bridging functional utility with a quiet Gen-Z editorial sensibility, it rejects the aggressive urgency and hyper-saturated palette typical of commercial ride-hailing apps in favor of composed clarity, generous negative space, and understated elegance.

The visual direction draws from **Warm Editorial Minimalism**:
- **Atmospheric Warmth:** Anchored on warm ivory paper-tone canvases rather than sterile laboratory whites, evoking high-grade stationery and quiet focus.
- **Editorial Restraint:** High typographic hierarchy with measured line lengths, hairline separators, and understated micro-interactions.
- **Tactile Softness:** Restrained pill shapes, soft ambient shadows, and organic botanic accents (sage, eucalyptus) convey security, balance, and unhurried punctuality.
- **Android-First Elegance:** Adheres to modern Material motion guidelines while replacing standard heavy elevation tiers with quiet tonal separation and refined tactile depth.

## Colors
The palette balances the deep gravitas of academic institutions with organic, soothing greens to relieve commuter friction.

- **Primary (`#172B3A` - Deep Ink Navy):** The structural core. Used for brand headers, primary call-to-actions, prominent icons, and core route paths on map canvases.
- **Secondary (`#9AAA9A` - Muted Sage):** Used for non-urgent status markers, passive toggle states, card outlines, and supportive metadata.
- **Tertiary / Accent (`#718B80` - Soft Eucalyptus):** Drives high-context feedback, active live-shuttle location pulses, and key transit milestones.
- **Neutral & Surfaces:**
  - `Canvas Background`: Warm Ivory (`#F7F5EF`) creates a natural, readable backdrop.
  - `Surface Elevated`: Soft White (`#FFFFFF`) isolates cards, modular bottom sheets, and route cards from the ivory ground.
  - `Text Secondary / Supportive`: Cool Grey (`#7C8585`) ensures legible secondary hierarchy without visual harshness.
- **Semantic Feedback:**
  - `Success`: Muted Green (`#6F927C`) for confirmed boarding, timely arrivals, and verified passes.
  - `Warning`: Soft Amber (`#C6A56A`) for route delays, heavy occupancy, and service updates.
  - `Error`: Muted Terracotta (`#B9786F`) for missed shuttles, invalid tokens, and cancellations.

## Typography
Plus Jakarta Sans was chosen for its geometric precision softened by humanist, contemporary proportions. It delivers effortless legibility on small Android viewports and live map cards without looking generic or sterile.

- **Display & Headlines:** Used for arrival count-downs, stop names, and primary screen titles. Set with tighter letter spacing (`-0.01em` to `-0.02em`) to deliver an editorial stance.
- **Body:** Kept at clean, readable line-heights (1.4–1.5x) to facilitate scanning schedule tables and driver notices in direct sunlight.
- **Labels & Micro-copy:** Labels leverage slight tracking expansion (`0.01em` to `0.04em`) to establish authoritative, easily scannable cues for ETAs, seat availability, and transit status.

## Layout & Spacing
The layout follows a disciplined 8-point baseline system constructed around dynamic mobile viewports.

- **Grid Model:** Mobile canvases utilize a flexible 4-column fluid layout with 16px (`1rem`) outer margins and gutters. On tablet and dashboard widths, this scales to an 8-column layout with 24px margins.
- **Rhythm & Breathing Space:** Generous vertical padding insulates transit cards, ensuring interactive targets never feel cramped during one-handed operation.
- **Content Blocks:** Transit schedules, shuttle selector carousels, and driver badges follow standardized outer containment with `space-md` (16px) internal padding, preserving a consistent visual tempo across fragmented Android screen sizes.

## Elevation & Depth
This design system avoids dark, heavy skeuomorphic drops. Instead, depth is achieved through **ambient tinted illumination** paired with **hairline separation**:

- **Hairline Dividers:** 1px borders colored in `rgba(23, 43, 58, 0.08)` replace abrupt container boundaries, maintaining an airy, editorial feel.
- **Ambient Floor Shadows:**
  - *Base Surface (Card Elevation):* `0px 4px 16px -2px rgba(23, 43, 58, 0.04)` creates light detachment from the Warm Ivory backdrop.
  - *Floating Elements (Bottom Sheets & Map FABs):* `0px 12px 32px -4px rgba(23, 43, 58, 0.08)`, offering clear spatial priority when overlaying live interactive maps.
- **Backdrop Blur:** Modal panels and sticky navigation bars feature subtle frosted glass: `rgba(247, 245, 239, 0.85)` with `12px` Gaussian blur, letting campus cartography peek through naturally.

## Shapes
A balanced medium roundedness (`roundedness: 2`) underpins this design system. Standard containers use `0.5rem` (8px), route cards and modals use `1rem` (16px), and interactive floating sheets use `1.5rem` (24px).

Pill forms (`rounded-full` / 9999px) are strictly reserved for:
- Operational badges (e.g., "ON TIME", "FEW SEATS").
- Primary transit action buttons.
- Search pills and campus filter chips.
This deliberate hierarchy distinguishes interactive items from passive structural containers.

## Components

- **Buttons:**
  - *Primary:* Filled with Deep Ink Navy (`#172B3A`), white label text, fully pill-shaped (height: 52px for mobile hit targets), accompanied by subtle inset tap micro-scaling.
  - *Secondary / Ghost:* Warm Ivory background with 1px hairline border in `#9AAA9A`, text in Deep Ink Navy.
- **Status & Info Chips:**
  - Minimal pill badges with 4px vertical, 12px horizontal padding.
  - Neutral badge uses Warm Ivory ground with Cool Grey text; dynamic badges utilize muted status tints with soft tone fills (e.g., Muted Green at 12% opacity with solid `#6F927C` text).
- **Cards & Route Panels:**
  - Rendered in pure Soft White (`#FFFFFF`) against Warm Ivory.
  - Outlined with 1px border (`rgba(23, 43, 58, 0.06)`), 16px border-radius, and padded with `space-md`.
  - Transit timeline elements within cards feature a 2px vertical guide in Muted Sage with hollow and filled circle nodes for departure and arrival.
- **Form Inputs & Location Selectors:**
  - 48px height, rounded-xl (12px), background `#FFFFFF`, border 1px `rgba(23, 43, 58, 0.12)`.
  - Focus state shifts border cleanly to Soft Eucalyptus (`#718B80`) without harsh glow rings.
- **Lists & Hairline Dividers:**
  - Edge-to-edge subtle dividers (`1px solid rgba(23, 43, 58, 0.06)`). Schedule rows feature leading circular bus icons, bold departure times, and secondary text for stop frequency.
- **Digital Campus Transit Pass:**
  - Distinct component mimicking a minimalist boarding slip with scalloped punch-out notches, QR code container, and live timestamp ticker in Soft Eucalyptus.