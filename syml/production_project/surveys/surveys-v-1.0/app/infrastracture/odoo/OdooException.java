package infrastracture.odoo;

public class OdooException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4851971038202619475L;

	public OdooException() {
		super();
	}

	public OdooException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public OdooException(String message, Throwable cause) {
		super(message, cause);
	}

	public OdooException(String message) {
		super(message);
	}

	public OdooException(Throwable cause) {
		super(cause);
	}

}
