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
package net.duckling.dhome.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.duckling.dhome.crawler.crawler.CrawlTask;
import net.duckling.dhome.crawler.crawler.ISISite;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionPaperCiteQueue;
import net.duckling.dhome.service.IInstitutionBackendPaperService;
import net.duckling.dhome.service.IInstitutionPaperCiteQueueService;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
/**
 * 论文引用抓取任务
 * @author brett
 *
 */
public class InstitutionPaperCiteJob extends QuartzJobBean {

	private static final Logger LOG = Logger.getLogger(InstitutionPaperCiteJob.class);
	
	private IInstitutionPaperCiteQueueService institutionPaperCiteQueueService;
	private IInstitutionBackendPaperService paperService;
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		LOG.info("institution paper cite job started...");

		InstitutionPaperCiteQueue item = institutionPaperCiteQueueService.getFirst();
		if(item!=null){
			InstitutionPaper paper = paperService.getPaperById(item.getPaperId());
			try {
				if(paper!=null){
					excuteCrawl(paper);
				}
			} catch (Exception e) {
				LOG.error("Cite crawl error. {paper_id:"+ paper.getId() +", title:"+ paper.getTitle() +"} :" + e.getMessage());
			}finally{
				institutionPaperCiteQueueService.delete(paper.getId());
			}
		}
		
		LOG.info("institution paper cite job ended.");
	}
	
	
	private String excuteCrawl(InstitutionPaper paper) throws Exception{
		CrawlTask crawlTask = new CrawlTask();
		String doi = paper.getDoi();
		String cite = "";
		if(doi != null && !doi.equals("")){
			doi = doi.replace("doi:", "");
			doi = doi.replace("DOI:", "");
			doi = doi.replace("DOI", "");
			
			crawlTask.setQuery(doi);
	        crawlTask.setType(CrawlTask.DO);
	        ISISite isiSite = new ISISite(crawlTask);
			
			cite = isiSite.getCite();
			
			LOG.info("crawl("+ CrawlTask.DO +") finished.{doi:"+doi+",cite:"+cite+"}");
		}
		
		if(cite.equals("")){
			crawlTask.setQuery(paper.getTitle());
	        crawlTask.setType(CrawlTask.TI);
	        ISISite isiSite = new ISISite(crawlTask);
			
			cite = isiSite.getCite();
			
			LOG.info("crawl("+ CrawlTask.TI +") finished.{title:"+paper.getTitle()+",cite:"+cite+"}");
		}
		
		if(!cite.equals("")){
			paper.setCitation(cite);
		}else{
			cite = "--";
			paper.setCitation("-1");
		}
		paper.setCitationQueryTime(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
		paperService.updatePaper(paper);
		return cite;
	}

	
	
	public void setInstitutionPaperCiteQueueService(
			IInstitutionPaperCiteQueueService institutionPaperCiteQueueService) {
		this.institutionPaperCiteQueueService = institutionPaperCiteQueueService;
	}
	public void setPaperService(IInstitutionBackendPaperService paperService) {
		this.paperService = paperService;
	}
}
