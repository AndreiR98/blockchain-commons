package uk.co.roteala.exceptions;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

public class ApplicationRuntimeException extends RuntimeException{
    private static final long serialVersionUID = -7331L;

    private ErrorCode errorCode;

    public ApplicationRuntimeException(ErrorCode errorCode) {
        super(ApplicationRuntimeException.ResourceBundleSource.getInstance().getMessage(errorCode.getKey(), (Object[])null, (Locale)null));
        this.errorCode = errorCode;
    }

    public ApplicationRuntimeException(ErrorCode errorCode, Object... args) {
        super(ApplicationRuntimeException.ResourceBundleSource.getInstance().getMessage(errorCode.getKey(), args, (Locale)null));
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    private static class ResourceBundleSource {
        private static final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        private ResourceBundleSource() {
        }

        public static ResourceBundleMessageSource getInstance() {
            return messageSource;
        }

        static {
            messageSource.setBasenames(new String[]{"i18n.errors"});
        }
    }
}
