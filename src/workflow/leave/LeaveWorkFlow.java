package workflow.leave;

import org.copperengine.core.AutoWire;
import org.copperengine.core.Interrupt;
import org.copperengine.core.Response;
import org.copperengine.core.WaitMode;
import org.copperengine.core.WorkflowDescription;
import org.copperengine.core.persistent.PersistentWorkflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import example.WorkflowDef;
import example.leave.LeaveAdapter;
import example.leave.LeaveData;

@WorkflowDescription(alias = WorkflowDef.LEAVE, majorVersion = 1, minorVersion = 0, patchLevelVersion = 0)
public class LeaveWorkFlow extends PersistentWorkflow<LeaveData>
{

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(LeaveWorkFlow.class);

    private transient LeaveAdapter leaveAdapter;

    @AutoWire
    public void setLeaveAdapter(LeaveAdapter leaveAdapter)
    {
        this.leaveAdapter = leaveAdapter;
    }

    @Override
    public void main() throws Interrupt
    {
        LeaveData data = getData();
        String correlationId;
        Response<LeaveData> response;

        correlationId = leaveAdapter.initLeave(data);
        logger.info(correlationId + " Init Leave");

        wait(WaitMode.ALL, 5 * 60 * 60 * 1000, correlationId);
        response = getAndRemoveResponse(correlationId);

        logger.info(correlationId + " Send Leave: " + response.getResponse().getSender());

        wait(WaitMode.ALL, 5 * 60 * 60 * 1000, correlationId);
        response = getAndRemoveResponse(correlationId);

        logger.info(correlationId + " Reply Leave: " + response.getResponse().getReplier());
    }

}
