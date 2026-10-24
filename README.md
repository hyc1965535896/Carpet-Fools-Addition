# Carpet Fools Addition

![Carpet Fools Addition](logo.png)

**Carpet Fools Addition**（mod id：`funnyport`）——把 Minecraft **历代愚人节快照**的内容移植到**常规版本**的 Fabric Carpet 扩展。

**一次构建，全版本产出**：基于 Stonecutter 的多版本工程，一个源码树覆盖 **MC 1.16.5 → 26.3 共 21 个版本**（Fabric Carpet 官方发布过扩展 API 的全部版本线，含新日历版本线 26.x）。

## 版本矩阵（v0.2.0 全部构建通过）

| MC 版本 | Carpet | 产物 |
| --- | --- | --- |
| 1.16.5 | 1.16.5-1.4.44 | `funnyport-0.2.0+1.16.5.jar` |
| 1.17.1 | 1.17.1-1.4.57 | `funnyport-0.2.0+1.17.1.jar` |
| 1.18.2 | 1.18.2-1.4.69 | `funnyport-0.2.0+1.18.2.jar` |
| 1.19.2 / 1.19.4 | 1.4.84 / 1.4.101 | 同格式 |
| 1.20 / 1.20.2 / 1.20.3 / 1.20.6 | 1.4.112 / 1.4.119 / 1.4.128 / 1.4.141 | 同格式 |
| 1.21 / 1.21.2 / 1.21.4 / 1.21.5 / 1.21.6 / 1.21.7 / 1.21.9 / 1.21.10 / 1.21.11 | 1.4.147 → 1.4.194 | 同格式 |
| 26.1 / 26.2 / 26.3 | 26.1+v260401 / 26.2+v260616 / 26.3+v261008 | 同格式 |

> 注意：Fabric Carpet 官方在 1.20.x 只发过 1.20 / 1.20.2 / 1.20.3 / 1.20.6（1.20.1 和 1.20.4 没有），所以矩阵里没有这两个版本。
> MC 1.20.5 起 carpet 改用 Mojang 官方映射；本项目**全矩阵统一 mojmap**，一套源码名，版本差异全部用 Stonecutter 条件分支消化。
> 26.1+ 接线：Mojang 停发官方映射（MC 不再混淆，名字即官方名），构建走 `loom-back-compat` 插件（老节点经典 loom，26.x 节点 loom-remap 变体），Java 25 工具链。26.3 差异：`Level.random` 转 protected（用 `getRandom()`）、`EntityType` 静态常量取消（走 `BuiltInRegistries`）、`swing` 换 `SwingAnimation` 三参签名。

## 已移植内容（10 条规则）

| 来源（愚人节版本） | 内容 | 规则 | 默认 |
| --- | --- | --- | --- |
| 20w14∞（2020） | 8 个内置搞笑维度 `boxes` / `creeper` / `void` / `islands` / `floodgate` / `unicorns` / `dark` / `ocean`，jar 内置数据包注册，开服即存在 | `funnyWarp` | 开 |
| 20w14∞（2020） | 书本传送门：书与笔任意一页写下维度名，手持右键直接传送（不弹编辑界面） | `funnyBookPortals` | 开 |
| 22w13oneBlockAtATime（2022） | 抛掷方块：潜行 + 右键把手中的方块整个抛出去 | `throwableBlocks` | 关 |
| 22w13oneBlockAtATime（2022） | 捡方块：空手潜行左键，把目标方块整个拿进手里（基岩等挖不动的除外） | `blockGrabbing` | 关 |
| Minecraft 2.0（2013） | 万物可喂：任意非空物品都能当繁殖食物喂动物 | `feedingFrenzy` | 关 |
| Minecraft 2.0（2013） | 红石虫：放红石粉有 15% 概率刷出一条叫"红石虫"的银鱼虫 | `redstoneBugs` | 关 |
| Minecraft 2.0（2013） | 钻石鸡：名叫 `diamond_chicken` 的鸡下钻石不下蛋 | `diamondChickens` | 关 |
| 15w14a 爱与抱抱（2015） | 怪物不再把玩家设为目标 | `loveAndHugs` | 关 |
| 23w13a_or_b 投票更新（2023） | `/funnyvote ask <题目> <选项...>` 聊天栏按钮投票，`cast` 投票，`end` 开票 | `funnyVote` | 开 |
| 24w14potato（2024） | 土豆天罚：吃下毒土豆触发 7 选 1 随机效果（飘浮/剧毒/黑暗/掉土豆…） | `potatoOfDoom` | 关 |

## 用法

- **维度**：`/funnywarp <名字>`（OP 2，1.21.11 起对应 MODERATOR 权限）、`/funnywarp list`，或直接 `/execute in funnyport:boxes run tp @s ~ ~ ~`
- **书本传送门**：书与笔整页写 `creeper` 等维度名（自动去格式化、忽略大小写），手持右键
- **投票**：`/funnyvote ask "谁最可爱" "豹猫" "河豚"` → 全服点按钮或 `/funnyvote cast 1` → `/funnyvote end` 开票
- **规则**：`/funnyport set <规则> <值>`、`/funnyport list`（扩展自己的规则命名空间，描述带中英文）

## 构建

需要 JDK 17+（JDK 25 实测可用；Gradle 通过 foojay 自动补齐各版本工具链）：

```bash
./gradlew build        # 全部 18 个版本（Windows: gradlew.bat build）
./gradlew :1.20.6:build   # 只构建某一个版本
```

产物在 `versions/<版本>/build/libs/funnyport-<MC版本>+0.2.7.jar`（命名规则：MC 版本在前，mod 版本固定追加在后；文件名里的 funnyport 是 mod id，显示名是 Carpet Fools Addition）。
首次构建会拉 18 个 MC + 映射 + 依赖（几个 GB），之后增量极快。内存不足给 `org.gradle.jvmargs` 加到 `-Xmx3G` 以上。

## 工程结构

```
├── settings.gradle.kts        # Stonecutter：18 个版本节点
├── stonecutter.gradle.kts     # 控制器：active 版本 + buildAll 聚合任务
├── build.gradle.kts           # 每节点执行：loom + mojmap + 按版本依赖号 + fabric.mod.json 模板展开
├── versions/<MC>/gradle.properties   # 每版本的 carpet / fabric-api 版本号
└── src/main/…                 # 共享源码：//? if 条件分支处理全部版本差异
```

版本差异分支的依据全部来自对 18 个 named jar 的 javap 实测，主要分界：

- 文本：`Component.literal` 从 1.19.2 起（更早用 `TextComponent`）；`sendSystemMessage` / `broadcastSystemMessage` 从 1.19.2 起；`sendSuccess(Supplier)` 从 1.20 起
- 映射：`ResourceLocation` 公开构造器用到 1.20.6，1.21-1.21.10 用 `fromNamespaceAndPath`，1.21.11 改名 `Identifier`
- NBT→组件：书本页 <1.20.5 走 NBT，≥1.20.5 走 `WritableBookContent`
- 返回值统一：1.21.2 起 `Item#use` 系全部 `InteractionResult`（含 fabric `UseItemCallback`）
- 传送：1.21.2 起 `teleportTo` 带 `Set<Relative>`
- 权限：1.21.11 起 `hasPermission(int)` 换成 `PermissionSet`
- 包移动：1.20.5 `ChunkStatus`；1.21.11 鸡类进 `animal.chicken`
- 事件：fabric `UseBlockCallback` / `AttackBlockCallback` 全版本 `InteractionResult`；`UseItemCallback` 1.21.2 起换 `InteractionResult`
- 效果：1.21.5 起 `DAMAGE_BOOST` 改名 `STRENGTH`
- 1.16.5 特殊：Java 8 目标（源码全树 Java 8 兼容）、NBT 类型常量用数字字面量、日志用 log4j（无 slf4j）

## 已知边界（诚实声明）

- 20w14∞ 原版约 21 亿个程序生成主题维度，这里只内置 8 个标志性维度（`creeper` 用棋盘生物群系、`dark` 用下界地形生成器等，地形细节与原快照不完全一致）
- 22w13oneBlockAtATime 的完整机制（单手持物、方块物理形态、落地自动放置）未做，当前是抛掷+拾取子集
- 15w14a 只做了"怪物不攻击玩家"；原快照的抱抱粒子、爱心箭等纯客户端表现未做
- 23w13a_or_b 只做了投票框架；原快照的 `/vote` 议案系统、假合成配方等未做
- 24w14potato 是自创风格化移植（原快照的毒土豆维度等未做）
- 钻石鸡 mixin 打在 `Chicken#aiStep` 的 `spawnAtLocation` 上，1.21.11 该调用已迁移（require=0 静默跳过，即 1.21.11 上此功能可能不生效）；毒土豆 ≥1.21.2 走 `Item#finishUsingItem`，若消耗品组件路径绕过该方法则同样静默跳过
- **0.2.1 修复**：`Animal#isFood` 从 1.20.6 起是 abstract 方法（1.16.5 是具体方法），@Inject 方法本体会 NPE 崩服（26.2 实测暴露）；feedingFrenzy 已改为重定向 `mobInteract` 里对 `isFood` 的调用点，全版本通用。**0.2.0 的 1.20.6-26.3 jar 都带此隐患，请全部替换**
- **全部 mixin 均 `require = 0`**：目标缺失/结构变化时该功能静默关闭，绝不炸服；代价是个别版本上个别功能可能不生效，排查时先看是不是这个原因
- **0.2.5-0.2.7 修复（26.2 服务端+客户端实测）**：毒土豆崩服 = 辅助类误放 mixin 包（Mixin 包是受保护命名空间，业务类直接引用会 IllegalClassLoadError）→ 已搬主包+加防炸盾；鸡 mixin 类按版本三选一注册（Modern/Legacy/Classic），mixins.json 的版本差异用 Gradle expand 注入（stonecutter 不处理资源文件里的 //? 条件）
- **0.2.3 修复**：carpet 26.x 的规则描述键改为扩展自己命名空间（`funnyport.rule.<名>.desc`），且解析时 carpet 会先装载各扩展翻译（`Translations.updateLanguage`）——0.2.0/0.2.2 的键格式或注解形式在 26.2 上都会被拒
- **carpet 26.x 规则注解规则**：新 API SettingsManager 只认新注解（描述走翻译键），legacy 注解只在 legacy SettingsManager 里可用；<1.20.5 的 carpet 只有 legacy 一条路，故注解形式按版本分支
- **0.2.4 修复（26.2 实测）**：26.x 的 noise 生成器 JSON 中 `biome_source` 从可省略变为**必填**——`dark` 维度缺该字段导致整个数据包校验失败、世界无法加载；已补 `multi_noise` + `minecraft:nether` preset（26.2 原版带此 preset 文件）。写自定义维度 JSON 时注意：noise 生成器必须显式给 biome_source
- 1.RV（2016）、3D Shareware（2019）的主体内容是客户端资源/渲染，未移植

## 路线图

- 1.RV / 3D Shareware 的客户端内容（模型、渲染 shader）
- Craftmine（25w14craftmine，1.21.5 线）核心玩法简化移植
- 20w14∞ 程序生成主题维度（运行时动态注册维度）

## 回滚说明

本项目纯新增目录，删项目目录即回滚。装到服务端后回滚：删 mods 里的 `funnyport-*.jar`；已生成的 `funnyport:*` 维度数据留在存档 `dimensions/funnyport`（不加载、无害），想彻底清掉就删该目录。

## 故障排查

- **构建时拉不到 `carpet:fabric-carpet`**：masa.dy.fi 偶尔抽风，重试或手动装进本地仓库
- **Gradle 内存不足（GC thrashing）**：全量构建时把 `org.gradle.jvmargs` 调到 `-Xmx3G` 以上，或降低 `org.gradle.parallel`
- **某版本功能静默不生效**：带 `require = 0` 的 mixin 在目标缺失时跳过（1.21.11 钻石鸡等），见"已知边界"
