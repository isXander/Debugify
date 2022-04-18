<img width="200" src="https://dl.isxander.dev/logos/debugify/v2/debugify-512x.png" alt="Debugify Icon" align="right">
<div align="left">
<h1>Debugify</h1>
<h3>Debugify is a project that fixes <b>over 60</b> bugs found on the <a href="https://bugs.mojang.com/projects/MC/issues">bug tracker</a> in Minecraft.
(and does nothing more!)</h3>
</div>

[![wakatime](https://wakatime.com/badge/github/W-OVERFLOW/Debugify.svg?style=for-the-badge)](https://wakatime.com/badge/github/W-OVERFLOW/Debugify)
![Lines of Code](https://img.shields.io/tokei/lines/github/W-OVERFLOW/Debugify?color=%23ff4747&label=Lines%20of%20code&style=for-the-badge)

## What does this mod replace?
This mod replaces many mods and implements fixes from some others

- **[FastOpenLinksAndFolders](https://www.curseforge.com/minecraft/mc-mods/fastopenlinksandfolders)**: Replaced
- **[Shift-Scroll Fix](https://www.curseforge.com/minecraft/mc-mods/shift-scroll-fix)**: Replaced
- **[ForgetMeChunk](https://www.curseforge.com/minecraft/mc-mods/forgetmechunk)**: Replaced
- **[ChunkSavingFix](https://www.curseforge.com/minecraft/mc-mods/chunk-saving-fix)**: Replaced
- **[force-close-world-loading-screen](https://modrinth.com/mod/forcecloseworldloadingscreen)**: Replaced
- **[TieFix](https://www.curseforge.com/minecraft/mc-mods/tiefix)**: Semi-replaced - some fixes have been implemented

All of these mods have helped us create Debugify. We thank you very much for your effort! We made sure to follow the license of every one of these mods when creating our own version.

## Links and other info
[GitHub](https://github.com/W-OVERFLOW/Debugify) • [Curseforge](https://curseforge.com/minecraft/mc-mods/debugify) • [Patched bugs (GitHub List)](https://github.com/W-OVERFLOW/Debugify/blob/1.18/PATCHED.md) • [Discord (W-OVERFLOW)](https://woverflow.cc/discord)

## What if I want to enable some bug fixes, but not others?
Debugify has a configuration GUI accessible by Fabric's [Mod Menu](https://modrinth.com/mod/modmenu) or Forge's Built-in mod list.
If you don't want to use either of them, there is always the configuration file located at
`.minecraft/config/debugify.json`
![configuration menu](https://i.imgur.com/0hv9cvu.png)

## Client, or Server?
Debugify includes many fixes for both the client and server (all server fixes also apply to client).
So you should definitely use it on both.

## Can I include this in my modpack?
Yes! Of course! We even hid a little feature in the mod for you! The constant updates may be exhausting to maintain,
so we added a config option that defaults new bug fixes to off, until you get round to looking at it.

```json5
{
  // ...
  "default_disabled": true
}
```

## Credits
- [**isXander**](https://github.com/isXander) - Founder of project
- [**W-OVERFLOW**](https://github.com/orgs/W-OVERFLOW/people) - Backing organization
- [**Contributors**](https://github.com/W-OVERFLOW/Debugify/graphs/contributors) - For PRing new fixes!
- [**MoonTidez**](https://github.com/MoonTidez) - Creating an awesome logo!
