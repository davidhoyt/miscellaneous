package com.ticketfly;

/**
 * Defines a callback that's used to facilitate a more loosely-coupled
 * relationship between two classes.
 *
 * Class "A" using class "B" can pass a reference to an instance of this
 * class. "A" defines work to do on itself, but "B" is the one that calls
 * the method to do it. This allows "B" to safely call into its creator
 * without knowing anything about it.
 *
 * @author David Hoyt &lt;dhoyt@hoytsoft.org&gt;
 */
public interface CrossCallback {
    void callback();
}
