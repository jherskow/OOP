/**
 * Represents a code protected electronic gate.
 * The gate can be opened or closed using its methods.
 * In order to open the gate one must enter the right code.
 * @author OOP staff
 * @see ElectronicDevice
 */
public class ElectronicGate extends ElectronicDevice {
	/**
	 * The code length in digits. It is a constant (static final).
	 */
	public static final int CODE_DIGIT_COUNT = 4;
	private final String encryptedGateCode;
	private boolean isOpen;
	/**
	 * Constructor.
	 * @param code The code of the gate. This code will be used to verify
	 *
	a request to open the gate.
	 * @param brandName Manufacturer of the electronic gate.
	 */
	public ElectronicGate(int code, String brandName) {
		super(brandName);
		encryptedGateCode = encrypt(code);
		isOpen = false;
	}
	/**
	 * Opens the electronic gate. If the gate is already opened, the method
	 * will have no effect.
	 * The code must match the gate code or no effect will take place.
	 * @param code The gate code. Only a valid code will open the gate.
	 * @return true if the gate was opened successfully, false otherwise.
	 * @throws ElectronicMachanismException On electric fault.
	 */
	public boolean open(int code) throws ElectronicMachanismException {
		if (!encrypt(code).equals(encryptedGateCode)) {
			return false;
		}
		isOpen = true;
		return true;
	}
	/**
	 * Closes the gate. If the gate is already closed, there’s no effect.
	 * @throws ElectronicMachanismException On electric fault.
	 */
	public void close() throws ElectronicMachanismException {
		isOpen = false;
	}/*
* Implementation comment for private methods.
*
* Encrypt the specified integer. Encryption is consistent for a given
* number.
*/
	private static String encrypt(int num) {
...
	}
}