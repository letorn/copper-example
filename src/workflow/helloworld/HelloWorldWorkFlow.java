package workflow.helloworld;

import org.copperengine.core.AutoWire;
import org.copperengine.core.Interrupt;
import org.copperengine.core.Response;
import org.copperengine.core.WaitMode;
import org.copperengine.core.Workflow;
import org.copperengine.core.WorkflowDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import example.WorkflowDef;
import example.helloworld.HelloWorldAdapter;
import example.helloworld.HelloWorldData;

@WorkflowDescription(alias = WorkflowDef.HELLOWORLD, majorVersion = 1, minorVersion = 0, patchLevelVersion = 0)
public class HelloWorldWorkFlow extends Workflow<HelloWorldData>
{

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(HelloWorldWorkFlow.class);

    private transient HelloWorldAdapter helloWorldAdapter;

    @AutoWire
    public void setHelloWorldAdapter(HelloWorldAdapter helloWorldAdapter)
    {
        this.helloWorldAdapter = helloWorldAdapter;
    }

    @Override
    public void main() throws Interrupt
    {
        HelloWorldData data = getData();
        String correlationId;
        Response<HelloWorldData> response;

        correlationId = helloWorldAdapter.initHelloWorld(data);
        logger.info(correlationId + " Init HelloWorld");

        wait(WaitMode.ALL, 5 * 60 * 60 * 1000, correlationId);
        response = getAndRemoveResponse(correlationId);

        logger.info(correlationId + " Send HelloWorld: " + response.getResponse().getSender());

        wait(WaitMode.ALL, 5 * 60 * 60 * 1000, correlationId);
        response = getAndRemoveResponse(correlationId);

        logger.info(correlationId + " Reply HelloWorld: " + response.getResponse().getReplier());
    }

}
