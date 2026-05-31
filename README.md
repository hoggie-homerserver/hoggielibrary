# Hoggie Library

A comprehensive Minecraft modding framework for **Fabric 1.21.1** (Java 21).

Hoggie Library provides a unified, discoverable API for creating Minecraft mods with professional-grade frameworks for combat, practice servers, Bedwars, RPGs, bots, GUI/HUD, storage, and more.

```
Hoggie.gui.open(screen);
Hoggie.combat.attack(target);
Hoggie.inventory.findSword();
Hoggie.world.placeBlock(pos);
Hoggie.rotation.lookAt(entity);
Hoggie.pathfinder.walkTo(pos);
Hoggie.pvp.cps().getCps();
Hoggie.bedwars.team().createTeam("red", "§c", pos);
Hoggie.rpg.npc().createNpc("villager", "Villager", pos);
```

## Features

- **Central API**: Everything accessible through `Hoggie.*`
- **PvP Framework**: Combat, rotation, targeting, reach, damage, CPS, prediction
- **Practice Framework**: Arenas, duels, queues, matches, spectating, timers, replays, stats
- **Bedwars Framework**: Teams, beds, generators, shops, upgrades, matches
- **RPG Framework**: NPCs, dialogue, quests, skill trees, achievements, stats, economy, classes, perks
- **Bot Framework**: Combat AI, target AI, navigation, pathfinding, behavior trees, decision trees
- **GUI/HUD Framework**: Screen management, HUD elements, themes, animations
- **World Framework**: Structure, region, chunk, biome, schematic utilities
- **Storage Framework**: JSON, SQLite, caching, data migration, backup
- **Developer Tools**: Debug, profiling, auto-registration, dependency injection
- **Utility Systems**: Scheduler, keybind, notification, render, inventory, command, math, color, file

## How It Works

Hoggie Library is a **standalone Fabric mod** (jar) that you install in your `mods/` folder. Your feature mods declare a dependency on it in their `fabric.mod.json`:

```json
"depends": {
    "hoggielibrary": "*"
}
```

Both the library jar and your feature mod jar go into the `mods/` folder at runtime.

### Building the Library

```bash
cd HoggieLibrary
./gradlew build
```

The output jar is at `build/libs/hoggielibrary-1.0.0.jar`. Drop this into your `mods/` folder.

### Creating a mod that uses Hoggie Library

Place `hoggielibrary-1.0.0.jar` in your mod project's `libs/` directory, then in your `build.gradle`:

```gradle
repositories {
    flatDir { dirs "libs" }
    maven { url = "https://maven.fabricmc.net/" }
}

dependencies {
    minecraft "com.mojang:minecraft:1.21.1"
    mappings "net.fabricmc:yarn:1.21.1+build.1:v2"
    modImplementation "net.fabricmc:fabric-loader:0.16.9"
    modImplementation "net.fabricmc.fabric-api:fabric-api:0.102.0+1.21.1"
    modImplementation files("libs/hoggielibrary-1.0.0.jar")
}
```

Your `fabric.mod.json` should declare:

```json
"depends": {
    "fabricloader": ">=0.16.9",
    "fabric-api": "*",
    "hoggielibrary": "*",
    "minecraft": ">=1.21.1"
}
```

### Basic usage

```java
import net.hoggielibrary.api.Hoggie;

public class MyMod implements ModInitializer {
    @Override
    public void onInitialize() {
        Hoggie.notifications.info("Mod enabled");
        Hoggie.config.set("my.setting", true);
        Hoggie.config.save();
    }
}
```

## API Overview

| API | Description |
|-----|-------------|
| `Hoggie.gui` | Screen management |
| `Hoggie.combat` | Entity attacking |
| `Hoggie.inventory` | Item and slot management |
| `Hoggie.player` | Player state and movement |
| `Hoggie.world` | Block and world interaction |
| `Hoggie.render` | 2D/3D rendering utilities |
| `Hoggie.notifications` | In-game messages and alerts |
| `Hoggie.rotation` | Look direction and aiming |
| `Hoggie.pathfinder` | Navigation and movement |
| `Hoggie.bridge` | Block bridging utilities |
| `Hoggie.pvp` | Complete PvP framework |
| `Hoggie.hud` | HUD element management |
| `Hoggie.practice` | Practice server framework |
| `Hoggie.bedwars` | Bedwars game framework |
| `Hoggie.rpg` | RPG game framework |
| `Hoggie.bot` | Bot/AI framework |
| `Hoggie.storage` | Data persistence |
| `Hoggie.developer` | Development tools |
| `Hoggie.scheduler` | Task scheduling |
| `Hoggie.config` | Configuration management |
| `Hoggie.events` | Event bus |

## Commands

- `/hlib reload` - Reload configuration
- `/hlib debug` - Toggle debug mode
- `/hlib profile` - Show profiling information
- `/hlib dump` - Dump framework state
- `/hlib version` - Show version
- `/hlib stats` - Show runtime statistics

## Building

```bash
# Build the library jar
./gradlew build
# Jar is at: build/libs/hoggielibrary-1.0.0.jar

# Install to local Maven (optional, for Maven-based workflows)
./gradlew publishToMavenLocal
```

For the example mods, first build the library, then copy `build/libs/hoggielibrary-1.0.0.jar` into each example's `libs/` directory before building the example.

## License

MIT
