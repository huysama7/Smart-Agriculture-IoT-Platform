package com.huysama.builderDto.config;

public class UserContextHolder {
    private static final ThreadLocal<String> currentUserId = new ThreadLocal<>();
    private static final ThreadLocal<String> currentOrgId = new ThreadLocal<>();

    public static String getCurrentUserId() {
        return currentUserId.get();
    }

    public static void setCurrentUserId(String userId) {
        currentUserId.set(userId);
    }

    public static String getCurrentOrgId() {
        return currentOrgId.get();
    }

    public static void setCurrentOrgId(String orgId) {
        currentOrgId.set(orgId);
    }

    public static void clear() {
        currentUserId.remove();
        currentOrgId.remove();
    }
}
