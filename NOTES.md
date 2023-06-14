# Notes

## PvP Helpers

- Specbar ✅
- Clan Hide Attack/Cast ✅
- Reorder Prayer ✅
- Plugin Hub ✅

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

- Almost fully working out of the box from previous implementations
- Reorder fails when selecting Quick prayers, reverts prayers to default layout
- However, quick prayer activation correctly selects reordered prayers
- Full implementation depends on `componentTable.getNodes()` requiring `RSNodeHashTable` and `RSNodeHashTableMixin`. Perhaps there is another way to check if we are in Quick prayer book tab

Note: new reorder prayer inside "Prayer" plugin messes up old one, if plugin is flicked on/off. Need to create or copy fresh profile. Downside of new plugin is that you cant turn it on/off, manually have to order prayers each time.

### Plugin Hub

- Hard code manifest URL to get access to plugin hub, one minor version lower than current since current is not uploaded to `https://repo.runelite.net/plugins/`
- Don't really need Plugin hub, but access to already installed plugins
- Maybe can skip manifest check and load installed external plugins, ignoring plugin version updates
