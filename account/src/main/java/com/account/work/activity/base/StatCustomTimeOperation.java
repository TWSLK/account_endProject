package com.account.work.activity.base;

/**
 * Custom time section, statistical operation interface, custom time
 */

public interface StatCustomTimeOperation{
    int getStartDate();
    int getEndDate();
    void clearSelectionDate();

    /**
     * Move to custom page
     */
    void moveToCustom();
}
