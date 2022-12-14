/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ctakes.ytex.web.search;

import java.util.List;

/**
 * Dao to get Concepts corresponding to the given text
 * 
 * @author vijay
 * 
 */
public interface ConceptSearchService {

	/**
	 * get Concepts that start with the specified text.
	 * 
	 * @param fword
	 * @return
	 */
	public abstract List<ConceptFirstWord> getConceptByFirstWord(String fword);

	public String getTermByConceptId(String conceptId);

	/**
	 * if the conceptId is a valid conceptId, get the corresponding term
	 * 
	 * @param conceptId
	 * @return term
	 */
	public abstract String checkTermByConceptId(String conceptId);

}