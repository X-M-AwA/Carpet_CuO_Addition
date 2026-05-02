## Loggers
### (计划刻队列记录器)scheduleQueue
`/log scheduleQueue`

记录当前计划刻队列内元素的数量

可用于辅助计划刻阻塞

![picture](.././docs/114514/ScheduleQueue.png)
* `S`总计划刻数
* `B`方块计划刻数
* `F`流体计划刻数

属性:
* 默认选项：N/A
* 参考选项：N/A
### (tick记录器)tick
`/log tick`

记录当前游戏tick计数器的值

![picture](.././docs/114514/Tick.png)
* `Tick`当前tick计数器的值

属性:
* 默认选项：N/A
* 参考选项：N/A
### (更新深度记录器)update
`/log update <选项>`

* `skipping` 当发生更新跳略时，会打印当前的深度及发生跳略的坐标

![picture](.././docs/114514/update_0.png)

* `chain` 打印当前更新链的深度，使用chainUpdateLoggerThreshold规则设置阈值

![picture](.././docs/114514/update_1.png)

属性:
* 默认选项：`skipping`
* 参考选项：`skipping`，`chain`