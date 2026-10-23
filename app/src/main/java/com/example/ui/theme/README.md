The application uses a single Material 3 theme above the splash screen and application scaffold. Appearance mode and color palette are independent, locally saved preferences. The existing `theme` preference key is retained; missing palettes default to Forest. Dynamic wallpaper colors are intentionally unavailable.

| Palette | Primary actions | Secondary accents | Tertiary accents / neutral family |
| --- | --- | --- | --- |
| Forest | Gold | Burgundy | Forest green |
| Ocean | Navy / accessible pale blue in dark mode | Teal | Coral / navy neutrals |
| Royal | Deep purple / accessible lavender in dark mode | Lavender | Gold / purple neutrals |
| Earth | Brown / accessible warm tan in dark mode | Sage | Terracotta / brown neutrals |

`Color.kt` defines all eight schemes, including foreground/container pairs, surface elevation roles, outlines, inverse roles, and separate Material error reds. Components consume these semantic roles. Selected tabs, filters, radio buttons, bookmarks, and primary actions use `primary`; their filled foregrounds use `onPrimary`. The supplied Which AI brand image retains its original artwork on the splash screen. The splash uses a white background and the selected palette's light text colors for readability; the home header contains text only.

Android 12+ launch themes use a white background and dark system-bar icons to match the white splash screen, regardless of the saved appearance preferences. Stable theme names are retained for platform persistence. System bars are transparent over themed surfaces with matching light/dark icon contrast; older navigation bars use the palette's inverse surface where dark navigation icons are unavailable.

Tests cover text contrast of at least 4.5:1, enabled outline contrast of at least 3:1, preference migration/restoration, all eight UI combinations, and System mode across day/night recreation. Disabled Material controls retain standard disabled opacity.
