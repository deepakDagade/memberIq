package com.nexoraa.memberiq.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nexoraa.memberiq.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LoginController {

    private final UserService userService;

    @Value("${template.web-app-url}")
    private String webAppUrl;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // If SessionId is null Redirect to the frontend application
        if (request.getRequestedSessionId() == null) {
            response.sendRedirect(webAppUrl + "/login");
            return null;
        }
        model.addAttribute("webAppUrl", webAppUrl);
        return "login";
    }

//    @PostMapping("/users/forgot")
//    public ResponseEntity<Response> forgotPassword(@RequestParam(required = true) String email,
//                                                   @RequestParam(required = true) String type) {
//        Response response = new Response();
//        try {
//            userService.generatePasswordResetToken(email, type);
//            response.setStatus(HttpStatus.OK.value());
//            response.setStatusMessage(ResponseMessages.PASSWORD_RESET_EMAIL_SENT_SUCCESS);
//        } catch (NetZeroMediaException ex) {
//            log.error("Error in forgotPassword: {}", ex.getMessage(), ex);
//            response.setStatusMessage(ex.getMessage());
//            response.setStatus(HttpStatus.BAD_REQUEST.value());
//        } catch (Exception ex) {
//            log.error("Error in forgotPassword: {}", ex.getMessage(), ex);
//            response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        }
//        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
//
//    }
//
//    @PostMapping("/users/reset")
//    public ResponseEntity<Response> resetPassword(@RequestBody ResetPasswordDto resetPassword) {
//        Response response = new Response();
//        try {
//            userService.resetPassword(resetPassword.getToken(), resetPassword.getNewPassword());
//            response.setStatus(HttpStatus.OK.value());
//            response.setStatusMessage(ResponseMessages.PASSWORD_RESET_SUCCESS);
//        } catch (NetZeroMediaException ex) {
//            log.error("Error in resetPassword: {}", ex.getMessage(), ex);
//            response.setStatusMessage(ex.getMessage());
//            response.setStatus(HttpStatus.BAD_REQUEST.value());
//        } catch (Exception ex) {
//            log.error("Error in resetPassword: {}", ex.getMessage(), ex);
//            response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        }
//        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
//
//    }

}