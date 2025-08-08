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
@RequestMapping("/api/partner1")
public class Partner1Controller {
    
    @Autowired
    private RandomExceptionGenerator exceptionGenerator;
    
    private final Random random = new Random();
    
    // 주문 생성 API
    @PostMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOrder(@RequestBody String request) {
        // 랜덤 예외 발생
        exceptionGenerator.throwRandomExceptionIfNeeded();

        // 제휴사 주문번호 생성
        String partnerOrderNumber = "P1_" + System.currentTimeMillis();
        
        // 성공 응답 JSON 생성
        String response = String.format("{\n" +
            "    \"resultCode\": \"SUCCESS\",\n" +
            "    \"resultMessage\": \"주문이 성공적으로 생성되었습니다.\",\n" +
            "    \"partnerOrderNumber\": \"%s\",\n" +
            "    \"orderStatus\": \"주문접수\"\n" +
            "}", partnerOrderNumber);

        return ResponseEntity.ok(response);
    }
    
    // 주문 상태 조회 API
    @GetMapping(value = "/orders/{orderNumber}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrderStatus(@PathVariable String orderNumber, @RequestParam(value = "itemCode") String itemCode) {
        // 랜덤 예외 발생
        exceptionGenerator.throwRandomExceptionIfNeeded();
        
        // 랜덤 주문 상태 생성
        String[] statuses = {"주문접수", "상품준비중", "배송중", "배송완료"};
        String randomStatus = statuses[random.nextInt(statuses.length)];
        
        String response = String.format("{\n" +
            "    \"resultCode\": \"SUCCESS\",\n" +
            "    \"resultMessage\": \"주문 상태 조회 성공\",\n" +
            "    \"orderStatus\": \"%s\",\n" +
            "    \"lastUpdateTime\": \"%s\",\n" +
            "    \"deliveryCompany\": \"CJ배송\"\n" +
            "}", randomStatus, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        return ResponseEntity.ok(response);
    }
    
    // 주문 취소 API
    @PostMapping(value = "/orders/{orderNumber}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cancelOrder(@PathVariable String orderNumber, @RequestBody String request) {
        // 랜덤 예외 발생
        exceptionGenerator.throwRandomExceptionIfNeeded();
        
        // 성공 응답 JSON 생성
        String response = "{\n" +
            "    \"resultCode\": \"SUCCESS\",\n" +
            "    \"resultMessage\": \"주문이 성공적으로 취소되었습니다.\",\n" +
            "    \"cancelStatus\": \"CANCELLED\"\n" +
            "}";
        
        return ResponseEntity.ok(response);
    }
} 
