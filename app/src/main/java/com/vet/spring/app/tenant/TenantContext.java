package com.vet.spring.app.tenant;

/**
 * Contexto ThreadLocal para almacenar el ID del tenant actual.
 * Cada request HTTP tendrá su propio tenant ID almacenado de forma aislada.
 */
public class TenantContext {
    
    private static final ThreadLocal<Integer> CURRENT_TENANT = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_TENANT_CODE = new ThreadLocal<>();
    
    /**
     * Establece el ID del tenant para el hilo actual
     */
    public static void setTenantId(Integer tenantId) {
        CURRENT_TENANT.set(tenantId);
    }
    
    /**
     * Obtiene el ID del tenant del hilo actual
     */
    public static Integer getTenantId() {
        return CURRENT_TENANT.get();
    }
    
    /**
     * Establece el código del tenant para el hilo actual
     */
    public static void setTenantCode(String tenantCode) {
        CURRENT_TENANT_CODE.set(tenantCode);
    }
    
    /**
     * Obtiene el código del tenant del hilo actual
     */
    public static String getTenantCode() {
        return CURRENT_TENANT_CODE.get();
    }
    
    /**
     * Limpia el contexto del tenant (importante para evitar memory leaks)
     */
    public static void clear() {
        CURRENT_TENANT.remove();
        CURRENT_TENANT_CODE.remove();
    }
    
    /**
     * Verifica si hay un tenant establecido en el contexto actual
     */
    public static boolean hasTenant() {
        return CURRENT_TENANT.get() != null;
    }
}
