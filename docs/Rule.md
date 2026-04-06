## Rules
### (最大连锁更新深度)maxChainDepth
更改`maxChainDepth`字段的值，即服务器配置文件中`max-chained-neighbor-updates`的值

* 类型：`int`
* 默认值：`1000000`
* 选项：`0`，`65535`，`1000000`，`2147483647`
* 分类：`CuO`，`CREATIVE`，`NOT_VANILLA`
### (无限开宝库)infinitelyVault
玩家可以无限的开启宝库
仅限该玩家未开启过的宝库

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`，`CREATIVE`,`FEATURE`,`NOT_VANILLA`
### (比较器复制修复)comparatorDupeFix
修复比较器复制（*其实就是在调用Comparator.update()时判断自身是否是空气*）

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`,`BUGFIX`
### (最大更新深度)maxUpdateDepth
修改形参`maxUpdateDepth`的值

* 类型：`int`
* 默认值：`512`
* 选项：`0`，`512`，`1000000`，`2147483647`
* 分类：`CuO`，`CREATIVE`，`NOT_VANILLA`
### (树叶和苔藓可作为燃料)useLeavesAndMossAsFuel
可以使用树叶类方块和苔藓块作为熔炉燃料使用

1.21.2+需要重启服务器使更改生效

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`，`CREATIVE`，`FEATURE`，`NOT_VANILLA`
### (无限图腾)infinitelyTotem
无限使用图腾

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`，`CREATIVE`，`FEATURE`，`NOT_VANILLA`
### (没有附魔等级上限)noEnchantmentLevelLimit
没有附魔等级上限

这意味着你可以在铁砧上敲出的附魔等级最高为255

建议搭配noTooExpensive食用

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`，`NOT_VANILLA`，`EXPERIMENTAL`
### (没有过于昂贵)noTooExpensive
开启后过于昂贵需要的等级不再是40，而是int上限

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`，`NOT_VANILLA`，`EXPERIMENTAL`
### (铜傀儡修复)copperGolemFix
版本：1.21.9+

开启后铜傀儡不会再在空气中变为雕像

~~不知道麻将是不是故意这样设计的~~

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`，`CREATIVE`，`FEATURE`，`BUGFIX`
### (重新引入方块实体替换)blockEntitySwapReintroduced

开启后重载区块时不会再检查方块实体是否合法，放置一个带方块实体的方块时也不会再删除原来坐标的方块实体

至于如何保存方块实体就看你自己了

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`，`CREATIVE`，`FEATURE`
### (末地门框架可以被挖掘)endPortalFrameCanBeMined
可以挖掘末地传送门框来获取物品形式的末地门框(硬度为黑曜石的硬度)

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`，`CREATIVE`，`NOT_VANILLA`
### (基岩可以被挖掘)bedrockCanBeMined
可以挖掘基岩来获取物品形式的基岩(硬度为黑曜石的硬度)

注意：这项功能与carpet org addition的setBedrockHardness冲突

如果你仅开启其中的任意一项都无法使用全部的功能

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`，`CREATIVE`，`NOT_VANILLA`
### (猪灵交易无硬控时间)piglinTradeInstantly
移除了猪灵端详的时间，可以1gt交易一次

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`，`EXPERIMENTAL`，`FEATURE`
### (方块染色)blockDyeing
主手拿燃料右键染色方块可以将方块颜色改变为染料颜色

~~因技术原因不支持潜影盒还有床~~

支持的方块种类有:羊毛 地毯 蜡烛 混凝土&混凝土粉末 陶瓦&带釉陶瓦 染色玻璃&染色玻璃板

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`，`FEATURE`，`CREATIVE`
### (右键方块发出方块更新)rightClickBlockUpdate
右键方块可以发出nc更新(不知道有什么用)

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`，`CREATIVE`，`EXPERIMENTAL`
### (瞬时发射器&投掷器)instantDispenserAndDropper
发射器和投掷器不再添加4gt计划刻，而是立即执行

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`，`CREATIVE`，`EXPERIMENTAL`
### (竹子崩溃修复)bambooBlockCrashFix
修复了因竹子在调用grow时未添加边界检查而导致错误的获取void_air方块的stage属性

~~该bug配合instantDispenserAndDropper可用作更新抑制器~~

26.1snapshot8修复

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`,`BUGFIX`
### (活塞破坏方块产生更新)pistonBreakingBlockProducesUpdate
活塞推动时破坏方块将会产生更新

* 类型：`boolean`
* 默认值：`false`
* 选项：`false`，`true`
* 分类：`CuO`，`CREATIVE`，`NOT_VANILLA`
