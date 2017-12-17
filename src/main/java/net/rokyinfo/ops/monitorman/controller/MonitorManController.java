package net.rokyinfo.ops.monitorman.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.rokyinfo.ops.monitorman.annotation.PersistenceData2File;
import net.rokyinfo.ops.monitorman.entity.ResultEntity;
import net.rokyinfo.ops.monitorman.model.MonitorManForm;
import net.rokyinfo.ops.monitorman.service.MonitorManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * @author yuanzhijian
 */
@Api(value = "/monitors", tags = "monitors", description = "监控处理")
@RestController()
@RequestMapping("/v1.0/monitors")
public class MonitorManController {

    @Autowired
    private MonitorManService monitorManService;

    @ApiOperation(value = "创建监控", notes = "")
    @PostMapping()
    @PersistenceData2File()
    public ResultEntity<?> createMonitorMan(@Valid @ModelAttribute MonitorManForm monitorManForm) {
        return new ResultEntity<>(monitorManService.createMonitorMan(monitorManForm));
    }

    @ApiOperation(value = "删除监控", notes = "")
    @DeleteMapping()
    @PersistenceData2File()
    public ResultEntity<?> deleteMonitorMan(Long monitorId) {
        return new ResultEntity<>(monitorManService.deleteMonitorMan(monitorId));
    }

    @ApiOperation(value = "修改监控", notes = "")
    @PostMapping("/update")
    @PersistenceData2File()
    public ResultEntity<?> updateMonitorMan(@Valid @ModelAttribute MonitorManForm monitorManForm) {
        return new ResultEntity<>(monitorManService.updateMonitorMan(monitorManForm));
    }

    @ApiOperation(value = "监控列表", notes = "")
    @GetMapping()
    public ResultEntity<?> getMonitorList() {
        return new ResultEntity<>(monitorManService.getMonitorManList());
    }

    @ApiOperation(value = "监控详情", notes = "")
    @GetMapping("/{id}")
    public ResultEntity<?> getMonitorDetail(@PathVariable Long id) {
        return new ResultEntity<>(monitorManService.getMonitorDetail(id));
    }

    @ApiOperation(value = "启动监控", notes = "")
    @PutMapping("/start")
    @PersistenceData2File()
    public ResultEntity<?> startMonitor(Long monitorId) {
        return new ResultEntity<>(monitorManService.startMonitor(monitorId));
    }

    @ApiOperation(value = "停止监控", notes = "")
    @PutMapping("/stop")
    @PersistenceData2File()
    public ResultEntity<?> stopMonitor(Long monitorId) {
        return new ResultEntity<>(monitorManService.stopMonitor(monitorId));
    }


}
