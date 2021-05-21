package br.com.astrosoft.framework.util;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;

public class DetectFormat {
	public static void main(String[] args) throws IOException, URISyntaxException {
		DetectFormat detectFormat = new DetectFormat();
		String urlLink = "https://images-shoptime.b2w" +
		                 ".io/produtos/01/00/img/1663897/7/1663897787_2SZ.jpg";
		String format = detectFormat.getImageFileExtFromUrl(new URL(urlLink));
		System.out.println(format);
	}

	public String getImageFileExtFromUrl(URL urlObject) throws URISyntaxException, IOException {
		System.out.println("IN DOWNLOAD FILE FROM URL METHOD");
		String tmpFolder = System.getProperty("java.io.tmpdir");
		String tmpFileStr = tmpFolder + "/" + new Date().getTime();
		ReadableByteChannel readableByteChannel = Channels.newChannel(urlObject.openStream());
		FileOutputStream fileOutputStream = new FileOutputStream(tmpFileStr);
		FileChannel fileChannel = fileOutputStream.getChannel();
		//Files.copy(urlObject.openStream(), Paths.get(tmpFileStr), StandardCopyOption
		// .REPLACE_EXISTING);
		fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
		File download = new File(tmpFileStr);
		System.out.println("FILE DOWNLOAD EXISTS: " + download.exists());
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(download);
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			ImageReader reader = iter.next();
			String formatName = reader.getFormatName();
			System.out.println("FOUND IMAGE FORMAT :" + formatName);
			iis.close();
			return formatName;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Files.delete(Paths.get(tmpFileStr));
		}
		return null;
	}
}


