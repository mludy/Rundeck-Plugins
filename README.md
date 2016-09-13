# Rundeck-log4j-notification-plugin

Sends rundeck notification messages to a specific log4j appender. Plugin allow to pass user input data to log4j logs. 
To expose user data you need to add var to your Log4jConversionPattern. Name the var like the name of the user input for example:
env -> %X{env}
country -> %X{Country}  

# Installation Instructions

Copy groovy script into your $RDECK_BASE/libext.
See the rundeck documentation for more information on installing rundeck plugins.

# Configuration

## Rundeck job
Choose specific job to use notification. You can define it under job edit page
Send Notification -> Yes
On Success -> Rundeck Notification Log4j plugin -> logger -> ( define your logger name. Example: notification )
On Failure -> Rundeck Notification Log4j plugin -> logger -> ( define your logger name. Example: notification )
On Start -> Rundeck Notification Log4j plugin -> logger -> ( define your logger name. Example: notification )

## Rundeck Log4j logger

Configuration done with assumption that you choose logger appender name = notification

 #Enable extended notification logging
log4j.logger.notification =INFO, notification
 # Custom notification excution logging
log4j.appender.notification=org.apache.log4j.DailyRollingFileAppender
log4j.appender.notification.file=/var/log/rundeck/rundeck.notification.log
log4j.appender.notification.append=true
log4j.appender.notification.layout=org.apache.log4j.PatternLayout
log4j.appender.notification.layout.ConversionPattern=[%d{ISO8601}] %X{eventUser} %X{event} [%X{id}:%X{state}] %X{project} %X{user}/%X{abortedby} "%X{groupPath}/%X{jobName}"[%X{uuid}] %X{averageDuration} "%X{env}" "%X{country} "%n
