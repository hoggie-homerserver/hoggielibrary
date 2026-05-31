# Changelog

## [1.0.0] - 2026-05-30

### Added
- Initial release of Hoggie Library
- Central `Hoggie` API class with discoverable subsystems
- PvP Framework: CombatUtils, RotationManager, TargetManager, ReachCalculations, DamageCalculations, PredictionUtilities, CpsTracker, AttackCooldownUtilities, WeaponUtilities
- Practice Framework: ArenaAPI, DuelAPI, QueueAPI, MatchAPI, SpectatorAPI, CheckpointAPI, TimerAPI, ReplayAPI, StatsAPI
- Bedwars Framework: TeamAPI, BedAPI, GeneratorAPI, ShopAPI, UpgradeAPI, MatchAPI
- RPG Framework: NpcAPI, DialogueAPI, QuestAPI, SkillTreeAPI, AchievementAPI, RpgStatsAPI, EconomyAPI, ClassAPI, PerkAPI
- Bot Framework: CombatAI, TargetAI, NavigationAI, PathfindingAPI, BehaviorTree, DecisionTree
- GUI/HUD Framework: GuiAPI, HudAPI, HudElement interface
- World Framework: StructureAPI, RegionAPI, ChunkAPI, BiomeAPI, SchematicAPI
- Storage Framework: JsonStorage, SqliteStorage, CacheSystem, DataMigration, BackupSystem
- Developer Framework: DebugTools, ProfilingTools, AutoRegister, DependencyInjection
- Core Systems: HoggieEventBus, HoggieConfig, HoggieScheduler, HoggieLogger, HoggieKeybindManager
- Theme System with dark/light themes
- Animation System with easing functions
- Utility classes for math, color, and file operations
- Built-in commands (/hlib reload, debug, profile, dump, version, stats)
- Service registry for dependency injection
- Network API for custom packets
- Built-in event types
- Javadoc documentation on all public methods
- Maven publishing configuration (local + GitHub Packages)
- Example mods: PvP, Practice, RPG, GUI, Bedwars
- sourcesJar and javadocsJar generation
