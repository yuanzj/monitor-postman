# monitor-postman
基于postman、newman 实现了HTTP API 监控。

项目中使用spring定时任务执行newman脚本，解析脚本执行的输出的测试报告，发现有failure 项时触发邮件告警（可自行按照邮件告警实现方式扩展短信告警）

> 前端web管理项目地址 [monitor-postman-tradition](https://github.com/yuanzj/monitor-postman-tradition) 采用了bootstrap+vue实现下个版本使用webpack打包支持统计图表

![image](http://7xs7jt.com1.z0.glb.clouddn.com/monitor-man.jpeg)
![image](http://7xs7jt.com1.z0.glb.clouddn.com/addmonitor.jpeg)

## 安装
- node.js
- newman
- Java8
- IntelliJ IDEA
- Lombok plugin(IntelliJ IDEA 插件)

## 运行

```
nohup java -jar monitor-man-0.0.1-SNAPSHOT.jar --monitor.storage.directory=测试报告存储绝对路径 &
```

## Docker
即将发布


## License
This software is licensed under MIT. See the [LICENSE](LICENSE) file for more information.