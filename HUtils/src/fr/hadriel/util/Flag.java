package fr.hadriel.util;

public final class Flag {
    private static final int NONE = 0;

    private int value;

    public Flag() {
        this.value = NONE;
    }

    /**
     * Clears all flags
     */
    public void clear() {
        value = NONE;
    }

    /**
     * Raises the flag
     * @param flag the flag to raise
     */
    public void raise(int flag) {
        value |= flag;
    }

    /**
     * Clears the flag
     * @param flag the flag to clear
     */
    public void clear(int flag) {
        value &= ~flag;
    }

    /**
     * Test if a flag is raised
     * @param flag the flag to test
     * @return true if the flag is raised
     */
    public boolean isRaised(int flag) {
        return (value & flag) != 0;
    }

    /**
     * Test if any flag is raised
     * @return true if any flag is raised
     */
    public boolean anyRaised() {
        return value != NONE;
    }
}