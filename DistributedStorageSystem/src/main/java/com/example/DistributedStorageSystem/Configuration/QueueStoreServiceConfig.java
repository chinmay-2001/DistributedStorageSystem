package com.example.DistributedStorageSystem.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Configuration
public class QueueStoreServiceConfig {
    @Bean
    public  QueueStoreSpace<File> queueStoreSpace(){
        return  new QueueStoreSpace.Builder<File>(100).withNbProviders(2).build();
    }
}

//package com.se.pim.dal.export.task;
//
//import com.se.pim.dal.export.entities.ExportProperties;
//import com.se.pim.dal.export.process.AbstractProcess;
//import com.se.pim.dal.persistence.client.ESHttpClient;
//import com.se.pim.dal.export.exception.ConfigurationException;
//import com.se.pim.dal.export.exception.StopTaskException;
//import com.se.pim.dal.export.log.AddFunctionnalLogger;
//import com.se.pim.dal.export.pim.PIMHttpClient;
//import com.se.pim.dal.export.utils.CharacterUtils;
//import com.se.pim.dal.export.utils.DateTimeUtils;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;

/**
 * Concrete class for {@link AbstractRunnableInformerTask}.
// *
// * <p>
// * Export has access to the following properties :
// * <ul>
// *  <li> {@link #internalProperties} : specific properties for this export class
// *  <li> {@link #exportProperties} : general properties
// *  <li> {@link #outputdir} :  directory where export results will be generated,
// *  <li> A specific logger : {@link #getLogger()}
// * </ul>
// * <p>
// * Usage :
// * <ul>
// *  <li>Subclass and in your constructor : read properties you need from {@link #exportProperties}, {@link #internalProperties}, add your subtasks with {@link #addSubTask(Task, boolean)}.
// *  <li>Subtask task will be executed by calling {@link #run()} method. ( run method calls execute method)
// *  <li>Optional : You can also override {@link #execute()} if you need more granularity of subtask execution.
// * </ul>
// */
//public abstract class AbstractExportTask extends AbstractRunnableInformerTask {
//
//    /**
//     * Export internal properties, not updatable by user configuration (src/main/resource).
//     * Properties are loaded by class constructor using file name returned by {@link #getExportTaskPropertiesFileName()}
//     */
//    protected Properties internalProperties;
//
//    /**
//     * Full path to export output directory where export results will be generated,
//     * useful for writer tasks
//     */
//    protected String outputdir;
//    /**
//     * Full path to export log directory
//     */
//    protected String outputlog;
//
//    /**
//     * ExportProperties general properties
//     */
//    protected ExportProperties exportProperties;
//
//    /**
//     * ESHttpClient
//     */
//    protected ESHttpClient client;
//
//    /**
//     * PIM rest client
//     */
//    protected PIMHttpClient pimClient;
//
//    /**
//     * Functionnal logger init by constructor.
//     * it uses {@link #internalProperties} "name" property and  {@link AddFunctionnalLogger} class to create the logger
//     */
//    protected Logger functionnalLogger;
//
//    /**
//     * This constructor Init {@link #functionnalLogger} and {@link #internalProperties} with given params.
//     * Use this constructor if you need to reuse props from other {@link AbstractExportTask}
//     *
//     * @param client
//     * @param outputdir
//     * @param exportProperties
//     * @param internalProperties
//     * @param functionnalLogger
//     */
//    protected AbstractExportTask(ESHttpClient client, String outputdir, ExportProperties exportProperties, Properties internalProperties, Logger functionnalLogger) {
//
//        this.exportProperties = exportProperties;
//        this.client = client;
//        this.outputdir = outputdir;
//        this.internalProperties = internalProperties;
//        this.functionnalLogger = functionnalLogger;
//    }
//
//    /**
//     * This constructor init {@link #functionnalLogger} using outputlog
//     * and {@link #internalProperties} using internalPropertiesFileName
//     *
//     * @param client                     ESHttpClient
//     * @param outputdir                  full path to export output directory where export results will be generated, useful for writer tasks
//     * @param outputlog                  full path to export log directory where export logs will be generated
//     * @param exportProperties           ExportProperties properties usually setup in dal-ui export configuration (jobName ...)
//     * @param internalPropertiesFileName properties file name for this Export or null. Example "foo.properties".
//     *                                   This file should be in src/main/resources/ directory
//     * @throws ConfigurationException
//     */
//    protected AbstractExportTask(ESHttpClient client, String outputdir, String outputlog, ExportProperties exportProperties, String internalPropertiesFileName)
//            throws ConfigurationException {
//
//        this.exportProperties = exportProperties;
//        this.client = client;
//        this.outputdir = outputdir;
//        this.outputlog = outputlog;
//        this.internalProperties = new Properties();
//
//        if (internalPropertiesFileName != null) {
//            InputStream input = getClass().getClassLoader().getResourceAsStream(internalPropertiesFileName);
//            if (input == null) {
//                throw new ConfigurationException(
//                        "Internal property file : " + internalPropertiesFileName + " not found in classpath");
//            }
//            try {
//                internalProperties.load(input);
//                createFunctionalLogger(internalPropertiesFileName);
//            } catch (IOException e) {
//                throw new ConfigurationException(String.format("Error reading property file : %s", internalPropertiesFileName));
//            }
//
//        }
//
//        getLogger().debug("Init ExportTask : {} , internal properties : {}", this.getClass().getCanonicalName(), internalProperties);
//        logPackageInfo();
//    }
//
//    /**
//     * create functional logger
//     *
//     * @param internalPropertiesFileName name of internal properties file
//     * @throws ConfigurationException
//     */
//    private void createFunctionalLogger(String internalPropertiesFileName) throws ConfigurationException {
//        String loggerName = internalProperties.getProperty("name");
//        if (StringUtils.isBlank(loggerName)) {
//            throw new ConfigurationException(
//                    String.format("Unable to init functional logger, could not find property 'name' in file : %s", internalPropertiesFileName));
//        }
//        AddFunctionnalLogger addLogger = new AddFunctionnalLogger.Builder(loggerName)
//                .workingLog(this.outputlog)
//                .fileNamePrefix(loggerName)
//                .build();
//        addLogger.execute();
//        functionnalLogger = LoggerFactory.getLogger(loggerName);
//    }
//
//    /**
//     * This constructor init {@link #functionnalLogger} using outputlog
//     * and {@link #internalProperties} using internalPropertiesFileName
//     *
//     * @param client                     ESHttpClient
//     * @param outputdir                  full path to export output directory where export results will be generated, useful for writer tasks
//     * @param outputlog                  full path to export log directory where export logs will be generated
//     * @param exportProperties           ExportProperties properties usually setup in dal-ui export configuration (jobName ...)
//     * @param internalPropertiesFileName properties file name for this Export or null. Example "foo.properties".
//     * @param createFunctionalLog        switch for functional logger
//     *                                   This file should be in src/main/resources/ directory
//     * @throws ConfigurationException
//     */
//    protected AbstractExportTask(ESHttpClient client, String outputdir, String outputlog, ExportProperties exportProperties, String internalPropertiesFileName, boolean createFunctionalLog)
//            throws ConfigurationException {
//
//        this.exportProperties = exportProperties;
//        this.client = client;
//        this.outputdir = outputdir;
//        this.outputlog = outputlog;
//        this.internalProperties = new Properties();
//
//        if (internalPropertiesFileName != null) {
//            InputStream input = getClass().getClassLoader().getResourceAsStream(internalPropertiesFileName);
//            if (input == null) {
//                throw new ConfigurationException(
//                        String.format("Internal property file : %s not found in classpath", internalPropertiesFileName));
//            }
//            try {
//                internalProperties.load(input);
//                if (createFunctionalLog) {
//                    createFunctionalLogger(internalPropertiesFileName);
//                }
//            } catch (IOException e) {
//                throw new ConfigurationException(String.format("Error reading property file : %s", internalPropertiesFileName));
//            }
//
//        }
//
//        getLogger().debug("Init ExportTask : {} , internal properties : {}", this.getClass().getCanonicalName(), internalProperties);
//        logPackageInfo();
//    }
//
//    protected String getInjectedProperty(String externalPropertiesFileName, String property)
//            throws ConfigurationException {
//        Properties externalProperties = new Properties();
//        if (externalPropertiesFileName != null) {
//            InputStream input = getClass().getClassLoader().getResourceAsStream(externalPropertiesFileName);
//            if (input == null) {
//                throw new ConfigurationException(
//                        "External property file : " + externalPropertiesFileName + " not found in classpath");
//            }
//            try {
//                externalProperties.load(input);
//            } catch (IOException e) {
//                throw new ConfigurationException(
//                        "Error reading external property file : " + externalPropertiesFileName);
//            }
//
//            String injectedProperty = externalProperties.getProperty(property);
//            if (StringUtils.isBlank(injectedProperty)) {
//                throw new ConfigurationException("Injected property " + property + " is missing in " + externalPropertiesFileName);
//            }
//
//            internalProperties.setProperty(property, injectedProperty.trim());
//        }
//
//        return internalProperties.getProperty(property);
//    }
//
//
//    private void execSubTasks() {
//
//        if (CollectionUtils.isEmpty(subTasks)) {
//            getLogger().info("No subtask to execute ");
//        } else {
//            getLogger().info("{} subtask(s) to execute ", subTasks.size());
//            ExecutorService executor = Executors.newFixedThreadPool(subTasks.size());
//            for (Task subTask : subTasks) {
//                if (!isStatusStopped() && !isStatusCancelled()) {
//
//                    getLogger().info("Execute subtask : {} ", subTask.getClass().getName());
//                    if (subTask instanceof AbstractRunnableInformerTask) {
//                        executor.execute((AbstractRunnableInformerTask) subTask);
//                    } else {
//                        getLogger().error(
//                                "Could not execute task {}, task is not instance of AbstractRunnableInformerTask",
//                                subTask.getClass().getName());
//                    }
//                }
//            }
//
//            executor.shutdown();
//            try {
//                final boolean awaitTermination = executor.awaitTermination(1, TimeUnit.DAYS);
//                if (!awaitTermination) {
//                    throw new InterruptedException("The subtask(s) are interrupted because it exceeded 24 hours");
//                }
//            } catch (InterruptedException interruptedException) {
//                stop(interruptedException);
//                executor.shutdownNow();
//                getLogger().error("The subtask(s) are interrupted", interruptedException);
//                Thread.currentThread().interrupt();
//            }
//        }
//
//
//    }
//
//    /**
//     * Method called after {@link #execute()}.
//     * Can be used to : get data generated by task, change calculated <code>nbItems</code> or any post execute logic .
//     */
//    public abstract void postExecute();
//
//    /**
//     * {@inheritDoc}
//     * <p>
//     * <li>Execute all subtasks (if status is not stopped or canceled)
//     * <li>Set status, state
//     * <li>Set nb Items (sum subtask items eligible for count) (see {@link #addSubTask(Task, boolean)} )
//     * <li>Calls {@link #postExecute()}
//     * <li>Remove subTasks
//     */
//    @Override
//    public void execute() throws ConfigurationException {
//        long t1 = System.currentTimeMillis();
//        int nbSubTasksExecuted = 0;
//
//        try {
//            String subTaskNames = String.join(CharacterUtils.COMMA, getSubTasksNames());
//            if (!isStatusCancelled() && !isStatusStopped()) {
//                execSubTasks();
//                nbSubTasksExecuted = getNbSubTasks();
//
//                //update status & state
//                if (!isStatusCancelled() && !isStatusStopped()) {
//                    status = TaskStatus.DONE;
//
//                    //state is initialized to failed
//                    //TODO change initialization (initialized to success)
//                    if (!TaskState.WARNING.equals(state) && !TaskState.FTP_WARNING.equals(state) && !TaskState.INCONSISTENCY_WARNING.equals(state)) {
//                        state = TaskState.SUCCESS;
//                    }
//                    for (Task task : subTasks) {
//                        if (task.isWarning()) {
//                            state = TaskState.WARNING;
//                            break;
//                        } else if (task.isInconsistencyWarning()) {
//                            state = TaskState.INCONSISTENCY_WARNING;
//                            break;
//                        }
//                    }
//                }
//                int nbSubItems = this.countSubTasksNbItems();
//                if (nbSubItems > 0) {
//                    this.nbItems = nbSubItems;
//                }
//
//                long t2 = System.currentTimeMillis();
//                String duration = DateTimeUtils.formatTime(t2 - t1);
//
//                getLogger().info("{} subtasks executed, Duration : {}, nb items exported: {} ", nbSubTasksExecuted, duration, nbItems);
//            } else {
//                getLogger().info("status is {}, subtasks ({}) not executed", status, subTaskNames);
//            }
//
//            postExecute();
//
//            removeSubTasks();
//
//        } catch (Exception e) {
//            getLogger().error("Execution error", e);
//            stop(e);
//        } finally {
//            getLogger().info("Process status : {}, state : {} ", status, state);
//        }
//
//    }
//
//    public void checkStatusTask() throws StopTaskException {
//        if (isStatusCancelled() || isStatusStopped()) {
//            throw new StopTaskException(String.format("status is %s", status));
//        }
//    }
//
//    /**
//     *  Pass the state of the children to the parent
//     *  Using for the portage of Talend exports (CDQT, Datascore, Clipsal/PDL)
//     * @param processStatus status (SUCCESS,WARNING,FAILURE)
//     */
//    protected void passStateOfChildrenToParent(AbstractProcess.ProcessStatus processStatus) {
//        if (processStatus.equals(AbstractProcess.ProcessStatus.FAILURE)) {
//            state = TaskState.FAILED;
//        } else if (!state.equals(TaskState.FAILED) && processStatus.equals(AbstractProcess.ProcessStatus.WARNING)) {
//            state = TaskState.WARNING;
//        }
//    }
//
//}
