package org.jsoup.parser;

/**
 * A Parse Error records an error in the input HTML that occurs in either the tokenisation or the tree building phase.
 */
public class ParseError {
    private int pos;
    private String errorMsg;
    
    //!!!
    private boolean custom = false;

    ParseError(int pos, String errorMsg) {
        this.pos = pos;
        this.errorMsg = errorMsg;
        this.custom = false;
    }

    ParseError(int pos, String errorMsg, boolean isCustom) {
        this.pos = pos;
        this.errorMsg = errorMsg;
        this.custom = isCustom;
    }
    
    ParseError(int pos, String errorFormat, Object... args) {
        this.errorMsg = String.format(errorFormat, args);
        this.pos = pos;
    }

    /**
     * Retrieve the error message.
     * @return the error message.
     */
    public String getErrorMessage() {
        return errorMsg;
    }

    /**
     * Retrieves the offset of the error.
     * @return error offset within input
     */
    public int getPosition() {
        return pos;
    }

    @Override
    public String toString() {
        return pos + ": " + errorMsg;
    }

    //!!!
	public boolean isCustom() {
		return custom;
	}

	public void setCustom(boolean custom) {
		this.custom = custom;
	}
}
