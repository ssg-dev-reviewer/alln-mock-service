package com.ssg.api.ssgallnmock.controller;

import com.ssg.api.ssgallnmock.util.RandomExceptionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@RestController
@RequestMapping("/api/partner2")
public class Partner2Controller {
    
    @Autowired
    private RandomExceptionGenerator exceptionGenerator;
    
    private final Random random = new Random();
    
    // 주문 생성 API
    @PostMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOrder(@RequestBody String request) {

        //Sleep Time
        exceptionGenerator.sleepTimeoutIfNeeded();

        // 랜덤 예외 발생
        exceptionGenerator.throwRandomExceptionIfNeeded();

        // 제휴사 주문번호 생성
        String merchantOrderId = "P2_" + System.currentTimeMillis();
        
        // 성공 응답 JSON 생성
        String response = String.format("{\n" +
            "    \"status\": \"SUCCESS\",\n" +
            "    \"message\": \"주문이 성공적으로 생성되었습니다.\",\n" +
            "    \"merchantOrderId\": \"%s\",\n" +
            "    \"currentStatus\": \"ORDER_RECEIVED\"\n" +
            "}", merchantOrderId);

        return ResponseEntity.ok(response);
    }
    
    // 주문 상태 조회 API
    @GetMapping(value = "/orders/{orderId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrderStatus(@PathVariable String orderId, @RequestParam(value = "sku") String sku) {

        //Sleep Time
        exceptionGenerator.sleepTimeoutIfNeeded();

        // 랜덤 예외 발생
        exceptionGenerator.throwRandomExceptionIfNeeded();
        
        // 랜덤 주문 상태 생성
        String[] statuses = {"ORDER_RECEIVED", "PROCESSING", "SHIPPING", "DELIVERED"};
        String randomStatus = statuses[random.nextInt(statuses.length)];
        
        String response = String.format("{\n" +
            "    \"status\": \"SUCCESS\",\n" +
            "    \"message\": \"주문 상태 조회 성공\",\n" +
            "    \"currentStatus\": \"%s\",\n" +
            "    \"updatedAt\": \"%s\",\n" +
            "    \"processingCenter\": \"부산처리센터\"\n" +
            "}", randomStatus, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        return ResponseEntity.ok(response);
    }
    
    // 주문 취소 API
    @PostMapping(value = "/orders/{orderId}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cancelOrder(@PathVariable String orderId, @RequestBody String request) {

        //Sleep Time
        exceptionGenerator.sleepTimeoutIfNeeded();

        // 랜덤 예외 발생
        exceptionGenerator.throwRandomExceptionIfNeeded();
        
        // 성공 응답 JSON 생성
        String response = "{\n" +
            "    \"status\": \"SUCCESS\",\n" +
            "    \"message\": \"주문이 성공적으로 취소되었습니다.\",\n" +
            "    \"cancelStatus\": \"CANCELLED\"\n" +
            "}";
        
        return ResponseEntity.ok(response);
    }
} 
