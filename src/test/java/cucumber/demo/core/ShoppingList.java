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
package cucumber.demo.core;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ShoppingList {
    private SortedMap<String,Integer> items = new TreeMap<String, Integer>();

    public void addItem(String name, Integer count) {
        items.put(name, count);
    }

    public void print(Appendable out) throws IOException {
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            out.append(entry.getValue().toString()).append(" ").append(entry.getKey()).append("\n");
        }
    }
}
