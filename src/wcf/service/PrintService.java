package wcf.service;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

public class PrintService {
	public static void print(byte[] bytes) throws PrintException {
		// Adapted from https://docs.oracle.com/javase/7/docs/technotes/guides/jps/spec/jpsOverview.fm4.html
		DocFlavor format = DocFlavor.BYTE_ARRAY.TEXT_PLAIN_US_ASCII;
		Doc doc = new SimpleDoc(bytes, format, null);
		PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
		attributes.add(new Copies(1));
		attributes.add(MediaSizeName.ISO_A4);
		attributes.add(Sides.ONE_SIDED);
		javax.print.PrintService[] services = PrintServiceLookup.lookupPrintServices(format, attributes);
		if (services.length > 0) {
			services[0].createPrintJob().print(doc, attributes);
		}
	}
}
