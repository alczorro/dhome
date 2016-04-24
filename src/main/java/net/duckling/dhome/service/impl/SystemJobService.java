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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.duckling.dhome.common.util.GHIndexCaculator;
import net.duckling.dhome.dao.IInstitutionHomeDAO;
import net.duckling.dhome.dao.IInstitutionPaperDAO;
import net.duckling.dhome.dao.IInstitutionStatisticDAO;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionPublicationStatistic;
import net.duckling.dhome.domain.object.PaperStatistics;
import net.duckling.dhome.domain.object.PaperYear;
import net.duckling.dhome.service.ISystemJobService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统定时执行的任务，目前主要用来统计所有机构的论文信息
 * 
 * @author Yangxp
 * @since 2012-09-25
 */
@Service
public class SystemJobService implements ISystemJobService {

    private static final Logger LOG = Logger.getLogger(SystemJobService.class);
    @Autowired
    private IInstitutionPaperDAO paperDAO;
    @Autowired
    private IInstitutionStatisticDAO statDAO;
    @Autowired
    private IInstitutionHomeDAO homeDAO;

    public void setPaperDAO(IInstitutionPaperDAO paperDAO) {
        this.paperDAO = paperDAO;
    }

    public void setStatDAO(IInstitutionStatisticDAO statDAO) {
        this.statDAO = statDAO;
    }

    public void setHomeDAO(IInstitutionHomeDAO homeDAO) {
        this.homeDAO = homeDAO;
    }

    /**
     * 论文统计任务入口函数
     */
    @Override
    public void synchronizePaperStatistics() {
        LOG.info("Institution publications statistics job start........");
        prepare();
        doPaperStatisticWork();
        LOG.info("Institution publications statistics job complete!");
    }
    
    /**
     * 清空论文统计信息表，清理paper表的年份信息
     */
    private void prepare(){
    	statDAO.clear(); //清空机构的论文统计信息数据表
    	if(paperDAO.isAllYearInfoEmpty()){ //paper表中year未清理
    		List<PaperYear> paperYears = paperDAO.getAllPaperYear();
    		for(PaperYear paperYear : paperYears){
    			int year = getIntYear(paperYear.getPublishedTime());
    			int curYear = Calendar.getInstance().get(Calendar.YEAR);
    			year = (year > curYear) ? 0 : year; //输入的年份大于当前年份，则年份信息非法，置为未知年份(0)
    			paperYear.setYear(year);
    		}
    		paperDAO.updateIntYear(paperYears);
    	}
        
    }
    
    /**
     * 执行论文统计任务。
     */
    private void doPaperStatisticWork(){
    	List<PaperStatistics> statistics = getAllPaperStatistics();
    	if(null != statistics && !statistics.isEmpty()){
    		List<InstitutionPublicationStatistic> list = new ArrayList<InstitutionPublicationStatistic>();
    		int curInstitutionId = statistics.get(0).getInstitutionId(); //当前处理的机构ID
    		int sumPaperCount = 0; //当前机构累积的论文数量
    		int sumTimeCited = 0; //当前机构累积的论文被引频次
    		PaperStatistics noYearStatistic = null; //没有年份信息的论文统计数据
	    	for(PaperStatistics stat : statistics){
	    		if(curInstitutionId == stat.getInstitutionId()){ //处理当前机构某年的数据
	    			if(stat.getYear()<=0){
	    				noYearStatistic = stat;
	    			}else{
		    			sumPaperCount += stat.getCount();
		    			sumTimeCited += stat.getTimeCited();
		                list.add(InstitutionPublicationStatistic.build(stat.getYear(), stat.getInstitutionId(), 
		                		stat.getCount(), stat.getTimeCited(), sumPaperCount, sumTimeCited));
	    			}
	    		}else{//当前机构已处理完毕
	    			addNoYearPaperData(list, noYearStatistic, sumPaperCount, sumTimeCited);
	    			updateInstitutionHome(list.get(list.size()-1));//更新机构主页表中的论文统计信息
	    			if(stat.getYear()<=0){
	    				noYearStatistic = stat;
	    				sumPaperCount = 0;
		    			sumTimeCited = 0;
	    			}else{
	    				noYearStatistic = null;
	    				sumPaperCount = stat.getCount();
		    			sumTimeCited = stat.getTimeCited();
		                list.add(InstitutionPublicationStatistic.build(stat.getYear(), stat.getInstitutionId(), 
		                		stat.getCount(), stat.getTimeCited(), sumPaperCount, sumTimeCited));
	    			}
	    			curInstitutionId = stat.getInstitutionId();
	    		}
	    	}
	    	if(list.size()>0){
		    	addNoYearPaperData(list, noYearStatistic, sumPaperCount, sumTimeCited);
		    	updateInstitutionHome(list.get(list.size()-1));//更新最后一个机构主页表中的论文统计信息
	    	}
	    	statDAO.batchCreate(list);
    	}
    }
    
    /**
     * 获取所有论文的统计信息，按照机构和年份分组排序
     * @return
     */
    private List<PaperStatistics> getAllPaperStatistics(){
    	List<PaperStatistics> dsnStatistics = paperDAO.getAllDSNPaperStatistics(); //来源于DSN的论文统计信息
    	List<PaperStatistics> noDsnStatistics = paperDAO.getAllNotDSNPaperStatistics(); //来源于非DSN的论文统计信息
    	List<PaperStatistics> result = null;
    	if(!dsnStatistics.isEmpty() && !noDsnStatistics.isEmpty()){
    		result = mergePaperStatistics(dsnStatistics, noDsnStatistics);
    	}else if(dsnStatistics.isEmpty()){
    		result = noDsnStatistics;
    	}else if(noDsnStatistics.isEmpty()){
    		result = dsnStatistics;
    	}
    	return result;
    }
    /**
     * 将两个来源不同的论文统计信息列表合并，合并后按机构ID和年份信息升序排列。其中dsn和noDsn已经是按照该规则升序排列的
     * @param dsn 来源于DSN的论文统计信息
     * @param noDsn 来源于非DSN的论文统计信息
     * @return 合并后的论文统计信息列表
     */
    private List<PaperStatistics> mergePaperStatistics(List<PaperStatistics> dsn, List<PaperStatistics> noDsn){
    	List<PaperStatistics> total = new ArrayList<PaperStatistics>();
    	int dsnIndex = 0, noDsnIndex = 0;
    	int dsnSize = dsn.size(), noDsnSize = noDsn.size();
    	do{
    		PaperStatistics dsnPS = dsn.get(dsnIndex), noDsnPS = noDsn.get(noDsnIndex);
    		int dsnInsId = dsnPS.getInstitutionId();
    		int noDsnInsId = noDsnPS.getInstitutionId();
    		if(dsnInsId > noDsnInsId){
    			List<PaperStatistics> temp = getPSWithSameInsId(noDsn, noDsnIndex);
    			total.addAll(temp);
    			noDsnIndex += temp.size();
    		}else if(dsnInsId < noDsnInsId){
    			List<PaperStatistics> temp = getPSWithSameInsId(dsn, dsnIndex);
    			total.addAll(temp);
    			dsnIndex += temp.size();
    		}else{
    			List<PaperStatistics> tempDsn = getPSWithSameInsId(dsn, dsnIndex);
    			List<PaperStatistics> tempNoDsn = getPSWithSameInsId(noDsn, noDsnIndex);
    			List<PaperStatistics> tempTotal = mergePSByYear(tempDsn, tempNoDsn);
    			total.addAll(tempTotal);
    			dsnIndex += tempDsn.size();
    			noDsnIndex += tempNoDsn.size();
    		}
    	}while(dsnIndex < dsnSize && noDsnIndex < noDsnSize);
    	
    	while(dsnIndex < dsnSize){
    		total.add(dsn.get(dsnIndex++));
    	}
    	
    	while(noDsnIndex < noDsnSize){
    		total.add(noDsn.get(noDsnIndex++));
    	}
    	return total;
    }
    /**
     * 从psList中的第index+1个元素开始，将与该元素具有相同机构ID的元素提取出来。其中psList中的元素必须是按机构ID升序排列
     * @param psList 待提取的元素列表
     * @param index 开始提取的索引
     * @return 具有相同机构ID的元素列表
     */
    private List<PaperStatistics> getPSWithSameInsId(List<PaperStatistics> psList, int index){
    	List<PaperStatistics> result = new ArrayList<PaperStatistics>();
    	int start = index;
    	int size = psList.size();
    	int insId = psList.get(start).getInstitutionId();
    	while(start < size){
    		PaperStatistics ps = psList.get(start);
    		if(ps.getInstitutionId() != insId){
    			break;
    		}
    		result.add(ps);
    		start++;
    	}
    	return result;
    }
    /**
     * 比较dsn和noDsn中论文统计信息的年份信息，并合并成一个列表。其中，这两个列表中的insId都是相等的，且都是升序排列
     * @param dsn 来源于DSN的论文统计信息
     * @param noDsn 来源于非DSN的论文统计信息
     * @return 合并后的论文统计信息
     */
    private List<PaperStatistics> mergePSByYear(List<PaperStatistics> dsn, List<PaperStatistics> noDsn){
    	List<PaperStatistics> result = new ArrayList<PaperStatistics>();
    	int dsnIndex = 0, noDsnIndex = 0;
    	int dsnSize = dsn.size(), noDsnSize = noDsn.size();
    	do{
    		PaperStatistics dsnPS = dsn.get(dsnIndex), noDsnPS = noDsn.get(noDsnIndex);
    		int dsnYear = dsnPS.getYear(), noDsnYear = noDsnPS.getYear();
    		if(dsnYear > noDsnYear){
    			result.add(noDsnPS);
    			noDsnIndex ++;
    		}else if(dsnYear < noDsnYear){
    			result.add(dsnPS);
    			dsnIndex ++;
    		}else{
    			result.add(PaperStatistics.build(dsnPS.getInstitutionId(), dsnYear, 
    					dsnPS.getCount()+noDsnPS.getCount(), 
    					dsnPS.getTimeCited()+noDsnPS.getTimeCited()));
    			dsnIndex++;
    			noDsnIndex++;
    		}
    	}while(dsnIndex < dsnSize && noDsnIndex < noDsnSize);
    	
    	while(dsnIndex < dsnSize){
    		result.add(dsn.get(dsnIndex++));
    	}
    	
    	while(noDsnIndex < noDsnSize){
    		result.add(noDsn.get(noDsnIndex++));
    	}
    	return result;
    }
    
    /**
     * 如果存在没有年份信息的论文统计数据，则将其标记当前年份并添加到list中
     * @param list 所有机构所有年份的论文统计信息列表
     * @param noYearPS 没有年份信息的论文统计数据
     * @param sumPaperCount 当前机构累计的论文数量
     * @param sumTimeCited 当前机构累计的论文被引频次
     */
    private void addNoYearPaperData(List<InstitutionPublicationStatistic> list,
    		PaperStatistics noYearPS, int sumPaperCount, int sumTimeCited){
    	if(null != noYearPS){
    		int lastYear = list.get(list.size()-1).getYear(); //最后添加的论文年份
    		int lastInsId = list.get(list.size()-1).getInstitutionId(); //最后添加的论文机构ID
	        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
	        InstitutionPublicationStatistic ips = null;
	        if (lastInsId == noYearPS.getInstitutionId() &&
	        		lastYear == currentYear && !list.isEmpty()) { //当前机构的当前年份的论文统计信息已经添加过了，则取出该信息，并将无年份信息的论文统计数据累计上去
	            ips = list.get(list.size() - 1);
	            ips.setAnnualPaperCount(ips.getAnnualPaperCount() + noYearPS.getCount());
	            ips.setAnnualCitationCount(ips.getAnnualCitationCount() + noYearPS.getTimeCited());
	            ips.setTotalCitationCount(ips.getTotalCitationCount() + noYearPS.getTimeCited());
	            ips.setTotalPaperCount(ips.getTotalPaperCount() + noYearPS.getCount());
	        } else { //当前年份的论文统计信息没有被添加过，则添加
	            list.add(InstitutionPublicationStatistic.build(currentYear, noYearPS.getInstitutionId(), 
	            		noYearPS.getCount(), noYearPS.getTimeCited(), noYearPS.getCount() + sumPaperCount, 
	            		noYearPS.getTimeCited() + sumTimeCited));
	        }
    	}
    }

    private int getIntYear(String year) {
        String temp = "0";
        Pattern pat = Pattern.compile("\\d{4}");
        Matcher match = pat.matcher(year);
        if (match.find()) {
            temp = match.group();
        }
        return Integer.valueOf(temp);
    }

    private void updateInstitutionHome(InstitutionPublicationStatistic ips) {
        int insId = ips.getInstitutionId();
        InstitutionHome home = homeDAO.getInstitutionByInstitutionId(insId);
        if(home==null){
        	LOG.error("can't find InstitutionHome from this institutionId["+insId+"],the InstitutionHome is deleted or set invalid by admin");
        	return;
        }
        home.setPaperCount(ips.getTotalPaperCount());
        home.setCitationCount(ips.getTotalCitationCount());
        List<Integer> citeNums = paperDAO.getPaperCitedTimes(insId);
        home.setGindex(GHIndexCaculator.caculateGIndex(citeNums));
        home.setHindex(GHIndexCaculator.caculateHIndex(citeNums));
        homeDAO.updateInstitutionHomeForZeroFieldById(home);
    }

}
