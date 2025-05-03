package com.nexoraa.memberiq.entity;

import java.util.List;

import lombok.Data;

@Data
public class TemplateMessageRequest {
    private String template_name;
    private String broadcast_name;
    private List<String> parameters;
    private String phone_number;
}