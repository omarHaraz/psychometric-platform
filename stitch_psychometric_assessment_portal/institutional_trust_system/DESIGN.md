---
name: Institutional Trust System
colors:
  surface: '#faf8ff'
  surface-dim: '#d2d9f4'
  surface-bright: '#faf8ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f3ff'
  surface-container: '#eaedff'
  surface-container-high: '#e2e7ff'
  surface-container-highest: '#dae2fd'
  on-surface: '#131b2e'
  on-surface-variant: '#3d4947'
  inverse-surface: '#283044'
  inverse-on-surface: '#eef0ff'
  outline: '#6d7a77'
  outline-variant: '#bcc9c6'
  surface-tint: '#006a61'
  primary: '#00685f'
  on-primary: '#ffffff'
  primary-container: '#008378'
  on-primary-container: '#f4fffc'
  inverse-primary: '#6bd8cb'
  secondary: '#515f74'
  on-secondary: '#ffffff'
  secondary-container: '#d5e3fd'
  on-secondary-container: '#57657b'
  tertiary: '#585d60'
  on-tertiary: '#ffffff'
  tertiary-container: '#707579'
  on-tertiary-container: '#fbfcff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#89f5e7'
  primary-fixed-dim: '#6bd8cb'
  on-primary-fixed: '#00201d'
  on-primary-fixed-variant: '#005049'
  secondary-fixed: '#d5e3fd'
  secondary-fixed-dim: '#b9c7e0'
  on-secondary-fixed: '#0d1c2f'
  on-secondary-fixed-variant: '#3a485c'
  tertiary-fixed: '#dfe3e7'
  tertiary-fixed-dim: '#c3c7cb'
  on-tertiary-fixed: '#171c1f'
  on-tertiary-fixed-variant: '#43474b'
  background: '#faf8ff'
  on-background: '#131b2e'
  surface-variant: '#dae2fd'
typography:
  headline-lg:
    fontFamily: Inter
    fontSize: 30px
    fontWeight: '600'
    lineHeight: 38px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  body-lg:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.01em
  headline-lg-mobile:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  base: 4px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 40px
  container-max: 1024px
  gutter: 24px
---

## Brand & Style

The design system is engineered for high-stakes psychometric environments where clarity and emotional neutrality are paramount. The visual identity follows a **Safe** directive, prioritizing institutional trust, professional rigor, and the reduction of test-taker anxiety. 

The aesthetic leans into **Corporate Modernism**, utilizing a structured layout and a restrained palette to minimize cognitive load. Every element is designed to feel secure and authoritative, ensuring that the platform recedes into the background so the user can focus entirely on the assessment content. The design avoids all forms of gamification, favoring a serious, scholarly atmosphere that treats the user's data and performance with the highest level of respect.

## Colors

The palette is rooted in a deep **Slate Navy** (`#0F172A`) for core text and structural elements, establishing a foundation of stability. The primary action color is a **Calm Teal** (`#0D9488`), chosen for its association with precision and balance rather than the urgency of a standard blue.

- **Primary:** Calm Teal for progress indicators, primary buttons, and active states.
- **Secondary:** Slate Grey for secondary actions and subtle iconography.
- **Surfaces:** A hierarchy of Soft Blue-Greys (`#F8FAFC` to `#F1F5F9`) provides a clean, low-contrast background for assessment modules.
- **Urgency:** Muted amber and crimson are used strictly for countdown timers and critical alerts to signal importance without inducing panic.

## Typography

This design system utilizes **Inter** across all levels to leverage its exceptional legibility and neutral, systematic character. The typographic scale is optimized for long-form reading and rapid comprehension of complex questions.

RTL support is native to the implementation; line heights are slightly increased to accommodate Arabic script descenders and accents without crowding. Text alignment defaults to "Start," ensuring seamless mirroring between LTR and RTL locales. Headline weights are kept at Semi-Bold (600) to maintain authority without appearing aggressive.

## Layout & Spacing

The layout philosophy is centered on a **Fixed, Centered Grid** for assessment content to prevent eye strain on wide monitors. The maximum container width is capped at 1024px to maintain an optimal line length for readability.

- **RTL-First Logic:** All horizontal layouts use logical properties (padding-inline, margin-inline). The grid columns flip order automatically for Arabic support.
- **Rhythm:** An 8px linear scale governs all spatial relationships. 
- **Desktop:** 12-column grid with 24px gutters and 40px minimum side margins.
- **Mobile:** Single-column layout with 16px side margins. Progress trackers and segmented buttons may transition to vertical stacks if horizontal space is insufficient.

## Elevation & Depth

This design system uses a **Tonal Layering** approach instead of heavy shadows to maintain a flat, professional profile. Depth is communicated through subtle shifts in surface color.

- **Level 0 (Base):** White (`#FFFFFF`) for the primary content card.
- **Level 1 (Sub-surface):** Soft Blue-Grey (`#F8FAFC`) for page backgrounds.
- **Level 2 (Interactive):** Very thin, 1px borders in Slate-200 (`#E2E8F0`) define card boundaries.
- **Shadows:** Reserved exclusively for active drag-and-drop states. When used, shadows are ultra-diffused: `0 4px 12px rgba(15, 23, 42, 0.08)`.

## Shapes

The shape language is **Soft** and conservative. A base radius of 0.25rem (4px) is applied to all interactive elements like buttons and input fields, providing a modern feel without the playfulness of hyper-rounded corners.

- **Standard Elements:** 4px (rounded).
- **Cards & Containers:** 8px (rounded-lg).
- **Progress Bars:** 4px (rounded) to maintain a crisp, linear look.
- **Likert Segments:** Outer containers use 4px, while internal segments remain sharp to emphasize the continuity of the scale.

## Components

### Progress Trackers
Horizontal 4-step indicators. Completed steps use the Calm Teal fill with a check icon; the current step uses a Teal outline; future steps use a Slate-200 outline. Labels are placed below the indicators.

### Countdown Timers
A minimalist digital readout. 
- **Standard:** Slate Navy text.
- **Urgent (< 5 mins):** Amber text with a subtle "Time Remaining" label.
- **Critical (< 1 min):** Crimson text. No blinking or movement; just color change to maintain seriousness.

### Likert Scale & Radio Cards
- **Likert Scale:** Segmented buttons arranged horizontally. Active segments use a Solid Teal fill with white text.
- **Radio Cards:** For multiple-choice questions, use large-surface cards with 1px borders. Upon selection, the border thickens to 2px Teal and a subtle Teal tint is applied to the background.

### Drag-and-Drop Handles
Used for ranking tasks. Handles are represented by a 6-dot icon pattern (`⋮⋮`). During the "active" drag state, the card gains a level 2 shadow and a 2px Teal border.

### Warning & Alert Blocks
Solid, thick left-border (right-border in RTL) callouts. Backgrounds are very pale tints of the status color. Icons are required for accessibility. The tone of the copy must remain objective and instructional.