package example;

import java.util.ArrayList;
import java.util.List;

import org.copperengine.core.DependencyInjector;
import org.copperengine.core.EngineIdProvider;
import org.copperengine.core.EngineIdProviderBean;
import org.copperengine.core.common.IdFactory;
import org.copperengine.core.common.JdkRandomUUIDFactory;
import org.copperengine.core.common.ProcessorPoolManager;
import org.copperengine.core.common.WorkflowRepository;
import org.copperengine.core.monitoring.NullRuntimeStatisticsCollector;
import org.copperengine.core.monitoring.RuntimeStatisticsCollector;
import org.copperengine.core.persistent.PersistentProcessorPool;
import org.copperengine.core.persistent.PersistentScottyEngine;
import org.copperengine.core.persistent.ScottyDBStorageInterface;
import org.copperengine.core.wfrepo.CompilerOptionsProvider;
import org.copperengine.core.wfrepo.FileBasedWorkflowRepository;
import org.copperengine.core.wfrepo.URLClassloaderClasspathProvider;
import org.copperengine.spring.SpringDependencyInjector;

public class PersistentEngineFactory
{

    public static PersistentScottyEngine create(String sourceDir, String targetDir)
    {
        PersistentScottyEngine engine = new PersistentScottyEngine();
        engine.setIdFactory(createIdFactory());
        engine.setEngineIdProvider(createEngineIdProvider());
        engine.setDependencyInjector(createDependencyInjector());
        engine.setProcessorPoolManager(createProcessorPoolManager());
        engine.setDbStorage(createScottyDBStorage());
        engine.setStatisticsCollector(createRuntimeStatisticsCollector());
        engine.setWfRepository(createWorkflowRepository(sourceDir, targetDir));
        return engine;
    }

    private static IdFactory createIdFactory()
    {
        return new JdkRandomUUIDFactory();
    }

    private static EngineIdProvider createEngineIdProvider()
    {
        return new EngineIdProviderBean("PersistentEngine#DEFAULT");
    }

    private static DependencyInjector createDependencyInjector()
    {
        return new SpringDependencyInjector();
    }

    private static ProcessorPoolManager<PersistentProcessorPool> createProcessorPoolManager()
    {
        return null;
    }

    private static ScottyDBStorageInterface createScottyDBStorage()
    {
        return null;
    }

    private static RuntimeStatisticsCollector createRuntimeStatisticsCollector()
    {
        return new NullRuntimeStatisticsCollector();
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