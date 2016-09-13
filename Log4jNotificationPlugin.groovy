import com.dtolabs.rundeck.core.logging.LogLevel
import com.dtolabs.rundeck.plugins.notification.NotificationPlugin
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.log4j.MDC


def exposeUserData = { execution, event ->
    MDC.put('eventUser', execution.user ?: '-')
    MDC.put('user', execution.context.job.username ?: '-')
    MDC.put('jobName', execution.job.name ?: '-')
    MDC.put('groupPath', execution.job.group ?: '-')
    MDC.put('id', execution.id ?: '-')
    MDC.put('project', execution.project ?: '-')
    MDC.put('abortedby', execution.abortedby ?: '-')
    MDC.put('state', execution.status ?: '-')
    MDC.put('uuid', execution.job.id ?: '-')
    MDC.put('url', execution.href ?: '-')
    MDC.put('loglevel', execution.loglevel ?: '-')
    MDC.put('averageDuration', execution.job.averageDuration ?: '-')
    MDC.put('event', event ?: '-')
    execution.context.option.each { MDC.put(it.key, it.value ?: '-') }


}

/**
 * This NotificationPlugin log writer sends notification messages to log4j
 */
rundeckPlugin(NotificationPlugin) {
    title = 'Rundeck Notification Log4j plugin'
    description = 'Notification plugin to get user input data available in log4j logs'
    configuration {
        logger = 'notification'
        logger required: true, description: "Logger instance name"


    }

    /**
     * handleTrigger is the common closure used for all the triggers
     */
    def handleTrigger = { String message, String event, Map execution, Map config ->
        try {
            def Level level
            def Logger logger = Logger.getLogger(config.logger)
            switch (execution.loglevel) {
                case LogLevel.ERROR:
                    level = Level.ERROR
                    break
                case LogLevel.WARN:
                    level = Level.WARN
                    break
                case LogLevel.VERBOSE:
                case LogLevel.DEBUG:
                    level = Level.DEBUG
                    break
                case LogLevel.NORMAL:
                case LogLevel.OTHER:
                default:
                    level = Level.INFO
            }
            exposeUserData(execution, event)
            logger.log(level, message)
            true
        } catch (Exception e) {
            message = 'Problem with' + config.title
            logger.log(Level.ERROR, message);
        }


    }

    //define the triggers, and curry the handleTrigger closure to specify
    onsuccess(handleTrigger.curry('Job finished successfuly', 'finish'))
    onfailure(handleTrigger.curry('Job failed', 'finish'))
    onstart(handleTrigger.curry('Job started', 'start'))
}

