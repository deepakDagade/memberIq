package com.nexoraa.memberiq.service;

import com.nexoraa.memberiq.entity.TemplateMessageRequest;

public interface WatiApiService {

	String sendTemplateMessage(TemplateMessageRequest request);

}
