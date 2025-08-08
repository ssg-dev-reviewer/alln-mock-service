package com.ssg.api.ssgallnmock.util;

import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class RandomExceptionGenerator {
    
    private final Random random = new Random();
    
    // 10% 확률로 예외 발생
    private static final double EXCEPTION_PROBABILITY = 0.1;
    
    // 10% 확률로 timeout 발생
    private static final double TIMEOUT_PROBABILITY = 0.1;

    public void sleepTimeoutIfNeeded() {
        // Timeout 체크 (10% 확률)
        if (random.nextDouble() < TIMEOUT_PROBABILITY) {
            try {
                // 5-10초 사이의 랜덤한 시간만큼 대기
                int timeoutSeconds = 5 + random.nextInt(6); // 5-10초
                Thread.sleep(timeoutSeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("요청이 중단되었습니다.", e);
            }
        }
    }
    public void throwRandomExceptionIfNeeded() {
        // 예외 체크 (10% 확률)
        if (random.nextDouble() < EXCEPTION_PROBABILITY) {
            int exceptionType = random.nextInt(3);
            
            switch (exceptionType) {
                case 0:
                    throw new RuntimeException("제휴사 서버 일시적 오류가 발생했습니다.");
                case 1:
                    throw new RuntimeException("제휴사 API 인증 실패");
                case 2:
                    throw new RuntimeException("제휴사 시스템 점검 중입니다.");
                default:
                    throw new RuntimeException("알 수 없는 오류가 발생했습니다.");
            }
        }
    }
} 