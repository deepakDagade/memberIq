package com.nexoraa.memberiq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexoraa.memberiq.entity.TemplateMessageRequest;
import com.nexoraa.memberiq.service.WatiApiService;

@RestController
@RequestMapping("/api/wati")
public class WatiController {

    @Autowired
    private WatiApiService watiApiService;

    @PostMapping("/send")
    public String sendTemplate(@RequestBody TemplateMessageRequest request) {
        return watiApiService.sendTemplateMessage(request);
    }
}