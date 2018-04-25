# OA请假订阅通知
该服务用于订阅iPIN公司内部OA管理系统上的请假通知。

## 程序组件
1. 定时拉取OA系统的请假信息。将新的请假信息发送到rabbitMQ，并在Mongo中记录。
2. 订阅web服务，通过web页面订阅某个或者多个同事的请假通知。
3. 获取rabbitMQ上的请假信息，发邮件通知订阅该信息的人员。

## 程序打包
使用maven打包，mvn package -Dmaven.test.skip=true，生成jar包 oa-notify-formmain-server/target/oa-notify-formmain-server-1.0-SNAPSHOT.jar。

## 部署
在jar包所在目录创建config文件夹，将目录oa-notify-formmain-server/src/main/resources下的配置文件( application.properties , application-dev.properties , application-prod.properties , logback.xml ) 移动到config目录下，使用java 8 启动， java -jar oa-notify-formmain-server-1.0-SNAPSHOT.jar -Dlogback.configurationFile=config/logback.xml 
# oa-notify-formmain-server
