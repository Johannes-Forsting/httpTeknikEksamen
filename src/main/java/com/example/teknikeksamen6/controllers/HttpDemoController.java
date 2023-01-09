package com.example.teknikeksamen6.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HttpDemoController {


    @GetMapping("/redirect")
    void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("redirect.html");
    }

    @GetMapping("/uups")
    void ups(HttpServletResponse response) throws IOException {
        int x = 100/0;
    }


    @GetMapping("/listHeaders")
    public ResponseEntity<String> listAllHeaders(@RequestHeader Map<String, String> headers) {
        String foundHeaders = headers.keySet().stream().map(key -> key + " : "+headers.get(key)).
                collect(Collectors.joining("<br/>"));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<String>(foundHeaders,responseHeaders, HttpStatus.OK);
    }

    boolean httpOnly = true;


    @PostMapping("/dummy-login")
    public ResponseEntity<String> login(HttpServletResponse response) {
        //No spaces in token to simplify things
        String token = "VerySecretTokenhd6a868afahkjhfkafajk";
        Cookie cookie = new Cookie("Token",token);
        cookie.setPath("/");
        cookie.setSecure(httpOnly);
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
        return new ResponseEntity<String>("Logged in",HttpStatus.OK);
    }

    @PostMapping("/clear")
    public void clearCookies(HttpSession session, HttpServletResponse response){
        //It's easy to delete the session cookie
        session.invalidate();
        //Other cookies must be deleted like this
        Cookie cookie = new Cookie("Token",null);
        cookie.setPath("/");
        cookie.setSecure(httpOnly);
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


    @GetMapping("/see-all-from-session")
    public ResponseEntity<List<String>> readMessages( HttpSession session) {
        List<String> messages = (List<String>) session.getAttribute("SESSION_MESSAGES");
        if (messages == null) {
            messages = new ArrayList<>();
        }
        return new ResponseEntity<List<String>>(messages,HttpStatus.OK);
    }

    @PostMapping("/save-txt/{msg}")
    public String persistMessage(@PathVariable("msg") String msg, HttpServletRequest request) {
        List<String> messages = (List<String>) request.getSession().getAttribute("SESSION_MESSAGES");
        if (messages == null) {
            messages = new ArrayList<>();
            request.getSession().setAttribute("SESSION_MESSAGES", messages);
        }
        messages.add(msg);
        request.getSession().setAttribute("SESSION_MESSAGES", messages);
        return "{\"status\": \"OK\"}";
    }


}

