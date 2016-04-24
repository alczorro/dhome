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
package net.duckling.dhome.service;
/**
 * 系统定时任务服务
 * @author Yangxp
 * @since 2012-09-18
 */

public interface ISystemJobService {
	/**
	 * 同步机构的论文统计数据，每次进行该任务时，系统会重新计算
	 * 所有机构的论文统计数据，并更新institution_publication_statistic表
	 */
	void synchronizePaperStatistics();
}
