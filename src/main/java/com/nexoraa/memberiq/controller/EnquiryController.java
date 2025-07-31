package com.nexoraa.memberiq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.nexoraa.memberiq.entity.FrontOffice;

@Controller
public class EnquiryController {

	@GetMapping("/enquiry/{organizationId}")
	public String showEnquiryForm(@PathVariable String organizationId, Model model) {
		model.addAttribute("organizationId", organizationId);
		model.addAttribute("frontOffice", new FrontOffice());
		return "enquiry";
	}

	@PostMapping("/enquiry/{organizationId}")
	public String submitEnquiry(@PathVariable String organizationId, @ModelAttribute FrontOffice enquiry, Model model) {
		// Process the enquiry along with organizationId
		System.out.println(enquiry.getEmail());
		System.out.println(enquiry.getPhoneNo());
		System.out.println(enquiry.getName());
		System.out.println(organizationId);
		model.addAttribute("message", "Enquiry submitted successfully!");
		return "result";
	}
}
