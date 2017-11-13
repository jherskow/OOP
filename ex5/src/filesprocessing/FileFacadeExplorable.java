package filesprocessing;

import java.io.*;

/**
 * A class which wraps a file in the interface methods of the Explorable Interface.
 *
 * @author jherskow
 * @author felber
 */
class FileFacadeExplorable implements Explorable {
	private File file;
	private static final double KIBIBYTE = 1024;

	/**
	 * Wraps a file in the interface methods of the Explorable Interface
	 * @param file file to wrap
	 */
	FileFacadeExplorable(File file){
		this.file = file;
	}

	@Override
	public double getSize() {
		return file.length()/ KIBIBYTE;
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public boolean canWrite() {
		return file.canWrite();
	}

	@Override
	public boolean canExecute() {
		return file.canExecute();
	}

	@Override
	public String getType() {
		String[] splitPath = file.getAbsolutePath().split("\\.");
		return splitPath[splitPath.length-1];
	}

	@Override
	public boolean isHidden() {
		return file.isHidden();
	}

	@Override
	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}
}
