# AGENTS.md - Hoggie Library

## Overview
Fabric mod for Minecraft 1.21.11 providing a framework/library API for PvP, rendering, networking, storage, and utilities. Used as a runtime dependency by feature mods.

## Project Status
- **BUILD SUCCESSFUL** as of 31 May 2026
- `.\gradlew build` produces `build/libs/hoggielibrary-1.0.0.jar` (227 KB)
- The jar can be copied to feature mod `libs/` directories for compile-time dependency
- Feature mods declare `"hoggielibrary": "*"` in `fabric.mod.json` depends and use `modImplementation files("libs/hoggielibrary-1.0.0.jar")`

## Build Configuration
- Minecraft 1.21.11 (Mounts of Mayhem)
- Fabric Loom 1.14.10
- Gradle 9.2.1 (wrapper upgraded from 8.11)
- Yarn mappings: 1.21.11+build.6
- Fabric API: 0.140.2+1.21.11
- Java 21 (Adoptium JDK 21.0.10.7-hotspot)

## Key Architecture Decisions

### Networking (HoggieNetwork.java)
- Uses `CustomPayload.Id<T>` directly instead of `CustomPayload.Type<B,T>` to avoid Java wildcard capture issues
- `PacketType` record stores `CustomPayload.Id<T>` and `PacketCodec<? super RegistryByteBuf, T>` separately
- Registration uses `PayloadTypeRegistry.playC2S()`/`.playS2C()` from Fabric API

### Rendering (RenderAPI.java)
- `BufferBuilder` and `Tessellator` classes were **completely removed** in 1.21.11
- Blaze3D rendering pipeline replaced with GPU-oriented API (GpuDevice, RenderPass, CommandEncoder)
- Drawing methods (`drawRect`, `drawLine`) use LWJGL `GL11` immediate mode directly
- Text rendering uses `TextRenderer.draw()` with `VertexConsumerProvider.Immediate` via `client.getBufferBuilders()`

### Registry Access
- `DynamicRegistryManager.get(RegistryKey)` removed in 1.21.11
- All registry lookups for enchantments (Sharpness) and attributes (attack speed) replaced with fallback hardcoded values
- Sharpness damage bonus: returns 0 (functionality preserved for non-library code; this is a library proxy)
- Attack speed calculation: uses `ClientPlayerEntity.getAttackCooldownProgressPerTick()` directly

### Weapon Detection
- `SwordItem` and `AxeItem.getMaterial().getAttackDamage()` removed in 1.21.11
- Sword detection uses hardcoded `Set<Item>` of vanilla swords
- Axe base damage uses hardcoded `Map<Item, Double>` with values from weapon components
- Only vanilla items detected; modded weapons not automatically recognized

### Permissions
- `CommandHoggie` bypasses permission check with `.requires(source -> true)`
- Proper permission API not yet researched

### Access Widener
- `hoggielibrary.accesswidener` file emptied (all entries removed)
- Fields like `yaw`/`pitch` on entities no longer exist in 1.21.11
- All code uses public getter methods instead of direct field access

### Input API (1.21.11 Changes)
- `Screen.mouseClicked(double, double, int)` → `Screen.mouseClicked(Click, boolean)`
- `Screen.mouseReleased(double, double, int)` → `Screen.mouseReleased(Click)`
- `Screen.mouseDragged(double, double, int, double, double)` → `Screen.mouseDragged(Click, double, double)`
- `Screen.keyPressed(int, int, int)` → `Screen.keyPressed(KeyInput)`
- `Screen.charTyped(char, int)` → `Screen.charTyped(CharInput)`
- `Click` record: `click.x()`, `click.y()`, `click.button()`, `click.modifiers()` — from `net.minecraft.client.gui.Click`
- `KeyInput` record: `keyInput.key()`, `keyInput.scancode()`, `keyInput.modifiers()` — from `net.minecraft.client.input.KeyInput`
- `CharInput` record: `charInput.codepoint()`, `charInput.modifiers()` — from `net.minecraft.client.input.CharInput`
- HoggieWidget uses old parameter format internally (not extending Element), HoggieScreen extracts values from records and passes them to widgets

### GUI Framework
- Fully custom widget system (HoggieWidget base, Panel container, Button, Label, Slider, TextField, Toggle, Dropdown, ScrollPanel, ColorPicker)
- Widgets DO NOT extend Minecraft's Element — they have their own simple method signatures
- `HoggieScreen` extends Screen, uses `children` list for custom widgets (not `addDrawableChild`)
- `GuiScreenRegistry` maps `Identifier` to `Supplier<HoggieScreen>` — call `GuiScreenRegistry.register(id, () -> new MyScreen())`
- `GuiCommand` registers `/hoggie gui <screen>` client-side command to open any registered screen
- `HoggieClientCommandRegistry` uses `ClientCommandRegistrationCallback.EVENT` with `FabricClientCommandSource`
- Language file at `assets/hoggielibrary/lang/en_us.json` — keybind categories use `"key.categories.hoggielibrary.<path>": "Display Name"`
- Keybind category identifiers: use `Identifier.of("hoggielibrary", "general")` format, NOT raw strings

## Publishing

### Local Maven (same PC, no network needed)
```powershell
# In library project — publish jar to ~/.m2/repository/
.\gradlew.bat publishToMavenLocal
```
Then in any feature mod's `build.gradle`:
```groovy
repositories {
    mavenLocal()
    maven { url = "https://maven.fabricmc.net/" }
    mavenCentral()
}
dependencies {
    modImplementation "net.hoggielibrary:hoggielibrary:1.0.0"
}
```

### GitHub Packages (team/CI, no local file needed)
1. Create a GitHub token with `read:packages` scope at https://github.com/settings/tokens
2. Set env vars on each machine:
   ```powershell
   $env:GITHUB_ACTOR = "your-github-username"
   $env:GITHUB_TOKEN = "ghp_your_token_here"
   ```
3. Publish once:
   ```powershell
   .\gradlew.bat publishMavenJavaPublicationToGitHubPackagesRepository
   ```
4. Then any feature mod (any PC) just needs:
   ```groovy
   repositories {
       maven { url = "https://maven.pkg.github.com/hoggie/hoggielibrary" }
       maven { url = "https://maven.fabricmc.net/" }
       mavenCentral()
   }
   dependencies {
       modImplementation "net.hoggielibrary:hoggielibrary:1.0.0"
   }
   ```
   With `GITHUB_ACTOR` + `GITHUB_TOKEN` env vars set at build time (or in CI secrets).

### Combined publish
```powershell
.\gradlew.bat publishAll   # mavenLocal + GitHub Packages
```

## Commands
- Build: `.\gradlew.bat build`
- Build classes only: `.\gradlew.bat classes`
- Publish to local Maven: `.\gradlew.bat publishToMavenLocal`
- Publish to GitHub Packages: `.\gradlew.bat publishMavenJavaPublicationToGitHubPackagesRepository`
- Publish both: `.\gradlew.bat publishAll`
- Run client: not configured (no run directory generated)
- Clean: `.\gradlew.bat clean`

## File Count
- 144 source files in 20 API subsystem packages (added 8 GUI widget components + GuiScreenRegistry + GuiCommand + HoggieClientCommandRegistry + en_us.json)
- Build output: hoggielibrary-1.0.0.jar, sources jar, javadoc jar
- Testmod: `C:\Users\hoggie\testmod\` — standalone project consuming library via mavenLocal
