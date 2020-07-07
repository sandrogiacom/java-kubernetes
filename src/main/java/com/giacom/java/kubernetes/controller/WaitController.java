package com.giacom.java.kubernetes.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wait")
public class WaitController {

    @GetMapping
    public String waitingFor() throws InterruptedException, UnknownHostException {
        InetAddress ip = InetAddress.getLocalHost();
        System.out.println("*** Your current host/IP address : " + ip);
        System.out.println("*** start waiting for 10 seconds...");
        Thread.sleep(10000);
        System.out.println("*** end of waiting... ");
        return "Sorry for being late...";
    }

}
