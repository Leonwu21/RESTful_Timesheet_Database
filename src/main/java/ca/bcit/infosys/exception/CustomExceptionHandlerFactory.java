package ca.bcit.infosys.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * Class to create ExceptionHandler objects.
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 *
 */
public class CustomExceptionHandlerFactory extends ExceptionHandlerFactory {
    /**
     * The exception handler factory.
     */
    private final ExceptionHandlerFactory parent;

    /**
     * Constructor for CustomExceptionHandlerFactory. Handles JSF.
     * @param exceptionHandlerFactory The ExceptionHandlerFactory.
     */
    @SuppressWarnings("deprecation")
    public CustomExceptionHandlerFactory(ExceptionHandlerFactory exceptionHandlerFactory) {
        parent = exceptionHandlerFactory;
    }

    @Override
    /**
     * Gets the newly created instance of the exception handler.
     * @return The ExceptionHandler.
     */
    public ExceptionHandler getExceptionHandler() {
        final ExceptionHandler exceptionHandler = new CustomExceptionHandler(parent.getExceptionHandler());
        return exceptionHandler;
    }

}
