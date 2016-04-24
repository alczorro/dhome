/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package net.duckling.dhome.common.util.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PushbuttonField;

public class PDFTemplatePrinter {
	private static final Logger LOG = Logger
			.getLogger(PDFTemplatePrinter.class);
	private static final String TIME_FORMAT = "yyyyMMddHHmmssSSS";

	public static final String FONT_ARIALBD = "arialbd";
	public static final String FONT_BELL = "bell";
	public static final String FONT_HTOWERT = "htowert";
	public static final String FONT_HTOWERTI = "htowerti";
	public static final String FONT_MSYH = "msyh";
	public static final int SEGMENT_SIZE = 100;

	private PdfReader reader;

	public PDFTemplatePrinter(InputStream ins) {
		try {
			reader = new PdfReader(ins);
		} catch (IOException e) {
			LOG.error(e);
		}
	}

	public PDFTemplatePrinter(String path) {
		try {
			this.reader = new PdfReader(path);
		} catch (IOException e) {
			LOG.error(e);
		}
	}

	public String createPdf(List<Map<String, Object>> models,
			String templatePath, String cacheDir) throws IOException,
			DocumentException {
		setCHNFont();
		int segmentSize = (int) Math
				.ceil(models.size() / (double) SEGMENT_SIZE);
		String[] tempPath = new String[segmentSize];
		String[] names = new String[segmentSize];
		LOG.info(segmentSize);
		String fileKey = getTimeStamp();
		for (int i = 0; i < segmentSize; i++) {
			tempPath[i] = cacheDir + File.separator
					+ getTempCacheFileName("badge", fileKey, i + "");
			names[i] = getTempCacheFileName("badge", fileKey, i + "");
		}
		for (int i = 0; i < segmentSize; i++) {
			LOG.info(tempPath[i]);
			ByteArrayOutputStream tempBaos = getSegmentFileStream(models,
					templatePath, i);
			writeCacheFile(tempPath[i], tempBaos);
		}
		String badgeZipName = cacheDir + File.separator + "badge_" + fileKey
				+ ".zip";
		fileZip(badgeZipName, tempPath, names);
		return badgeZipName;
	}

	public void fileZip(String outFilePath, String files[], String names[]) {
		FileInputStream infile = null;
		ZipOutputStream oufile = null;
		try {
			oufile = new ZipOutputStream(new FileOutputStream(outFilePath));
			LOG.info("...........开始.......");
			for (int i = 0; i < files.length; i++) {
				File file = new File(files[i]);
				infile = new FileInputStream(file);
				// 创建压缩文件中的条目
				ZipEntry entry = new ZipEntry(names[i]);
				// 将创建好的条目加入到压缩文件中
				oufile.putNextEntry(entry);
				// 写入当前条目所对应的具体内容
				byte[] buff = new byte[1024];
				int len = 0;
				while ((len = infile.read(buff)) != -1) {
					oufile.write(buff, 0, len);
				}
				infile.close();
				file.delete();
			}
			LOG.info("...........结束.......");
		} catch (FileNotFoundException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		} finally {
			if (infile != null) {
				try {
					infile.close();
				} catch (IOException e) {
					LOG.error(e);
				}
			}
			if (oufile != null) {
				try {
					oufile.close();
				} catch (IOException e) {
					LOG.error(e);
				}

			}
		}
	}

	public ByteArrayOutputStream getPDFOutpuStream(Map<String, Object> model)
			throws IOException, DocumentException {
		ByteArrayOutputStream byteos = new ByteArrayOutputStream();
		PdfStamper stamper = new PdfStamper(reader, byteos);

		fillPDFForm(model, stamper);

		stamper.setFormFlattening(true);
		stamper.close();
		return byteos;
	}

	public PdfReader getPdfReader() {
		return reader;
	}


	public ByteArrayOutputStream getSegmentFileStream(
			List<Map<String, Object>> models, String templatePath, int segIndex)
			throws DocumentException, IOException, BadPdfFormatException {
		ByteArrayOutputStream segBaos = new ByteArrayOutputStream();
		Document segDoc = new Document();
		PdfCopy segPdfCopy = new PdfCopy(segDoc, segBaos);
		segDoc.open();
		int offset = (int) (SEGMENT_SIZE * (segIndex));
		int index = 0;
		for (int i = 0; i < SEGMENT_SIZE; i++) {
			index = offset + i;
			if (index >= models.size()) {
				break;
			}
			addSingleDataSheet(segPdfCopy, new PdfReader(templatePath),
					models.get(index));
			LOG.debug("Handle page:" + index);
		}
		segPdfCopy.close();
		segDoc.close();
		return segBaos;
	}

	public ByteArrayOutputStream getTempFileOutputStream(
			List<Map<String, Object>> models, String templatePath,
			String cacheDir) throws BadPdfFormatException, DocumentException,
			IOException {
		setCHNFont();
		return getSegmentFileStream(models, templatePath, 1);
	}

	public boolean isValidateTemplate(Set<String> validateSet)
			throws IOException, DocumentException, NullPointerException {
		boolean result = true;
		ByteArrayOutputStream byteos = new ByteArrayOutputStream();
		PdfStamper stamper = new PdfStamper(this.reader, byteos);
		result = isValidateForm(validateSet, result, stamper);
		stamper.close();
		return result;
	}
	
	private void addSingleDataSheet(PdfCopy copy, PdfReader templateReader,
			Map<String, Object> modelData) throws IOException,
			DocumentException, BadPdfFormatException {
		ByteArrayOutputStream pageBaos = new ByteArrayOutputStream();
		PdfStamper stamper = new PdfStamper(templateReader, pageBaos);
		fillPDFForm(modelData, stamper);
		stamper.setFormFlattening(true);
		stamper.close();
		PdfReader pageReader = new PdfReader(pageBaos.toByteArray());
		copy.addPage(copy.getImportedPage(pageReader, 1));
		templateReader.close();
		pageReader.close();
		pageBaos.close();
	}

	protected  void fillPDFForm(Map<String, Object> model, PdfStamper stamper)
			throws IOException, DocumentException {
		AcroFields form = stamper.getAcroFields();

		Iterator<String> formFldsIt = form.getFields().keySet().iterator();
		// according pdf model to iterator form fields and set the fields' value
		while (formFldsIt.hasNext()) {
			String keyStrOld = formFldsIt.next();

			String keyStr = (keyStrOld
					.substring(keyStrOld.lastIndexOf('.') + 1));
			keyStr = keyStr.substring(0, keyStr.lastIndexOf('['));
			if (keyStr == null || keyStr.equals("snBarCode")) {
				continue;
			}
			if (model.containsKey(keyStr) && model.get(keyStr) != null) {
				form.setField(keyStrOld, model.get(keyStr).toString());
			}
		}
		if (model.containsKey("snBarCode")) {
			BarcodeQRCode qrcode = new BarcodeQRCode(model.get("snBarCode")
					.toString(), 3, 3, null);
			Image img = qrcode.getImage();
			PushbuttonField bt = form.getNewPushbuttonFromField("snBarCode");
			bt.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
			bt.setProportionalIcon(false);
			bt.setImage(img);
			form.replacePushbuttonField("snBarCode", bt.getField());
		}
	}

	private String getTempCacheFileName(String prefix, String timestamp,
			String i) {
		return prefix + "_" + timestamp + "_" + i + ".pdf";
	}

	private String getTimeStamp() {
		SimpleDateFormat sf = new SimpleDateFormat(TIME_FORMAT);
		return sf.format(new Date());
	}

	private boolean isValidateForm(Set<String> validateSet, boolean result,
			PdfStamper stamper) {
		AcroFields form = stamper.getAcroFields();
		for (String key : validateSet) {
			if (form.getField(key) == null) {
				return false;
			}
		}
		return result;
	}

	private void setCHNFont() {
		FontSelector selector = new FontSelector();
		selector.addFont(FontFactory.getFont(FontFactory.TIMES_ROMAN, 12));
		selector.addFont(FontFactory.getFont("STSongStd-Light", "UniGB-UCS2-H",
				BaseFont.NOT_EMBEDDED));
	}

	private void writeCacheFile(String filepath, ByteArrayOutputStream baos) {
		File tempFile = new File(filepath);
		if (!tempFile.getParentFile().exists()) {
			tempFile.getParentFile().mkdirs();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(filepath));
			fos.write(baos.toByteArray());
			fos.flush();
		} catch (FileNotFoundException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					LOG.error(e);
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					LOG.error(e);
				}
			}
		}
	}
}