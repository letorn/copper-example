package example;

import java.util.ArrayList;
import java.util.List;

import org.copperengine.core.DependencyInjector;
import org.copperengine.core.EngineIdProvider;
import org.copperengine.core.EngineIdProviderBean;
import org.copperengine.core.common.DefaultProcessorPoolManager;
import org.copperengine.core.common.DefaultTicketPoolManager;
import org.copperengine.core.common.IdFactory;
import org.copperengine.core.common.JdkRandomUUIDFactory;
import org.copperengine.core.common.ProcessorPoolManager;
import org.copperengine.core.common.TicketPoolManager;
import org.copperengine.core.common.WorkflowRepository;
import org.copperengine.core.monitoring.NullRuntimeStatisticsCollector;
import org.copperengine.core.monitoring.RuntimeStatisticsCollector;
import org.copperengine.core.tranzient.DefaultEarlyResponseContainer;
import org.copperengine.core.tranzient.DefaultTimeoutManager;
import org.copperengine.core.tranzient.EarlyResponseContainer;
import org.copperengine.core.tranzient.TimeoutManager;
import org.copperengine.core.tranzient.TransientPriorityProcessorPool;
import org.copperengine.core.tranzient.TransientProcessorPool;
import org.copperengine.core.tranzient.TransientScottyEngine;
import org.copperengine.core.wfrepo.CompilerOptionsProvider;
import org.copperengine.core.wfrepo.FileBasedWorkflowRepository;
import org.copperengine.core.wfrepo.URLClassloaderClasspathProvider;
import org.copperengine.spring.SpringDependencyInjector;

public class TransientEngineFactory
{

    public static TransientScottyEngine create(String sourceDir, String targetDir)
    {
        TransientScottyEngine engine = new TransientScottyEngine();
        engine.setIdFactory(createIdFactory());
        engine.setEngineIdProvider(createEngineIdProvider());
        engine.setDependencyInjector(createDependencyInjector());
        engine.setPoolManager(createProcessorPoolManager());
        engine.setTicketPoolManager(createTicketPoolManager());
        engine.setTimeoutManager(createTimeoutManager());
        engine.setStatisticsCollector(createRuntimeStatisticsCollector());
        engine.setEarlyResponseContainer(createEarlyResponseContainer());
        engine.setWfRepository(createWorkflowRepository(sourceDir, targetDir));
        return engine;
    }

    private static IdFactory createIdFactory()
    {
        return new JdkRandomUUIDFactory();
    }

    private static EngineIdProvider createEngineIdProvider()
    {
        return new EngineIdProviderBean("TransientEngine#DEFAULT");
    }

    private static DependencyInjector createDependencyInjector()
    {
        return new SpringDependencyInjector();
    }

    private static ProcessorPoolManager<TransientProcessorPool> createProcessorPoolManager()
    {
        DefaultProcessorPoolManager<TransientProcessorPool> ppm = new DefaultProcessorPoolManager<TransientProcessorPool>();
        TransientPriorityProcessorPool defaultPP = new TransientPriorityProcessorPool(
                TransientProcessorPool.DEFAULT_POOL_ID);
        ppm.addProcessorPool(defaultPP);
        return ppm;
    }

    private static TicketPoolManager createTicketPoolManager()
    {
        return new DefaultTicketPoolManager();
    }

    private static TimeoutManager createTimeoutManager()
    {
        return new DefaultTimeoutManager();
    }

    private static RuntimeStatisticsCollector createRuntimeStatisticsCollector()
    {
        return new NullRuntimeStatisticsCollector();
    }

    private static EarlyResponseContainer createEarlyResponseContainer()
    {
        return new DefaultEarlyResponseContainer();
    }

    private static WorkflowRepository createWorkflowRepository(String sourceDir, String targetDir)
    {
        FileBasedWorkflowRepository repo = new FileBasedWorkflowRepository();
        repo.setSourceDirs(sourceDir);
        repo.setTargetDir(targetDir);
        List<CompilerOptionsProvider> compilerOptionsProviders = new ArrayList<>();
        compilerOptionsProviders.add(new URLClassloaderClasspathProvider());
        repo.setCompilerOptionsProviders(compilerOptionsProviders);
        repo.setLoadNonWorkflowClasses(true);
        return repo;
    }

}