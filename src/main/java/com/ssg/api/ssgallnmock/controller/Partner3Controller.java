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
@RequestMapping("/api/partner3")
public class Partner3Controller {
    
    @Autowired
    private RandomExceptionGenerator exceptionGenerator;
    
    private final Random random = new Random();
    
    // 주문 생성 REST API
    @PostMapping(value = "/orders", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> createOrder(@RequestBody String request) {
        // 랜덤 예외 발생
        exceptionGenerator.throwRandomExceptionIfNeeded();

        // 제휴사 주문번호 생성
        String affiliateOrderNumber = "P3_" + System.currentTimeMillis();
        
        // 성공 응답 XML 생성
        String response = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<orderResponse>\n" +
            "    <code>200</code>\n" +
            "    <description>주문이 성공적으로 생성되었습니다.</description>\n" +
            "    <affiliateOrderNumber>%s</affiliateOrderNumber>\n" +
            "    <orderState>RECEIVED</orderState>\n" +
            "</orderResponse>", affiliateOrderNumber);
        
        return ResponseEntity.ok(response);
    }
    
    // 주문 상태 조회 REST API
    @GetMapping(value = "/orders/{transactionNumber}/status", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getOrderStatus(@PathVariable String transactionNumber, @RequestParam(value = "productId") String productId) {
        // 랜덤 예외 발생
        exceptionGenerator.throwRandomExceptionIfNeeded();
        
        // 랜덤 주문 상태 생성
        String[] statuses = {"RECEIVED", "PREPARING", "IN_TRANSIT", "DELIVERED"};
        String randomStatus = statuses[random.nextInt(statuses.length)];
        
        String response = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<statusResponse>\n" +
            "    <code>200</code>\n" +
            "    <description>주문 상태 조회 성공</description>\n" +
            "    <orderState>%s</orderState>\n" +
            "    <modifiedAt>%s</modifiedAt>\n" +
            "    <shippingMethod>택배</shippingMethod>\n" +
            "</statusResponse>", randomStatus, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        return ResponseEntity.ok(response);
    }
    
    // 주문 취소 REST API
    @PostMapping(value = "/orders/{transactionNumber}/cancel", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> cancelOrder(@PathVariable String transactionNumber, @RequestBody String request) {
        // 랜덤 예외 발생
        exceptionGenerator.throwRandomExceptionIfNeeded();
        
        // 성공 응답 XML 생성
        String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<cancelResponse>\n" +
            "    <code>200</code>\n" +
            "    <description>주문이 성공적으로 취소되었습니다.</description>\n" +
            "    <cancelStatus>CANCELLED</cancelStatus>\n" +
            "</cancelResponse>";
        
        return ResponseEntity.ok(response);
    }
} 
