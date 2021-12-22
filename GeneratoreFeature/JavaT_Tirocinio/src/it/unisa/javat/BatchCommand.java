package it.unisa.javat;


import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class GoodExec extends Thread {
	private InputStream _is;
	private String _type;

	public GoodExec(InputStream is, String type) {
		_is = is;
		_type = type;
	}

	public void run() {
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			try {
				isr = new InputStreamReader(_is);
				br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null) {
					if (!line.equals("")) {
						System.out.println(_type + " [" + line + "]");
					}
				}
			} finally {
				isr.close();
				br.close();
			}
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
}

public class BatchCommand {
	
	public void execCommand(String cmdline)
	{
		System.out.println("Command [" + cmdline + "]");
		java.lang.Process p = null;
		try
		{
			p = Runtime.getRuntime().exec(cmdline);
			new GoodExec(p.getErrorStream(), "ERROR").start();
			new GoodExec(p.getInputStream(), "OUTPUT").start();

			p.waitFor();
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		} catch (InterruptedException ie)
		{
			if (p != null)
				p.destroy();
		}
	}


}
