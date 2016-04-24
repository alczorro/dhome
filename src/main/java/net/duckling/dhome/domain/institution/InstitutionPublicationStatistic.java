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
package net.duckling.dhome.domain.institution;
/**
 * 论文统计信息
 * @author Yangxp
 * @since 2012-09-25
 */
public class InstitutionPublicationStatistic {

    private int id;
    private int institutionId;
    private int year;
    private int annualPaperCount;
    private int annualCitationCount;
    private int totalPaperCount;
    private int totalCitationCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAnnualPaperCount() {
        return annualPaperCount;
    }

    public void setAnnualPaperCount(int annualPaperCount) {
        this.annualPaperCount = annualPaperCount;
    }

    public int getAnnualCitationCount() {
        return annualCitationCount;
    }

    public void setAnnualCitationCount(int annualCitationCount) {
        this.annualCitationCount = annualCitationCount;
    }

    public int getTotalPaperCount() {
        return totalPaperCount;
    }

    public void setTotalPaperCount(int totalPaperCount) {
        this.totalPaperCount = totalPaperCount;
    }

    public int getTotalCitationCount() {
        return totalCitationCount;
    }

    public void setTotalCitationCount(int totalCitationCount) {
        this.totalCitationCount = totalCitationCount;
    }
    /**
     * 创建InstitutionPublicationStatistic对象
     * @param year 年份
     * @param institutionId 机构ID
     * @param annualPaperCount 当前年的论文数量
     * @param annualCitationCount 当前年的论文引用次数
     * @param totalPaperCount 截止到当前年的累积论文数量
     * @param totalCitationCount 截止到当前年的累积论文引用次数
     * @return 
     */
    public static InstitutionPublicationStatistic build(int year, int institutionId,
    		int annualPaperCount, int annualCitationCount, int totalPaperCount,
    		int totalCitationCount){
    	InstitutionPublicationStatistic ins = new InstitutionPublicationStatistic();
    	ins.setYear(year);
    	ins.setInstitutionId(institutionId);
    	ins.setAnnualPaperCount(annualPaperCount);
    	ins.setAnnualCitationCount(annualCitationCount);
    	ins.setTotalPaperCount(totalPaperCount);
    	ins.setTotalCitationCount(totalCitationCount);
    	return ins;
    }
}
