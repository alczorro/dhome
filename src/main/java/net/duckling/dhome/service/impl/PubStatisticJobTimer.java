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
package net.duckling.dhome.service.impl;

import net.duckling.dhome.service.ISystemJobService;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
/**
 * 论文统计任务的定时器
 * @author Yangxp
 *
 */
public class PubStatisticJobTimer extends QuartzJobBean {

	private static final Logger LOG = Logger.getLogger(PubStatisticJobTimer.class);
	private ISystemJobService sjs;
	
	public ISystemJobService getSjs() {
		return sjs;
	}

	public void setSjs(ISystemJobService sjs) {
		this.sjs = sjs;
	}
	/**
	 * 定时执行所有机构的论文统计
	 * @param arg0
	 * @throws JobExecutionException
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			sjs.synchronizePaperStatistics();
		} catch (Exception e) {
			LOG.error("AnalysisTime", e);
		}
	}

}
