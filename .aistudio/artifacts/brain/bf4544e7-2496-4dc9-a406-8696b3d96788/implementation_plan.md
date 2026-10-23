# Which AI — Comprehensive AI Encyclopedia

"Which AI" is a polished, beginner-friendly Android application designed to serve as an exhaustive visual encyclopedia of notable AI models, systems, and breakthrough agents created by humanity. It organizes over 60+ AI systems into 7 distinct categories, fully formatted in the requested emoji-bracket markdown structure, providing clear, jargon-free explanations, practical use cases, pros, cons, details, and official links.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The app will feature an extensive, comprehensive catalog of **60+ notable AI models and systems** spanning history up to the present day across 7 practical categories, fully searchable, filterable by category, bookmarkable as favorites, and equipped with a side-by-side AI Comparison feature and an AI Recommendation Quiz helper.

- **Expanded Catalog Scale**: 60+ fully curated entries covering Image/Art, Text Assistants & Chatbots, Search & Research Engines, Audio/Voice & Music Creators, Video & Motion Generators, Coding & Developer Assistants, and Scientific & Historical Breakthroughs.
- **Visual Aesthetic**: Sleek Modern Tech theme with Indigo/Cyan accents, dark mode support, and clean M3 cards.

---

## 1. Overview & Core Concept

- **What It Does**: Provides a searchable, browsable encyclopedia of major AI models (ChatGPT, Claude, Gemini, Midjourney, Suno, Sora, DeepSeek, Llama, Flux, Perplexity, AlphaFold, etc.) tailored specifically for non-technical users.
- **Target Audience**: Everyday smartphone users who want to know which AI tool to use for specific tasks without wading through technical jargon.
- **Key Value**: Instant clarity on what each AI does best, real-world everyday use cases, pros, cons, and direct links.

---

## 2. User Experience & Visual Design

- **Key User Flows**:
  1. **Home / Explore Screen**: Category pills (All, Image, Writing, Search, Audio, Video, Coding, Science) and a real-time search bar.
  2. **Catalog Card List**: Clean M3 cards displaying the emoji anchor `[ICON] **AI Name**`, 1-sentence plain English description, and quick tags.
  3. **Detail Bottom Sheet / Screen**: Full breakdown including *What it does*, *Best For*, *Pros*, *Cons*, *Details & Creator*, and *Web Link*.
  4. **Compare Mode**: Select any two AI models to compare them side-by-side.
  5. **Recommendation Wizard (Quiz)**: Quick 3-question quiz ("What do you want to create today?") that recommends the best AI tool.
  6. **Bookmarks**: Save favorite AI tools for quick access.
- **Visual Identity & Theme**:
  - *Palette*: Deep slate background (`#0F172A`), rich surface cards (`#1E293B`), vibrant indigo/cyan accents (`#6366F1`, `#06B6D4`).
  - *Typography*: Clean Sans-Serif hierarchy with bold display titles.

---

## 3. Key Product Decisions & Trade-Offs

- **Exhaustive Curated Dataset in-app**: Stored in a robust Kotlin data repository so the app is instantly lightning-fast and works fully offline.
- **External Links**: Direct intent handling to open official landing page URLs in the browser.

---

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────┐
│                      UI Layer                          │
│  (Jetpack Compose: Home, Detail, Compare, Quiz, Bookmarks)│
└───────────────────────────┬────────────────────────────┘
                            │
┌───────────────────────────▼────────────────────────────┐
│                   AIViewModel                          │
│       (StateFlow: search query, selected category,     │
│        favorites, compare list, quiz state)            │
└───────────────────────────┬────────────────────────────┘
                            │
┌───────────────────────────▼────────────────────────────┐
│                    AICatalogRepo                       │
│     (60+ structured models across 7 categories with    │
│      exact markdown formatting and emoji anchors)      │
└────────────────────────────────────────────────────────┘
```

- **Data Models**: `AiModel` data class containing name, emoji, category, whatItDoes, bestFor (list), pros (list), cons (list), detailsAndCreator, and webLink.
- **Persistence**: DataStore for saving user bookmarks locally.
