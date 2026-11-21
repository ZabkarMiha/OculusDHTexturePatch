# About
Since Distant Horizons introduced texture support to LODs, the mod that runs the shaders, so Iris, had to be updated to support those textured LODs, but since Iris does not exist on 1.20.1 Forge, you have to resort
to Oculus. Oculus has long ago stopped support and development, so I decided to throw together this patch for Oculus, that enables textured LODs.

## Installation
If downloading through CurseForge, just drop the downloaded .jar file into your minecraft `mods` folder

If downloading the source code, the only thing needed is to open your terminal of choice in the root of the source code folder location, then run `./gradlew build`.
The .jar file should be located in `build/libs`

## Credits
All credits go to the developers of Distant Horizons and Iris, and the developers who back ported Iris (Oculus) to Forge
- https://www.curseforge.com/minecraft/mc-mods/distant-horizons
- https://www.curseforge.com/minecraft/mc-mods/irisshaders
- https://www.curseforge.com/minecraft/mc-mods/oculus

## Requirements
- Minecraft 1.20.1
- Forge 47.x.x
- Oculus 1.8.0
- Distant Horizons >= 3.3.0
- A shader pack that supports Distant Horizons LOD textures

**NOTE: This is not a separate release of any of the mentioned mods, it's just a patch through mixins that stands as it's own mod**

**NOTE2: AI has been used to bring the actual mixins together, but the code has been studied and reviewed by me**
