package com.xm.recommendationservice.api.filters;


import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.redis.redisson.cas.RedissonBasedProxyManager;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpThrottlingFilter extends OncePerRequestFilter {

    private static final BucketConfiguration configuration = BucketConfiguration.builder()
            .addLimit(limit -> limit.capacity(10).refillGreedy(10, Duration.ofMinutes(1)))
            .build();

    private final RedissonBasedProxyManager<String> proxyManager;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = getClientIpAddress(request);

        try {
            // acquire cheap proxy to the bucket
            Bucket bucket = proxyManager.getProxy(clientIp, () -> configuration);

            // tryConsume returns false immediately if no tokens available
            if (bucket.tryConsume(1)) {
                filterChain.doFilter(request, response);
                return;
            }
        } catch (Exception e) {
            log.error("Rate limiting failed for IP {}. Bypassing filter.", clientIp, e);
            filterChain.doFilter(request, response);
            return;
        }

        sendTooManyRequestsResponse(response);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        // Find the client's IP address if its behind ALB/Nginx etc
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null || xForwardedForHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        // X-Forwarded-For can contain a comma-separated list of IPs. 
        // The first one is the original client.
        return xForwardedForHeader.split(",")[0].trim();
    }

    private void sendTooManyRequestsResponse(HttpServletResponse response) throws IOException {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString()); // Better to stringify Instant for JSON
        body.put("status", 429);
        body.put("error", "Too many requests");
        body.put("message", "Rate limit exceeded. Please try again later.");

        response.setContentType("application/json");
        response.setStatus(429);
        objectMapper.writeValue(response.getWriter(), body);
    }
}
