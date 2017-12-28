package com.desiremc.core;

/**
 * Used to allow our systems to be easily portable from place to place. Examples are Session and FSession both having a
 * sendMessage() method.
 * 
 * @author Michael Ziluck
 */
public interface Messageable
{

    /**
     * Sends a message to the {@link Messageable} entity.
     * 
     * @param message the message to send.
     */
    public void sendMessage(String message);

}
