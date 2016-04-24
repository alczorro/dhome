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
package net.duckling.dhome.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.dsn.PaperJSONHelper;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionPublicationStatistic;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionPaperService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IInstitutionPubStatisticService;
import net.duckling.dhome.service.IUserService;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
/**
 * 机构的学术论文
 * @author Yangxp
 * @since 2012-09-25
 */
@Controller
@RequestMapping("/institution/{domain}/publications.html")
public class InstitutionPaperController {
	
	private static final String SPLIT = ",";
	private static final String EMPTY = "";
	private static final String DOMAIN = "domain";
	private static final String END_EMPTY_IPS = ",0],";
	/**前台显示走势图时需要至少9个点     **/
	private static final int IPS_NUMBER = 9;
	private static final Logger LOG = Logger.getLogger(InstitutionPaperController.class);
	
	@Autowired 
	private IInstitutionPaperService ips;
	@Autowired
	private IInstitutionHomeService ihs;
	@Autowired
	private IUserService us;
	@Autowired
	private IInstitutionPubStatisticService ipss;
	@Autowired
	private IInstitutionPeopleService peopleService;
	
	public void setIps(IInstitutionPaperService ips) {
		this.ips = ips;
	}

	public void setIhs(IInstitutionHomeService ihs) {
		this.ihs = ihs;
	}

	public void setUs(IUserService us) {
		this.us = us;
	}

	public void setIpss(IInstitutionPubStatisticService ipss) {
		this.ipss = ipss;
	}

	public void setPeopleService(IInstitutionPeopleService peopleService) {
		this.peopleService = peopleService;
	}

	/**
	 * 显示学术论文页面
	 * @param request
	 * @param domain 学术机构的域名
	 * @return
	 */
	@RequestMapping
	public ModelAndView display(HttpServletRequest request, @PathVariable(DOMAIN) String domain){
		ModelAndView mv = new ModelAndView("institution/institutePaper");
		InstitutionHome ih = ihs.getInstitutionByDomain(domain);
		if(null != ih){
			int insId = ih.getInstitutionId();
			List<Integer> uids = ips.getPaperAuthorIds(insId);
			List<InstitutionPublicationStatistic> stats = ipss.getStatisticsByInstitutionId(insId);
			List<String> years = ips.getYearsOfAllPaper(insId);
			mv.addObject("years", filterIntYear(years));
			mv.addObject("authors", getJSONUser(us.getSimpleUsersByUid(uids)));
			mv.addObject("stats", getJSONStatistic(stats));
			mv.addObject("amount", ih.getPaperCount());
			mv.addObject("citedNum", ih.getCitationCount());
			mv.addObject("gindex", ih.getGindex());
			mv.addObject("hindex", ih.getHindex());
			mv.addObject("institution", ih);
			mv.addObject("isMember", peopleService.isMember(SessionUtils.getUserId(request), insId));
			String mydomain = SessionUtils.getDomain(request);
			if(null!=mydomain){
				mv.addObject("addPaperURL", UrlUtil.getRootURL(request)+"/people/"+mydomain+"/admin/paper/edit");
			}
		}
		return mv;
	}
	
	/**
	 * 根据请求生成学术论文数据，以JSON形式返回。论文顺序按照引用次数排序
	 * @param domain
	 * @param offset
	 * @param size
	 * @return
	 */
	@RequestMapping(params="func=paperCite")
	@ResponseBody
	public JSONArray paperCite(HttpServletRequest request, @PathVariable(DOMAIN) String domain, 
			@RequestParam("offset") int offset, @RequestParam("size") int size){
		int insId = ihs.getInstitutionIdByDomain(domain);
		List<Paper> papers = ips.getPaperSortByCiteTime(insId, offset, size);
		return getJSONPaper(request, papers); 
	}
	
	/**
	 * 根据请求生成学术论文数据，以JSON形式返回。论文顺序按照年份排序。<br/>
	 * 1）若year<0, 则查询所有年份的论文数据<br/>
	 * 2）若year=0, 则查询未知年份的论文数据<br/>
	 * 3）若year>0, 则查询特定年份的论文数据<br/>
	 * @param domain
	 * @param year 年份信息
	 * @param offset 偏移量
	 * @param size 每次取论文的大小
	 * @return
	 */
	@RequestMapping(params="func=paperYear")
	@ResponseBody
	public JSONArray paperYear(HttpServletRequest request, @PathVariable(DOMAIN) String domain, @RequestParam("year") String year,
			@RequestParam("offset") int offset, @RequestParam("size") int size){
		int insId = ihs.getInstitutionIdByDomain(domain);
		List<Paper> papers = ips.getPaperSortByYear(insId, year, offset, size); 
		return getJSONPaper(request, papers); 
	}
	/**
	 * 根据请求生成学术论文数据，以JSON形式返回。论文顺序按照作者中文名排序。<br/>
	 * 1）若uid<=0, 则查询所有作者的论文数据<br/>
	 * 2）若uid>0, 则查询特定作者的论文数据
	 * @param domain
	 * @param uid
	 * @param offset
	 * @param size
	 * @return
	 */
	@RequestMapping(params="func=paperAuthor")
	@ResponseBody
	public JSONArray paperAuthor(HttpServletRequest request, @PathVariable(DOMAIN) String domain, @RequestParam("uid") int uid,
			@RequestParam("offset") int offset, @RequestParam("size") int size){
		int insId = ihs.getInstitutionIdByDomain(domain);
		List<Paper> papers = ips.getPaperSortByAuthor(insId, uid, offset, size); 
		return getJSONPaper(request, papers); 
	}
	
	/**
	 * 提取years列表中所有的整数年份
	 * @param years
	 * @return
	 */
	private List<String> filterIntYear(List<String> years){
		Set<String> result = new TreeSet<String>();
		if(null != years && !years.isEmpty()){
			for(String year : years){
				result.add(getIntYear(year));
			}
		}
		return reverse(result);
	}
	
	/**
	 * 逆序排列集合中的数据
	 * @param sets
	 * @return
	 */
	private List<String> reverse(Set<String> sets){
		List<String> result = new ArrayList<String>();
		if(null != sets && !sets.isEmpty()){
			Iterator<String> itr = sets.iterator();
			while(itr.hasNext()){//待改进
				result.add(0, itr.next());
			}
		}
		return result;
	}
	
	private String getIntYear(String year){
		String temp = "0";
		Pattern pat = Pattern.compile("\\d{4}");
		Matcher match = pat.matcher(year);
		if(match.find()){
			temp = match.group();
		}
		return temp;
	}
	
	private JSONArray getJSONUser(List<SimpleUser> users){
		JSONArray array = new JSONArray();
		if(null != users && !users.isEmpty()){
			for(SimpleUser su : users){
				JSONObject obj = new JSONObject();
				obj.put("id", su.getId());
				obj.put("zhName", su.getZhName());
				array.add(obj);
			}
		}
		return array;
	}
	
	private JSONArray getJSONPaper(HttpServletRequest request, List<Paper> papers){
		JSONArray array = new JSONArray();
		if(null != papers && !papers.isEmpty()){
			for(Paper paper : papers){
				if(!isEmptyPaper(paper)){
					JSONObject obj = new JSONObject();
					obj.put("id", paper.getId());
					obj.put("title", paper.getTitle());
					obj.put("authors", paper.getAuthors());
					obj.put("source", paper.getSource());
					obj.put("publishedTime", paper.getPublishedTime());
					obj.put("volumeIssue", PaperJSONHelper.eraseBracket(paper.getVolumeIssue()));
					obj.put("pages", paper.getPages());
					obj.put("paperURL", PaperJSONHelper.filterURL(paper.getPaperURL()));
					obj.put("summary", paper.getSummary());
					obj.put("downloadURL", getPaperDownloadURL(request, paper.getClbId()));
					array.add(obj);
				}
			}
		}
		return array;
	}
	
	private String getPaperDownloadURL(HttpServletRequest request, int clbId){
		return (clbId>0)?UrlUtil.getRootURL(request)+"/system/download/"+clbId:"";
	}
	
	private boolean isEmptyPaper(Paper paper){
		if(null == paper.getTitle() || "".equals(paper.getTitle())){
			return true;
		}
		if(null == paper.getAuthors() || "".equals(paper.getAuthors())){
			return true;
		}
		return false;
	}
	/**
	 * 此处默认stats中的数据都是按年份排好序的
	 * @param stats
	 * @return
	 */
	private JSONObject getJSONStatistic(List<InstitutionPublicationStatistic> stats){
		JSONObject result = new JSONObject();
		StringBuilder cite = new StringBuilder();//每年的引用次数
		StringBuilder num = new StringBuilder();//每年的论文数
		StringBuilder year = new StringBuilder();//年份
		StringBuilder totalCite = new StringBuilder();//截止到该年的累积引用次数
		StringBuilder totalNum = new StringBuilder();//截止到该年的累积论文数
		int curYear = -1;
		if(null != stats && !stats.isEmpty()){
			for(InstitutionPublicationStatistic statsRecord : stats){
				getData(statsRecord, cite, num, year, totalCite, totalNum, curYear);
				curYear = statsRecord.getYear();
			}
			int systemYear = Calendar.getInstance().get(Calendar.YEAR);
			if(curYear != systemYear){
				insertEmptyYear(cite, num, year, totalCite, totalNum, curYear, systemYear+1);
			}
			paddingRecords(cite, num, year, totalCite, totalNum);
			cite.replace(cite.lastIndexOf(SPLIT), cite.length(), EMPTY);
			num.replace(num.lastIndexOf(SPLIT), num.length(), EMPTY);
			year.replace(year.lastIndexOf(SPLIT), year.length(), EMPTY);
			totalCite.replace(totalCite.lastIndexOf(SPLIT), totalCite.length(), EMPTY);
			totalNum.replace(totalNum.lastIndexOf(SPLIT), totalNum.length(), EMPTY);
		}
		result.put("cite", cite.toString());
		result.put("num", num.toString());
		result.put("year", year.toString());
		result.put("totalCite", totalCite.toString());
		result.put("totalNum", totalNum.toString());
		result.put("isEmpty", isNoPaper(stats));
		return result;
	}
	
	private boolean isNoPaper(List<InstitutionPublicationStatistic> stats){
		if(null == stats || stats.isEmpty()){
			return true;
		}
		InstitutionPublicationStatistic ipsEntity = stats.get(stats.size()-1);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		if(currentYear == ipsEntity.getYear()){
			return ipsEntity.getTotalPaperCount() == 0;
		}else{
			LOG.info("");
			return false;
		}
	}
	
	/**
	 * 将论文统计信息解析出来，并存入对应的StringBuilder对象中
	 * @param ips 论文统计信息
	 * @param cite 引用次数
	 * @param num 论文数量
	 * @param year 年份
	 * @param totalCite 累积引用次数
	 * @param totalNum 累积论文数量
	 * @param curYear 当前年
	 */
	private void getData(InstitutionPublicationStatistic ips, StringBuilder cite,
			StringBuilder num, StringBuilder year, StringBuilder totalCite,
			StringBuilder totalNum, int curYear){
		int ipsYear = ips.getYear();
		if(curYear != -1 && (ipsYear - curYear > 1)){
			insertEmptyYear(cite, num, year, totalCite, totalNum, curYear, ipsYear);
		}
		year.append(ipsYear+SPLIT);
		cite.append("["+ipsYear+SPLIT+ips.getAnnualCitationCount()+"]"+SPLIT);
		num.append("["+ipsYear+SPLIT+ips.getAnnualPaperCount()+"]"+SPLIT);
		totalCite.append("["+ipsYear+SPLIT+ips.getTotalCitationCount()+"]"+SPLIT);
		totalNum.append("["+ipsYear+SPLIT+ips.getTotalPaperCount()+"]"+SPLIT);
	}
	
	/**
	 * 对于论文时间跳跃度比较大的年份之间插入冗余年份信息。 如某机构有两篇论文分别发表于2009、2012年，<br/>
	 * 则插入2010和2011年的冗余信息。
	 * @param cite 引用次数
	 * @param num 论文数量
	 * @param year 年份
	 * @param totalCite 累积引用次数
	 * @param totalNum 累积论文数量
	 * @param curYear 起始年
	 * @param ipsYear 结束年
	 */
	private void insertEmptyYear(StringBuilder cite, StringBuilder num, StringBuilder year, StringBuilder totalCite,
			StringBuilder totalNum, int curYear, int ipsYear){
		String temp = totalCite.substring(0, totalCite.lastIndexOf(SPLIT));
		int start = temp.lastIndexOf(SPLIT)>=0?temp.lastIndexOf(SPLIT)+1:0;
		int end = temp.lastIndexOf(']')>=0?temp.lastIndexOf(']'):temp.length();
		String curTotalCite = temp.substring(start, end);
		temp = totalNum.substring(0, totalNum.lastIndexOf(SPLIT));
		start = temp.lastIndexOf(SPLIT)>=0?temp.lastIndexOf(SPLIT)+1:0;
		end = temp.lastIndexOf(']')>=0?temp.lastIndexOf(']'):temp.length();
		String curTotalNum = temp.substring(start, end);
		for(int i=curYear+1; i<ipsYear; i++){
			year.append(i+SPLIT);
			cite.append("["+i+",0]"+SPLIT);
			num.append("["+i+",0]"+SPLIT);
			totalCite.append("["+i+SPLIT+curTotalCite+"]"+SPLIT);
			totalNum.append("["+i+SPLIT+curTotalNum+"]"+SPLIT);
		}
	}
	
	/**
	 * 填充空的统计数据，已满足至少返回9条论文统计信息供前台显示
	 * @param cite
	 * @param num
	 * @param year
	 * @param totalCite
	 * @param totalNum
	 */
	private void paddingRecords(StringBuilder cite, StringBuilder num, StringBuilder year, StringBuilder totalCite,
			StringBuilder totalNum){
		String[] years = year.toString().split(SPLIT);
		int yearNum = years.length;
		if(yearNum < IPS_NUMBER){
			int firstYear = Integer.valueOf(years[0]);
			int paddingLen = IPS_NUMBER - yearNum;
			for(int i =1; i<=paddingLen; i++){
				int tempYear = (firstYear-i);
				year.insert(0, tempYear+",");
				cite.insert(0, "["+tempYear+END_EMPTY_IPS);
				num.insert(0, "["+tempYear+END_EMPTY_IPS);
				totalCite.insert(0, "["+tempYear+END_EMPTY_IPS);
				totalNum.insert(0, "["+tempYear+END_EMPTY_IPS);
			}
		}
	}
}
