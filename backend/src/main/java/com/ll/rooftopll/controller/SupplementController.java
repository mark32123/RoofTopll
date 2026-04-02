package com.ll.rooftopll.controller;


import com.ll.rooftopll.commn.api.Result;
import com.ll.rooftopll.dto.SupplementTemplateDTO;
import com.ll.rooftopll.entity.SupplementLog;
import com.ll.rooftopll.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/supplement")
public class SupplementController {

    @Autowired
    private WorkoutService workoutService;

    /**
     * 获取常用补剂清单
     */
    @GetMapping("/templates")
    public Result<List<SupplementTemplateDTO>> getTemplates() {
        List<SupplementTemplateDTO> templates = new ArrayList<>();
        templates.add(new SupplementTemplateDTO("肌酸", "g"));
        templates.add(new SupplementTemplateDTO("蛋白粉", "勺"));
        templates.add(new SupplementTemplateDTO("氮泵", "勺"));
        templates.add(new SupplementTemplateDTO("咖啡因", "mg"));

        return Result.success(templates);
    }

    /**
     * 提交补剂服用记录
     */
    @PostMapping("/log")
    public Result<String> logSupplement(@RequestBody SupplementLog log) {
        workoutService.logSupplement(log);
        return Result.success("记录成功，Mark！营养补给已存档。");
    }
}