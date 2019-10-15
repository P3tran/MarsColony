package com.pefthymiou.mars.security.ratelimit;

import com.google.common.collect.LinkedListMultimap;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class RequestsWindow {

    private final int windowSize;
    private final int threshold;
    private long lastEpochMinute;

    private LinkedListMultimap<String, Long> requestsPerIp;

    public RequestsWindow(int windowSize, int threshold) {
        requestsPerIp = LinkedListMultimap.create();
        this.windowSize = windowSize;
        this.threshold = threshold;
        lastEpochMinute = 0;
    }

    public synchronized void addRequestFrom(String ipAddress) {
        long epochSecond = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        requestsPerIp.put(ipAddress, epochSecond);

        long epochMinute = epochSecond - (epochSecond % 60);
        if (epochMinute > lastEpochMinute) {
            lastEpochMinute = epochMinute;
            cleanExpiredRequests();
        }
    }

    private void cleanExpiredRequests() {
        long expiredEpochMinute = lastEpochMinute - (windowSize * 60);

        for (String ipAddress : requestsPerIp.keySet()) {
            List<Long> requests = requestsPerIp.get(ipAddress);

            for (Long request : requests)
                if (request < expiredEpochMinute)
                    requests.remove(request);

            if (requests.isEmpty())
                requestsPerIp.removeAll(ipAddress);
        }
    }

    public synchronized boolean ipAddressReachedLimit(String ip) {
        int requestsFromIp = requestsPerIp.get(ip).size();
        return (requestsFromIp > threshold);
    }
}