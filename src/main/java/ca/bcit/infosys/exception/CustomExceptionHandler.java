package ca.bcit.infosys.exception;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 * Class to handle exceptions.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 *
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {
    
    /**
     * The exception handler.
     */
    private final ExceptionHandler wrapped;

    /**
     * Constructor for CustomExceptionHandler.
     * @param exception The exception handler.
     */
    @SuppressWarnings("deprecation")
    CustomExceptionHandler(ExceptionHandler exception) {
        wrapped = exception;
    }

    /**
     * Gets the exception handler.
     * @return The exception handler.
     */
    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    /**
     * Handles thrown exception.
     */
    @Override
    public void handle() throws FacesException {

        final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();
        while (iterator.hasNext()) {
            final ExceptionQueuedEvent event = iterator.next();
            final ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            // Gets exception fron context
            final Throwable throwable = context.getException();

            final FacesContext facesContext = FacesContext.getCurrentInstance();

            // Do what you want from exception
            try {

                facesContext.addMessage(null, new FacesMessage(throwable.getLocalizedMessage()));

            } finally {
                // Remove exception from queue
                iterator.remove();
            }
        }
        // Parent handle
        getWrapped().handle();
    }
}
