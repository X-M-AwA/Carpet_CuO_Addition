## Commands
### chunk
* remove
`/chunk remove <ChunkPosX> <ChunkPosZ>`

从存档中(`region`文件)删除指定区块，没有后边的参数则删除玩家所在的区块
* regen
`/chunk regen <ChunkPosX> <ChunkPosZ>`

删除存档中的指定区块并卸载该区块，使其从头开始重新生成，没有后边的参数则重新生成玩家所在的区块
### rules
`/rules`
* `close`将所有已加载的carpet规则(类型为boolean)设置为false
* `enabled`将所有已加载的carpet规则(类型为boolean)设置为true
* `def`将所有已加载的carpet规则设为默认值