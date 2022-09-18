# Notes

## PvP Helpers

- Specbar ✅
- Clan Hide Attack/Cast ✅
- Reorder Prayer

### Specbar

- Needed rs2asm script to expose specbar and clickable
- Simple plugin wrapper for above script callback event

### Clan Hide Attack/Cast

- Ended up filtering menu entry actions by `MenuAction.PLAYER_*` types on `onClientTick` under `swapMenuEntry`
  - Potentially refactor into `deleteMenuEntry`
  - Entirely refactor to separate plugin
- Implements delete function inspired by swap
  - TODO: check array remove performance, could be a heavy array copy
- Did not end up using this [PR](https://github.com/open-osrs/runelite/pull/900), could help make it more efficient
  - Uses `onMenuOpened` event, current implementation does `onClientTick` and a check to see if a menu is open, effectively the same
  - Above makes it like a middleware of sorts, filtering out attack/cast menu entires before setting them client side
  - Looks like there was a hotkey to swap them back with Walk Here?
- Another interesting [PR](https://github.com/open-osrs/runelite/pull/2916/files), client seems to handle the hiding, not sure how
- [Related](https://github.com/open-osrs/runelite/pull/2300/files)

### Reorder Prayer

- Probably will be the hardest, not sure RL natively supports this
  - Doing a deob and recompile will be a lot of work
  - Possible 3pc flag
- Use open as a reference
- Grab existing prayer book positions
- Logic to swap positions, probably need `oldPrayBook` and `newPrayBook` data structures


