package com.swiftdating.app.data.network;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class Resource<T>
{
    private static final int NO_CODE = -1;
    @NonNull
    public final Status status;
    @Nullable
    public final String message;
    @Nullable
    public final T data;
    public final int code;
    public final Exception exception;

    public final Throwable throwable;

    public Resource(@NonNull Status status, @Nullable T data, @Nullable String message, int code,
                    @Nullable Exception e) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.code = code;
        this.exception = e;
        this.throwable = null;
    }

    public Resource(@NonNull Status status, @Nullable T data, @Nullable String message, int code,
                    @Nullable Throwable t) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.code = code;
        this.exception = null;
        this.throwable = t;
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(Status.SUCCESS, data, null, NO_CODE, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data, int code,
                                        @Nullable Exception e) {
        return new Resource<>(Status.ERROR, data, msg, code, e);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data, int code,
                                        @Nullable Throwable t) {
        return new Resource<>(Status.ERROR, data, msg, code, t);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null, NO_CODE, null);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Resource<?> resource = (Resource<?>) o;

        if (status != resource.status) {
            return false;
        }
        if (message != null ? !message.equals(resource.message) : resource.message != null) {
            return false;
        }
        if (code != resource.code) {
            return false;
        }
        return data != null ? data.equals(resource.data) : resource.data == null;
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", code=" + code +
                '}';
    }
}
