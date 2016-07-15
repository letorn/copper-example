package controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import example.leave.LeaveAdapter;
import example.leave.LeaveData;

@Controller
@RequestMapping("leave/")
public class LeaveController
{

    @Autowired
    private LeaveAdapter leaveAdapter;

    @RequestMapping("list.do")
    @ResponseBody
    public List<LeaveData> list()
    {
        List<LeaveData> dataList = new ArrayList<>();
        dataList = leaveAdapter.getDataList();
        return dataList;
    }

    @RequestMapping("create.do")
    @ResponseBody
    public LeaveData create()
    {
        LeaveData data = new LeaveData();
        leaveAdapter.runWorkflow(data);
        return data;
    }

    @RequestMapping("send.do")
    @ResponseBody
    public Boolean send(String cid, String sender)
    {
        leaveAdapter.sendLeave(cid, sender);
        return true;
    }

    @RequestMapping("reply.do")
    @ResponseBody
    public Boolean reply(String cid, String replier)
    {
        leaveAdapter.replyLeave(cid, replier);
        return true;
    }

}
