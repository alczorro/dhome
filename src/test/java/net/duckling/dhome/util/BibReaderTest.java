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
package net.duckling.dhome.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;
import net.duckling.dhome.common.exception.BibResolveFailedException;
import net.duckling.dhome.common.util.BibReader;
import net.duckling.dhome.domain.people.Paper;

import org.junit.Test;

public class BibReaderTest {
	private BibReader br = null;
	private int uid=108;
	
	@Test
	public void testAnalyze() throws BibResolveFailedException, IOException{
		String curPath = this.getClass().getResource("").getPath();
		File bib = new File(curPath+"bibs/test.bib");
		InputStream in = new FileInputStream(bib);
		br = new BibReader(in,uid);
		List<Paper> papers = br.analyze((int)bib.length());
		Assert.assertNotNull(papers);
		Assert.assertEquals(1, papers.size());
		Paper paper = papers.get(0);
		Assert.assertEquals("Alattar", paper.getAuthors());
	}
	
	@Test
	public void testAnalyzeNoTitleAuthorSourceBib() throws BibResolveFailedException, IOException{
		String curPath = this.getClass().getResource("").getPath();
		InputStream in = new FileInputStream(new File(curPath+"bibs/wrong.bib"));
		br = new BibReader(in,uid);
		List<Paper> papers = br.analyze(in.available());
		Assert.assertNotNull(papers);
		Assert.assertEquals(0, papers.size());
	}
	
	@Test
	public void testAnalyzeNotBib() throws IOException{
		String curPath = this.getClass().getResource("").getPath();
		File bib = new File(curPath+"bibs/wrong.txt");
		InputStream in = new FileInputStream(bib);
		br = new BibReader(in,uid);
		List<Paper> papers = null;
		try{
			papers = br.analyze((int)bib.length());
		}catch(BibResolveFailedException e){
			Assert.assertNull(papers);
		}
	}

}
