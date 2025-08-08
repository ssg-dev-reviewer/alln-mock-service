package com.ssg.api.ssgallnmock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String requestPath = request.getRequestURI();
        
        // Partner3 API 경로인지 확인
        if (requestPath.startsWith("/api/partner3")) {
            return handlePartner3Exception(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        // 기존 JSON 응답 (다른 API들)
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("path", requestPath);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String requestPath = request.getRequestURI();
        
        // Partner3 API 경로인지 확인
        if (requestPath.startsWith("/api/partner3")) {
            return handlePartner3Exception(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        // 기존 JSON 응답 (다른 API들)
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "알 수 없는 오류가 발생했습니다.");
        errorResponse.put("path", requestPath);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    
    private ResponseEntity<String> handlePartner3Exception(Exception ex, HttpStatus status) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        String xmlResponse = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<errorResponse>\n" +
            "    <code>%d</code>\n" +
            "    <description>%s</description>\n" +
            "    <timestamp>%s</timestamp>\n" +
            "    <error>Internal Server Error</error>\n" +
            "</errorResponse>", 
            status.value(), 
            ex.getMessage() != null ? ex.getMessage() : "알 수 없는 오류가 발생했습니다.",
            timestamp);
        
        return ResponseEntity.status(status)
            .contentType(MediaType.APPLICATION_XML)
            .body(xmlResponse);
    }
} 
