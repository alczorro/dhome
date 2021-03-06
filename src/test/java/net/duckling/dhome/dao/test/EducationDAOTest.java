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
package net.duckling.dhome.dao.test;

import java.util.List;

import junit.framework.Assert;
import net.duckling.dhome.dao.impl.EducationDAO;
import net.duckling.dhome.domain.people.Education;

import org.junit.BeforeClass;
import org.junit.Test;

public class EducationDAOTest extends AbstractDAOTest{
    
    private static EducationDAO dao = new EducationDAO();

    @BeforeClass
    public static void setup() throws Exception {
        setupDatabase(dao,"/data/user.xml");
    }

    @Test
    public void testGetEdusByUID(){
        List<Education> eduList = dao.getEdusByUID(1);
        Assert.assertNotNull(eduList);
    }

}
